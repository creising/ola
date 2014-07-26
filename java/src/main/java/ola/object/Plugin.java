package ola.object;

/**
 * Represents an OLA plugin.
 */
public interface Plugin
{
    /**
     * Gets the ID of the plugin.
     *
     * @return the plugin's id.
     */
    int getId();

    /**
     * Gets the name of the plugin.
     *
     * @return the plugin's name. Can be {@code null}.
     */
    String getName();
}
