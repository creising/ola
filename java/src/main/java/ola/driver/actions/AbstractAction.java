package ola.driver.actions;

import ola.OlaAsyncClient;
import ola.driver.InputParser;

/**
 * Base implementation if an {@code Action}.
 *
 * author: creising
 * Date: 8/23/14
 * Time: 11:42 PM
 */
public abstract class AbstractAction implements Action
{

    /** The client called by the action. */
    protected final OlaAsyncClient client;

    /** Reads input from the user. */
    protected final InputParser parser = new InputParser();

    /**
     * Creates a new {@code AbstractAction}.
     *
     * @param client the client used to talk to OLA.
     * @throws NullPointerException if {@code client} is {@code null}.
     */
    public AbstractAction(OlaAsyncClient client)
    {
        if(client == null)
        {
            throw new NullPointerException("client cannot be null.");
        }
        this.client = client;
    }
}
