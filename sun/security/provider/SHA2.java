package sun.security.provider;

import java.util.Arrays;

import java.util.Objects;

import static sun.security.provider.ByteArrayAccess.*;

/**
 * This class implements the Secure Hash Algorithm SHA-256 developed by
 * the National Institute of Standards and Technology along with the
 * National Security Agency.
 *
 * <p>It implements java.security.MessageDigestSpi, and can be used
 * through Java Cryptography Architecture (JCA), as a pluggable
 * MessageDigest implementation.
 *
 * @since       1.4.2
 * @author      Valerie Peng
 * @author      Andreas Sterbenz
 */
abstract class SHA2 extends DigestBase {

    private static final int ITERATION = 64;
    // Constants for each round
    private static final int[] ROUND_CONSTS = {
        0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5,
        0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
        0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,
        0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
        0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
        0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
        0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
        0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
        0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
        0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
        0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
        0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    // buffer used by implCompress()
    private int[] W;

    // state of this object
    private int[] state;

    // initial state value. different between SHA-224 and SHA-256
    private final int[] initialHashes;

    /**
     * Creates a new SHA object.
     */
    SHA2(String name, int digestLength, int[] initialHashes) {
        super(name, digestLength, 64);
        this.initialHashes = initialHashes;
        state = new int[8];
        W = new int[64];
        resetHashes();
    }

    /**
     * Resets the buffers and hash value to start a new hash.
     */
    void implReset() {
        resetHashes();
        Arrays.fill(W, 0);
    }

    private void resetHashes() {
        System.arraycopy(initialHashes, 0, state, 0, state.length);
    }

    void implDigest(byte[] out, int ofs) {
        long bitsProcessed = bytesProcessed << 3;

        int index = (int)bytesProcessed & 0x3f;
        int padLen = (index < 56) ? (56 - index) : (120 - index);
        engineUpdate(padding, 0, padLen);

        i2bBig4((int)(bitsProcessed >>> 32), buffer, 56);
        i2bBig4((int)bitsProcessed, buffer, 60);
        implCompress(buffer, 0);

        i2bBig(state, 0, out, ofs, engineGetDigestLength());
    }

    /**
     * logical function ch(x,y,z) as defined in spec:
     * @return (x and y) xor ((complement x) and z)
     * @param x int
     * @param y int
     * @param z int
     */
    private static int lf_ch(int x, int y, int z) {
        return (x & y) ^ ((~x) & z);
    }

    /**
     * logical function maj(x,y,z) as defined in spec:
     * @return (x and y) xor (x and z) xor (y and z)
     * @param x int
     * @param y int
     * @param z int
     */
    private static int lf_maj(int x, int y, int z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    /**
     * logical function R(x,s) - right shift
     * @return x right shift for s times
     * @param x int
     * @param s int
     */
    private static int lf_R( int x, int s ) {
        return (x >>> s);
    }

    /**
     * logical function S(x,s) - right rotation
     * @return x circular right shift for s times
     * @param x int
     * @param s int
     */
    private static int lf_S(int x, int s) {
        return (x >>> s) | (x << (32 - s));
    }

    /**
     * logical function sigma0(x) - xor of results of right rotations
     * @return S(x,2) xor S(x,13) xor S(x,22)
     * @param x int
     */
    private static int lf_sigma0(int x) {
        return lf_S(x, 2) ^ lf_S(x, 13) ^ lf_S(x, 22);
    }

    /**
     * logical function sigma1(x) - xor of results of right rotations
     * @return S(x,6) xor S(x,11) xor S(x,25)
     * @param x int
     */
    private static int lf_sigma1(int x) {
        return lf_S( x, 6 ) ^ lf_S( x, 11 ) ^ lf_S( x, 25 );
    }

    /**
     * logical function delta0(x) - xor of results of right shifts/rotations
     * @return int
     * @param x int
     */
    private static int lf_delta0(int x) {
        return lf_S(x, 7) ^ lf_S(x, 18) ^ lf_R(x, 3);
    }

    /**
     * logical function delta1(x) - xor of results of right shifts/rotations
     * @return int
     * @param x int
     */
    private static int lf_delta1(int x) {
        return lf_S(x, 17) ^ lf_S(x, 19) ^ lf_R(x, 10);
    }

    /**
     * Process the current block to update the state variable state.
     */
    void implCompress(byte[] buf, int ofs) {
        implCompressCheck(buf, ofs);
        implCompress0(buf, ofs);
    }

    private void implCompressCheck(byte[] buf, int ofs) {
        Objects.requireNonNull(buf);

        // The checks performed by the method 'b2iBig64'
        // are sufficient for the case when the method
        // 'implCompressImpl' is replaced with a compiler
        // intrinsic.
        b2iBig64(buf, ofs, W);
    }

    // The method 'implCompressImpl' seems not to use its parameters.
    // The method can, however, be replaced with a compiler intrinsic
    // that operates directly on the array 'buf' (starting from
    // offset 'ofs') and not on array 'W', therefore 'buf' and 'ofs'
    // must be passed as parameter to the method.
    private void implCompress0(byte[] buf, int ofs) {
        // The first 16 ints are from the byte stream, compute the rest of
        // the W[]'s
        for (int t = 16; t < ITERATION; t++) {
            W[t] = lf_delta1(W[t-2]) + W[t-7] + lf_delta0(W[t-15])
                   + W[t-16];
        }

        int a = state[0];
        int b = state[1];
        int c = state[2];
        int d = state[3];
        int e = state[4];
        int f = state[5];
        int g = state[6];
        int h = state[7];

        for (int i = 0; i < ITERATION; i++) {
            int T1 = h + lf_sigma1(e) + lf_ch(e,f,g) + ROUND_CONSTS[i] + W[i];
            int T2 = lf_sigma0(a) + lf_maj(a,b,c);
            h = g;
            g = f;
            f = e;
            e = d + T1;
            d = c;
            c = b;
            b = a;
            a = T1 + T2;
        }
        state[0] += a;
        state[1] += b;
        state[2] += c;
        state[3] += d;
        state[4] += e;
        state[5] += f;
        state[6] += g;
        state[7] += h;
    }

    public Object clone() throws CloneNotSupportedException {
        SHA2 copy = (SHA2) super.clone();
        copy.state = copy.state.clone();
        copy.W = new int[64];
        return copy;
    }

    /**
     * SHA-224 implementation class.
     */
    public static final class SHA224 extends SHA2 {
        private static final int[] INITIAL_HASHES = {
            0xc1059ed8, 0x367cd507, 0x3070dd17, 0xf70e5939,
            0xffc00b31, 0x68581511, 0x64f98fa7, 0xbefa4fa4
        };

        public SHA224() {
            super("SHA-224", 28, INITIAL_HASHES);
        }
    }

    /**
     * SHA-256 implementation class.
     */
    public static final class SHA256 extends SHA2 {
        private static final int[] INITIAL_HASHES = {
            0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
            0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
        };

        public SHA256() {
            super("SHA-256", 32, INITIAL_HASHES);
        }
    }
}
