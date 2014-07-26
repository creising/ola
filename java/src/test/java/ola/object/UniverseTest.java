package ola.object;

import ola.proto.Ola;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@code Universe} class.
 */
public class UniverseTest {

    /**
     * Test creating a {@code Universe} that default to HTP.
     */
    @Test
    public void testUniverse() {

        int id = 1;
        String name = "name";
        Universe universe = new Universe(id, name);
        assertEquals(id, universe.getId());
        assertEquals(name, universe.getName());
        assertEquals(Ola.MergeMode.HTP, universe.getMergeMode());
    }

    /**
     * Test creating a {@code Universe}.
     */
    @Test
    public void testUniverse2() {

        int id = 1;
        String name = "name";
        Ola.MergeMode mergeMode = Ola.MergeMode.HTP;
        Universe universe = new Universe(id, name);
        assertEquals(id, universe.getId());
        assertEquals(name, universe.getName());
        assertEquals(mergeMode, universe.getMergeMode());
    }

    /**
     * Test creating a {@code Universe} with {@code null} merge mode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullMergeMode() {
        new Universe(1, "name", null);
    }
}
