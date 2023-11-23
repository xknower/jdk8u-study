package sun.misc;

import java.io.InvalidClassException;
import java.io.ObjectInputStream;

public interface JavaOISAccess {
    void setObjectInputFilter(ObjectInputStream stream, ObjectInputFilter filter);
    ObjectInputFilter getObjectInputFilter(ObjectInputStream stream);
    void checkArray(ObjectInputStream stream, Class<?> arrayType, int arrayLength)
        throws InvalidClassException;
}
