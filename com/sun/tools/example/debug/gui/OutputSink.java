package com.sun.tools.example.debug.gui;

import java.io.*;

// This class is used in 'CommandInterpreter' as a hook to
// allow messagebox style command output as an alternative
// to a typescript. It should be an interface, not a class.

public class OutputSink extends PrintWriter {

    // Currently, we do no buffering,
    // so 'show' is a no-op.

    OutputSink(Writer writer) {
        super(writer);
    }

    public void show() {
        // ignore
    }
}
