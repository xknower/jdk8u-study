package com.sun.jdi;

/**
 * A local variable in the target VM. Each variable declared within a
 * {@link Method} has its own LocalVariable object. Variables of the same
 * name declared in different scopes have different LocalVariable objects.
 * LocalVariables can be used alone to retrieve static information
 * about their declaration, or can be used in conjunction with a
 * {@link StackFrame} to set and get values.
 *
 * @see StackFrame
 * @see Method
 *
 * @author Robert Field
 * @author Gordon Hirsch
 * @author James McIlree
 * @since  1.3
 */

@jdk.Exported
public interface LocalVariable extends Mirror, Comparable<LocalVariable> {

    /**
     * Gets the name of the local variable.
     *
     * @return a string containing the name.
     */
    String name();

    /**
     * Returns a text representation of the type
     * of this variable.
     * Where the type is the type specified in the declaration
     * of this local variable.
     * <P>
     * This type name is always available even if
     * the type has not yet been created or loaded.
     *
     * @return a String representing the
     * type of this local variable.

     */
    String typeName();

    /**
     * Returns the type of this variable.
     * Where the type is the type specified in the declaration
     * of this local variable.
     * <P>
     * Note: if the type of this variable is a reference type (class,
     * interface, or array) and it has not been created or loaded
     * by the class loader of the enclosing class,
     * then ClassNotLoadedException will be thrown.
     * Also, a reference type may have been loaded but not yet prepared,
     * in which case the type will be returned
     * but attempts to perform some operations on the returned type
     * (e.g. {@link ReferenceType#fields() fields()}) will throw
     * a {@link ClassNotPreparedException}.
     * Use {@link ReferenceType#isPrepared()} to determine if
     * a reference type is prepared.
     *
     * @see Type
     * @see Field#type() Field.type() - for usage examples
     * @return the {@link Type} of this local variable.
     * @throws ClassNotLoadedException if the type has not yet been loaded
     * through the appropriate class loader.
     */
    Type type() throws ClassNotLoadedException;

    /**
     * Gets the JNI signature of the local variable.
     *
     * @see <a href="doc-files/signature.html">Type Signatures</a>
     * @return a string containing the signature.
     */
    String signature();

    /**
     * Gets the generic signature for this variable if there is one.
     * Generic signatures are described in the
     * <cite>The Java&trade; Virtual Machine Specification</cite>.
     *
     * @return a string containing the generic signature, or <code>null</code>
     * if there is no generic signature.
     *
     * @since 1.5
     */
    String genericSignature();

    /**
     * Determines whether this variable can be accessed from the given
     * {@link StackFrame}.
     *
     * See {@link StackFrame#visibleVariables} for a complete description
     * variable visibility in this interface.
     *
     * @param frame the StackFrame querying visibility
     * @return <code>true</code> if this variable is visible;
     * <code>false</code> otherwise.
     * @throws IllegalArgumentException if the stack frame's method
     * does not match this variable's method.
     */
    boolean isVisible(StackFrame frame);

    /**
     * Determines if this variable is an argument to its method.
     *
     * @return <code>true</code> if this variable is an argument;
     * <code>false</code> otherwise.
     */
    boolean isArgument();

    /**
     * Compares the specified Object with this LocalVariable for equality.
     *
     * @return  true if the Object is a LocalVariable, if both LocalVariables
     * are contained in the same method (as determined by
     * {@link Method#equals}), and if both LocalVariables mirror
     * the same declaration within that method
     */
    boolean equals(Object obj);

    /**
     * Returns the hash code value for this LocalVariable.
     *
     * @return the integer hash code
     */
    int hashCode();
}
