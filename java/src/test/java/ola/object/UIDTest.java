package ola.object;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the {@code UID} class.
 */
public class UIDTest {

    /**
     * Test creating a valid UID.
     */
    @Test
    public void testUID() {

        int manufacturerID = 0;
        int deviceID = 0;
        UID uid = new UID(manufacturerID, deviceID);
        assertEquals(manufacturerID, uid.getManufacturerId());
        assertEquals(deviceID, uid.getDeviceId());
        assertEquals("0000:00000000", uid.toString());
    }

    /**
     * Test a UID with an invalid manufacturer upper bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testManufacturerInvalidUpperBounds() {

        new UID(65536, 1);
    }

    /**
     * Test a UID with an invalid manufacturer lower bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testManufacturerInvalidLowerBounds() {

        new UID(-1, 1);
    }

    /**
     * Test a UID with an invalid device upper bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeviceInvalidUpperBounds() {

        new UID(1, 4294967296L);
    }

    /**
     * Test a UID with an invalid manufacturer lower bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeviceInvalidLowerBounds() {

        new UID(1, -1);
    }

    /**
     * Test checking if the UID is a broadcast for a manufacturer.
     */
    @Test
    public void testIsBroadcast() {

        UID uid = new UID(1, UID.MAX_DEVICE_ID);
        assertTrue(uid.isBroadcast());
    }

    /**
     * Test the equals and hash code methods.
     */
    @Test
    public void testEquals() {

        int manufacturerID = 1;
        int deviceID = 2;
        UID uid = new UID(manufacturerID, deviceID);
        UID uid2 = new UID(manufacturerID, deviceID);
        assertTrue(uid.equals(uid2));
        assertEquals(uid.hashCode(), uid2.hashCode());
    }

    @Test
    public void testNullEquals() {

        int manufacturerID = 1;
        int deviceID = 2;
        UID uid = new UID(manufacturerID, deviceID);
        assertFalse(uid.equals(null));
    }

    /**
     * Test comparing a UID to {@code null}.
     */
    @Test
    public void testCompareWithNull() {

        int manufacturerID = 1;
        int deviceID = 2;
        UID uid = new UID(manufacturerID, deviceID);
        assertEquals(1, uid.compareTo(null));
    }

    /**
     * Test comparing with matching manufacturer IDs.
     */
    @Test
    public void testCompareWithMatchingManufacIds() {

        int manufacturerID = 1;
        int deviceID = 2;
        UID uid = new UID(manufacturerID, deviceID);
        UID uid2 = new UID(manufacturerID, deviceID+1);
        int result = uid.compareTo(uid2);
        assertTrue(result < 0);
    }

    /**
     * Test comparing with different manufacturer IDs.
     */
    @Test
    public void testCompareWithUniqueManufacIds() {

        int manufacturerID = 1;
        int deviceID = 2;
        UID uid = new UID(manufacturerID + 1, deviceID);
        UID uid2 = new UID(manufacturerID, deviceID);
        int result = uid.compareTo(uid2);
        assertTrue(result > 0);
    }

    /**
     * Test creating a broadcast UID for a manufacturer.
     */
    @Test
    public void testBroadcastDeviceId() {

        int manufacturerID = 1;
        UID uid = UID.allManufacturerDevices(manufacturerID);
        assertEquals(manufacturerID, uid.getManufacturerId());
        assertEquals(UID.MAX_DEVICE_ID, uid.getDeviceId());
    }

    /**
     * Test creating a UID from a string.
     */
    @Test
    public void testUidFromString() {

        String testString = "ffff:ffffffff";
        UID uid = UID.fromString(testString);
        assertEquals(UID.MAX_MANUFACTURER_ID, uid.getManufacturerId());
        assertEquals(UID.MAX_DEVICE_ID, uid.getDeviceId());
    }

    /**
     * Test creating a UID from a string with the incorrect format.
     */
    @Test
    public void testInvalidNumberOfValues() {

        assertNull(UID.fromString("1:2:3"));
    }

    /**
     * Test creating a UID from a string with an invalid manufacturer ID.
     */
    @Test
    public void testInvalidManufacturer() {

        assertNull(UID.fromString("10000:0"));
    }

    /**
     * Test creating a UID from a string with a string as the manufacturer ID.
     */
    @Test(expected = NumberFormatException.class)
    public void testInvalidManufactureFormat() {

        UID.fromString("text:0");
    }

    /**
     * Test creating a UID from a string with a string as the device ID.
     */
    @Test(expected = NumberFormatException.class)
    public void testInvalidDeviceFormat() {

        UID.fromString("0:text");
    }

    /**
     * Test creating a UID from a string with an invalid device ID.
     */
    @Test
    public void testInvalidDevice() {

        assertNull(UID.fromString("0:100000000"));
    }

    /**
     * Test creating a UID from a string with a {@code null} UID.
     */
    @Test(expected = NullPointerException.class)
    public void testNullString() {

        assertNull(UID.fromString(null));
    }

    /**
     * Test getting the next UID.
     */
    @Test
    public void testNextId() {

        int manufacturerID = 1;
        int deviceID = 2;
        UID uid = new UID(manufacturerID, deviceID);
        UID nextUID = UID.nextUID(uid);
        assertEquals(manufacturerID, nextUID.getManufacturerId());
        assertEquals(deviceID + 1, nextUID.getDeviceId());
    }

    /**
     * Test getting the next UID from a UID that has its device set to the max.
     */
    @Test
    public void testNextIdWhenIdIsBroadcast() {

        int manufacturerID = 1;
        long deviceID = UID.MAX_DEVICE_ID;
        UID uid = new UID(manufacturerID, deviceID);
        UID nextUID = UID.nextUID(uid);
        assertEquals(manufacturerID + 1, nextUID.getManufacturerId());
        assertEquals(0, 0);
    }

    /**
     * Test getting the next ID when the UID passed in is maxed out.
     */
    @Test(expected = UIDOutOfRangeException.class)
    public void testMaxNextId() {

        UID.nextUID(UID.allDevices());
    }

    /**
     * Test getting the next UID from {@code null}.
     */
    @Test(expected = NullPointerException.class)
    public void testNullNextUid() {

        UID.nextUID(null);
    }

    /**
     * Test getting the previous ID.
     */
    @Test
    public void testPreviousId() {

        int manufacturerID = 1;
        int deviceID = 2;
        UID uid = new UID(manufacturerID, deviceID);
        UID nextUID = UID.previousUID(uid);
        assertEquals(manufacturerID, nextUID.getManufacturerId());
        assertEquals(deviceID - 1, nextUID.getDeviceId());
    }

    /**
     * Test getting the previous ID when the device is zero.
     */
    @Test
    public void testPreviousIdWhenIdIsZero() {

        int manufacturerID = 1;
        long deviceID = 0;
        UID uid = new UID(manufacturerID, deviceID);
        UID nextUID = UID.previousUID(uid);
        assertEquals(manufacturerID - 1, nextUID.getManufacturerId());
        assertEquals(UID.MAX_DEVICE_ID, nextUID.getDeviceId());
    }

    /**
     * Test getting the previous ID from a UID that is at its minimum value.
     */
    @Test(expected = UIDOutOfRangeException.class)
    public void testMinPreviousId() {

        UID.previousUID(new UID(0, 0));
    }

    /**
     * Test getting the previous ID from {@code null}/
     */
    @Test(expected = NullPointerException.class)
    public void testNullPreviousUid() {

        UID.previousUID(null);
    }
}
