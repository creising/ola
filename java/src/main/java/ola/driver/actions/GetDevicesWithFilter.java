package ola.driver.actions;

import ola.OlaAsyncClient;
import ola.OlaCallback;
import ola.RequestStatus;
import ola.driver.InputParser;
import ola.object.Device;
import ola.object.Port;
import ola.proto.Ola;

import java.util.List;

/**
 * Calls the available plugins asynchronously.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:39 PM
 */
public class GetDevicesWithFilter extends AbstractAction
{

    /**
     * Creates a new {@code GetPlugins} action.
     * @param client the client called by the action.
     * @throws NullPointerException if {@code client} is {@code null}.
     */
    public GetDevicesWithFilter(OlaAsyncClient client)
    {
        super(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOption()
    {
        return "Get devices with filter.";
    }

    /**
     * Gets all of the devices asynchronously.
     */
    @Override
    public void execute()
    {
        client.getDevices(new OlaCallback<List<Device>>()
        {
            @Override
            public void processUpdate(RequestStatus status,
                                      List<Device> receivedData)
            {
                System.out.printf("The status is: %s\n", status);
                for(Device device : receivedData)
                {
                    System.out.println("----------------------");
                    System.out.printf("ID %s\n", device.getId());
                    System.out.printf("Alias %s\n", device.getAlias());
                    System.out.printf("Name %s\n", device.getName());
                    System.out.printf("Plugin ID %s\n", device.getPluginId());
                    System.out.printf("Input ports: ");
                    printPorts(device.getInputPorts());
                    System.out.printf("Output ports: ");
                    printPorts(device.getOutputPorts());
                }
            }
        }, Ola.PluginIds.OLA_PLUGIN_ALL);
    }

    /**
     * Prints the port information within the provided list.
     *
     * @param ports the list of ports to print.
     */
    private void printPorts(List<Port> ports)
    {
        for(Port port : ports)
        {
            System.out.printf("Port ID %d\n", port.getId());
            System.out.printf("Universe %d\n", port.getUniverse());
            System.out.printf("Is Active %b\n", port.getActive());
            System.out.printf("Supports RDM %b\n", port.supportsRDM());
            System.out.printf("Description %s\n", port.getDescription());
        }
    }
}
