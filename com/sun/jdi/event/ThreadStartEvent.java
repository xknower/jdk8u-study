package com.sun.jdi.event;

import com.sun.jdi.*;

/**
 * Notification of a new running thread in the target VM.
 * The new thread can be the result of a call to
 * <code>{@link java.lang.Thread#start}</code> or the result of
 * attaching a new thread to the VM though JNI. The
 * notification is generated by the new thread some time before
 * its execution starts.
 * Because of this timing, it is possible to receive other events
 * for the thread before this event is received. (Notably,
 * {@link MethodEntryEvent}s and {@link MethodExitEvent}s might occur
 * during thread initialization.)
 * It is also possible for {@link VirtualMachine#allThreads} to return
 * a new started thread before this event is received.
 * <p>
 * Note that this event gives no information
 * about the creation of the thread object which may have happened
 * much earlier, depending on the VM being debugged.
 *
 * @see EventQueue
 * @see VirtualMachine
 * @see ThreadReference
 *
 * @author Robert Field
 * @since  1.3
 */
@jdk.Exported
public interface ThreadStartEvent extends Event {
    /**
     * Returns the thread which has started.
     *
     * @return a {@link ThreadReference} which mirrors the event's thread in
     * the target VM.
     */
    public ThreadReference thread();
}
