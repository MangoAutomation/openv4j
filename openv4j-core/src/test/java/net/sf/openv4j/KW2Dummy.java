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
package net.sf.openv4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.openv4j.protocolhandlers.MemoryImage;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class KW2Dummy extends MemoryImage {
    private static Logger log = LoggerFactory.getLogger(KW2Dummy.class);
    private KW2Encoder enc = new KW2Encoder();
    private MockInputStream is = new MockInputStream();
    private MockOutputStream os = new MockOutputStream();
    private byte[] memMap = new byte[0x010000];
    private final int afterDataSent05Time;
    private final int default05Time;
    private int next05Time;

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
     * @return DOCUMENT ME!
     */
    public InputStream getInputStream() {
        return is;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public OutputStream getOutputStream() {
        return os;
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

    class KW2Encoder {
        KW2EncodeStates state = KW2EncodeStates.IDLE;
        byte[] writeData;
        int parsedAddress;
        int parsedData;
        int writeDataPos;

        void parseByte(int theData) {
            switch (state) {
                case IDLE:

                    if (theData == 0x01) {
                        setState(KW2EncodeStates.START_RECEIVED);
                    }

                    break;

                case START_RECEIVED:

                    if (theData == 0xF4) {
                        setState(KW2EncodeStates.WRITE_ADDR_0);
                    } else if (theData == 0xF7) {
                        setState(KW2EncodeStates.READ_ADDR_0);
                    } else {
                        setState(KW2EncodeStates.IDLE);
                    }

                    break;

                case READ_ADDR_0:
                    parsedAddress = (theData & 0x00FF) << 8;
                    setState(KW2EncodeStates.READ_ADDR_1);

                    break;

                case READ_ADDR_1:
                    parsedAddress |= (theData & 0x00FF);
                    setState(KW2EncodeStates.READ_LENGTH);

                    break;

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

                    for (DataPoint dp : DataPoint.values()) {
                        if ((dp.getAddr() >= parsedAddress) && (dp.getAddr() < (parsedAddress + theData))) {
                            sb.append(String.format("\t@%04x %s:%s (%s)", dp.getAddr(), dp.getGroup().getLabel(), dp.getLabel(), dp.decode(KW2Dummy.this)));
                        }
                    }

                    log.info(sb.toString());
                    is.setSendData(result);
                    parsedAddress = 0;
                    setState(KW2EncodeStates.IDLE);
                }

                break;

                case WRITE_ADDR_0:
                    parsedAddress = (theData & 0x00FF) << 8;
                    setState(KW2EncodeStates.WRITE_ADDR_1);

                    break;

                case WRITE_ADDR_1:
                    parsedAddress |= (theData & 0x00FF);
                    setState(KW2EncodeStates.WRITE_LENGTH);

                    break;

                case WRITE_LENGTH:
                    writeData = new byte[theData];
                    setState(KW2EncodeStates.WRITE_DATA);

                    break;

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
                        log.info(sb.toString());
                        is.setSendData(new byte[] { 0 });
                        parsedAddress = 0;
                        setState(KW2EncodeStates.IDLE);
                    }
                }

                break;
            }
        }

        private void setState(KW2EncodeStates newState) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Change State from %s to %s", state, newState));
            }

            state = newState;
        }
    }

    class MockInputStream extends InputStream {
        byte[] dataToSend;
        int sendPos = 0x0100;

        @Override
        public int read() throws IOException {
            if (sendPos < 0x00FF) {
                final int result = 0x00FF & dataToSend[sendPos++];

                if (dataToSend.length == sendPos) {
                    sendPos = 0x100;
                    next05Time = afterDataSent05Time;
                }

                return result;
            }

            try {
                Thread.sleep(next05Time);
                next05Time = default05Time;
            } catch (InterruptedException ex) {
            }

            return 0x05;
        }

        private void setSendData(byte[] dataToSend) {
            this.dataToSend = dataToSend;
            sendPos = 0;
        }
    }

    class MockOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            if (log.isTraceEnabled()) {
                log.trace(String.format("TX: 0x%02x", b & 0x00FF));
            }

            enc.parseByte(b & 0x00FF);
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
