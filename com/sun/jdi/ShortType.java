package com.sun.jdi;

/**
 * The type of all primitive <code>short</code> values
 * accessed in the target VM. Calls to {@link Value#type} will return an
 * implementor of this interface.
 *
 * @see LongValue
 *
 * @author James McIlree
 * @since  1.3
 */
@jdk.Exported
public interface ShortType extends PrimitiveType {
}
