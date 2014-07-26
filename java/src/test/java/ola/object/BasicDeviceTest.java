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

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the {@code Device} class.
 */
public class BasicDeviceTest
{

    /** ID used for testing. */
    private static final String TEST_ID = "Test ID";

    /** Alias used to testing. */
    private static final int TEST_ALIAS = 1;

    /** Name used for testing. */
    private static final String TEST_NAME=  "Test Name";

    /** Plugin ID used for testing. */
    private static final int TEST_PLUGIN_ID = 2;

    /** Port used for testing. */
    private static final Port PORT1 = new Port(1, 20, true, "", false);

    /** Another port used for testing. */
    private static final Port PORT2 = new Port(2, 20, true, "", false);

    /** Device used for testing. */
    private Device testDevice;

    /**
     * Setup a {@code Device} for testing.
     */
    @Before
    public void setupTest() {
        List<Port> inputPorts = new ArrayList<Port>();
        inputPorts.add(PORT1);
        List<Port> outputPorts = new ArrayList<Port>();
        outputPorts.add(PORT2);
        testDevice = new BasicDevice(TEST_ID, TEST_ALIAS, TEST_NAME,
                TEST_PLUGIN_ID, inputPorts, outputPorts);

    }

    /**
     * Test the ID getter.
     */
    @Test
    public void testDeviceId() {

        assertEquals(TEST_ID, testDevice.getId());
    }

    /**
     * Test the alias getter.
     */
    @Test
    public void testAlias() {

        assertEquals(TEST_ALIAS, testDevice.getAlias());
    }

    /**
     * Test the device name getter.
     */
    @Test
    public void testDeviceName() {

        assertEquals(TEST_NAME, testDevice.getName());
    }

    /**
     * Test the plugin ID getter.
     */
    @Test
    public void testPlugId() {

        assertEquals(TEST_PLUGIN_ID, testDevice.getPluginId());
    }

    /**
     * Check the input port getter.
     */
    @Test
    public void checkInputPort() {

        List<Port> inputPorts = testDevice.getInputPorts();
        assertEquals(1, inputPorts.size());
        assertEquals(PORT1, inputPorts.get(0));
    }

    /**
     * Test the output port getter.
     */
    @Test
    public void checkOutputPort() {

        List<Port> outputPorts = testDevice.getOutputPorts();
        assertEquals(1, outputPorts.size());
        assertEquals(PORT2, outputPorts.get(0));
    }

    /**
     * Test that the device is sorting the input and output port by ID.
     */
    @Test
    public void testSortedPorts() {

        List<Port> ports = new ArrayList<Port>(2);
        ports.add(0, PORT2);
        ports.add(1, PORT1);

        Device device = new BasicDevice(TEST_ID, TEST_ALIAS, TEST_NAME,
                TEST_PLUGIN_ID, ports, ports);
        List<Port> inputPorts =  device.getInputPorts();
        List<Port> outputPorts =  device.getOutputPorts();

        assertEquals(PORT1, inputPorts.get(0));
        assertEquals(PORT2, inputPorts.get(1));

        assertEquals(PORT1, outputPorts.get(0));
        assertEquals(PORT2, outputPorts.get(1));
    }

    /**
     * Test passing {@code null} for the input port.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputPort() {

        new BasicDevice(TEST_ID, TEST_ALIAS, TEST_NAME,
                TEST_PLUGIN_ID, null, new ArrayList<Port>());
    }

    /**
     * Test passing {@code null} for the output port.
     */
    @Test(expected = NullPointerException.class)
    public void testNullOutputPort() {

        new BasicDevice(TEST_ID, TEST_ALIAS, TEST_NAME,
                TEST_PLUGIN_ID, new ArrayList<Port>(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullId()
    {
        new BasicDevice(null, TEST_ALIAS, TEST_NAME,
                TEST_PLUGIN_ID, new ArrayList<Port>(), new ArrayList<Port>());
    }
}
