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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.openv4j.DataPoint;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class SegmentedDataContainerTest {
    private SegmentedDataContainer instance;

    /**
     * Creates a new SegmentedDataContainerTest object.
     */
    public SegmentedDataContainerTest() {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * DOCUMENT ME!
     */
    @Before
    public void setUp() {
        instance = new SegmentedDataContainer();
    }

    /**
     * DOCUMENT ME!
     */
    @After
    public void tearDown() {
        instance = null;
    }

    /**
     * Test of addToReadOut method, of class SegmentedDataContainer.
     */
    @Test
    public void testAddToDataContainer_16() {
        System.out.println("addToReadOut");
        instance.setSegmentSize(16);

        for (int i : DataPoint.BLOCKS_16) {
            instance.addToDataContainer(i, 16);
        }

        assertEquals(DataPoint.BLOCKS_16.length, instance.getDataBlockCount());

        int i = 0;

        for (int addr : DataPoint.BLOCKS_16) {
            assertEquals("Index: " + i, addr, instance.getDataBlock(i).getBaseAddress());
            i++;
        }
    }

    /**
     * Test of addToReadOut method, of class SegmentedDataContainer.
     */
    @Test
    public void testAddToDataContainer_2() {
        System.out.println("addToReadOut");
        instance.addToDataContainer(0, 32);
        assertEquals(1, instance.getDataBlockCount());
    }

    /**
     * Test of addToReadOut method, of class SegmentedDataContainer.
     */
    @Test
    public void testaddToDataContainer() {
        System.out.println("addToReadOut");
        instance.addToDataContainer(0, 1);
        assertEquals(1, instance.getDataBlockCount());
        instance.addToDataContainer(0x1000, 0x1000);
        assertEquals(129, instance.getDataBlockCount());
        instance.addToDataContainer(0x4001, 0x0fff);
        assertEquals(256, instance.getDataBlockCount());
    }
}
