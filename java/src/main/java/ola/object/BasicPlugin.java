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

/**
 * Represents an OLA plugin.
 */
public class BasicPlugin implements Plugin
{

    /** The plugin's ID. */
    private final int id;

    /** The name of the plugin. */
    private final String name;

    /**
     * Creates a new {@code Plugin} with the given ID and name.
     *
     * @param id   the id of this plugin
     * @param name the name of this plugin
     */
    public BasicPlugin(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Plugin name: " + name + " plugin ID " + id;
    }
}    

