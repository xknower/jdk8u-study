package com.sun.tools.example.debug.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.sun.tools.example.debug.bdi.*;

public class ApplicationTool extends JPanel {

    private static final long serialVersionUID = 310966063293205714L;

    private ExecutionManager runtime;

    private TypeScript script;

    private static final String PROMPT = "Input:";

    public ApplicationTool(Environment env) {

        super(new BorderLayout());

        this.runtime = env.getExecutionManager();

        this.script = new TypeScript(PROMPT, false); // No implicit echo.
        this.add(script);

        script.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runtime.sendLineToApplication(script.readln());
            }
        });

        runtime.addApplicationEchoListener(new TypeScriptOutputListener(script));
        runtime.addApplicationOutputListener(new TypeScriptOutputListener(script));
        runtime.addApplicationErrorListener(new TypeScriptOutputListener(script));

        //### should clean up on exit!

    }

    /******
    public void setFont(Font f) {
        script.setFont(f);
    }
    ******/

}
