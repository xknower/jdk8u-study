package com.sun.jdi;

/**
 * The type associated with non-object values in a target VM.
 * Instances of one of the sub-interfaces of this interface will be
 * returned from {@link Value#type} for all {@link PrimitiveValue} objects.
 *
 * @see PrimitiveValue
 *
 * @author Robert Field
 * @author Gordon Hirsch
 * @author James McIlree
 * @since  1.3
 */
@jdk.Exported
public interface PrimitiveType extends Type {
}
