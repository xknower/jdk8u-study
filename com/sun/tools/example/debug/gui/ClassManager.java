package com.sun.tools.example.debug.gui;

public class ClassManager {

    // This class is provided primarily for symmetry with
    // SourceManager.  Currently, it does very little.
    // If we add facilities in the future that require that
    // class files be read outside of the VM, for example, to
    // provide a disassembled view of a class for bytecode-level
    // debugging, the required class file management will be done
    // here.

    private SearchPath classPath;

    public ClassManager(Environment env) {
        this.classPath = new SearchPath("");
    }

    public ClassManager(SearchPath classPath) {
        this.classPath = classPath;
    }

    /*
     * Set path for access to class files.
     */

    public void setClassPath(SearchPath sp) {
        classPath = sp;
    }

    /*
     * Get path for access to class files.
     */

    public SearchPath getClassPath() {
        return classPath;
    }

}
