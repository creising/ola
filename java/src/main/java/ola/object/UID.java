/***********************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 *************************************************************************/
package ola.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a OLA UID.
 */
public class UID implements Comparable<UID> {

    /** The max manufacture ID. */
    public static final int MAX_MANUFACTURER_ID = 65535;

    /** The max device ID. */
    public static final long MAX_DEVICE_ID  =  4294967295L;

    /** For logging errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UID.class);

    /** The UID for all devices. */
    private static final UID ALL_DEVICES= new UID(MAX_MANUFACTURER_ID,
            MAX_DEVICE_ID);

    /** The manufacturers ID. */
    private final int manufacturerId;

    /** The device's ID. */
    private final long deviceId;

    /** A formatted string containing the manufacturer's ID and the device's
     *  ID. */
    private final String formattedString;

    /**
     * Creates a new {@code UID}.
     *
     * @param manufacturerId the ID of the manufacturer.
     * @param deviceId the ID of the manufacturer's device.
     */
    public UID(int manufacturerId, long deviceId) {

        if(!checkManufacturerBounds(manufacturerId)){
            throw new IllegalArgumentException("The manufacturer must be " +
                    "between 0 and " + MAX_MANUFACTURER_ID);
        }

        if(!checkDeviceBounds(deviceId)) {
            throw new IllegalArgumentException("The device must be between " +
                    "0 and " + MAX_DEVICE_ID);
        }

        this.manufacturerId = manufacturerId;
        this.deviceId = deviceId;
        formattedString = String.format("%04X:%08X", manufacturerId, deviceId);
    }

    /**
     * @return the manufacturerId
     */
    public int getManufacturerId() {
        return manufacturerId;
    }

    /**
     * Gets the device ID.
     *
     * @return the device ID.
     */
    public long getDeviceId() {
        return deviceId;
    }

    /**
     * Determines if this UID's device ID is a broadcast ID or not.
     *
     * @return {@code true} if the device ID is a broadcast ID {@code false}
     * otherwise.
     */
    public boolean isBroadcast() {
        return deviceId == MAX_DEVICE_ID;
    }

    @Override
    public String toString() {
        return formattedString;
    }

    @Override
    public int hashCode() {
        return formattedString.hashCode();
    }

    @Override
    public boolean equals(Object object){

        if(object == null) {
            return false;
        }
        // As per hash code's contract equal objects must have equal hash codes.
        // Since hash code is implemented based on the formatted string
        // we will compare objects based on their formatted string.
        return object.toString().equals(formattedString);
    }

    public int compareTo(UID other) {

        if (other == null) {
            return 1;
        }
        if (manufacturerId == other.manufacturerId) {
            return (Long.compare(deviceId, other.deviceId));
        }
        return (Integer.compare(manufacturerId, other.manufacturerId));
    }

    /**
     * Creates a broadcast UID for a given vendor.
     *
     * @param manufacturerId the ID of the vendor.
     * @return a broadcast UID for the given vendor.
     */
    public static UID allManufacturerDevices(int manufacturerId) {

        return new UID(manufacturerId, MAX_DEVICE_ID);
    }

    /**
     * Create a new UID from a string.
     *
     * @param uidStr The string representation of the UID, e.g. 00f0:12345678.
     * @return a UID created from the given string. If the string could not be
     * parsed into a valid UID {@code null} will be returned.
     * @throws NullPointerException if the input is {@code null}.
     * @throws NumberFormatException if the string cannot be parsed into two
     * numbers.
     */
    public static UID fromString(String uidStr) {

        String[] parts = uidStr.split(":");
        if (parts.length != 2) {
            LOGGER.error("The string must be two values separated by a colan. " +
                    "The value given was: {}", uidStr);
            return null;
        }

        int manufacturerId = Integer.parseInt(parts[0], 16);
        long deviceId = Long.parseLong(parts[1], 16);

        if (!checkManufacturerBounds(manufacturerId)) {
            LOGGER.error("The manufacturer ID {} is not valid", manufacturerId);
            return null;
        }

        if(!checkDeviceBounds(deviceId)) {
            LOGGER.error("The device ID {} is not valid", manufacturerId);
            return null;
        }

        return new UID(manufacturerId, deviceId);
    }

    /**
     * Checks to see if the given ID is a valid manufacturer ID.
     *
     * @param number the number to check.
     * @return {@code true} if the ID is valid {@code false} if not.
     */
    private static boolean checkManufacturerBounds(int number) {

        return number >= 0  && number <= MAX_MANUFACTURER_ID;
    }

    /**
     * Checks to see if the given ID is a valid device ID.
     *
     * @param number the number to check.
     * @return {@code true} if the ID is valid {@code false} if not.
     */
    private static boolean checkDeviceBounds(long number) {

        return number >= 0  && number <= MAX_DEVICE_ID;
    }

    /**
     * Creates a new {@code UID} that is not equal to the given UID. If the
     * UID is a manufacturer's broadcast ID the device ID will be set to 0 and
     * the manufacturer's ID will be incremented by 1. Otherwise the
     * manufacturer's ID will not change and the device ID will be incremented
     * by 1.
     *
     * @param uid the {@code UID} to create a new {@code UID} from.
     * @return a new {@code UID}.
     * @throws UIDOutOfRangeException if the manufacturer and device ID are at
     * their max values.
     * @throws NullPointerException if {@code uid} is {@code null}.
     */
    public static UID nextUID(UID uid) {

        if (uid == UID.allDevices()) {
            LOGGER.error("UID {} cannot be incremented", uid);
            throw new UIDOutOfRangeException(uid);
        }

        if (uid.isBroadcast()) {
            return new UID(uid.manufacturerId + 1, 0);
        }
        else {
            return new UID(uid.manufacturerId, uid.deviceId + 1);
        }
    }

    /**
     * Creates a new {@code UID} that is not equal to the given UID. If the
     * device ID is zero the new device ID will be set to a broadcast ID and
     * the manufacturer's ID will be decremented by 1. Otherwise the
     * manufacturer's ID will not change and the device ID will be decremented
     * by 1.
     *
     * @param uid the {@code UID} to create a new {@code UID} from.
     * @return a new {@code UID}.
     * @throws UIDOutOfRangeException if the manufacturer and device ID are at
     * their max values.
     * @throws NullPointerException if {@code uid} is {@code null}.
     */
    public static UID previousUID(UID uid)
            throws UIDOutOfRangeException {

        if (uid.getManufacturerId() == 0 && uid.getDeviceId() == 0) {
            LOGGER.error("UID {} cannot be decremented", uid);
            throw new UIDOutOfRangeException(uid);
        }

        if (uid.deviceId == 0) {
            return new UID(uid.manufacturerId - 1, MAX_DEVICE_ID);
        }
        else {
            return new UID(uid.manufacturerId, uid.deviceId - 1);
        }
    }

    /**
     * Creates a new {@code UID} for all devices.
     *
     * @return a new UID set to all devices.
     */
    public static UID allDevices() {
        return ALL_DEVICES;
    }
}
