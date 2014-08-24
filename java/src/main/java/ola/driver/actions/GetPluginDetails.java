package ola.driver.actions;

import ola.OlaAsyncClient;
import ola.OlaCallback;
import ola.RequestStatus;
import ola.driver.InputParser;
import ola.object.Plugin;

import java.util.List;

/**
 * Calls the available plugins asynchronously.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:39 PM
 */
public class GetPluginDetails extends AbstractAction
{

    /**
     * Creates a new {@code GetPlugins} action.
     * @param client the client called by the action.
     * @throws NullPointerException if {@code client} is {@code null}.
     */
    public GetPluginDetails(OlaAsyncClient client)
    {
        super(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOption()
    {
        return "Get plugin details";
    }

    /**
     * Calls the available plugins asynchronously.
     */
    @Override
    public void execute()
    {
        Integer id = parser.readInt("Enter the ID of the plugin you would " +
                "like details for.");
        if(id != null)
        {
            client.getPluginDescription(new OlaCallback<String>()
            {
                @Override
                public void processUpdate(RequestStatus status,
                                          String receivedData)
                {
                    System.out.printf("The status is: %s\n", status);
                    System.out.printf("The response is: %s\n", receivedData);
                }
            }, id);
        }
    }
}
