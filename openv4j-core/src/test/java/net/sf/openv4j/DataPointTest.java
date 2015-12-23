/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import net.sf.openv4j.protocolhandlers.DataContainer;
import net.sf.openv4j.protocolhandlers.MemoryImage;
import org.easymock.classextension.EasyMock;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class DataPointTest {
    /**
     * Creates a new DataPointTest object.
     */
    public DataPointTest() {
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
    }

    /**
     * DOCUMENT ME!
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of addressBelongsToMe method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testAddressBelongsToMe() {
        System.out.println("addressBelongsToMe");

        int addr = 0;
        DataPoint instance = null;
        boolean expResult = false;
        boolean result = instance.addressBelongsToMe(addr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decode method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testDecode() {
        System.out.println("decode");

        MemoryImage dc = null;
        DataPoint instance = null;
        Object expResult = null;
        Object result = instance.decode(dc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encode of byte value method, of class DataPoint.
     */
    @Test
    public void testEncodeByte() {
        System.out.println("encode Byte");
        DataPoint instance = DataPoint.A1M1_CONFIG_OPERATING_TYPE;
        Byte b = 1;
        MemoryImage mi = EasyMock.createMock(MemoryImage.class);
        mi.setByte(instance.getAddr(), b.byteValue());
        EasyMock.replay(mi);
        instance.encode(mi, b);
        EasyMock.verify(mi);
    }

    /**
     * Test of encode of byte value method, of class DataPoint.
     */
    @Test
    public void testEncodeDouble() {
        System.out.println("encode Double");
        DataPoint instance = DataPoint.M2_CONFIG_SLOPE;
        Double d = 1.1;
        MemoryImage mi = EasyMock.createMock(MemoryImage.class);
        mi.setByte(instance.getAddr(), (byte)11);
        EasyMock.replay(mi);
        instance.encode(mi, d);
        EasyMock.verify(mi);
    }

    /**
     * Test of findByAddr method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testFindByAddr() {
        System.out.println("findByAddr");

        int addr = 0;
        DataPoint expResult = null;
        DataPoint result = DataPoint.findByAddr(addr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAccess method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetAccess() {
        System.out.println("getAccess");

        DataPoint instance = null;
        AccessType expResult = null;
        AccessType result = instance.getAccess();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAddr method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetAddr() {
        System.out.println("getAddr");

        DataPoint instance = null;
        int expResult = 0;
        int result = instance.getAddr();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAlternative method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetAlternative() {
        System.out.println("getAlternative");

        DataPoint instance = null;
        Alternative expResult = null;
        Alternative result = instance.getAlternative();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFactor method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetFactor() {
        System.out.println("getFactor");

        DataPoint instance = null;
        double expResult = 0.0;
        double result = instance.getFactor();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGroup method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetGroup() {
        System.out.println("getGroup");

        DataPoint instance = null;
        Group expResult = null;
        Group result = instance.getGroup();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLabel method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetLabel() {
        System.out.println("getLabel");

        DataPoint instance = null;
        String expResult = "";
        String result = instance.getLabel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLength method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetLength() {
        System.out.println("getLength");

        DataPoint instance = null;
        int expResult = 0;
        int result = instance.getLength();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetName() {
        System.out.println("getName");

        DataPoint instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSortedPoints method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetSortedPoints() {
        System.out.println("getSortedPoints");

        DataPoint[] expResult = null;
        DataPoint[] result = DataPoint.getSortedPoints();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetType() {
        System.out.println("getType");

        DataPoint instance = null;
        DataType expResult = null;
        DataType result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetValue() {
        System.out.println("getValue");

        DataPoint instance = null;
        Property expResult = null;
        Property result = instance.getValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValueKind method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testGetValueKind() {
        System.out.println("getValueKind");

        DataPoint instance = null;
        PropertyType expResult = null;
        PropertyType result = instance.getValueKind();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isInKnownBlock16 method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testIsInKnownBlock16() {
        System.out.println("isInKnownBlock16");

        int address = 0;
        boolean expResult = false;
        boolean result = DataPoint.isInKnownBlock16(address);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isInKnownBlocks16 method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testIsInKnownBlocks16() {
        System.out.println("isInKnownBlocks16");

        int address = 0;
        int length = 0;
        boolean expResult = false;
        boolean result = DataPoint.isInKnownBlocks16(address, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testNamingConventions() {
        for (DataPoint dp : DataPoint.values()) {
            String expectedName = String.format("%s_%s_%s", dp.getGroup().name(), dp.getValueKind().name(), dp.getValue().name());

            if (!dp.name().equals(expectedName)) {
                expectedName = String.format("%s_%s_%s_AT_0X%04X", dp.getGroup().name(), dp.getValueKind().name(), dp.getValue().name(), dp.getAddr());
                assertEquals(expectedName, dp.getName());
            }
        }
    }

    /**
     * Test of printAddresses method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testPrintAddresses() {
        System.out.println("printAddresses");

        StringBuilder sb = null;
        MemoryImage memImage = null;
        DataPoint.printAddresses(sb, memImage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printAll method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testPrintAll() {
        System.out.println("printAll");

        StringBuilder sb = null;
        DataContainer dc = null;
        DataPoint.printAll(sb, dc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printMatchingAddesses method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testPrintMatchingAddesses() {
        System.out.println("printMatchingAddesses");

        DataPoint dataPoint = null;
        MemoryImage mem = null;
        StringBuilder sb = null;
        DataPoint.printMatchingAddesses(dataPoint, mem, sb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testToString() {
        System.out.println("toString");

        MemoryImage mem = null;
        StringBuilder sb = null;
        DataPoint instance = null;
        instance.toString(mem, sb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testValueOf() {
        System.out.println("valueOf");

        String name = "";
        DataPoint expResult = null;
        DataPoint result = DataPoint.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of values method, of class DataPoint.
     */
    @Ignore
    @Test
    public void testValues() {
        System.out.println("values");

        DataPoint[] expResult = null;
        DataPoint[] result = DataPoint.values();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
