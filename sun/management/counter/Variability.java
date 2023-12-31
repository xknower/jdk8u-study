package sun.management.counter;

/**
 * Provides a typesafe enumeration for the Variability attribute for
 * instrumentation objects.
 *
 * @author   Brian Doherty
 */
public class Variability implements java.io.Serializable {

    /* The enumeration values for this typesafe enumeration must be
     * kept in synchronization with the Variability enum in the perfData.hpp file
     * in the HotSpot source base.
     */

    private static final int NATTRIBUTES = 4;
    private static Variability[] map = new Variability[NATTRIBUTES];

    private String name;
    private int value;

    /**
     * An invalid Variablity value.
     */
    public static final Variability INVALID = new Variability("Invalid",0);

    /**
     * Variability attribute representing Constant counters.
     */
    public static final Variability CONSTANT = new Variability("Constant",1);

    /**
     * Variability attribute representing a Monotonically changing counters.
     */
    public static final Variability MONOTONIC = new Variability("Monotonic",2);

    /**
     * Variability attribute representing Variable counters.
     */
    public static final Variability VARIABLE = new Variability("Variable",3);

    /**
     * Returns a string describing this Variability attribute.
     *
     * @return String - a descriptive string for this enum.
     */
    public String toString() {
        return name;
    }

    /**
     * Returns the integer representation of this Variability attribute.
     *
     * @return int - an integer representation of this Variability attribute.
     */
    public int intValue() {
        return value;
    }

    /**
     * Maps an integer value its corresponding Variability attribute.
     * If the integer value does not have a corresponding Variability enum
     * value, the {@link Variability#INVALID} is returned
     *
     * @param value an integer representation of a Variability attribute
     * @return Variability - The Variability object for the given
     *                       <code>value</code> or {@link Variability#INVALID}
     *                       if out of range.
     */
    public static Variability toVariability(int value) {

        if (value < 0 || value >= map.length || map[value] == null) {
            return INVALID;
        }

        return map[value];
    }

    private Variability(String name, int value) {
        this.name = name;
        this.value = value;
        map[value]=this;
    }

    private static final long serialVersionUID = 6992337162326171013L;
}
