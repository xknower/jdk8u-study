package com.sun.tools.example.debug.event;

/**
 * The adapter which receives JDI event sets. The methods in this
 * class are empty; this class is provided as a convenience for
 * easily creating listeners by extending this class and overriding
 * only the methods of interest.
 */
public class JDIAdapter implements JDIListener {

    @Override
    public void accessWatchpoint(AccessWatchpointEventSet e) {
    }

    @Override
    public void classPrepare(ClassPrepareEventSet e)  {
    }

    @Override
    public void classUnload(ClassUnloadEventSet e)  {
    }

    @Override
    public void exception(ExceptionEventSet e)  {
    }

    @Override
    public void locationTrigger(LocationTriggerEventSet e)  {
    }

    @Override
    public void modificationWatchpoint(ModificationWatchpointEventSet e)  {
    }

    @Override
    public void threadDeath(ThreadDeathEventSet e)  {
    }

    @Override
    public void threadStart(ThreadStartEventSet e)  {
    }

    @Override
    public void vmDeath(VMDeathEventSet e)  {
    }

    @Override
    public void vmDisconnect(VMDisconnectEventSet e)  {
    }

    @Override
    public void vmStart(VMStartEventSet e)  {
    }

}
