package ola.driver.actions;

import ola.OlaAsyncClient;
import ola.OlaCallback;
import ola.RequestStatus;
import ola.object.Device;
import ola.object.Port;
import ola.object.Universe;
import ola.proto.Ola;

import java.util.List;

/**
 * Calls the available universes asynchronously.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:39 PM
 */
public class GetUniverse extends AbstractAction
{

    /**
     * Creates a new {@code GetUniverse} action.
     * @param client the client called by the action.
     * @throws NullPointerException if {@code client} is {@code null}.
     */
    public GetUniverse(OlaAsyncClient client)
    {
        super(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOption()
    {
        return "Get universe details.";
    }

    /**
     * Gets all of the universes asynchronously.
     */
    @Override
    public void execute()
    {
        client.getUniverses(new OlaCallback<List<Universe>>()
        {
            @Override
            public void processUpdate(RequestStatus status,
                                      List<Universe> receivedData)
            {
                System.out.printf("The status is: %s\n", status.getState());
                System.out.printf("The number of universes: %s\n",
                        receivedData.size());

                for(Universe universe : receivedData)
                {
                    System.out.printf("Universe ID %d\n", universe.getId());
                    System.out.printf("Universe Name %s\n", universe.getName());
                    System.out.printf("Universe Merge Mode %s\n",
                            universe.getMergeMode());
                }
            }
        });
    }
}
