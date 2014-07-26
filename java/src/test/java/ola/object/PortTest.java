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

import static org.junit.Assert.assertEquals;

/**
 * Test the {@code Port} class.
 */
public class PortTest {

    /** Port test ID. */
    private static final int ID = 0;

    /** Test port universe number. */
    private static final int UNIVERSE = 1;

    /** Test port active state. */
    private static final boolean ACTIVE = true;

    /** Test port RDM support. */
    private static final boolean SUPPORTS_RPM = true;

    /** Test port description. */
    private static final String DESCRIPTION = "description";

    /** Port used for testing. */
    private Port testPort;

    /**
     * Setup port for testing.
     */
    @Before
    public void setup() {
        testPort = new Port(ID, UNIVERSE, ACTIVE, DESCRIPTION, SUPPORTS_RPM);
    }

    /**
     * Test getting the ID.
     */
    @Test
    public void testID() {
        assertEquals(ID, testPort.getId());
    }

    /**
     * Test getting the universe.
     */
    @Test
    public void testUniverse() {
        assertEquals(UNIVERSE, testPort.getUniverse());
    }

    /**
     * Test getting the description.
     */
    @Test
    public void testDescription() {
        assertEquals(DESCRIPTION, testPort.getDescription());
    }

    /**
     * Test getting the port's RDM support.
     */
    @Test
    public void testSupportsRDM() {
        assertEquals(SUPPORTS_RPM, testPort.supportsRDM());
    }
}
