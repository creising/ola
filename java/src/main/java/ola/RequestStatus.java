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

import com.google.protobuf.RpcController;

/**
 * Represents the status of a request
 */
public class RequestStatus
{

    private final State state; //the state of the operation
    private final String message; //an error message if it failed

    public RequestStatus(RpcController controller) {
        if (controller.failed()) {
            state = State.FAILED;
            message = controller.errorText();
        } else if (controller.isCanceled()) {
            state = State.CANCELLED;
            message = controller.errorText();
        } else {
            state = State.SUCCESS;
            message = null;
        }

    }

    /**
     * True if this request succeeded
     *
     * @return
     */
    public boolean succeeded() {
        return state == State.SUCCESS;
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }


    public enum State {
        SUCCESS(0),
        FAILED(1),
        CANCELLED(2);
        private int value;

        private State(int value) {
            this.value = value;
        }


    }

    ;


}

