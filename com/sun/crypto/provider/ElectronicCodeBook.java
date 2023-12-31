package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.ProviderException;

/**
 * This class represents ciphers in electronic codebook (ECB) mode.
 *
 * <p>This mode is implemented independently of a particular cipher.
 * Ciphers to which this mode should apply (e.g., DES) must be
 * <i>plugged-in</i> using the constructor.
 *
 * <p>NOTE: This class does not deal with buffering or padding.
 *
 * @author Gigi Ankeny
 */

final class ElectronicCodeBook extends FeedbackCipher {

    ElectronicCodeBook(SymmetricCipher embeddedCipher) {
        super(embeddedCipher);
    }

    /**
     * Gets the name of the feedback mechanism
     *
     * @return the name of the feedback mechanism
     */
    String getFeedback() {
        return "ECB";
    }

    /**
     * Resets the iv to its original value.
     * This is used when doFinal is called in the Cipher class, so that the
     * cipher can be reused (with its original iv).
     */
    void reset() {
        // empty
    }

    /**
     * Save the current content of this cipher.
     */
    void save() {}

    /**
     * Restores the content of this cipher to the previous saved one.
     */
    void restore() {}

    /**
     * Initializes the cipher in the specified mode with the given key
     * and iv.
     *
     * @param decrypting flag indicating encryption or decryption
     * @param algorithm the algorithm name
     * @param key the key
     * @param iv the iv
     *
     * @exception InvalidKeyException if the given key is inappropriate for
     * initializing this cipher
     */
    void init(boolean decrypting, String algorithm, byte[] key, byte[] iv)
            throws InvalidKeyException {
        if ((key == null) || (iv != null)) {
            throw new InvalidKeyException("Internal error");
        }
        embeddedCipher.init(decrypting, algorithm, key);
    }

    /**
     * Performs encryption operation.
     *
     * <p>The input plain text <code>in</code>, starting at
     * <code>inOff</code> and ending at * <code>(inOff + len - 1)</code>,
     * is encrypted. The result is stored in <code>out</code>, starting at
     * <code>outOff</code>.
     *
     * @param in the buffer with the input data to be encrypted
     * @param inOff the offset in <code>plain</code>
     * @param len the length of the input data
     * @param out the buffer for the result
     * @param outOff the offset in <code>cipher</code>
     * @exception ProviderException if <code>len</code> is not
     * a multiple of the block size
     * @return the length of the encrypted data
     */
    int encrypt(byte[] in, int inOff, int len, byte[] out, int outOff) {
        RangeUtil.blockSizeCheck(len, blockSize);
        RangeUtil.nullAndBoundsCheck(in, inOff, len);
        RangeUtil.nullAndBoundsCheck(out, outOff, len);

        for (int i = len; i >= blockSize; i -= blockSize) {
            embeddedCipher.encryptBlock(in, inOff, out, outOff);
            inOff += blockSize;
            outOff += blockSize;
        }
        return len;
    }

    /**
     * Performs decryption operation.
     *
     * <p>The input cipher text <code>in</code>, starting at
     * <code>inOff</code> and ending at * <code>(inOff + len - 1)</code>,
     * is decrypted.The result is stored in <code>out</code>, starting at
     * <code>outOff</code>.
     *
     * @param in the buffer with the input data to be decrypted
     * @param inOff the offset in <code>cipherOffset</code>
     * @param len the length of the input data
     * @param out the buffer for the result
     * @param outOff the offset in <code>plain</code>
     * @exception ProviderException if <code>len</code> is not
     * a multiple of the block size
     * @return the length of the decrypted data
     */
    int decrypt(byte[] in, int inOff, int len, byte[] out, int outOff) {
        RangeUtil.blockSizeCheck(len, blockSize);
        RangeUtil.nullAndBoundsCheck(in, inOff, len);
        RangeUtil.nullAndBoundsCheck(out, outOff, len);

        for (int i = len; i >= blockSize; i -= blockSize) {
            embeddedCipher.decryptBlock(in, inOff, out, outOff);
            inOff += blockSize;
            outOff += blockSize;
        }
        return len;
    }
}
