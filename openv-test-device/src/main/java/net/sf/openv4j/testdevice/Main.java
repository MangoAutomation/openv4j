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
package net.sf.openv4j.testdevice;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
import gnu.io.UnsupportedCommOperationException;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);
    private final KW2Dummy kwDummy;

    /**
     * Creates a new Main object.
     */
    public Main() {
        super();
        log = LoggerFactory.getLogger(Main.class);
        kwDummy = new KW2Dummy();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public void close() throws InterruptedException {
        kwDummy.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option helpOpt = new Option("h", "help", false, "print this help message");
        options.addOption(helpOpt);

        Option portOpt = new Option("p", "port", true, "serial port to use");
        portOpt.setArgName("port");
        portOpt.setType(String.class);
        portOpt.setRequired(true);
        options.addOption(portOpt);

        OptionGroup memMapOptionGroup = new OptionGroup();
        memMapOptionGroup.setRequired(true);

        Option fileMemMapOpt = new Option("f", "file", true, "read from File");
        fileMemMapOpt.setArgName("mem map file name");
        portOpt.setType(String.class);
        memMapOptionGroup.addOption(fileMemMapOpt);

        options.addOptionGroup(memMapOptionGroup);

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            printHelp(options);

            return;
        }

        if (cmd.hasOption('h')) {
            printHelp(options);

            return;
        }

        InputStream is = null;

        if (cmd.hasOption('f')) {
            is = new FileInputStream(cmd.getOptionValue('f'));
        }

        run(cmd.getOptionValue('p'), is);
    }

    /**
     * DOCUMENT ME!
     *
     * @param port DOCUMENT ME!
     * @param is DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void run(String port, InputStream is)
                    throws Exception {
        final Main device = new Main();

        try {
            device.readFromStream(is);
            device.openPort(port);
            System.out.print("Press any key to quit!");
            System.in.read();
        } finally {
            device.close();
        }

        log.info("Ende");
    }

    private void openPort(String serialPortName) throws IOException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        kwDummy.openPort(serialPortName);
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("openv4j-memory-image", opts);
    }

    private void readFromStream(InputStream is) throws IOException {
        kwDummy.readFromStream(is);
    }
}
