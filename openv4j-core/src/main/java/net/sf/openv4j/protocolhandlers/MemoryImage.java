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

import java.util.Calendar;
import java.util.Date;

import net.sf.openv4j.CycleTimeEntry;
import net.sf.openv4j.CycleTimes;
import net.sf.openv4j.ErrorListEntry;
import net.sf.openv4j.Holiday;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public abstract class MemoryImage {
    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getRawByte(int address);

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param theData DOCUMENT ME!
     */
    public abstract void setRawByte(int addr, byte theData);

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public void addTime(int addr, Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, decodeBCD(getRawByte(addr)));
        c.set(Calendar.MINUTE, decodeBCD(getRawByte(addr + 1)));
        c.set(Calendar.SECOND, decodeBCD(getRawByte(addr + 2)));
        d.setTime(c.getTimeInMillis());
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean getBool(int addr) {
        final int value = getRawByte(addr);

        return (value == 0x00ff) ? null : (value != 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Byte getByte(int addr) {
        int result = getRawByte(addr);

        if (result == 0xff) {
            return null;
        }

        return (byte) result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param numberOfCycleTimes DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CycleTimes getCycleTimes(int addr, int numberOfCycleTimes) {
        CycleTimes result = new CycleTimes(numberOfCycleTimes / 2);

        for (int i = 0; i < (numberOfCycleTimes / 2); i++) {
            int dataByte = getRawByte(addr + (i * 2));

            if (dataByte == 0xff) {
                result.setEntry(i, null);
            } else {
                result.setEntry(i, new CycleTimeEntry());
                result.getEntry(i).setStart((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
                dataByte = getRawByte(addr + (i * 2) + 1);
                result.getEntry(i).setEnd((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ErrorListEntry getErrorListEntry(int addr) {
        return new ErrorListEntry(getRawByte(addr), getTimeStamp_8(addr + 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Holiday getHoliday(int addr) {
        Holiday result = new Holiday();
        result.setStart(getTimeStamp_Date(addr));
        result.setStartFlag((byte) getRawByte(addr + 4));
        addTime(addr + 5, result.getStart());
        result.setEnd(getTimeStamp_Date(addr + 8));
        result.setEndFlag((byte) getRawByte(addr + 12));
        addTime(addr + 13, result.getEnd());

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // swap bytes
    public Integer getInteger(int addr) {
        int result = getRawByte(addr);
        result |= (getRawByte(addr + 1) << 8);
        result |= (getRawByte(addr + 2) << 16);
        result |= (getRawByte(addr + 3) << 24);

        if (result == 0xffffffff) {
            return null;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // swap bytes
    public Short getShort(int addr) {
        int result = getRawByte(addr);
        result |= (getRawByte(addr + 1) << 8);

        if (result == 0xffff) {
            return null;
        }

        return (short) result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // Byte order as is
    public Short getShortHex(int addr) {
        int result = getRawByte(addr) << 8;
        result |= getRawByte(addr + 1);

        return (result == 0xffff) ? null : (short) result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getTimeStamp_8(int addr) {
        Calendar c = Calendar.getInstance();
        int year = (decodeBCD(getRawByte(addr)) * 100) + decodeBCD(getRawByte(addr + 1));
        int month = decodeBCD(getRawByte(addr + 2) - 1);
        int day = decodeBCD(getRawByte(addr + 3));

        //day of week getByte(addr + 4);
        int h = decodeBCD(getRawByte(addr + 5));
        int min = decodeBCD(getRawByte(addr + 6));
        int s = decodeBCD(getRawByte(addr + 7));
        c.set(year, month, day, h, min, s);

        return c.getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getTimeStamp_Date(int addr) {
        Calendar c = Calendar.getInstance();
        int year = (decodeBCD(getRawByte(addr)) * 100) + decodeBCD(getRawByte(addr + 1));
        int month = decodeBCD(getRawByte(addr + 2) - 1);
        int day = decodeBCD(getRawByte(addr + 3));

        c.set(year, month, day);

        return c.getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Short getUByte(int addr) {
        int result = getRawByte(addr);

        if (result == 0xff) {
            return null;
        }

        return (short) result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getUShort(int addr) {
        int result = getRawByte(addr);
        result |= (getRawByte(addr + 1) << 8);

        if (result == 0xffff) {
            return null;
        }

        return (int) result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setBool(int addr, boolean value) {
        setRawByte(addr, (byte) (value ? 1 : 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setByte(int addr, byte value) {
        setRawByte(addr, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param theData DOCUMENT ME!
     */
    public void setBytes(int addr, byte[] theData) {
        for (int i = 0; i < theData.length; i++) {
            setRawByte(addr + i, theData[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setInteger(int addr, int value) {
        setRawByte(addr, (byte) value);
        setRawByte(addr + 1, (byte) ((value >> 8) & 0x00FF));
        setRawByte(addr + 2, (byte) ((value >> 16) & 0x00FF));
        setRawByte(addr + 3, (byte) ((value >> 24) & 0x00FF));
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setShort(int addr, short value) {
        setRawByte(addr, (byte) value);
        setRawByte(addr + 1, (byte) ((value >> 8) & 0x00FF));
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setShortHex(int addr, short value) {
        setRawByte(addr, (byte) ((value >> 8) & 0x00FF));
        setRawByte(addr + 1, (byte) (value & 0x00FF));
    }

    private int decodeBCD(int bcdByte) {
        return (bcdByte & 0x0F) + (((bcdByte & 0x00F0) >> 4) * 10);
    }
}
