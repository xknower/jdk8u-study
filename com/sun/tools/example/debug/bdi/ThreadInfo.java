package com.sun.tools.example.debug.bdi;

import com.sun.jdi.*;

//### Should handle target VM death or connection failure cleanly.

public class ThreadInfo {

    private ThreadReference thread;
    private int status;

    private int frameCount;

    Object userObject;  // User-supplied annotation.

    private boolean interrupted = false;

    private void assureInterrupted() throws VMNotInterruptedException {
        if (!interrupted) {
            throw new VMNotInterruptedException();
        }
    }

    ThreadInfo (ThreadReference thread) {
        this.thread = thread;
        this.frameCount = -1;
    }

    public ThreadReference thread() {
        return thread;
    }

    public int getStatus() throws VMNotInterruptedException {
        assureInterrupted();
        update();
        return status;
    }

    public int getFrameCount() throws VMNotInterruptedException {
        assureInterrupted();
        update();
        return frameCount;
    }

    public StackFrame getFrame(int index) throws VMNotInterruptedException {
        assureInterrupted();
        update();
        try {
            return thread.frame(index);
        } catch (IncompatibleThreadStateException e) {
            // Should not happen
            interrupted = false;
            throw new VMNotInterruptedException();
        }
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object obj) {
        userObject = obj;
    }

    // Refresh upon first access after cache is cleared.

    void update() throws VMNotInterruptedException {
        if (frameCount == -1) {
            try {
                status = thread.status();
                frameCount = thread.frameCount();
            } catch (IncompatibleThreadStateException e) {
                // Should not happen
                interrupted = false;
                throw new VMNotInterruptedException();
            }
        }
    }

    // Called from 'ExecutionManager'.

    void validate() {
        interrupted = true;
    }

    void invalidate() {
        interrupted = false;
        frameCount = -1;
        status = ThreadReference.THREAD_STATUS_UNKNOWN;
    }

}
