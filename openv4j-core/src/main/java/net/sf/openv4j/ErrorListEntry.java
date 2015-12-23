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

import java.util.Date;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class ErrorListEntry {
    private Date tinestamp;
    private int errorCode;

    /**
     * Creates a new ErrorListEntry object.
     *
     * @param errorCode DOCUMENT ME!
     * @param timeStamp DOCUMENT ME!
     */
    public ErrorListEntry(int errorCode, Date timeStamp) {
        this.errorCode = errorCode;
        this.tinestamp = timeStamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the tinestamp
     */
    public Date getTinestamp() {
        return tinestamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tinestamp the tinestamp to set
     */
    public void setTinestamp(Date tinestamp) {
        this.tinestamp = tinestamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        return String.format("%02x  %s", errorCode & 0xFF, tinestamp.toString());
    }

    //TODO parse String
}
