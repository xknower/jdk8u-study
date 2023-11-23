package sun.security.ssl;

/**
 * Enum for SSL/TLS cipher types.
 */
enum CipherType {
    NULL_CIPHER,           // null cipher
    STREAM_CIPHER,         // stream cipher
    BLOCK_CIPHER,          // block cipher in CBC mode
    AEAD_CIPHER            // AEAD cipher
}

