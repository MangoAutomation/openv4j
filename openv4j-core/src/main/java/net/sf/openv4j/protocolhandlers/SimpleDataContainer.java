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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class SimpleDataContainer extends DataContainer {
    private static Logger log = LoggerFactory.getLogger(SimpleDataContainer.class);
    private List<DataBlock> data = new ArrayList<DataBlock>();

    /**
     * DOCUMENT ME!
     *
     * @param startAddress DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    @Override
    public void addToDataContainer(int startAddress, int length) {
        data.add(new DataBlock(startAddress, length));
    }

    /**
     * DOCUMENT ME!
     *
     * @param startAddress DOCUMENT ME!
     * @param bytes DOCUMENT ME!
     */
    @Override
    public void addToDataContainer(int startAddress, int[] bytes) {
        data.add(new DataBlock(startAddress, bytes));
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public DataBlock getDataBlock(int i) {
        return data.get(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public int getDataBlockCount() {
        return data.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return data.size();
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
        for (DataBlock db : data) {
            if (hasAddress(db, address)) {
                return db.getByteAtPos(address - db.getBaseAddress()) & 0x00ff;
            }
        }

        log.error(String.format("No such Address %04x", address));

        return 0x00ff;
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     * @param theData DOCUMENT ME!
     */
    @Override
    public void setRawByte(int address, byte theData) {
        for (DataBlock db : data) {
            if (hasAddress(db, address)) {
                db.setByteAtPos(address - db.getBaseAddress(), theData);
            }
        }

        log.error(String.format("No such Address %04x", address));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (DataBlock db : data) {
            sb.append(String.format("0x04x :", db.getBaseAddress()));

            for (int i = 0; i < db.getLength(); i++) {
                sb.append(String.format(" %02x", db.getByteAtPos(i)));
            }
        }

        return sb.toString();
    }

    private boolean hasAddress(DataBlock db, int address) {
        return (db.getBaseAddress() <= address) && ((db.getBaseAddress() + db.getLength()) > address);
    }
}
