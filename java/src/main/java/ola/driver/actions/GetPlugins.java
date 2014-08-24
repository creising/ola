package ola.driver.actions;

import ola.OlaAsyncClient;
import ola.OlaCallback;
import ola.RequestStatus;
import ola.object.Plugin;

import java.util.List;

/**
 * Calls the available plugins asynchronously.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:39 PM
 */
public class GetPlugins extends AbstractAction
{

    /**
     * Creates a new {@code GetPlugins} action.
     * @param client the client called by the action.
     * @throws NullPointerException if {@code client} is {@code null}.
     */
    public GetPlugins(OlaAsyncClient client)
    {
        super(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOption()
    {
        return "Get plugins";
    }

    /**
     * Calls the available plugins asynchronously.
     */
    @Override
    public void execute()
    {
        client.getPlugins(new OlaCallback<List<Plugin>>()
        {
            @Override
            public void processUpdate(RequestStatus status, List<Plugin> t)
            {
                for (Plugin plugin : t)
                {
                    System.out.printf("%s ID:%s\n", plugin.getName(),
                            plugin.getId());
                }
            }
        });
    }
}
