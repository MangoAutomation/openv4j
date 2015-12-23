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
package net.sf.openv4j.protocolhandlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class ProtocolHandler {
    private static final Logger log = LoggerFactory.getLogger(ProtocolHandler.class);
    private InputStream is;
    private OutputStream os;
    private StreamListener streamListener = new StreamListener();
    private Thread t;
    private boolean closed;

    /**
     * Creates a new ProtocolHandler object.
     */
    public ProtocolHandler() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public synchronized void close() throws InterruptedException {
        closed = true;
        Thread.sleep(100); //TODO wait?
        t.interrupt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param container DOCUMENT ME!
     */
    public void setReadRequest(DataContainer container) {
        streamListener.setReadRequest(container);
    }

    /**
     * DOCUMENT ME!
     *
     * @param is DOCUMENT ME!
     * @param os DOCUMENT ME!
     */
    public void setStreams(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        closed = false;
        start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param container DOCUMENT ME!
     */
    public void setWriteRequest(DataContainer container) {
        streamListener.setWriteRequest(container);
    }

    /**
     * DOCUMENT ME!
     *
     * @param theData DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHexASCII(byte[] theData) {
        StringBuilder sb = new StringBuilder(theData.length * 3);

        for (int i : theData) {
            sb.append(String.format("%02x ", i & 0xff));
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    private synchronized void start() {
        closed = false;
        t = new Thread(streamListener);
        t.setDaemon(true);
        t.start();
    }

    private class StreamListener implements Runnable {
        private DataBlock currentDataBlock;
        private DataContainer container;
        private State state = State.KW_IDLE;
        private byte[] received;
        private int bytesLeft;
        private int currentIndex;
        private int failedCount; // TODO impl
        private int retries;
        private long timeoutTimeStamp;

        @Override
        public void run() {
            System.out.println("THREAD START " + closed);

            try {
                int theData;

                try {
                    while (!closed) {
                        try {
                            theData = is.read();

                            switch (state) {
                                case KW_IDLE:

                                    if (theData == -1) {
                                        if (log.isDebugEnabled()) {
                                            log.debug("Idle timeout received");
                                        }
                                    } else {
                                        log.info(String.format("Idle char received: %02x", theData & 0x00ff));
                                    }

                                    break;

                                case KW_WAIT_FOR_READ_RDY:

                                    if (theData == 0x05) {
                                        sendReadKWDataPackage(currentDataBlock.getBaseAddress(), currentDataBlock.getLength());
                                    }

                                    break;

                                case KW_WAIT_FOR_READ_RESP:

                                    if (!checkConnBroken(theData, state.KW_WAIT_FOR_READ_RDY)) {
                                        setState(State.KW_COLLECT_READ_DATA);
                                        dataReaded(theData);
                                    }

                                    break;

                                case KW_COLLECT_READ_DATA:

                                    if (!checkConnBroken(theData, state.KW_WAIT_FOR_READ_RDY)) {
                                        dataReaded(theData);
                                    }

                                    break;

                                case KW_WAIT_FOR_WRITE_RDY:

                                    if (theData == 0x05) {
                                        sendWriteKWDataPackage(currentDataBlock.getBaseAddress(), currentDataBlock.getBytes());
                                    }

                                    break;

                                case KW_WAIT_FOR_WRITE_RESP:

                                    if (!checkConnBroken(theData, State.KW_WAIT_FOR_WRITE_RDY)) {
                                        dataWritten(theData);
                                    }

                                    break;
                            }
                        } catch (NullPointerException npe) {
                            if (!closed) {
                                throw new RuntimeException(npe);
                            }
                        }
                    }

                    log.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    log.error("run()", e);
                } catch (Exception e) {
                    log.info("finished waiting for packages", e);
                }
            } finally {
            }
        }

        public void sendReadKWDataPackage(int address, int length)
                                   throws IOException {
            sendKWPackageHeader(address, 0xf7, length);
            timeoutTimeStamp = System.currentTimeMillis();
            received = new byte[length];
            bytesLeft = received.length;
            setState(State.KW_WAIT_FOR_READ_RESP);

            if (log.isDebugEnabled()) {
                log.debug(String.format("Send readPackage @0x%04x %d", address, length));
            }
        }

        public void setReadRequest(DataContainer container) {
            this.container = container;
            retries = 3;
            setCurrentIndex(0);
            setState(State.KW_WAIT_FOR_READ_RDY);
        }

        public void setWriteRequest(DataContainer container) {
            this.container = container;
            retries = 3;
            setCurrentIndex(0);
            setState(State.KW_WAIT_FOR_WRITE_RDY);
        }

        private void checkClosed(int theData) throws InterruptedException {
            if ((theData == -1) && closed) {
                throw new InterruptedException("Port Closed");
            }
        }

        private boolean checkConnBroken(int theData, State state) {
            if (theData == -1) {
                retries--;

                if (retries == 0) {
                    log.info("Timeout received set No retries left finishing");
                    setState(State.KW_IDLE);

                    final Object o = container;

                    if (o != null) {
                        synchronized (o) {
                            o.notifyAll();
                        }
                    }

                    return true;
                } else {
                    log.info(String.format("Timeout received set state to %s Retries left %d", state.name(), retries));
                    setState(state);

                    return true;
                }
            } else {
                return false;
            }
        }

        private void dataReaded(int theData) {
            if (theData != -1) {
                received[received.length - bytesLeft--] = (byte) theData;

                if (bytesLeft == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("Data received: [%s]", toHexASCII(received)));
                    }

                    currentDataBlock.setBytesAtPos(0, received);

                    if ((currentIndex + 1) < container.getDataBlockCount()) {
                        setCurrentIndex(currentIndex + 1);
                        setState(State.KW_WAIT_FOR_READ_RDY);
                    } else {
                        setState(state.KW_IDLE);

                        final Object o = container;

                        if (o != null) {
                            synchronized (o) {
                                o.notifyAll();
                            }
                        }
                    }
                }
            }
        }

        private void dataWritten(int theData) {
            if (theData != -1) {
                if (theData == 0) {
                    received = null;
                    bytesLeft = 0;

                    if ((currentIndex + 1) < container.getDataBlockCount()) {
                        setCurrentIndex(currentIndex + 1);
                        setState(State.KW_WAIT_FOR_WRITE_RDY);
                    } else {
                        setState(state.KW_IDLE);

                        final Object o = container;

                        if (o != null) {
                            synchronized (o) {
                                o.notifyAll();
                            }
                        }
                    }
                } else {
                    // Try again
                    failedCount++;
                    setState(State.KW_WAIT_FOR_WRITE_RDY);
                }
            }
        }

        private void sendKWPackageHeader(int address, int command, int length)
                                  throws IOException {
            os.write(0x01);
            os.write(command);
            os.write((address >> 8) & 0xff);
            os.write(address & 0xff);
            os.write(length);
        }

        private void sendWriteKWDataPackage(int address, byte[] theData)
                                     throws IOException {
            sendKWPackageHeader(address, 0xf4, theData.length);
            os.write(theData);
            timeoutTimeStamp = System.currentTimeMillis();
            received = new byte[1];
            bytesLeft = received.length;
            setState(State.KW_WAIT_FOR_WRITE_RESP);

            if (log.isDebugEnabled()) {
                log.debug(String.format("Send writePackage @0x%04x [%s]", address, toHexASCII(theData)));
            }
        }

        private void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
            received = null;
            currentDataBlock = container.getDataBlock(currentIndex);
        }

        private void setState(State state) {
            this.state = state;
        }
    }

    enum State {KW_IDLE,
        KW_WAIT_FOR_READ_RDY,
        KW_WAIT_FOR_READ_RESP,
        KW_COLLECT_READ_DATA,
        KW_WAIT_FOR_WRITE_RDY,
        KW_WAIT_FOR_WRITE_RESP,
        _300_IDLE;
    }
}
