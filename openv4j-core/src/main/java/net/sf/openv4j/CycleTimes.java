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
public class CycleTimes {
    private CycleTimeEntry[] entries;

    /**
     * Creates a new CycleTimes object.
     *
     * @param numberOfCycles DOCUMENT ME!
     */
    public CycleTimes(int numberOfCycles) {
        entries = new CycleTimeEntry[numberOfCycles];
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CycleTimeEntry getEntry(int i) {
        return entries[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param entry DOCUMENT ME!
     */
    public void setEntry(int i, CycleTimeEntry entry) {
        entries[i] = entry;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (CycleTimeEntry ct : entries) {
            if (ct == null) {
                sb.append("ON --:-- OFF --:-- | ");
            } else {
                sb.append(String.format("ON %02d:%02d OFF %02d:%02d | ", ct.getStartHour(), ct.getStartMin(), ct.getEndHour(), ct.getEndMin()));
            }
        }

        sb.delete(sb.lastIndexOf(" | "), sb.length());

        return sb.toString();
    }

    //TODO parse String....
}
