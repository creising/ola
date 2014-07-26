package ola.object;

import java.util.List;

/**
 * Represents an OLA device.
 */
public interface Device
{
    /**
     * Get the unique ID of the device.
     *
     * @return Never {@code null}.
     */
    String getId();

    /**
     * Gets the device's name.
     *
     * @return the name.
     */
    int getAlias();

    /**
     * Gets the device's name.
     *
     * @return the name.
     */
    String getName();

    /**
     * Gets the plugin ID.
     *
     * @return the plugin ID.
     */
    int getPluginId();

    /**
     * Gets a copy of the input ports sorted by ID. If the device does not have
     * any input ports an empty list will be returned.
     *
     * @return a copy of the input ports sorted by ID.
     */
    List<Port> getInputPorts();

    /**
     * Gets a copy of the output ports sorted by ID. If the device does not have
     * any input ports an empty list will be returned.
     *
     * @return a copy of the output ports sorted by ID.
     */
    List<Port> getOutputPorts();
}
