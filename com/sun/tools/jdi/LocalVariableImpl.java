package com.sun.tools.jdi;
import com.sun.jdi.*;

public class LocalVariableImpl extends MirrorImpl
                               implements LocalVariable, ValueContainer
{
    private final Method method;
    private final int slot;
    private final Location scopeStart;
    private final Location scopeEnd;
    private final String name;
    private final String signature;
    private String genericSignature = null;

    LocalVariableImpl(VirtualMachine vm, Method method,
                      int slot, Location scopeStart, Location scopeEnd,
                      String name, String signature,
                      String genericSignature) {
        super(vm);
        this.method = method;
        this.slot = slot;
        this.scopeStart = scopeStart;
        this.scopeEnd = scopeEnd;
        this.name = name;
        this.signature = signature;
        if (genericSignature != null && genericSignature.length() > 0) {
            this.genericSignature = genericSignature;
        } else {
            // The Spec says to return null for non-generic types
            this.genericSignature = null;
        }
    }

    public boolean equals(Object obj) {
        if ((obj != null) && (obj instanceof LocalVariableImpl)) {
            LocalVariableImpl other = (LocalVariableImpl)obj;
            return ((slot() == other.slot()) &&
                    (scopeStart != null) &&
                    (scopeStart.equals(other.scopeStart)) &&
                    (super.equals(obj)));
        } else {
            return false;
        }
    }

    public int hashCode() {
        /*
         * TO DO: Better hash code
         */
        return ((scopeStart.hashCode() << 4) + slot());
    }

    public int compareTo(LocalVariable object) {
        LocalVariableImpl other = (LocalVariableImpl)object;

        int rc = scopeStart.compareTo(other.scopeStart);
        if (rc == 0) {
            rc = slot() - other.slot();
        }
        return rc;
    }

    public String name() {
        return name;
    }

    /**
     * @return a text representation of the declared type
     * of this variable.
     */
    public String typeName() {
        JNITypeParser parser = new JNITypeParser(signature);
        return parser.typeName();
    }

    public Type type() throws ClassNotLoadedException {
        return findType(signature());
    }

    public Type findType(String signature) throws ClassNotLoadedException {
        ReferenceTypeImpl enclosing = (ReferenceTypeImpl)method.declaringType();
        return enclosing.findType(signature);
    }

    public String signature() {
        return signature;
    }

    public String genericSignature() {
        return genericSignature;
    }

    public boolean isVisible(StackFrame frame) {
        validateMirror(frame);
        Method frameMethod = frame.location().method();

        if (!frameMethod.equals(method)) {
            throw new IllegalArgumentException(
                       "frame method different than variable's method");
        }

        // this is here to cover the possibility that we will
        // allow LocalVariables for native methods.  If we do
        // so we will have to re-examinine this.
        if (frameMethod.isNative()) {
            return false;
        }

        return ((scopeStart.compareTo(frame.location()) <= 0)
             && (scopeEnd.compareTo(frame.location()) >= 0));
    }

    public boolean isArgument() {
        try {
            MethodImpl method = (MethodImpl)scopeStart.method();
            return (slot < method.argSlotCount());
        } catch (AbsentInformationException e) {
            // If this variable object exists, there shouldn't be absent info
            throw new InternalException();
        }
    }

    int slot() {
        return slot;
    }

    /*
     * Compilers/VMs can have byte code ranges for variables of the
     * same names that overlap. This is because the byte code ranges
     * aren't necessarily scopes; they may have more to do with the
     * lifetime of the variable's slot, depending on implementation.
     *
     * This method determines whether this variable hides an
     * identically named variable; ie, their byte code ranges overlap
     * this one starts after the given one. If it returns true this
     * variable should be preferred when looking for a single variable
     * with its name when both variables are visible.
     */
    boolean hides(LocalVariable other) {
        LocalVariableImpl otherImpl = (LocalVariableImpl)other;
        if (!method.equals(otherImpl.method) ||
            !name.equals(otherImpl.name)) {
            return false;
        } else {
            return (scopeStart.compareTo(otherImpl.scopeStart) > 0);
        }
    }

    public String toString() {
       return name() + " in " + method.toString() +
              "@" + scopeStart.toString();
    }
}
