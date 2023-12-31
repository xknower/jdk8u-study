package com.sun.crypto.provider;

import java.util.Arrays;

import java.nio.ByteBuffer;

import javax.crypto.MacSpi;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.*;

/**
 * This class constitutes the core of HMAC-<MD> algorithms, where
 * <MD> can be SHA1 or MD5, etc. See RFC 2104 for spec.
 *
 * It also contains the implementation classes for SHA-224, SHA-256,
 * SHA-384, and SHA-512 HMACs.
 *
 * @author Jan Luehe
 */
abstract class HmacCore extends MacSpi implements Cloneable {

    private MessageDigest md;
    private byte[] k_ipad; // inner padding - key XORd with ipad
    private byte[] k_opad; // outer padding - key XORd with opad
    private boolean first;       // Is this the first data to be processed?

    private final int blockLen;

    /**
     * Standard constructor, creates a new HmacCore instance using the
     * specified MessageDigest object.
     */
    HmacCore(MessageDigest md, int bl) {
        this.md = md;
        this.blockLen = bl;
        this.k_ipad = new byte[blockLen];
        this.k_opad = new byte[blockLen];
        first = true;
    }

    /**
     * Standard constructor, creates a new HmacCore instance instantiating
     * a MessageDigest of the specified name.
     */
    HmacCore(String digestAlgorithm, int bl) throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance(digestAlgorithm), bl);
    }

    /**
     * Returns the length of the HMAC in bytes.
     *
     * @return the HMAC length in bytes.
     */
    protected int engineGetMacLength() {
        return this.md.getDigestLength();
    }

    /**
     * Initializes the HMAC with the given secret key and algorithm parameters.
     *
     * @param key the secret key.
     * @param params the algorithm parameters.
     *
     * @exception InvalidKeyException if the given key is inappropriate for
     * initializing this MAC.
     * @exception InvalidAlgorithmParameterException if the given algorithm
     * parameters are inappropriate for this MAC.
     */
    protected void engineInit(Key key, AlgorithmParameterSpec params)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (params != null) {
            throw new InvalidAlgorithmParameterException
                ("HMAC does not use parameters");
        }

        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Secret key expected");
        }

        byte[] secret = key.getEncoded();
        if (secret == null) {
            throw new InvalidKeyException("Missing key data");
        }

        // if key is longer than the block length, reset it using
        // the message digest object.
        if (secret.length > blockLen) {
            byte[] tmp = md.digest(secret);
            // now erase the secret
            Arrays.fill(secret, (byte)0);
            secret = tmp;
        }

        // XOR k with ipad and opad, respectively
        for (int i = 0; i < blockLen; i++) {
            int si = (i < secret.length) ? secret[i] : 0;
            k_ipad[i] = (byte)(si ^ 0x36);
            k_opad[i] = (byte)(si ^ 0x5c);
        }

        // now erase the secret
        Arrays.fill(secret, (byte)0);
        secret = null;

        engineReset();
    }

    /**
     * Processes the given byte.
     *
     * @param input the input byte to be processed.
     */
    protected void engineUpdate(byte input) {
        if (first == true) {
            // compute digest for 1st pass; start with inner pad
            md.update(k_ipad);
            first = false;
        }

        // add the passed byte to the inner digest
        md.update(input);
    }

    /**
     * Processes the first <code>len</code> bytes in <code>input</code>,
     * starting at <code>offset</code>.
     *
     * @param input the input buffer.
     * @param offset the offset in <code>input</code> where the input starts.
     * @param len the number of bytes to process.
     */
    protected void engineUpdate(byte input[], int offset, int len) {
        if (first == true) {
            // compute digest for 1st pass; start with inner pad
            md.update(k_ipad);
            first = false;
        }

        // add the selected part of an array of bytes to the inner digest
        md.update(input, offset, len);
    }

    /**
     * Processes the <code>input.remaining()</code> bytes in the ByteBuffer
     * <code>input</code>.
     *
     * @param input the input byte buffer.
     */
    protected void engineUpdate(ByteBuffer input) {
        if (first == true) {
            // compute digest for 1st pass; start with inner pad
            md.update(k_ipad);
            first = false;
        }

        md.update(input);
    }

    /**
     * Completes the HMAC computation and resets the HMAC for further use,
     * maintaining the secret key that the HMAC was initialized with.
     *
     * @return the HMAC result.
     */
    protected byte[] engineDoFinal() {
        if (first == true) {
            // compute digest for 1st pass; start with inner pad
            md.update(k_ipad);
        } else {
            first = true;
        }

        try {
            // finish the inner digest
            byte[] tmp = md.digest();

            // compute digest for 2nd pass; start with outer pad
            md.update(k_opad);
            // add result of 1st hash
            md.update(tmp);

            md.digest(tmp, 0, tmp.length);
            return tmp;
        } catch (DigestException e) {
            // should never occur
            throw new ProviderException(e);
        }
    }

    /**
     * Resets the HMAC for further use, maintaining the secret key that the
     * HMAC was initialized with.
     */
    protected void engineReset() {
        if (first == false) {
            md.reset();
            first = true;
        }
    }

    /*
     * Clones this object.
     */
    public Object clone() throws CloneNotSupportedException {
        HmacCore copy = (HmacCore) super.clone();
        copy.md = (MessageDigest) md.clone();
        copy.k_ipad = k_ipad.clone();
        copy.k_opad = k_opad.clone();
        return copy;
    }

    // nested static class for the HmacSHA224 implementation
    public static final class HmacSHA224 extends HmacCore {
        public HmacSHA224() throws NoSuchAlgorithmException {
            super("SHA-224", 64);
        }
    }

    // nested static class for the HmacSHA256 implementation
    public static final class HmacSHA256 extends HmacCore {
        public HmacSHA256() throws NoSuchAlgorithmException {
            super("SHA-256", 64);
        }
    }

    // nested static class for the HmacSHA384 implementation
    public static final class HmacSHA384 extends HmacCore {
        public HmacSHA384() throws NoSuchAlgorithmException {
            super("SHA-384", 128);
        }
    }

    // nested static class for the HmacSHA512 implementation
    public static final class HmacSHA512 extends HmacCore {
        public HmacSHA512() throws NoSuchAlgorithmException {
            super("SHA-512", 128);
        }
    }
}
