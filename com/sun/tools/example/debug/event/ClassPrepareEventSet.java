package com.sun.tools.example.debug.event;

import com.sun.jdi.*;
import com.sun.jdi.event.*;

public class ClassPrepareEventSet extends AbstractEventSet {

    private static final long serialVersionUID = 5958493423581010491L;

    ClassPrepareEventSet(EventSet jdiEventSet) {
        super(jdiEventSet);
    }

    /**
     * Returns the thread in which this event has occurred.
     *
     * @return a {@link ThreadReference} which mirrors the event's thread in
     * the target VM.
     */
    public ThreadReference getThread() {
        return ((ClassPrepareEvent)oneEvent).thread();
    }


    /**
     * Returns the reference type for which this event was generated.
     *
     * @return a {@link ReferenceType} which mirrors the class, interface, or
     * array which has been linked.
     */
    public ReferenceType getReferenceType() {
        return ((ClassPrepareEvent)oneEvent).referenceType();
    }

    @Override
    public void notify(JDIListener listener) {
        listener.classPrepare(this);
    }
}
