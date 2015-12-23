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

import java.io.Serializable;

/**
 * UINT LSB first HEX MSB first
 */
public enum DataType implements Serializable {BOOL(1),
    BYTE(1),
    UBYTE(1),
    SHORT(2),
    USHORT(2),
    INTEGER(4),
    SHORT_HEX(2),
    TIME_STAMP_8(8),
    CYCLE_TIMES(8),
    ERROR_LIST_ENTRY(9),
    HOLIDAY(16);

    final int length;

    private DataType(int length) {
        this.length = length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getLength() {
        return length;
    }
}
