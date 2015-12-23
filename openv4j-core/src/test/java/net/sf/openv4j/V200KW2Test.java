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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class V200KW2Test extends Device {
    /**
     * Creates a new V200KW2Test object.
     */
    public V200KW2Test() {
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
        super.mySetUp();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    @After
    public void tearDown() throws Exception {
        super.myTearDown();
    }

    /**
     * Test of setStreams method, of class PrintMemoryMap.
     *
     * @throws Exception DOCUMENT ME!
     */
    @Test
    public void testPrintAddresses() throws Exception {
        System.out.println("PrintAddresses");
        container.readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));

        StringBuilder sb = new StringBuilder();

        DataPoint.printAddresses(sb, container);

        System.out.print(sb.toString());
        System.out.println("PrintAddresses done");
    }

    /**
     * Test of setStreams method, of class PrintMemoryMap.
     *
     * @throws Exception DOCUMENT ME!
     */
    @Test
    public void testSearchAddresses() throws Exception {
        System.out.println("SearchAddresses");
        container.readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));

        StringBuilder sb = new StringBuilder();

        for (DataPoint p : DataPoint.values()) {
            switch (p.getValueKind()) {
                case TEMP_ACTUAL:
                case TEMP_DAMPED:
                case TEMP_LOW_PASS:
                case TEMP_NOMINAL:
                    DataPoint.printMatchingAddesses(p, container, sb);

                    break;

                default:
                    DataPoint.printMatchingAddesses(p, container, sb);

                    break;
            }
        }

        //        System.err.print(sb.toString());
        System.out.println("SearchAddresses done");
    }

    /**
     * Test of setStreams method, of class PrintMemoryMap.
     *
     * @throws Exception DOCUMENT ME!
     */
    @Test
    public void testToString() throws Exception {
        System.out.println("All");
        container.readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));

        System.out.println(container.toString());

        StringBuilder sb = new StringBuilder();
        DataPoint.printAll(sb, container);
        System.out.println(sb.toString());
        System.out.println("All done");
    }
}
