/*
 * Copyright 2009, openv4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * $Id: $
 *
 * @author arnep
 */
package net.sf.openv4j.memoryimage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import net.sf.openv4j.DataPoint;
import net.sf.openv4j.protocolhandlers.DataContainer;
import net.sf.openv4j.protocolhandlers.ProtocolHandler;
import net.sf.openv4j.protocolhandlers.SegmentedDataContainer;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * Creates a new Main object.
     */
    public Main() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Options options = new Options();
        Option opt;
        OptionGroup optg;

        opt = new Option("h", "help", false, "print this help message");
        options.addOption(opt);

        optg = new OptionGroup();
        optg.setRequired(true);

        opt = new Option("p", "port", true, "serial port to use");
        opt.setArgName("port");
        opt.setType(String.class);
        optg.addOption(opt);

        opt = new Option("r", "reevaluate-file", true, "reevaluate memory image file");
        opt.setArgName("reevaluateImage");
        opt.setType(String.class);
        optg.addOption(opt);

        options.addOptionGroup(optg);

        opt = new Option("s", "segment-size", true, "segment-size default " + SegmentedDataContainer.DEFAULT_SEGMENT_SIZE);
        opt.setArgName("segmentSize");
        opt.setType(Integer.class);
        options.addOption(opt);

        optg = new OptionGroup();
        optg.setRequired(true);

        opt = new Option("a", "all-known-blocks", false, "fetch all known 16 Byte blocks wich are different from 0xFF");
        opt.setArgName("allBlocks");
        optg.addOption(opt);

        opt = new Option("d", "all-data-points", false, "fetch all known datapoints");
        opt.setArgName("allDataPoints");
        optg.addOption(opt);

        opt = new Option("f", "full-scan", false, "fetch all 0x0000 to 0xffff");
        opt.setArgName("complete memory map");
        optg.addOption(opt);

        options.addOptionGroup(optg);
        opt = null;
        optg = null;

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            printHelp(options);

            return;
        }

        if (cmd.hasOption("help")) {
            printHelp(options);

            return;
        }

        DataContainer container;

        if (cmd.hasOption("reevaluate-file")) {
            container = new SegmentedDataContainer(Integer.parseInt(cmd.getOptionValue("segment-size", String.valueOf(SegmentedDataContainer.DEFAULT_SEGMENT_SIZE))));
            extractMemoryImage(new FileInputStream(cmd.getOptionValue("reevaluate-file")), container);
        } else {
            container = new SegmentedDataContainer(Integer.parseInt(cmd.getOptionValue("segment-size", String.valueOf(SegmentedDataContainer.DEFAULT_SEGMENT_SIZE))));

            if (cmd.hasOption("all-known-blocks")) {
                for (int address : DataPoint.BLOCKS_16) {
                    container.addToDataContainer(address, 16);
                }
            } else if (cmd.hasOption("all-data-points")) {
                for (DataPoint dp : DataPoint.values()) {
                    container.addToDataContainer(dp);
                }
            } else if (cmd.hasOption("full-scan")) {
                container.addToDataContainer(0x0000, 0x010000);
            }
        }

        Date startTime = new Date();
        String outName;

        if (cmd.hasOption("reevaluate-file")) {
            outName = cmd.getOptionValue("reevaluate-file");
        } else {
            run(cmd.getOptionValue("port"), container);
            outName = String.format("mem-image_%1$tY%1$tm%1$td_%1$tH%1$tM%1$tS.txt", startTime);
        }

        System.out.println("Data Points:");

        StringBuilder sb = new StringBuilder();
        DataPoint.printAll(sb, container);
        System.out.append(sb.toString());
        sb = null;

        FileOutputStream fos = new FileOutputStream(outName);
        sb = new StringBuilder();
        sb.append("Memory Image: {\n");
        sb.append(container.toString());
        sb.append("\n}");
        sb.append("\nProperties By Address: {\n");
        DataPoint.printAddresses(sb, container);
        sb.append("}");
        sb.append("\nProperties By Group: {\n");
        DataPoint.printAll(sb, container);
        sb.append("}");

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        fos.flush();
        fos.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param port DOCUMENT ME!
     * @param dc DOCUMENT ME!
     */
    public static void run(String port, final DataContainer dc) {
        SerialPort masterPort = null;
        Main expl = new Main();
        ProtocolHandler protocolHandler = new ProtocolHandler();

        try {
            masterPort = ProtocolHandler.openPort(port);
            protocolHandler.setStreams(masterPort.getInputStream(), masterPort.getOutputStream());
        } catch (NoSuchPortException ex) {
            log.error(ex.getMessage(), ex);
        } catch (PortInUseException ex) {
            log.error(ex.getMessage(), ex);
        } catch (UnsupportedCommOperationException ex) {
            log.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        try {
            protocolHandler.setReadRequest(dc);

            synchronized (dc) {
                dc.wait(dc.getDataBlockCount() * 60000);
            }
        } catch (Exception ex) {
            System.err.print("Error sleep " + ex);
        }

        try {
            System.out.println("CLOSE");
            protocolHandler.close();
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }

        if (masterPort != null) {
            masterPort.close();
        }
    }

    private static void extractMemoryImage(InputStream is, DataContainer container)
                                    throws IOException {
        boolean isMemoryImage = false;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = br.readLine()) != null) {
            if (!isMemoryImage) {
                if ("Memory Image: {".equals(line)) {
                    isMemoryImage = true;
                }
            } else {
                if ("}".equals(line)) {
                    return;
                } else {
                    container.addMemoryImageLine(line);
                }
            }
        }
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("openv4j-memory-image", opts);
    }
}
