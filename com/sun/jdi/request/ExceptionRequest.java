package com.sun.jdi.request;

import com.sun.jdi.*;

/**
 * Request for notification when an exception occurs in the target VM.
 * When an enabled ExceptionRequest is satisfied, an
 * {@link com.sun.jdi.event.EventSet event set} containing an
 * {@link com.sun.jdi.event.ExceptionEvent ExceptionEvent} will be placed
 * on the {@link com.sun.jdi.event.EventQueue EventQueue}.
 * The collection of existing ExceptionRequests is
 * managed by the {@link EventRequestManager}
 *
 * @see com.sun.jdi.event.ExceptionEvent
 * @see com.sun.jdi.event.EventQueue
 * @see EventRequestManager
 *
 * @author Robert Field
 * @since  1.3
 */
@jdk.Exported
public interface ExceptionRequest extends EventRequest {

    /**
     * Returns exception type for which exception events are requested.
     * @return
     * The exception (and its subclasses) requested
     * with {@link EventRequestManager#createExceptionRequest}, or
     * null if, as by default, all exceptions are requested.
     */
    ReferenceType exception();

    /**
     * Returns whether caught exceptions of the requested type
     * will generate events when they are thrown.
     * <p>
     * Note that at the time an exception is thrown, it is not always
     * possible to determine whether it is truly caught. See
     * {@link com.sun.jdi.event.ExceptionEvent#catchLocation} for
     * details.
     * @return
     * boolean true if caught exceptions will be reported, false
     * otherwise.
     */
    boolean notifyCaught();

    /**
     * Returns whether uncaught exceptions of the requested type
     * will generate events when they are thrown.
     * <p>
     * Note that at the time an exception is thrown, it is not always
     * possible to determine whether it is truly uncaught. See
     * {@link com.sun.jdi.event.ExceptionEvent#catchLocation} for
     * details.
     * @return
     * boolean true if caught exceptions will be reported, false
     * otherwise.
     */
    boolean notifyUncaught();

    /**
     * Restricts the events generated by this request to those in
     * the given thread.
     * @param thread the thread to filter on.
     * @throws InvalidRequestStateException if this request is currently
     * enabled or has been deleted.
     * Filters may be added only to disabled requests.
     */
    void addThreadFilter(ThreadReference thread);

    /**
     * Restricts the events generated by this request to those whose
     * location is in the given reference type or any of its subtypes.
     * An event will be generated for any location in a reference type
     * that can be safely cast to the given reference type.
     *
     * @param refType the reference type to filter on.
     * @throws InvalidRequestStateException if this request is currently
     * enabled or has been deleted.
     * Filters may be added only to disabled requests.
     */
    void addClassFilter(ReferenceType refType);

    /**
     * Restricts the events generated by this request to those
     * whose location is in a class whose name matches a restricted
     * regular expression. Regular expressions are limited
     * to exact matches and patterns that begin with '*' or end with '*';
     * for example, "*.Foo" or "java.*".
     *
     * @param classPattern the pattern String to filter for.
     * @throws InvalidRequestStateException if this request is currently
     * enabled or has been deleted.
     * Filters may be added only to disabled requests.
     */
    void addClassFilter(String classPattern);

    /**
     * Restricts the events generated by this request to those
     * whose location is in a class whose name does <b>not</b> match a
     * restricted regular expression. Regular expressions are limited
     * to exact matches and patterns that begin with '*' or end with '*';
     * for example, "*.Foo" or "java.*".
     *
     * @param classPattern the pattern String to filter against.
     * @throws InvalidRequestStateException if this request is currently
     * enabled or has been deleted.
     * Filters may be added only to disabled requests.
     */
    void addClassExclusionFilter(String classPattern);

    /**
     * Restricts the events generated by this request to those in
     * which the currently executing instance ("this") is the object
     * specified.
     * <P>
     * Not all targets support this operation.
     * Use {@link VirtualMachine#canUseInstanceFilters()}
     * to determine if the operation is supported.
     * @since 1.4
     * @param instance the object which must be the current instance
     * in order to pass this filter.
     * @throws java.lang.UnsupportedOperationException if
     * the target virtual machine does not support this
     * operation.
     * @throws InvalidRequestStateException if this request is currently
     * enabled or has been deleted.
     * Filters may be added only to disabled requests.
     */
    void addInstanceFilter(ObjectReference instance);
}
