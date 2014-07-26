package ola.driver.actions;

/**
 * The {@code Action} interface outlines the functions available to build a
 * simple CLI to OLA.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:37 PM
 */
public interface Action
{
    /**
     * Get the human readable text displayed to the user for the {@code Action}.
     *
     * @return Never {@code null}.
     */
    String getOption();

    /**
     * Called to execute the example.
     */
    void execute();
}
