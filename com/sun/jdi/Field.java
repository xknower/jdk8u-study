package com.sun.jdi;

/**
 * A class or instance variable in the target VM.
 * See {@link TypeComponent}
 * for general information about Field and Method mirrors.
 *
 * @see ObjectReference
 * @see ReferenceType
 *
 * @author Robert Field
 * @author Gordon Hirsch
 * @author James McIlree
 * @since  1.3
 */
@jdk.Exported
public interface Field extends TypeComponent, Comparable<Field> {

    /**
     * Returns a text representation of the type
     * of this field.
     * Where the type is the type specified in the declaration
     * of this field.
     * <P>
     * This type name is always available even if
     * the type has not yet been created or loaded.
     *
     * @return a String representing the
     * type of this field.
     */
    String typeName();

    /**
     * Returns the type of this field.
     * Where the type is the type specified in the declaration
     * of this field.
     * <P>
     * For example, if a target class defines:
     * <PRE>
     *    short s;
     *    Date d;
     *    byte[] ba;</PRE>
     * And the JDI client defines these <CODE>Field</CODE> objects:
     * <PRE>
     *    Field sField = targetClass.fieldByName("s");
     *    Field dField = targetClass.fieldByName("d");
     *    Field baField = targetClass.fieldByName("ba");</PRE>
     * to mirror the corresponding fields, then <CODE>sField.type()</CODE>
     * is a {@link ShortType}, <CODE>dField.type()</CODE> is the
     * {@link ReferenceType} for <CODE>java.util.Date</CODE> and
     * <CODE>((ArrayType)(baField.type())).componentType()</CODE> is a
     * {@link ByteType}.
     * <P>
     * Note: if the type of this field is a reference type (class,
     * interface, or array) and it has not been created or loaded
     * by the declaring type's class loader - that is,
     * {@link TypeComponent#declaringType <CODE>declaringType()</CODE>}
     * <CODE>.classLoader()</CODE>,
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
     * @return the {@link Type} of this field.
     * @throws ClassNotLoadedException if the type has not yet been loaded
     * or created through the appropriate class loader.
     */
    Type type() throws ClassNotLoadedException;

    /**
     * Determine if this is a transient field.
     *
     * @return <code>true</code> if this field is transient; false otherwise.
     */
    boolean isTransient();

    /**
     * Determine if this is a volatile field.
     *
     * @return <code>true</code> if this field is volatile; false otherwise.
     */
    boolean isVolatile();

    /**
     * Determine if this is a field that represents an enum constant.
     * @return <code>true</code> if this field represents an enum constant;
     * false otherwise.
     */
    boolean isEnumConstant();

    /**
     * Compares the specified Object with this field for equality.
     *
     * @return true if the Object is a Field and if both
     * mirror the same field (declared in the same class or interface, in
     * the same VM).
     */
    boolean equals(Object obj);

    /**
     * Returns the hash code value for this Field.
     *
     * @return the integer hash code
     */
    int hashCode();
}
