package com.sun.jdi;

/**
 * Thrown to indicate that there is an inconistency in the debug
 * information provided by the target VM. For example, this exception
 * is thrown if there is a type mismatch between a retrieved value's
 * runtime type and its declared type as reported by the target VM.
 *
 * @author Gordon Hirsch
 * @since  1.3
 */
@jdk.Exported
public class InconsistentDebugInfoException extends RuntimeException {
    private static final long serialVersionUID = 7964236415376861808L;
    public InconsistentDebugInfoException() {
        super();
    }

    public InconsistentDebugInfoException(String s) {
        super(s);
    }
}
