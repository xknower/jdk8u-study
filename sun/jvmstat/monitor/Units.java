package sun.jvmstat.monitor;

/**
 * Provides a typesafe enumeration for describing units of measurement
 * attribute for instrumentation objects.
 *
 * @author   Brian Doherty
 */
public class Units implements java.io.Serializable {

    /* The enumeration values for this typesafe enumeration must be
     * kept in synchronization with the Units enum in the perfData.hpp file
     * in the HotSpot source base.
     */

    private static final int NUNITS=8;

    private static Units[] map = new Units[NUNITS];

    private final String name;
    private final int value;

    /**
     * An Invalid Units value.
     */
    public static final Units INVALID = new Units("Invalid", 0);

    /**
     * Units attribute representing unit-less quantities.
     */
    public static final Units NONE = new Units("None", 1);

    /**
     * Units attribute representing Bytes.
     */
    public static final Units BYTES = new Units("Bytes", 2);

    /**
     * Units attribute representing Ticks.
     */
    public static final Units TICKS = new Units("Ticks", 3);

    /**
     * Units attribute representing a count of events.
     */
    public static final Units EVENTS = new Units("Events", 4);

    /**
     * Units attribute representing String data. Although not really
     * a unit of measure, this Units value serves to distinguish String
     * instrumentation objects from instrumentation objects of other types.
     */
    public static final Units STRING = new Units("String", 5);

    /**
     * Units attribute representing Hertz (frequency).
     */
    public static final Units HERTZ = new Units("Hertz", 6);

    /**
     * Returns a string describing this Unit of measurement attribute
     *
     * @return String - a descriptive string for this enum.
     */
    public String toString() {
        return name;
    }

    /**
     * Returns the integer representation of this Units attribute
     *
     * @return int - an integer representation of this Units attribute.
     */
    public int intValue() {
        return value;
    }

    /**
     * Maps an integer value to its corresponding Units attribute.
     * If the integer value does not have a corresponding Units enum
     * value, then {@link Units#INVALID} is returned.
     *
     * @param value an integer representation of counter Units
     * @return Units - the Units object for the given <code>value</code>
     *                 or {@link Units#INVALID} if out of range.
     */
    public static Units toUnits(int value) {

        if (value < 0 || value >= map.length || map[value] == null) {
            return INVALID;
        }

        return map[value];
    }

    private Units(String name, int value) {
        this.name = name;
        this.value = value;
        map[value] = this;
    }

    private static final long serialVersionUID = 6992337162326171013L;
}
