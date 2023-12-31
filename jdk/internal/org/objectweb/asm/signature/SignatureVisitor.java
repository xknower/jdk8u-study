package jdk.internal.org.objectweb.asm.signature;

import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * A visitor to visit a generic signature. The methods of this interface must be
 * called in one of the three following orders (the last one is the only valid
 * order for a {@link SignatureVisitor} that is returned by a method of this
 * interface):
 * <ul>
 * <li><i>ClassSignature</i> = ( <tt>visitFormalTypeParameter</tt>
 * <tt>visitClassBound</tt>? <tt>visitInterfaceBound</tt>* )* (
 * <tt>visitSuperClass</tt> <tt>visitInterface</tt>* )</li>
 * <li><i>MethodSignature</i> = ( <tt>visitFormalTypeParameter</tt>
 * <tt>visitClassBound</tt>? <tt>visitInterfaceBound</tt>* )* (
 * <tt>visitParameterType</tt>* <tt>visitReturnType</tt>
 * <tt>visitExceptionType</tt>* )</li>
 * <li><i>TypeSignature</i> = <tt>visitBaseType</tt> |
 * <tt>visitTypeVariable</tt> | <tt>visitArrayType</tt> | (
 * <tt>visitClassType</tt> <tt>visitTypeArgument</tt>* (
 * <tt>visitInnerClassType</tt> <tt>visitTypeArgument</tt>* )* <tt>visitEnd</tt>
 * ) )</li>
 * </ul>
 *
 * @author Thomas Hallgren
 * @author Eric Bruneton
 */
public abstract class SignatureVisitor {

    /**
     * Wildcard for an "extends" type argument.
     */
    public final static char EXTENDS = '+';

    /**
     * Wildcard for a "super" type argument.
     */
    public final static char SUPER = '-';

    /**
     * Wildcard for a normal type argument.
     */
    public final static char INSTANCEOF = '=';

    /**
     * The ASM API version implemented by this visitor. The value of this field
     * must be one of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     */
    protected final int api;

    /**
     * Constructs a new {@link SignatureVisitor}.
     *
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     */
    public SignatureVisitor(final int api) {
        if (api != Opcodes.ASM4 && api != Opcodes.ASM5) {
            throw new IllegalArgumentException();
        }
        this.api = api;
    }

    /**
     * Visits a formal type parameter.
     *
     * @param name
     *            the name of the formal parameter.
     */
    public void visitFormalTypeParameter(String name) {
    }

    /**
     * Visits the class bound of the last visited formal type parameter.
     *
     * @return a non null visitor to visit the signature of the class bound.
     */
    public SignatureVisitor visitClassBound() {
        return this;
    }

    /**
     * Visits an interface bound of the last visited formal type parameter.
     *
     * @return a non null visitor to visit the signature of the interface bound.
     */
    public SignatureVisitor visitInterfaceBound() {
        return this;
    }

    /**
     * Visits the type of the super class.
     *
     * @return a non null visitor to visit the signature of the super class
     *         type.
     */
    public SignatureVisitor visitSuperclass() {
        return this;
    }

    /**
     * Visits the type of an interface implemented by the class.
     *
     * @return a non null visitor to visit the signature of the interface type.
     */
    public SignatureVisitor visitInterface() {
        return this;
    }

    /**
     * Visits the type of a method parameter.
     *
     * @return a non null visitor to visit the signature of the parameter type.
     */
    public SignatureVisitor visitParameterType() {
        return this;
    }

    /**
     * Visits the return type of the method.
     *
     * @return a non null visitor to visit the signature of the return type.
     */
    public SignatureVisitor visitReturnType() {
        return this;
    }

    /**
     * Visits the type of a method exception.
     *
     * @return a non null visitor to visit the signature of the exception type.
     */
    public SignatureVisitor visitExceptionType() {
        return this;
    }

    /**
     * Visits a signature corresponding to a primitive type.
     *
     * @param descriptor
     *            the descriptor of the primitive type, or 'V' for <tt>void</tt>
     *            .
     */
    public void visitBaseType(char descriptor) {
    }

    /**
     * Visits a signature corresponding to a type variable.
     *
     * @param name
     *            the name of the type variable.
     */
    public void visitTypeVariable(String name) {
    }

    /**
     * Visits a signature corresponding to an array type.
     *
     * @return a non null visitor to visit the signature of the array element
     *         type.
     */
    public SignatureVisitor visitArrayType() {
        return this;
    }

    /**
     * Starts the visit of a signature corresponding to a class or interface
     * type.
     *
     * @param name
     *            the internal name of the class or interface.
     */
    public void visitClassType(String name) {
    }

    /**
     * Visits an inner class.
     *
     * @param name
     *            the local name of the inner class in its enclosing class.
     */
    public void visitInnerClassType(String name) {
    }

    /**
     * Visits an unbounded type argument of the last visited class or inner
     * class type.
     */
    public void visitTypeArgument() {
    }

    /**
     * Visits a type argument of the last visited class or inner class type.
     *
     * @param wildcard
     *            '+', '-' or '='.
     * @return a non null visitor to visit the signature of the type argument.
     */
    public SignatureVisitor visitTypeArgument(char wildcard) {
        return this;
    }

    /**
     * Ends the visit of a signature corresponding to a class or interface type.
     */
    public void visitEnd() {
    }
}