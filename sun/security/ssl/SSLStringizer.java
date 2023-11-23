package sun.security.ssl;

import java.nio.ByteBuffer;

/**
 * Interface to decode a {@code ByteBuffer} into legible {@code String}.
 */
interface SSLStringizer {
    /**
     * Returns a legible string representation of a {@code ByteBuffer}.
     *
     * Note that the implementation MUST not change the internal status of
     * the {@code buffer}.
     */
    String toString(ByteBuffer buffer);
}
