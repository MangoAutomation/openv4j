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

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class SegmentedDataContainer extends DataContainer {
    /**
     * At least the V200KW2 is capable to fetch all data with 32 byte segmentation
     */
    /**
     * At least the V200KW2 is capable to fetch all data with 32 byte segmentation
     */
    public static final int DEFAULT_SEGMENT_SIZE = 32;
    private DataBlock[] dataSegments;
    private int segmentSize;

    /**
     * Creates a new SegmentedDataContainer object.
     */
    public SegmentedDataContainer() {
        super();
        setSegmentSize(DEFAULT_SEGMENT_SIZE);
    }

    /**
     * Creates a new SegmentedDataContainer object.
     *
     * @param segmentSize DOCUMENT ME!
     */
    public SegmentedDataContainer(int segmentSize) {
        super();
        setSegmentSize(segmentSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param startAddress DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    @Override
    public void addToDataContainer(int startAddress, int[] data) {
        throw new UnsupportedOperationException("Write of segmented data is not supported.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param startAddress DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    @Override
    public void addToDataContainer(int startAddress, int length) {
        int baseInxed = startAddress / segmentSize;

        if (dataSegments[baseInxed] == null) {
            dataSegments[baseInxed] = new DataBlock(baseInxed * segmentSize, segmentSize);
        }

        for (int i = 1; i < (length / segmentSize); i++) {
            baseInxed++;

            if (dataSegments[baseInxed] == null) {
                dataSegments[baseInxed] = new DataBlock(baseInxed * segmentSize, segmentSize);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    @Override
    public DataBlock getDataBlock(int index) {
        int pos = 0;

        for (int i = 0; i < dataSegments.length; i++) {
            if (dataSegments[i] != null) {
                if (pos == index) {
                    return dataSegments[i];
                }

                pos++;
            }
        }

        throw new IndexOutOfBoundsException("Cant find index " + index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public int getDataBlockCount() {
        int result = 0;

        for (int i = 0; i < dataSegments.length; i++) {
            if (dataSegments[i] != null) {
                result++;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param currentSendSegmentAddr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNextSegmentAddr(int currentSendSegmentAddr) {
        if (currentSendSegmentAddr == -1) {
            for (int i = 0; i < dataSegments.length; i++) {
                if (dataSegments[i] != null) {
                    return i * segmentSize;
                }
            }
        } else {
            for (int i = (currentSendSegmentAddr / segmentSize) + 1; i < dataSegments.length; i++) {
                if (dataSegments[i] != null) {
                    return i * segmentSize;
                }
            }
        }

        return -1;
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
        final int baseIndex = address / segmentSize;

        if (dataSegments[baseIndex] == null) {
            return 0x00ff;
        } else {
            return dataSegments[baseIndex].getByteAtPos(address % segmentSize) & 0x00ff;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param theData DOCUMENT ME!
     */
    @Override
    public void setBytes(int addr, byte[] theData) {
        if (((addr % segmentSize) == 0) && (theData.length == segmentSize)) {
            dataSegments[addr / segmentSize].setBytesAtPos(addr % segmentSize, theData);
        } else {
            super.setBytes(addr, theData);
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
        dataSegments[addr / segmentSize].setByteAtPos(addr % segmentSize, theData);
    }

    /**
     * DOCUMENT ME!
     *
     * @param segmentSize DOCUMENT ME!
     */
    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
        dataSegments = new DataBlock[0x010000 / segmentSize];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < dataSegments.length; i++) {
            if (dataSegments[i] != null) {
                dataSegments[i].writeTo(sb, i == 0);
            }
        }

        return sb.toString();
    }
}
