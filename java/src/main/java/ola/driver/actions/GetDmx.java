package ola.driver.actions;

import ola.OlaAsyncClient;
import ola.OlaCallback;
import ola.OlaDmxCallback;
import ola.RequestStatus;
import ola.object.Universe;

import java.util.List;

/**
 * Calls the DMX values for a universes asynchronously.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:39 PM
 */
public class GetDmx extends AbstractAction
{

    /**
     * Creates a new {@code GetDmx} action.
     *
     * @param client the client called by the action.
     * @throws NullPointerException if {@code client} is {@code null}.
     */
    public GetDmx(OlaAsyncClient client)
    {
        super(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOption()
    {
        return "Get DMX values.";
    }

    /**
     * Gets all of the universes asynchronously.
     */
    @Override
    public void execute()
    {
        int universe = parser.readInt("Enter universe number: ");
        client.getDmx(new OlaDmxCallback<short[]>()
        {
            @Override
            public void Run(RequestStatus status, short[] shorts, int universe)
            {
                System.out.printf("Request statue: %s\n", status.getState());
                System.out.printf("Universe number %d\n", universe);
                System.out.printf("Number of channels: %d\n", shorts.length);

                for(int i = 0  ; i < shorts.length ; i++)
                {
                    System.out.printf("CH %d @ %d, ", i+1, shorts[i]);
                }
            }
        }, universe);
    }
}
