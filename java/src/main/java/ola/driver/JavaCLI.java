package ola.driver;

import ola.OlaAsyncClient;
import ola.driver.actions.Action;
import ola.driver.actions.GetPluginDetails;
import ola.driver.actions.GetPlugins;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code JavaCLI} utilizes the asynchronous OLA client to communicate
 * with a running instance of OLA. Currently, this is a utility to assist the
 * development of the OLA Java API.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:36 PM
 */
public class JavaCLI
{
    /** List of available actions. */
    private final List<Action> actions;

    /** Reads input from the user. */
    private final InputParser parser = new InputParser();

    /**
     * Creates a new {@code Driver}.
     *
     * @param actions the list of action avaiable to the user.
     */
    public JavaCLI(List<Action> actions)
    {
        this.actions = actions;

        while(true)
        {
            printList();
            Integer option = parser.readInt("Enter option ");
            if(option != null && option > 0 && option <= actions.size())
            {
                System.out.println("Running action " + option);
                actions.get(option - 1).execute();
            }
            else
            {
                System.out.printf("%d is not a valid option.\n", option);
            }
        }
    }

    /**
     * Prints all of the available options to the user.
     */
    private void printList()
    {
        System.out.println("------------------------------");

        for(int index = 0 ; index < actions.size() ; index++)
        {
            System.out.printf("%d: %s\n", index + 1,
                    actions.get(index).getOption());
        }

        System.out.println("------------------------------");

    }

    /**
     * Runs the driver.
     * @param args not used
     * @throws Exception if there is an error with the OLA client.
     */
    public static void main(String[] args) throws Exception
    {
        OlaAsyncClient testClient = new OlaAsyncClient();

        List<Action> actionList = new ArrayList<Action>();
        actionList.add(new GetPlugins(testClient));
        actionList.add(new GetPluginDetails(testClient));

        JavaCLI driver = new JavaCLI(actionList);
    }
}
