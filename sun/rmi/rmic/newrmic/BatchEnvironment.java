package sun.rmi.rmic.newrmic;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sun.rmi.rmic.newrmic.Constants.*;

/**
 * The environment for an rmic compilation batch.
 *
 * A BatchEnvironment contains a RootDoc, which is the entry point
 * into the doclet environment for the associated rmic compilation
 * batch.  A BatchEnvironment collects the source files generated
 * during the batch's execution, for eventual source code compilation
 * and, possibly, deletion.  Errors that occur during generation
 * activity should be reported through the BatchEnvironment's "error"
 * method.
 *
 * A protocol-specific generator class may require the use of a
 * particular BatchEnvironment subclass for enhanced environment
 * functionality.  A BatchEnvironment subclass must declare a
 * public constructor with one parameter of type RootDoc.
 *
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 *
 * @author Peter Jones
 **/
public class BatchEnvironment {

    private final RootDoc rootDoc;

    /** cached ClassDoc for certain types used by rmic */
    private final ClassDoc docRemote;
    private final ClassDoc docException;
    private final ClassDoc docRemoteException;
    private final ClassDoc docRuntimeException;

    private boolean verbose = false;
    private final List<File> generatedFiles = new ArrayList<File>();

    /**
     * Creates a new BatchEnvironment with the specified RootDoc.
     **/
    public BatchEnvironment(RootDoc rootDoc) {
        this.rootDoc = rootDoc;

        /*
         * Initialize cached ClassDoc for types used by rmic.  Note
         * that any of these could be null if the boot class path is
         * incorrect, which could cause a NullPointerException later.
         */
        docRemote = rootDoc().classNamed(REMOTE);
        docException = rootDoc().classNamed(EXCEPTION);
        docRemoteException = rootDoc().classNamed(REMOTE_EXCEPTION);
        docRuntimeException = rootDoc().classNamed(RUNTIME_EXCEPTION);
    }

    /**
     * Returns the RootDoc for this environment.
     **/
    public RootDoc rootDoc() {
        return rootDoc;
    }

    public ClassDoc docRemote() { return docRemote; }
    public ClassDoc docException() { return docException; }
    public ClassDoc docRemoteException() { return docRemoteException; }
    public ClassDoc docRuntimeException() { return docRuntimeException; }

    /**
     * Sets this environment's verbosity status.
     **/
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Returns this environment's verbosity status.
     **/
    public boolean verbose() {
        return verbose;
    }

    /**
     * Adds the specified file to the list of source files generated
     * during this batch.
     **/
    public void addGeneratedFile(File file) {
        generatedFiles.add(file);
    }

    /**
     * Returns the list of files generated during this batch.
     **/
    public List<File> generatedFiles() {
        return Collections.unmodifiableList(generatedFiles);
    }

    /**
     * Outputs the specified (non-error) message.
     **/
    public void output(String msg) {
        rootDoc.printNotice(msg);
    }

    /**
     * Reports an error using the specified resource key and text
     * formatting arguments.
     **/
    public void error(String key, String... args) {
        rootDoc.printError(Resources.getText(key, args));
    }
}
