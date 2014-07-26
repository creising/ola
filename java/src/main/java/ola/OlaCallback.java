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
 * Generic callback that will be used to receive data asynchronously from the
 * OLA server.
 * @param <T> the data received from the OLA server.
 */
public interface OlaCallback<T> {
    /**
     * Called once an the requested data is received from
     * the OLA server.
     *
     * @param status        the status of the request.
     * @param receivedData  the data received from the server.
     */
    public void processUpdate(RequestStatus status, T receivedData);

}
