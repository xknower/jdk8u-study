package com.sun.jdi.event;

import com.sun.jdi.*;
import com.sun.jdi.request.EventRequest;

/**
 * An occurrence in a target VM that is of interest to a debugger. Event is
 * the common superinterface for all events (examples include
 * {@link BreakpointEvent}, {@link ExceptionEvent},
 * {@link ClassPrepareEvent}).
 * When an event occurs, an instance of Event as a component of
 * an {@link EventSet} is enqueued in the
 * {@link VirtualMachine}'s {@link EventQueue}.
 *
 * @see EventSet
 * @see EventQueue
 *
 * @author Robert Field
 * @since  1.3
 */
@jdk.Exported
public interface Event extends Mirror {

    /**
     * @return The {@link EventRequest} that requested this event.
     * Some events (eg. {@link VMDeathEvent}) may not have
     * a cooresponding request and thus will return null.
     */
    EventRequest request();
}
