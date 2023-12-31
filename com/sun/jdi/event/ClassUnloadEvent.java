package com.sun.jdi.event;

import com.sun.jdi.*;

/**
 * Notification of a class unload in the target VM.
 * <p>
 * There are severe constraints on the debugger back-end during
 * garbage collection, so unload information is greatly limited.
 *
 * @see EventQueue
 * @see VirtualMachine
 *
 * @author Robert Field
 * @since  1.3
 */
@jdk.Exported
public interface ClassUnloadEvent extends Event {
    /**
     * Returns the name of the class that has been unloaded.
     */
    public String className();

    /**
     * Returns the JNI-style signature of the class that has been unloaded.
     */
    public String classSignature();
}
