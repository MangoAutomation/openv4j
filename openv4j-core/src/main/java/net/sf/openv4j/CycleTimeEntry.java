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

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class CycleTimeEntry {
    private int endHour;
    private int endMin;
    private int startHour;
    private int startMin;

    /**
     * DOCUMENT ME!
     *
     * @return the endHour
     */
    public int getEndHour() {
        return endHour;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the endMin
     */
    public int getEndMin() {
        return endMin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the startHour
     */
    public int getStartHour() {
        return startHour;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the startMin
     */
    public int getStartMin() {
        return startMin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     * @param min DOCUMENT ME!
     */
    public void setEnd(int h, int min) {
        endHour = h;
        endMin = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     * @param min DOCUMENT ME!
     */
    public void setStart(int h, int min) {
        startHour = h;
        startMin = min;
    }
}
