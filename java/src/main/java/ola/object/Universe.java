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

import ola.proto.Ola.MergeMode;

/**
 * Represents an OLA DMX universe.
 */
public class Universe {

    /** The universe's ID. */
    private final int id;

    /** The universe's name. */
    private final String name;

    /** The universe's merge mode. */
    private final MergeMode mergeMode;


    /**
     * Creates a new {@code Universe} with whose merge mode is set to HTP.
     * @param id    the universe id.
     * @param name  the name of this universe.
     */
    public Universe(int id, String name) {
        this(id, name, MergeMode.HTP);
    }

    /**
     * Creates a new {@code Universe}.
     *
     * @param id        the universe id.
     * @param name      the name of this universe.
     * @param mergeMode the merge mode this universe is using.
     * @throws IllegalArgumentException if {@code mergeMode} is {@code null}.
     */
    public Universe(int id, String name, MergeMode mergeMode) {

        this.id = id;
        this.name = name;
        if(mergeMode == null) {
            throw new IllegalArgumentException("Merge mode cannot be null");
        }
        this.mergeMode = mergeMode;
    }

    /**
     * Gets the ID of the universe.
     *
     * @return the universe's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the universe.
     *
     * @return the universe's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the merge mode of the universe.
     *
     * @return the universe's merge mode.
     */
    public MergeMode getMergeMode() {
        return mergeMode;
    }
}
