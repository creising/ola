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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an OLA device.
 */
public class BasicDevice implements Device
{

    /** The unique ID of the device. */
    private final String id;

    /** The integer alias of the device. */
    private final int alias;

    /** The name of the device. */
    private final String name;

    /** The ID of the plugin this device belongs to. */
    private final int pluginId;

    /** The sorted list of input ports. */
    private final List<Port> inputPorts;

    /** The sorted list of output ports. */
    private final List<Port> outputPorts;

    /**
     * Creates a new {@code Device}.
     *
     * @param id          the unique id of this device
     * @param alias       the integer alias for this device
     * @param name        the name of this device
     * @param pluginId    the plugin that this device belongs to
     * @param inputPorts  a list of Input Port objects
     * @param outputPorts a list of Output Port objects
     * @throws NullPointerException if either {@code inputPorts}, {@code id} or
     *                              {@code outputPorts} is {@code null}.
     */
    public BasicDevice(String id, int alias, String name, int pluginId,
                       List<Port> inputPorts, List<Port> outputPorts) {

        if(id == null)
        {
            throw new NullPointerException("id cannot be null.");
        }
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.pluginId = pluginId;

        // We don't want to affect the order of the list given to us.
        this.inputPorts = new ArrayList<Port>(inputPorts);
        this.outputPorts = new ArrayList<Port>(outputPorts);

        // sort the ports by ID
        Collections.sort(this.inputPorts);
        Collections.sort(this.outputPorts);
    }

    /**
     * Gets the device's ID.
     *
     * @return the ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the device's integer alias.
     *
     * @return the integer alias.
     */
    public int getAlias() {
        return alias;
    }

    /**
     * Gets the device's name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the plugin ID.
     *
     * @return the plugin ID.
     */
    public int getPluginId() {
        return pluginId;
    }

    /**
     * Gets a copy of the input ports sorted by ID. If the device does not have
     * any input ports an empty list will be returned.
     *
     * @return a copy of the input ports sorted by ID.
     */
    public List<Port> getInputPorts() {

        // create a defensive copy of our list.
        return new ArrayList<Port>(inputPorts);
    }

    /**
     * Gets a copy of the output ports sorted by ID. If the device does not have
     * any input ports an empty list will be returned.
     *
     * @return a copy of the output ports sorted by ID.
     */
    public List<Port> getOutputPorts() {

        // create a defensive copy of our list.
        return new ArrayList<Port>(outputPorts);
    }
}
