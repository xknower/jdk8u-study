package com.sun.jdi;

/**
 * The type of all primitive float values accessed in
 * the target VM. Calls to {@link Value#type} will return an
 * implementor of this interface.
 *
 * @see FloatValue
 *
 * @author James McIlree
 * @since  1.3
 */
@jdk.Exported
public interface FloatType extends PrimitiveType {
}
