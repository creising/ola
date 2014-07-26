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
package ola;


/**
 * Callback to be used when asking for dmx information from the ola server
 */
public interface OlaDmxCallback<T> {
    /**
     * The function that will be called
     *
     * @param status   the status of the request
     * @param t        the data received as a generic.
     * @param universe the universe where the data comes from
     */
    public void Run(RequestStatus status, T t, int universe);

}
