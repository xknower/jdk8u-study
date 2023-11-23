package sun.misc;

import java.io.ObjectInputStream;

/**
 * The interface to specify methods for accessing {@code ObjectInputStream}
 * @author sjiang
 */
public interface JavaObjectInputStreamAccess {
    /**
     * Sets a descriptor validating.
     * @param ois stream to have the descriptors validated
     * @param validator validator used to validate a descriptor.
     */
    public void setValidator(ObjectInputStream ois, ObjectStreamClassValidator validator);
}
