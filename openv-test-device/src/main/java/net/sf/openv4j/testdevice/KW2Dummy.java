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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import net.sf.openv4j.DataPoint;
import net.sf.openv4j.protocolhandlers.MemoryImage;
import net.sf.openv4j.protocolhandlers.ProtocolHandler;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class KW2Dummy extends MemoryImage {
    private static Logger log = LoggerFactory.getLogger(KW2Dummy.class);
    KW2EncodeStates state = KW2EncodeStates.IDLE;
    byte[] writeData;
    int parsedAddress;
    int parsedData;
    int writeDataPos;
    private DeviceTimings dt;
    private SerialPort serialPort;
    private Thread t;
    private byte[] memMap = new byte[0x010000];
    private boolean closed;
    private final int afterDataSent05Time;
    private final int default05Time;
    private long last0x05Timestamp;

    /**
     * Creates a new KW2Dummy object.
     */
    public KW2Dummy() {
        this.default05Time = 2300;
        this.afterDataSent05Time = 3000;

        for (int i = 0; i < memMap.length; i++) {
            memMap[i] = (byte) 0xFF;
        }
    }

    /**
     * Creates a new KW2Dummy object.
     *
     * @param default05Time DOCUMENT ME!
     * @param afterDataSent05Time DOCUMENT ME!
     */
    public KW2Dummy(int default05Time, int afterDataSent05Time) {
        this.default05Time = default05Time;
        this.afterDataSent05Time = afterDataSent05Time;

        for (int i = 0; i < memMap.length; i++) {
            memMap[i] = (byte) 0xFF;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public void close() throws InterruptedException {
        closed = true;
        Thread.sleep(100); //TODO wait?
        t.interrupt();

        if (serialPort != null) {
            serialPort.close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public int getRawByte(int address) {
        return memMap[address] & 0x00ff;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serialPortName DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws NoSuchPortException DOCUMENT ME!
     * @throws PortInUseException DOCUMENT ME!
     * @throws UnsupportedCommOperationException DOCUMENT ME!
     */
    public void openPort(String serialPortName) throws IOException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        serialPort = ProtocolHandler.openPort(serialPortName);
        closed = false;
        dt = new DeviceTimings();
        t = new Thread(dt);
        t.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void readFromStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            String[] splitted = line.split(" ");
            int address = -1;

            for (int i = 0; i < splitted.length; i++) {
                if (i == 0) {
                    address = Integer.parseInt(splitted[0], 16);
                } else {
                    if ((splitted[i].length() != 0) && (!"|".equals(splitted[i]))) {
                        memMap[address++] = (byte) Integer.parseInt(splitted[i], 16);
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param theData DOCUMENT ME!
     */
    @Override
    public void setRawByte(int addr, byte theData) {
        memMap[addr] = theData;
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public String streamToString(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param theData
     *
     * @return true if packed parses and sent
     *
     * @throws IOException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    boolean parseByte(int theData) throws IOException {
        switch (state) {
            case IDLE:

                if (theData == 0x01) {
                    setState(KW2EncodeStates.START_RECEIVED);
                }

                return false;

            case START_RECEIVED:

                if (theData == 0xF4) {
                    setState(KW2EncodeStates.WRITE_ADDR_0);
                } else if (theData == 0xF7) {
                    setState(KW2EncodeStates.READ_ADDR_0);
                } else {
                    setState(KW2EncodeStates.IDLE);
                }

                return false;

            case READ_ADDR_0:
                parsedAddress = (theData & 0x00FF) << 8;
                setState(KW2EncodeStates.READ_ADDR_1);

                return false;

            case READ_ADDR_1:
                parsedAddress |= (theData & 0x00FF);
                setState(KW2EncodeStates.READ_LENGTH);

                return false;

            case READ_LENGTH: {
                final byte[] result = Arrays.copyOfRange(memMap, parsedAddress, parsedAddress + theData);
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Read at x0%04x %d Bytes [", parsedAddress, theData));

                boolean first = true;

                for (byte b : result) {
                    if (first) {
                        sb.append(String.format("%02x", b));
                    } else {
                        sb.append(String.format(" %02x", b));
                    }
                }

                sb.append("]");
                appendDataPointValues(sb, parsedAddress, theData);
                log.info(sb.toString());
                write(result);
                parsedAddress = 0;
                setState(KW2EncodeStates.IDLE);
            }

            return true;

            case WRITE_ADDR_0:
                parsedAddress = (theData & 0x00FF) << 8;
                setState(KW2EncodeStates.WRITE_ADDR_1);

                return false;

            case WRITE_ADDR_1:
                parsedAddress |= (theData & 0x00FF);
                setState(KW2EncodeStates.WRITE_LENGTH);

                return false;

            case WRITE_LENGTH:
                writeData = new byte[theData];
                setState(KW2EncodeStates.WRITE_DATA);

                return false;

            case WRITE_DATA: {
                writeData[writeDataPos++] = (byte) (theData & 0x00FF);

                if (writeData.length == writeDataPos) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("Write at x0%04x %d Bytes [", parsedAddress, writeData.length));

                    int pos = parsedAddress;

                    for (byte b : writeData) {
                        if (pos == parsedAddress) {
                            sb.append(String.format("%02x", b));
                        } else {
                            sb.append(String.format(" %02x", b));
                        }

                        memMap[pos++] = b;
                    }

                    sb.append("]");
                    appendDataPointValues(sb, parsedAddress, writeData.length);
                    log.info(sb.toString());
                    write((byte) 0x00);
                    parsedAddress = 0;
                    setState(KW2EncodeStates.IDLE);

                    return true;
                }
            }

            return false;

            default:
                throw new RuntimeException("Unknown state" + state.name());
        }
    }

    private void appendDataPointValues(StringBuilder sb, int address, int lenght) {
        for (DataPoint dp : DataPoint.values()) {
            if ((dp.getAddr() >= address) && (dp.getAddr() < (address + lenght))) {
                sb.append(String.format("\t@%04x %s:%s (%s)", dp.getAddr(), dp.getGroup().getLabel(), dp.getLabel(), dp.decode(KW2Dummy.this)));
            }
        }
    }

    private int read() throws IOException {
        return serialPort.getInputStream().read();
    }

    private void setState(KW2EncodeStates newState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Change State from %s to %s", state, newState));
        }

        state = newState;
    }

    private void write(byte[] bytes) throws IOException {
        serialPort.getOutputStream().write(bytes);
    }

    private void write(byte data) throws IOException {
        serialPort.getOutputStream().write(data);
    }

    private class DeviceTimings implements Runnable {
        @Override
        public void run() {
            while (!closed) {
                try {
                    write((byte) 0x05);
                    last0x05Timestamp = System.currentTimeMillis();

                    // read loop
                    while (!closed) {
                        int data = read();

                        if (data == -1) {
                            Thread.sleep(default05Time - (System.currentTimeMillis() - last0x05Timestamp));

                            break;
                        } else {
                            if (parseByte(data)) {
                                Thread.sleep(afterDataSent05Time - (System.currentTimeMillis() - last0x05Timestamp));

                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    enum KW2EncodeStates {IDLE,
        START_RECEIVED,
        READ_ADDR_0,
        READ_ADDR_1,
        WRITE_ADDR_0,
        WRITE_ADDR_1,
        READ_LENGTH,
        WRITE_LENGTH,
        WRITE_DATA;
    }
}
