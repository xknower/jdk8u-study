package com.sun.tools.example.debug.bdi;

import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import java.util.List;
import java.util.Iterator;

public class ThreadIterator implements Iterator<ThreadReference> {
    Iterator<ThreadReference> it = null;
    ThreadGroupIterator tgi;

    public ThreadIterator(ThreadGroupReference tg) {
        tgi = new ThreadGroupIterator(tg);
    }

    //### make this package access only?
    public ThreadIterator(List<ThreadGroupReference> tgl) {
        tgi = new ThreadGroupIterator(tgl);
    }

    @Override
    public boolean hasNext() {
        while (it == null || !it.hasNext()) {
            if (!tgi.hasNext()) {
                return false; // no more
            }
            it = tgi.nextThreadGroup().threads().iterator();
        }
        return true;
    }

    @Override
    public ThreadReference next() {
        return it.next();
    }

    public ThreadReference nextThread() {
        return next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
