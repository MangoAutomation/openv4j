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

import java.util.Arrays;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class DataBlock {
    private byte[] data;
    private final int baseAddress;

    /**
     * Creates a new DataBlock object.
     *
     * @param baseAddress DOCUMENT ME!
     * @param size DOCUMENT ME!
     */
    public DataBlock(int baseAddress, int size) {
        super();
        this.baseAddress = baseAddress;
        data = new byte[size];
        Arrays.fill(data, (byte) 0xff);
    }

    /**
     * Creates a new DataBlock object.
     *
     * @param baseAddress DOCUMENT ME!
     */
    DataBlock(int baseAddress, int... bytes) {
        super();
        this.baseAddress = baseAddress;
        data = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            data[i] = (byte) bytes[i];
        }
    }

    public int getBaseAddress() {
        return baseAddress;
    }

    public byte getByteAtPos(int pos) {
        return data[pos];
    }

    public byte[] getBytes() {
        return data;
    }

    public int getLength() {
        return data.length;
    }

    public void setByteAtPos(int pos, int theData) {
        data[pos] = (byte) theData;
    }

    public void setBytesAtPos(int pos, byte[] theData) {
        if (theData.length == data.length) {
            data = theData;
        } else {
            for (int i = 0; i < theData.length; i++) {
                data[i + pos] = theData[i];
            }
        }
    }

    public void writeTo(StringBuilder sb, boolean suppressFirstNewLine) {
        for (int i = 0; i < data.length; i++) {
            if (((i + baseAddress) % 16) == 0) {
                if (suppressFirstNewLine) {
                    sb.append(String.format("%04x ", i + baseAddress));
                    suppressFirstNewLine = false;
                } else {
                    sb.append(String.format("\n%04x ", i + baseAddress));
                }
            } else if (((i + baseAddress) % 4) == 0) {
                sb.append(" |");
            }

            sb.append(String.format(" %02x", data[i]));
        }
    }
}
