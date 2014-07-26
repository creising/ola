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
 * Represents an OLA port.
 */
public class Port implements Comparable<Port> {

    /** The ID of the port. */
    private final int id;

    /** The ID of the universe. */
    private final int universeId;

    /** {@code true} if the port is active {@code false} if not. */
    private final boolean active;

    /** {@code true} if the port supports RDM {@code false} if not. */
    private final boolean supportsRDM;

    /** A description of the port. */
    private final String description;

    /**
     * Creates a new {@code Port}.
     *
     * @param id          the unique id of this port.
     * @param universe    the universe that this port belongs to.
     * @param active      {@code true} if this port is active {@code false}
     *                    otherwise.
     * @param description the description of the port.
     * @param supportsRDM {@code true} if the port supports RDM {@code false}
     *                    otherwise.
     */
    public Port(int id, int universe, boolean active, String description,
                boolean supportsRDM) {

        this.id = id;
        this.universeId = universe;
        this.description = description;
        this.supportsRDM = supportsRDM;
        this.active = active;
    }

    /**
     * Gets the unique id of this port.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the universe this port is associated with.
     *
     * @return the universe.
     */
    public int getUniverse() {
        return universeId;
    }

    /**
     * Gets the active state of this port.
     *
     * @return {@code true} if this port is active {@code false} if not.
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Gets a description of this port.
     *
     * @return a subscription of this port.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets whether or not this port supports RDM.
     *
     * @return {@code true} if this port supports RDM {@code false} if not.
     */
    public boolean supportsRDM() {
        return supportsRDM;
    }

    /**
     * Compares this port to another port by their ID.
     *
     * @param otherPort the port to compare this port to.
     * @return 0 if the port IDs are equal; a value less than 0 if this port's
     * ID is less than {@code otherPort}'s ID; and a value greater than 0 if
     * this port's ID is  greater than {@code otherPort}'s ID.
     */
    public int compareTo(Port otherPort) {
        return Integer.compare(id, otherPort.getId());
    }
}
