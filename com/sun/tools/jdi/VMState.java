package com.sun.tools.jdi;

import com.sun.jdi.*;

import java.lang.ref.WeakReference;
import java.util.*;

class VMState {
    private final VirtualMachineImpl vm;

    // Listeners
    private final List<WeakReference<VMListener>> listeners = new ArrayList<WeakReference<VMListener>>(); // synchronized (this)
    private boolean notifyingListeners = false;  // synchronized (this)

    /*
     * Certain information can be cached only when the entire VM is
     * suspended and there are no pending resumes. The field below
     * is used to track whether there are pending resumes.
     */
    private final Set<Integer> pendingResumeCommands = Collections.synchronizedSet(new HashSet<>());

    // This is cached only while the VM is suspended
    private static class Cache {
        List<ThreadGroupReference> groups = null;  // cached Top Level ThreadGroups
        List<ThreadReference> threads = null; // cached Threads
    }

    private Cache cache = null;               // synchronized (this)
    private static final Cache markerCache = new Cache();

    private void disableCache() {
        synchronized (this) {
            cache = null;
        }
    }

    private void enableCache() {
        synchronized (this) {
            cache = markerCache;
        }
    }

    private Cache getCache() {
        synchronized (this) {
            if (cache == markerCache) {
                cache = new Cache();
            }
            return cache;
        }
    }

    VMState(VirtualMachineImpl vm) {
        this.vm = vm;
    }

    /**
     * Is the VM currently suspended, for the purpose of caching?
     * Must be called synchronized on vm.state()
     */
    boolean isSuspended() {
        return cache != null;
    }

    /*
     * A JDWP command has been completed (reply has been received).
     * Update data that tracks pending resume commands.
     */
    void notifyCommandComplete(int id) {
        pendingResumeCommands.remove(id);
    }

    synchronized void freeze() {
        if (cache == null && (pendingResumeCommands.isEmpty())) {
            /*
             * No pending resumes to worry about. The VM is suspended
             * and additional state can be cached. Notify all
             * interested listeners.
             */
            processVMAction(new VMAction(vm, VMAction.VM_SUSPENDED));
            enableCache();
        }
    }

    synchronized PacketStream thawCommand(CommandSender sender) {
        PacketStream stream = sender.send();
        pendingResumeCommands.add(stream.id());
        thaw();
        return stream;
    }

    /**
     * All threads are resuming
     */
    void thaw() {
        thaw(null);
    }

    /**
     * Tell listeners to invalidate suspend-sensitive caches.
     * If resumingThread != null, then only that thread is being
     * resumed.
     */
    synchronized void thaw(ThreadReference resumingThread) {
        if (cache != null) {
            if ((vm.traceFlags & VirtualMachine.TRACE_OBJREFS) != 0) {
                vm.printTrace("Clearing VM suspended cache");
            }
            disableCache();
        }
        processVMAction(new VMAction(vm, resumingThread, VMAction.VM_NOT_SUSPENDED));
    }

    private synchronized void processVMAction(VMAction action) {
        if (!notifyingListeners) {
            // Prevent recursion
            notifyingListeners = true;

            Iterator<WeakReference<VMListener>> iter = listeners.iterator();
            while (iter.hasNext()) {
                WeakReference<VMListener> ref = iter.next();
                VMListener listener = ref.get();
                if (listener != null) {
                    boolean keep = true;
                    switch (action.id()) {
                        case VMAction.VM_SUSPENDED:
                            keep = listener.vmSuspended(action);
                            break;
                        case VMAction.VM_NOT_SUSPENDED:
                            keep = listener.vmNotSuspended(action);
                            break;
                    }
                    if (!keep) {
                        iter.remove();
                    }
                } else {
                    // Listener is unreachable; clean up
                    iter.remove();
                }
            }

            notifyingListeners = false;
        }
    }

    synchronized void addListener(VMListener listener) {
        listeners.add(new WeakReference<VMListener>(listener));
    }

    synchronized boolean hasListener(VMListener listener) {
        Iterator<WeakReference<VMListener>> iter = listeners.iterator();
        while (iter.hasNext()) {
            WeakReference<VMListener> ref = iter.next();
            if (listener.equals(ref.get())) {
                return true;
            }
        }
        return false;
    }

    synchronized void removeListener(VMListener listener) {
        Iterator<WeakReference<VMListener>> iter = listeners.iterator();
        while (iter.hasNext()) {
            WeakReference<VMListener> ref = iter.next();
            if (listener.equals(ref.get())) {
                iter.remove();
                break;
            }
        }
    }

    List<ThreadReference> allThreads() {
        List<ThreadReference> threads = null;
        try {
            Cache local = getCache();

            if (local != null) {
                // may be stale when returned, but not provably so
                threads = local.threads;
            }
            if (threads == null) {
                threads = Arrays.asList((ThreadReference[])JDWP.VirtualMachine.AllThreads.
                                        process(vm).threads);
                if (local != null) {
                    local.threads = threads;
                    if ((vm.traceFlags & VirtualMachine.TRACE_OBJREFS) != 0) {
                        vm.printTrace("Caching all threads (count = " +
                                      threads.size() + ") while VM suspended");
                    }
                }
            }
        } catch (JDWPException exc) {
            throw exc.toJDIException();
        }
        return threads;
    }


    List<ThreadGroupReference> topLevelThreadGroups() {
        List<ThreadGroupReference> groups = null;
        try {
            Cache local = getCache();

            if (local != null) {
                groups = local.groups;
            }
            if (groups == null) {
                groups = Arrays.asList(
                                (ThreadGroupReference[])JDWP.VirtualMachine.TopLevelThreadGroups.
                                       process(vm).groups);
                if (local != null) {
                    local.groups = groups;
                    if ((vm.traceFlags & VirtualMachine.TRACE_OBJREFS) != 0) {
                        vm.printTrace(
                          "Caching top level thread groups (count = " +
                          groups.size() + ") while VM suspended");
                    }
                }
            }
        } catch (JDWPException exc) {
            throw exc.toJDIException();
        }
        return groups;
    }

}
