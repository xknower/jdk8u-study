package sun.management.counter;

/**
 * The base class for a performance counter.
 *
 * @author   Brian Doherty
 */
public interface Counter extends java.io.Serializable {

    /**
     * Returns the name of this performance counter
     */
    public String getName();

    /**
     * Returns the Units for this performance counter
     */
    public Units getUnits();

    /**
     * Returns the Variability for this performance counter
     */
    public Variability getVariability();

    /**
     * Returns true if this performance counter is a vector
     */
    public boolean isVector();

    /**
     * Returns the length of the vector
     */
    public int getVectorLength();

    /**
     * Returns an Object that encapsulates the data value of this counter
     */
    public Object getValue();

    /**
     * Returns <tt>true</tt> if this counter is an internal counter.
     */
    public boolean isInternal();

    /**
     * Return the flags associated with the counter.
     */
    public int getFlags();
}
