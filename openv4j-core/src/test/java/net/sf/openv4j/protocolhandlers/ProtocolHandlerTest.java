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
import net.sf.openv4j.KW2Dummy;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class ProtocolHandlerTest {
    private KW2Dummy dummy;
    private ProtocolHandler protocolHandler;

    /**
     * Creates a new ProtocolHandlerTest object.
     */
    public ProtocolHandlerTest() {
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
     *
     * @throws Exception DOCUMENT ME!
     */
    @Before
    public void setUp() throws Exception {
        protocolHandler = new ProtocolHandler();
        dummy = new KW2Dummy(1, 1);
        protocolHandler.setStreams(dummy.getInputStream(), dummy.getOutputStream());
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    @After
    public void tearDown() throws Exception {
        protocolHandler.close();
        protocolHandler = null;
        dummy = null;
    }

    /**
     * Test of setReadRequest method, of class ProtocolHandler.
     *
     * @throws Exception DOCUMENT ME!
     */
    @Test
    public void testSetReadRequest() throws Exception {
        dummy.readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));

        DataContainer container = new SegmentedDataContainer(16);

        for (int i : DataPoint.BLOCKS_16) {
            container.addToDataContainer(i, 16);
        }

        protocolHandler.setReadRequest(container);

        synchronized (container) {
            container.wait(container.getDataBlockCount() * 60000);
        }

        String[] expected = dummy.streamToString(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt")).split("\n");
        String[] result = container.toString().split("\n");
        assertEquals("Length mismatch", expected.length, result.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals("Index " + i, expected[i], result[i]);
        }
    }

    /**
     * Test of setWriteRequest method, of class ProtocolHandler.
     *
     * @throws Exception DOCUMENT ME!
     */
    @Test
    public void testSetWriteRequest() throws Exception {
        System.out.println("setWriteRequest");

        DataContainer container = new SimpleDataContainer();

        container.addToDataContainer(DataPoint.M2_CONFIG_SHIFT);
        DataPoint.M2_CONFIG_SHIFT.encode(container, (byte) 22);
        protocolHandler.setWriteRequest(container);

        synchronized (container) {
            container.wait(container.getDataBlockCount() * 60000);
        }

        assertEquals((byte) 22, DataPoint.M2_CONFIG_SHIFT.decode(dummy));
    }
}
