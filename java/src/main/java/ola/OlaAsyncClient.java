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

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;
import ola.object.BasicDevice;
import ola.object.BasicPlugin;
import ola.object.Device;
import ola.object.Plugin;
import ola.object.Port;
import ola.object.Universe;
import ola.proto.Ola;
import ola.proto.Ola.Ack;
import ola.proto.Ola.DeviceConfigReply;
import ola.proto.Ola.DeviceConfigRequest;
import ola.proto.Ola.DeviceInfo;
import ola.proto.Ola.DeviceInfoReply;
import ola.proto.Ola.DeviceInfoRequest;
import ola.proto.Ola.DiscoveryRequest;
import ola.proto.Ola.DmxData;
import ola.proto.Ola.MergeMode;
import ola.proto.Ola.MergeModeRequest;
import ola.proto.Ola.OlaServerService;
import ola.proto.Ola.OptionalUniverseRequest;
import ola.proto.Ola.PatchAction;
import ola.proto.Ola.PatchPortRequest;
import ola.proto.Ola.PluginDescriptionReply;
import ola.proto.Ola.PluginDescriptionRequest;
import ola.proto.Ola.PluginIds;
import ola.proto.Ola.PluginInfo;
import ola.proto.Ola.PluginListReply;
import ola.proto.Ola.PluginListRequest;
import ola.proto.Ola.PortInfo;
import ola.proto.Ola.RDMDiscoveryRequest;
import ola.proto.Ola.RDMRequest;
import ola.proto.Ola.RDMResponse;
import ola.proto.Ola.RegisterAction;
import ola.proto.Ola.RegisterDmxRequest;
import ola.proto.Ola.TimeCode;
import ola.proto.Ola.TimeCodeType;
import ola.proto.Ola.UID;
import ola.proto.Ola.UIDListReply;
import ola.proto.Ola.UniverseInfo;
import ola.proto.Ola.UniverseInfoReply;
import ola.proto.Ola.UniverseNameRequest;
import ola.proto.Ola.UniverseRequest;
import ola.rpc.SimpleRpcController;
import ola.rpc.StreamRpcChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CLient used to communicate with olad
 */
public class OlaAsyncClient
{

    /** For logging. */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(OlaAsyncClient.class.getName());

    /** The OLA server service. */
    private final OlaServerService serverService;

    /** The RPC channel. */
    private final StreamRpcChannel channel;

    /** List of callbacks. */
    private final Map<Integer, OlaNewDataReceivedCallback<short[]>>
            universeCallbacks;

    /**
     * Creates a new instance of the asynchronous OLA controller.
     */
    public OlaAsyncClient()
    {

        universeCallbacks =
                new HashMap<Integer, OlaNewDataReceivedCallback<short[]>>();

        channel = new StreamRpcChannel();
        channel.connect();
        serverService = OlaServerService.Stub.newStub(channel);

    }

    //main test. To be moved to test classes
    public static void main(String[] args) {
        try {

            OlaAsyncClient client = new OlaAsyncClient();

            client.getPluginDescription(new OlaCallback<String>()
            {
                @Override
                public void processUpdate(RequestStatus status, String receivedData)
                {
                    System.out.println("status: " + status);
                    System.out.println("receivedData: " + receivedData);
                }
            }, 22322);
            /*
            OlaNewDataReceivedCallback<short[]> dc = new OlaNewDataReceivedCallback<short[]>() {
                @Override
                public void Run(short[] t) {
                    System.out.println("getdmx " + Short.toString(t[0]));
                }
            };

            client.registerUniverse(5, RegisterAction.REGISTER, dc, new OlaCallback<Boolean>() {
                @Override
                public void processUpdate(RequestStatus status, Boolean t) {
                    System.out.println("register");
                }
            });
            */
            System.out.println("At the end");
            // TODO: remove only for testing.
            while(true){}


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Generic method for making RPC Calls.
     *
     * @param method       Name of te RPC Method to call.
     * @param inputMessage the input message for the RPC call.
     * @param controller   the RPC controller.
     * @return true is the message failed, false if not.
     */
    private boolean callRpcMethod(String method,
                                  Message inputMessage,
                                  RpcController controller)
    {

        final Message[] outputMessage = new Message[1];


        RpcCallback<Message> cb = new RpcCallback<Message>()
        {
            @Override
            public void run(Message arg0)
            {
                outputMessage[0] = arg0;
            }
        };

        serverService.callMethod(
                serverService.getDescriptorForType().findMethodByName(method),
                controller, inputMessage, cb);


        return controller.failed();
    }

    /**
     * Generic method for making Rpc Calls passing a callback object.
     *
     * @param method       Name of te Rpc Method to call
     * @param inputMessage Input RpcMessage
     * @param controller   an RpcController
     * @param cb           the callback object to be call after the method is called
     * @return true if the message failed, false if not.
     */
    private boolean callRpcMethod(String method,
                                  Message inputMessage,
                                  RpcController controller,
                                  RpcCallback<Message> cb)
    {

        final Message[] outputMessage = new Message[1];
        controller.reset();


        serverService.callMethod(
                serverService.getDescriptorForType().findMethodByName(method),
                controller, inputMessage, cb);


        return controller.failed();
    }

    /**
     * Gets the list of plugins currently loaded in OLA.
     *
     * @param callback called once the retrieval is complete.
     */
    public void getPlugins(final OlaCallback<List<Plugin>> callback)
    {
        final SimpleRpcController controller = new SimpleRpcController();
        RpcCallback<Message> cb = new RpcCallback<Message>()
        {
            @Override
            public void run(Message message)
            {
                if(message instanceof PluginListReply)
                {
                    pluginsComplete(callback, controller,
                            (PluginListReply) message);
                }
                else
                {
                    throw new IllegalArgumentException("The callback was " +
                            "expecting the message to be an instance of " +
                            "PluginListReply, but got: " + message);
                }
            }
        };

        LOGGER.trace("Calling RPC method for GetPlugins.");

        callRpcMethod("GetPlugins", PluginListRequest.newBuilder().build(),
                controller, cb);
    }

    /**
     * Called once the request for the OLA plugin list is complete.
     *
     * @param callback the callback used to get the plugins.
     * @param controller the RPC controller.
     * @param response the plugin response.
     */
    private void pluginsComplete(OlaCallback<List<Plugin>> callback,
                                 SimpleRpcController controller,
                                 PluginListReply response)
    {
        RequestStatus status = new RequestStatus(controller);

        if (!status.succeeded())
        {
            LOGGER.warn("Call did not succeed.");
            callback.processUpdate(status, null);
        }
        else
        {
            LOGGER.debug("PluginListReply successful.");
            List<Plugin> pluginList = new ArrayList<Plugin>();
            for (PluginInfo plugin : response.getPluginList())
            {
                pluginList.add(new BasicPlugin(plugin.getPluginId(),
                        plugin.getName()));
            }

            callback.processUpdate(status, pluginList);
        }
    }

    /**
     * Gets the description information for a plugin
     *
     * @param callback the callbackObject to call once complete. The callback
     *                 two arguments the description string and a RequestStatus object
     * @param pluginId The id of the plugin to get the description
     * @return true if there was an error, false if not.
     */
    public boolean getPluginDescription(final OlaCallback<String> callback,
                                        int pluginId)
    {
        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>()
        {
            @Override
            public void run(Message message)
            {
                if(message instanceof PluginDescriptionReply)
                {
                    pluginDescriptionComplete(callback,
                            controller,
                            (PluginDescriptionReply) message);
                }
                else
                {
                    throw new IllegalArgumentException("The callback was " +
                            "expecting the message to be an instance of " +
                            "PluginDescriptionReply, but got: " + message);
                }
            }
        };

        PluginDescriptionRequest request = PluginDescriptionRequest.newBuilder()
                .setPluginId(pluginId)
                .build();

        return callRpcMethod("GetPluginDescription", request, controller, cb);
    }

    /**
     * Process the response for the OLA plugin description request.
     *
     * @param callback the OLA callback called after the request is complete.
     * @param controller the RPC controller used to detemrine if the request
     *                   was successful or not.
     * @param response the description reply.
     */
    private void pluginDescriptionComplete(OlaCallback<String> callback,
                                           SimpleRpcController controller,
                                           PluginDescriptionReply response)
    {
        RequestStatus status = new RequestStatus(controller);
        if (!status.succeeded())
        {
            LOGGER.warn("Status was not a success");
            callback.processUpdate(status, null);
        }
        else
        {
            LOGGER.debug("PluginDescriptionReply successful.");
            callback.processUpdate(status, response.getDescription());
        }
    }

    /**
     * Gets a filtered list of devices from the server.
     *
     * @param callback called once the request is complete.
     * @param pluginFilter specifics a filter for the plugins.
     * @return true if there was an error, false if not.
     */
    public boolean getDevices(final OlaCallback<List<Device>> callback,
                              PluginIds pluginFilter) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>()
        {

            @Override
            public void run(Message message)
            {
                if(message instanceof DeviceInfoReply)
                {
                    deviceInfoComplete(callback, controller, (DeviceInfoReply) message);
                }
                else
                {
                    throw new IllegalArgumentException("The callback was " +
                            "expecting the message to be an instance of " +
                            "DeviceInfoReply, but got: " + message);
                }
            }
        };

        DeviceInfoRequest request = DeviceInfoRequest.newBuilder()
                .setPluginId(pluginFilter.getNumber())
                .build();
        return callRpcMethod("GetDeviceInfo", request, controller, cb);
    }

    /**
     * Processes the result of a device info request.
     *
     * @param callback called once the processing is complete.
     * @param controller the RPC controller.
     * @param response the response to the request.
     */
    private void deviceInfoComplete(OlaCallback<List<Device>> callback,
                                    RpcController controller,
                                    DeviceInfoReply response)
    {
        RequestStatus status = new RequestStatus(controller);
        if (!status.succeeded())
        {
            callback.processUpdate(status, null);
            return;
        }

        List<Device> deviceList = new ArrayList<Device>();

        for (DeviceInfo deviceInfo : response.getDeviceList())
        {
            List<Port> inputPorts = new ArrayList<Port>();
            for (PortInfo port : deviceInfo.getInputPortList())
            {
                inputPorts.add(new Port(port.getPortId(),
                        port.getUniverse(), port.getActive(),
                        port.getDescription(), port.getSupportsRdm()));
            }

            List<Port> outputPorts = new ArrayList<Port>();

            for (PortInfo port : deviceInfo.getOutputPortList())
            {
                outputPorts.add(new Port(port.getPortId(), port.getUniverse(),
                        port.getActive(), port.getDescription(),
                        port.getSupportsRdm()));
            }

            deviceList.add(new BasicDevice(deviceInfo.getDeviceId(),
                    deviceInfo.getDeviceAlias(), deviceInfo.getDeviceName(),
                    deviceInfo.getPluginId(), inputPorts, outputPorts));
        }

        callback.processUpdate(status, deviceList);
    }

    /**
     * Requests a list of universes from the server.
     *
     * @param callback called once the request is complete.
     * @return true if there was an error, false if not.
     */
    public boolean getUniverses(final OlaCallback<List<Universe>> callback)
    {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>()
        {
            @Override
            public void run(Message message)
            {
                if(message instanceof UniverseInfoReply)
                {
                    universeInfoComplete(callback, controller,
                            (UniverseInfoReply) message);
                }
                else
                {
                    throw new IllegalArgumentException("The callback was " +
                            "expecting the message to be an instance of " +
                            "UniverseInfoReply, but got: " + message);
                }
            }
        };

        OptionalUniverseRequest request = OptionalUniverseRequest.newBuilder()
                .build();
        return callRpcMethod("GetUniverseInfo", request, controller, cb);
    }

    /**
     * Processes the response from a universe request.
     *
     * @param callback called once the processing is complete.
     * @param controller the RPC controller.
     * @param universeInfoReply the reply.
     */
    private void universeInfoComplete(OlaCallback<List<Universe>> callback,
                                      SimpleRpcController controller,
                                      UniverseInfoReply universeInfoReply)
    {
        RequestStatus status = new RequestStatus(controller);
        if (!status.succeeded())
        {
            callback.processUpdate(status, null);
            return;
        }

        List<Universe> universeList = new ArrayList<Universe>();
        for (UniverseInfo universe : universeInfoReply.getUniverseList())
        {
            universeList.add(new Universe(universe.getUniverse(),
                    universe.getName(), universe.getMergeMode()));
        }

        callback.processUpdate(status, universeList);
    }

    /**
     * Request DMX data from the server for a given universe ID.
     *
     * @param callback called once the request is complete.
     * @param universeId The universe id to get the data for.
     * @return true if there was an error, false if not.
     */
    public boolean getDmx(final OlaDmxCallback<short[]> callback,
                          int universeId)
    {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>()
        {
            @Override
            public void run(Message message)
            {
                if(message instanceof DmxData)
                {
                    dmxComplete(callback, controller, (DmxData) message);
                }
                else
                {
                    throwExceptionForInstanceOfFailure("DmxData", message);
                }
            }
        };
        UniverseRequest request = UniverseRequest.newBuilder()
                .setUniverse(universeId)
                .build();

        return callRpcMethod("GetDmx", request, controller, cb);
    }

    /**
     * Processes the response to a DMX request.
     *
     * @param callback called once the response is processed.
     * @param controller the RPC controller.`
     * @param dmxData the DMX data from the response.
     */
    private void dmxComplete(OlaDmxCallback<short[]> callback,
                             SimpleRpcController controller,
                             DmxData dmxData)
    {
        RequestStatus status = new RequestStatus(controller);

        if (!status.succeeded())
        {
            callback.Run(status, null, -1);
            return;
        }
        callback.Run(status, convertFromUnsigned(dmxData.getData()),
                dmxData.getUniverse());
    }

    /**
     * Send Dmx data to the server
     *
     * @param universeId The universe id to send the data for
     * @param values     an array object with the DMX data
     * @param callback   the callbackObject to call once complete. The callback
     *                   one argument a RequestStatus object
     * @return
     */
    public boolean sendDmx(int universeId, short[] values, final OlaCallback<Boolean> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                ackMessageComplete(callback, controller);
            }
        };

        DmxData dmxData = DmxData.newBuilder()
                .setUniverse(universeId)
                .setData(convertToUnsigned(values))
                .build();
        UniverseRequest request = UniverseRequest.newBuilder().setUniverse(universeId).build();
        callRpcMethod("UpdateDmxData", request, controller, cb);
        return true;
    }

    private void ackMessageComplete(OlaCallback<Boolean> callback, SimpleRpcController controller) {
        RequestStatus status = new RequestStatus(controller);
        if (!status.succeeded()) {
            callback.processUpdate(status, null);
            return;
        }
        callback.processUpdate(status, true);

    }

    /**
     * Set the name of a universe
     *
     * @param universeId the universe id to set the name of
     * @param name       the new name for the universe
     * @param callback   the callbackObject to call once complete. The callback
     *                   one argument a RequestStatus object
     * @return
     */
    public boolean setUniverseName(int universeId, String name, final OlaCallback<Boolean> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {

            @Override
            public void run(Message arg0) {
                ackMessageComplete(callback, controller);
            }
        };

        UniverseNameRequest request = UniverseNameRequest.newBuilder()
                .setUniverse(universeId)
                .setName(name)
                .build();

        callRpcMethod("SetUniverseName", request, controller, cb);
        return true;
    }

    /**
     * Set the merge mode of a universe
     *
     * @param universeId the universe id to set the name of
     * @param mode       the new {@link ola.proto.Ola.MergeMode} for the universe: HTP or LTP
     * @param callback   the callbackObject to call once complete. The callback
     *                   one argument a RequestStatus object
     * @return
     */
    public boolean setUniverseMergeMode(int universeId, MergeMode mode, final OlaCallback<Boolean> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                ackMessageComplete(callback, controller);
            }
        };

        MergeModeRequest request = MergeModeRequest.newBuilder()
                .setUniverse(universeId)
                .setMergeMode(mode)
                .build();

        callRpcMethod("SetMergeMode", request, controller, cb);
        return true;
    }

    /**
     * Register to receive dmx updates from a universe
     *
     * @param universeId   the universe id to register
     * @param action       the new {@link ola.proto.Ola.RegisterAction}
     *                     RegisterAction.REGISTER or RegisterAction.UNREGISTER
     * @param dataCallback the callbackObject to call when there is new data. The callback
     *                     one argument, the array of dmx data received
     * @param callback     the callbackObject to call once complete. The callback
     *                     one argument a RequestStatus object
     * @return
     */
    public boolean registerUniverse(int universeId, RegisterAction action, final OlaNewDataReceivedCallback<short[]> dataCallback, final OlaCallback<Boolean> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                ackMessageComplete(callback, controller);
            }
        };

        RegisterDmxRequest request = RegisterDmxRequest.newBuilder()
                .setUniverse(universeId)
                .setAction(action)
                .build();

        callRpcMethod("RegisterForDmx", request, controller, cb);
        if (action == RegisterAction.REGISTER) {
            universeCallbacks.put(universeId, dataCallback);
        } else {
            universeCallbacks.remove(universeId);
        }
        return true;
    }

    /**
     * Patch a port to a universe
     *
     * @param deviceAlias the alias to the device to configure
     * @param port        the id of the port
     * @param isOutput    Input or output port
     * @param action      the {@link ola.proto.Ola.PatchAction} : PATCH or UNPATCH
     * @param universeId  the universe id
     * @param callback    the callbackObject to call once complete. The callback
     *                    one argument a RequestStatus object
     * @return
     */
    public boolean patchPort(int deviceAlias, int port, boolean isOutput, PatchAction action, int universeId, final OlaCallback<Boolean> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                ackMessageComplete(callback, controller);
            }
        };

        PatchPortRequest patchRequest = PatchPortRequest.newBuilder()
                .setPortId(port)
                .setAction(action)
                .setDeviceAlias(deviceAlias)
                .setIsOutput(isOutput)
                .setUniverse(universeId)
                .setIsOutput(true)
                .build();

        callRpcMethod("PatchPort", patchRequest, controller, cb);
        return true;
    }

    /**
     * Send a device config request
     *
     * @param deviceAlias the alias of the device to configure
     * @param requestData The request to send to the device
     * @param callback    the callbackObject to call once complete. The callback
     *                    two arguments a response data array and a RequestStatus object
     * @return
     */
    public boolean configureDevice(int deviceAlias, short[] requestData, final OlaCallback<short[]> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                configureDeviceComplete(callback, controller, (DeviceConfigReply) arg0);
            }
        };

        DeviceConfigRequest request = DeviceConfigRequest.newBuilder()
                .setDeviceAlias(deviceAlias)
                .setData(convertToUnsigned(requestData))
                .build();

        callRpcMethod("ConfigureDevice", request, controller, cb);
        return true;
    }

    private void configureDeviceComplete(OlaCallback<short[]> callback, SimpleRpcController controller, DeviceConfigReply response) {
        RequestStatus status = new RequestStatus(controller);
        if (!status.succeeded()) {
            callback.processUpdate(status, null);
            return;
        }
        callback.processUpdate(status, convertFromUnsigned(response.getData()));

    }

    /**
     * Send Time Code Data
     *
     * @param type     a {@link ola.proto.Ola.TimeCodeType} enum value
     * @param hours    the hours
     * @param minutes  the minutes
     * @param seconds  the seconds
     * @param frames   the frame count
     * @param callback he callbackObject to call once complete. The callback
     *                 one argument a RequestStatus object
     * @return
     */
    public boolean sendTimeCode(TimeCodeType type, int hours, int minutes, int seconds, int frames, final OlaCallback<Boolean> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                ackMessageComplete(callback, controller);
            }
        };

        TimeCode request = TimeCode.newBuilder()
                .setFrames(frames)
                .setHours(hours)
                .setMinutes(minutes)
                .setSeconds(seconds)
                .setType(type)
                .build();

        callRpcMethod("SendTimeCode", request, controller, cb);
        return true;
    }

    /**
     * Method called when new DMX data is received
     *
     * @param controller A RpcController Object
     * @param request    A {@link ola.proto.Ola.DmxData} request
     * @param done       The RpcCallback to run once complete
     */

    //@Override
    public void updateDmxData(RpcController controller, DmxData request, RpcCallback<Ack> done) {
        if (universeCallbacks.containsKey(request.getUniverse())) {
            universeCallbacks.get(request.getUniverse())
                    .Run(convertFromUnsigned(request.getData()));
            done.run(Ack.getDefaultInstance());
        }

    }

    /**
     * Get the list of UIDS for a given universe
     *
     * @param universe the universe to get the UID list for
     * @param callback the callbackObject to call once complete. The callback
     *                 two arguments a List of UIDS and a RequestStatus object
     * @return
     */
    public boolean getUIDs(int universe, final OlaCallback<List<UID>> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                getUIDsComplete(callback, controller, (UIDListReply) arg0);
            }
        };

        UniverseRequest request = UniverseRequest.newBuilder().setUniverse(universe).build();

        callRpcMethod("GetUIDs", request, controller, cb);
        return true;
    }

    private void getUIDsComplete(OlaCallback<List<UID>> callback, SimpleRpcController controller, UIDListReply uidListReply) {
        ola.RequestStatus status = new RequestStatus(controller);
        if (!status.succeeded()) {
            callback.processUpdate(status, null);
            return;
        }
        List<UID> uids = new ArrayList<UID>();
        // TODO complete this
        /*
        for (ola.proto.Ola.UID uid : uidListReply.getUidList()) {
            try
            {
                uids.add(new UID(uid.getEstaId(), uid.getDeviceId()));
            } catch (InvalidProtocolBufferException e)
            {
                e.printStackTrace();
            }
        }
        Collections.sort(uids);
        */
        callback.processUpdate(status, uids);

    }

    /**
     * Triggers RDN discocery for a un
     *
     * @param universe
     * @param full     true to use full discovery, false for incremental (if supported)
     * @param callback the callbackObject to call once complete. The callback
     *                 two arguments a List of UIDS and a RequestStatus object
     * @return
     */
    public boolean runRDMDiscovery(int universe, boolean full, final OlaCallback<List<UID>> callback) {

        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message arg0) {
                getUIDsComplete(callback, controller, (UIDListReply) arg0);
            }
        };

        DiscoveryRequest request = DiscoveryRequest.newBuilder()
                .setUniverse(universe)
                .setFull(full)
                .build();

        callRpcMethod("ForceDiscovery", request, controller, cb);
        return true;
    }

    /**
     * Send an RDM command without data
     *
     * @param universe  The universe to send the rdm command data
     * @param uid       an uid object
     * @param subDevice the subdevice object     *
     * @param paramId   The param ID
     * @param callback  the callbackObject to call once complete. The callback
     *                  two arguments a RDMResponse and a RequestStatus object
     * @return
     */
    public boolean getRDM(int universe, UID uid, int subDevice, int paramId, final OlaCallback<RDMResponse> callback) {
        return getRDM(universe, uid, subDevice, paramId, callback, convertFromUnsigned(ByteString.EMPTY));
    }

    /**
     * Send an RDM command with data
     *
     * @param universe  The universe to send the rdm command data
     * @param uid       an uid object
     * @param subDevice the subdevice object     *
     * @param paramId   The param ID
     * @param callback  the callbackObject to call once complete. The callback
     *                  two arguments a RDMResponse and a RequestStatus object
     * @param data      the data to send
     * @return
     */
    public boolean getRDM(int universe, UID uid, int subDevice, int paramId, final OlaCallback<RDMResponse> callback, short[] data) {
        return RDMMessage(universe, uid, subDevice, paramId, callback, data, false);
    }

    public boolean setRDM(int universe, UID uid, int subDevice, int paramId, final OlaCallback<RDMResponse> callback) {
        return setRDM(universe, uid, subDevice, paramId, callback, convertFromUnsigned(ByteString.EMPTY));

    }

    public boolean setRDM(int universe, UID uid, int subDevice, int paramId, final OlaCallback<RDMResponse> callback, short[] data) {
        return RDMMessage(universe, uid, subDevice, paramId, callback, data, true);
    }
    //Basic implementation

    /**
     * Send a RDM Discovery command. Basic implementation, it just call the rpc without
     * abstracting the response
     *
     * @param universe  the universe to get the UID list for
     * @param uid       an UID object
     * @param subDevice the sub device index
     * @param paramId   the param ID
     * @param callback  the callbackObject to call once complete. The callback
     *                  two arguments a RDMResponse and a RequestStatus object
     * @param data      the data to send
     * @return
     */
    private boolean sendRawRDMDiscovery(int universe, UID uid, int subDevice, int paramId, final OlaCallback<RDMResponse> callback, short[] data) {
        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message pt) {
                callback.processUpdate(null, (RDMResponse) pt);
            }
        };

        ola.proto.Ola.UID protoUID = ola.proto.Ola.UID.newBuilder().setEstaId(uid.getEstaId()).setDeviceId(uid.getEstaId()).build();
        RDMDiscoveryRequest request = RDMDiscoveryRequest.newBuilder()
                .setUniverse(universe)
                .setUid(protoUID)
                .setSubDevice(subDevice)
                .setParamId(paramId)
                .setData(convertToUnsigned(data))
                .setIncludeRawResponse(true).build();

        callRpcMethod("rDMDiscoveryCommand", request, controller, cb);
        return true;


    }

//
//    /**
//     * Get candidate ports for universe.
//     *
//     * @param universe the id of the universe.
//     * @return device info
//     */
//    public DeviceInfoReply getCandidatePorts(int universe) {
//        OptionalUniverseRequest request = OptionalUniverseRequest.newBuilder().setUniverse(universe).build();
//        return (DeviceInfoReply) callRpcMethod("GetCandidatePorts", request);
//    }
//
//    /**
//     * Set port priority.
//     *
//     * @return true if request succeeded.
//     */
//    public boolean setPortPriority(int device, int port, int priority, int mode, boolean output) {
//        PortPriorityRequest request = PortPriorityRequest.newBuilder()
//            .setDeviceAlias(device)
//            .setPortId(port)
//            .setPriority(priority)
//            .setPriorityMode(mode)
//            .setIsOutput(output)
//            .build();
//
//        return callRpcMethod("SetPortPriority", request) != null;
//    }
//
//    /**
//     * Set source UID for device.
//     * @param device The id of the device
//     * @param estaId the UID to set.
//     * @return true if call succeeded.
//     */
//    public boolean setSourceUID(int device, int estaId) {
//        UID request = UID.newBuilder()
//                .setDeviceId(device)
//                .setEstaId(estaId)
//                .build();
//        return callRpcMethod("SetSourceUID", request) != null;
//    }

    private boolean RDMMessage(int universe, UID uid, int subDevice, int paramId, final OlaCallback<RDMResponse> callback, short[] data, boolean set) {
        final SimpleRpcController controller = new SimpleRpcController();

        RpcCallback<Message> cb = new RpcCallback<Message>() {
            @Override
            public void run(Message pt) {
                callback.processUpdate(null, (RDMResponse) pt);
            }
        };

        ola.proto.Ola.UID protoUID = ola.proto.Ola.UID.newBuilder().setEstaId(uid.getEstaId()).setDeviceId(uid.getEstaId()).build();
        RDMRequest request = RDMRequest.newBuilder()
                .setUniverse(universe)
                .setUid(protoUID)
                .setSubDevice(subDevice)
                .setParamId(paramId)
                .setData(convertToUnsigned(data)).setIsSet(set)
                .build();

        callRpcMethod("RDMCommand", request, controller, cb);
        return true;


    }

    /**
     * Throws an {@code IllegalArgumentException} to signify an instace of
     * checked failed.
     * @param type the name of the object you were expecting.
     * @param obj the actual object.
     */
    private void throwExceptionForInstanceOfFailure(String type, Object obj)
    {
        throw new IllegalArgumentException("The callback was " +
                "expecting the message to be an instance of " + type +
                " but got: " + obj);
    }

    /**
     * Convert short array to bytestring
     */
    public ByteString convertToUnsigned(short[] values) {
        byte[] unsigned = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            unsigned[i] = (byte) values[i];
        }
        return ByteString.copyFrom(unsigned);
    }

    /**
     * Convert bytestring to short array.
     */
    public short[] convertFromUnsigned(ByteString data) {
        byte[] values = data.toByteArray();
        short[] signed = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            signed[i] = (short) ((short) values[i] & 0xFF);
        }
        return signed;
    }
}
