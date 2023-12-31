package com.sun.tracing.dtrace;

/**
 * Enumeration for the DTrace stability levels.
 *
 * @see <a href="http://docs.sun.com/app/docs/doc/817-6223/6mlkidlnp?a=view">Solaris Dynamic Tracing Guide, Chapter 39: Stability</a>
 * @since 1.7
 */
public enum StabilityLevel {
    /**
     * The interface is private to DTrace and represents an implementation
     * detail of DTrace.
     */
    INTERNAL  (0),
    /**
     * The interface is private to Sun for use by other Sun products. It is
     * not yet publicly documented for use by customers and ISVs.
     */
    PRIVATE  (1),
    /**
     * The interface is supported in the current release but is scheduled
     * to be removed, most likely in a future minor release.
     */
    OBSOLETE (2),
    /**
     * The interface is controlled by an entity other than Sun.
     */
    EXTERNAL (3),
    /**
     * The interface gives developers early access to new or
     * rapidly changing technology or to an implementation artifact that is
     * essential for observing or debugging system behavior. A more
     * stable solution is anticipated in the future.
     */
    UNSTABLE (4),
    /**
     * The interface might eventually become Standard or Stable but is
     * still in transition.
     */
    EVOLVING (5),
    /**
     * The interface is a mature interface under Sun's control.
     */
    STABLE   (6),
    /**
     * The interface complies with an industry standard.
     */
    STANDARD (7);

    String toDisplayString() {
        return toString().substring(0,1) +
               toString().substring(1).toLowerCase();
    }

    public int getEncoding() { return encoding; }

    private int encoding;

    private StabilityLevel(int encoding) {
        this.encoding = encoding;
    }
}

