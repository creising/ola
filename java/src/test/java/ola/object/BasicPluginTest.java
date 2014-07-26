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
 * Test the {@code Plugin} class.
 */
public class BasicPluginTest
{

    /** ID used for testing. */
    private static final int ID = 1;

    /** Name used for testing. */
    private static final String NAME = "name";

    /** Plugin used in unit tests. */
    private BasicPlugin testBasicPlugin;

    /**
     * Create a plugin for testing.
     */
    @Before
    public void setup() {

        testBasicPlugin = new BasicPlugin(ID, NAME);
    }

    /**
     * Test getting the ID.
     */
    @Test
    public void testID() {

       assertEquals(ID, testBasicPlugin.getId());
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testName() {

        assertEquals(NAME, testBasicPlugin.getName());
    }
}
