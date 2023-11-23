package sun.security.util.math.intpoly;

import java.math.BigInteger;
public class IntegerPolynomialP384 extends IntegerPolynomial {
    private static final int BITS_PER_LIMB = 28;
    private static final int NUM_LIMBS = 14;
    private static final int MAX_ADDS = 2;
    public static final BigInteger MODULUS = evaluateModulus();
    private static final long CARRY_ADD = 1 << 27;
    private static final int LIMB_MASK = -1 >>> (64 - BITS_PER_LIMB);
    public IntegerPolynomialP384() {

        super(BITS_PER_LIMB, NUM_LIMBS, MAX_ADDS, MODULUS);

    }
    private static BigInteger evaluateModulus() {
        BigInteger result = BigInteger.valueOf(2).pow(384);
        result = result.subtract(BigInteger.valueOf(2).pow(128));
        result = result.subtract(BigInteger.valueOf(2).pow(96));
        result = result.add(BigInteger.valueOf(2).pow(32));
        result = result.subtract(BigInteger.valueOf(1));
        return result;
    }
    @Override
    protected void finalCarryReduceLast(long[] limbs) {
        long c = limbs[13] >> 20;
        limbs[13] -= c << 20;
        limbs[4] += (c << 16) & LIMB_MASK;
        limbs[5] += c >> 12;
        limbs[3] += (c << 12) & LIMB_MASK;
        limbs[4] += c >> 16;
        limbs[1] -= (c << 4) & LIMB_MASK;
        limbs[2] -= c >> 24;
        limbs[0] += c;
    }
    private void carryReduce(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26) {
        long c27 = 0;
        //reduce from position 26
        c16 += (c26 << 24) & LIMB_MASK;
        c17 += c26 >> 4;
        c15 += (c26 << 20) & LIMB_MASK;
        c16 += c26 >> 8;
        c13 -= (c26 << 12) & LIMB_MASK;
        c14 -= c26 >> 16;
        c12 += (c26 << 8) & LIMB_MASK;
        c13 += c26 >> 20;
        //reduce from position 25
        c15 += (c25 << 24) & LIMB_MASK;
        c16 += c25 >> 4;
        c14 += (c25 << 20) & LIMB_MASK;
        c15 += c25 >> 8;
        c12 -= (c25 << 12) & LIMB_MASK;
        c13 -= c25 >> 16;
        c11 += (c25 << 8) & LIMB_MASK;
        c12 += c25 >> 20;
        //reduce from position 24
        c14 += (c24 << 24) & LIMB_MASK;
        c15 += c24 >> 4;
        c13 += (c24 << 20) & LIMB_MASK;
        c14 += c24 >> 8;
        c11 -= (c24 << 12) & LIMB_MASK;
        c12 -= c24 >> 16;
        c10 += (c24 << 8) & LIMB_MASK;
        c11 += c24 >> 20;
        //reduce from position 23
        c13 += (c23 << 24) & LIMB_MASK;
        c14 += c23 >> 4;
        c12 += (c23 << 20) & LIMB_MASK;
        c13 += c23 >> 8;
        c10 -= (c23 << 12) & LIMB_MASK;
        c11 -= c23 >> 16;
        c9 += (c23 << 8) & LIMB_MASK;
        c10 += c23 >> 20;
        //reduce from position 22
        c12 += (c22 << 24) & LIMB_MASK;
        c13 += c22 >> 4;
        c11 += (c22 << 20) & LIMB_MASK;
        c12 += c22 >> 8;
        c9 -= (c22 << 12) & LIMB_MASK;
        c10 -= c22 >> 16;
        c8 += (c22 << 8) & LIMB_MASK;
        c9 += c22 >> 20;
        //reduce from position 21
        c11 += (c21 << 24) & LIMB_MASK;
        c12 += c21 >> 4;
        c10 += (c21 << 20) & LIMB_MASK;
        c11 += c21 >> 8;
        c8 -= (c21 << 12) & LIMB_MASK;
        c9 -= c21 >> 16;
        c7 += (c21 << 8) & LIMB_MASK;
        c8 += c21 >> 20;
        //reduce from position 20
        c10 += (c20 << 24) & LIMB_MASK;
        c11 += c20 >> 4;
        c9 += (c20 << 20) & LIMB_MASK;
        c10 += c20 >> 8;
        c7 -= (c20 << 12) & LIMB_MASK;
        c8 -= c20 >> 16;
        c6 += (c20 << 8) & LIMB_MASK;
        c7 += c20 >> 20;
        //reduce from position 19
        c9 += (c19 << 24) & LIMB_MASK;
        c10 += c19 >> 4;
        c8 += (c19 << 20) & LIMB_MASK;
        c9 += c19 >> 8;
        c6 -= (c19 << 12) & LIMB_MASK;
        c7 -= c19 >> 16;
        c5 += (c19 << 8) & LIMB_MASK;
        c6 += c19 >> 20;
        //reduce from position 18
        c8 += (c18 << 24) & LIMB_MASK;
        c9 += c18 >> 4;
        c7 += (c18 << 20) & LIMB_MASK;
        c8 += c18 >> 8;
        c5 -= (c18 << 12) & LIMB_MASK;
        c6 -= c18 >> 16;
        c4 += (c18 << 8) & LIMB_MASK;
        c5 += c18 >> 20;
        //reduce from position 17
        c7 += (c17 << 24) & LIMB_MASK;
        c8 += c17 >> 4;
        c6 += (c17 << 20) & LIMB_MASK;
        c7 += c17 >> 8;
        c4 -= (c17 << 12) & LIMB_MASK;
        c5 -= c17 >> 16;
        c3 += (c17 << 8) & LIMB_MASK;
        c4 += c17 >> 20;
        //reduce from position 16
        c6 += (c16 << 24) & LIMB_MASK;
        c7 += c16 >> 4;
        c5 += (c16 << 20) & LIMB_MASK;
        c6 += c16 >> 8;
        c3 -= (c16 << 12) & LIMB_MASK;
        c4 -= c16 >> 16;
        c2 += (c16 << 8) & LIMB_MASK;
        c3 += c16 >> 20;
        //reduce from position 15
        c5 += (c15 << 24) & LIMB_MASK;
        c6 += c15 >> 4;
        c4 += (c15 << 20) & LIMB_MASK;
        c5 += c15 >> 8;
        c2 -= (c15 << 12) & LIMB_MASK;
        c3 -= c15 >> 16;
        c1 += (c15 << 8) & LIMB_MASK;
        c2 += c15 >> 20;
        //reduce from position 14
        c4 += (c14 << 24) & LIMB_MASK;
        c5 += c14 >> 4;
        c3 += (c14 << 20) & LIMB_MASK;
        c4 += c14 >> 8;
        c1 -= (c14 << 12) & LIMB_MASK;
        c2 -= c14 >> 16;
        c0 += (c14 << 8) & LIMB_MASK;
        c1 += c14 >> 20;
        c14 = 0;

        carryReduce0(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27);
    }
    void carryReduce0(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27) {

        //carry from position 12
        long t0 = (c12 + CARRY_ADD) >> 28;
        c12 -= (t0 << 28);
        c13 += t0;
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 28;
        c13 -= (t0 << 28);
        c14 += t0;
        //reduce from position 14
        c4 += (c14 << 24) & LIMB_MASK;
        c5 += c14 >> 4;
        c3 += (c14 << 20) & LIMB_MASK;
        c4 += c14 >> 8;
        c1 -= (c14 << 12) & LIMB_MASK;
        c2 -= c14 >> 16;
        c0 += (c14 << 8) & LIMB_MASK;
        c1 += c14 >> 20;
        //carry from position 0
        t0 = (c0 + CARRY_ADD) >> 28;
        c0 -= (t0 << 28);
        c1 += t0;
        //carry from position 1
        t0 = (c1 + CARRY_ADD) >> 28;
        c1 -= (t0 << 28);
        c2 += t0;
        //carry from position 2
        t0 = (c2 + CARRY_ADD) >> 28;
        c2 -= (t0 << 28);
        c3 += t0;
        //carry from position 3
        t0 = (c3 + CARRY_ADD) >> 28;
        c3 -= (t0 << 28);
        c4 += t0;
        //carry from position 4
        t0 = (c4 + CARRY_ADD) >> 28;
        c4 -= (t0 << 28);
        c5 += t0;
        //carry from position 5
        t0 = (c5 + CARRY_ADD) >> 28;
        c5 -= (t0 << 28);
        c6 += t0;
        //carry from position 6
        t0 = (c6 + CARRY_ADD) >> 28;
        c6 -= (t0 << 28);
        c7 += t0;
        //carry from position 7
        t0 = (c7 + CARRY_ADD) >> 28;
        c7 -= (t0 << 28);
        c8 += t0;
        //carry from position 8
        t0 = (c8 + CARRY_ADD) >> 28;
        c8 -= (t0 << 28);
        c9 += t0;
        //carry from position 9
        t0 = (c9 + CARRY_ADD) >> 28;
        c9 -= (t0 << 28);
        c10 += t0;
        //carry from position 10
        t0 = (c10 + CARRY_ADD) >> 28;
        c10 -= (t0 << 28);
        c11 += t0;
        //carry from position 11
        t0 = (c11 + CARRY_ADD) >> 28;
        c11 -= (t0 << 28);
        c12 += t0;
        //carry from position 12
        t0 = (c12 + CARRY_ADD) >> 28;
        c12 -= (t0 << 28);
        c13 += t0;

        r[0] = c0;
        r[1] = c1;
        r[2] = c2;
        r[3] = c3;
        r[4] = c4;
        r[5] = c5;
        r[6] = c6;
        r[7] = c7;
        r[8] = c8;
        r[9] = c9;
        r[10] = c10;
        r[11] = c11;
        r[12] = c12;
        r[13] = c13;
    }
    private void carryReduce(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13) {
        long c14 = 0;
        //carry from position 12
        long t0 = (c12 + CARRY_ADD) >> 28;
        c12 -= (t0 << 28);
        c13 += t0;
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 28;
        c13 -= (t0 << 28);
        c14 += t0;
        //reduce from position 14
        c4 += (c14 << 24) & LIMB_MASK;
        c5 += c14 >> 4;
        c3 += (c14 << 20) & LIMB_MASK;
        c4 += c14 >> 8;
        c1 -= (c14 << 12) & LIMB_MASK;
        c2 -= c14 >> 16;
        c0 += (c14 << 8) & LIMB_MASK;
        c1 += c14 >> 20;
        //carry from position 0
        t0 = (c0 + CARRY_ADD) >> 28;
        c0 -= (t0 << 28);
        c1 += t0;
        //carry from position 1
        t0 = (c1 + CARRY_ADD) >> 28;
        c1 -= (t0 << 28);
        c2 += t0;
        //carry from position 2
        t0 = (c2 + CARRY_ADD) >> 28;
        c2 -= (t0 << 28);
        c3 += t0;
        //carry from position 3
        t0 = (c3 + CARRY_ADD) >> 28;
        c3 -= (t0 << 28);
        c4 += t0;
        //carry from position 4
        t0 = (c4 + CARRY_ADD) >> 28;
        c4 -= (t0 << 28);
        c5 += t0;
        //carry from position 5
        t0 = (c5 + CARRY_ADD) >> 28;
        c5 -= (t0 << 28);
        c6 += t0;
        //carry from position 6
        t0 = (c6 + CARRY_ADD) >> 28;
        c6 -= (t0 << 28);
        c7 += t0;
        //carry from position 7
        t0 = (c7 + CARRY_ADD) >> 28;
        c7 -= (t0 << 28);
        c8 += t0;
        //carry from position 8
        t0 = (c8 + CARRY_ADD) >> 28;
        c8 -= (t0 << 28);
        c9 += t0;
        //carry from position 9
        t0 = (c9 + CARRY_ADD) >> 28;
        c9 -= (t0 << 28);
        c10 += t0;
        //carry from position 10
        t0 = (c10 + CARRY_ADD) >> 28;
        c10 -= (t0 << 28);
        c11 += t0;
        //carry from position 11
        t0 = (c11 + CARRY_ADD) >> 28;
        c11 -= (t0 << 28);
        c12 += t0;
        //carry from position 12
        t0 = (c12 + CARRY_ADD) >> 28;
        c12 -= (t0 << 28);
        c13 += t0;

        r[0] = c0;
        r[1] = c1;
        r[2] = c2;
        r[3] = c3;
        r[4] = c4;
        r[5] = c5;
        r[6] = c6;
        r[7] = c7;
        r[8] = c8;
        r[9] = c9;
        r[10] = c10;
        r[11] = c11;
        r[12] = c12;
        r[13] = c13;
    }
    @Override
    protected void mult(long[] a, long[] b, long[] r) {
        long c0 = (a[0] * b[0]);
        long c1 = (a[0] * b[1]) + (a[1] * b[0]);
        long c2 = (a[0] * b[2]) + (a[1] * b[1]) + (a[2] * b[0]);
        long c3 = (a[0] * b[3]) + (a[1] * b[2]) + (a[2] * b[1]) + (a[3] * b[0]);
        long c4 = (a[0] * b[4]) + (a[1] * b[3]) + (a[2] * b[2]) + (a[3] * b[1]) + (a[4] * b[0]);
        long c5 = (a[0] * b[5]) + (a[1] * b[4]) + (a[2] * b[3]) + (a[3] * b[2]) + (a[4] * b[1]) + (a[5] * b[0]);
        long c6 = (a[0] * b[6]) + (a[1] * b[5]) + (a[2] * b[4]) + (a[3] * b[3]) + (a[4] * b[2]) + (a[5] * b[1]) + (a[6] * b[0]);
        long c7 = (a[0] * b[7]) + (a[1] * b[6]) + (a[2] * b[5]) + (a[3] * b[4]) + (a[4] * b[3]) + (a[5] * b[2]) + (a[6] * b[1]) + (a[7] * b[0]);
        long c8 = (a[0] * b[8]) + (a[1] * b[7]) + (a[2] * b[6]) + (a[3] * b[5]) + (a[4] * b[4]) + (a[5] * b[3]) + (a[6] * b[2]) + (a[7] * b[1]) + (a[8] * b[0]);
        long c9 = (a[0] * b[9]) + (a[1] * b[8]) + (a[2] * b[7]) + (a[3] * b[6]) + (a[4] * b[5]) + (a[5] * b[4]) + (a[6] * b[3]) + (a[7] * b[2]) + (a[8] * b[1]) + (a[9] * b[0]);
        long c10 = (a[0] * b[10]) + (a[1] * b[9]) + (a[2] * b[8]) + (a[3] * b[7]) + (a[4] * b[6]) + (a[5] * b[5]) + (a[6] * b[4]) + (a[7] * b[3]) + (a[8] * b[2]) + (a[9] * b[1]) + (a[10] * b[0]);
        long c11 = (a[0] * b[11]) + (a[1] * b[10]) + (a[2] * b[9]) + (a[3] * b[8]) + (a[4] * b[7]) + (a[5] * b[6]) + (a[6] * b[5]) + (a[7] * b[4]) + (a[8] * b[3]) + (a[9] * b[2]) + (a[10] * b[1]) + (a[11] * b[0]);
        long c12 = (a[0] * b[12]) + (a[1] * b[11]) + (a[2] * b[10]) + (a[3] * b[9]) + (a[4] * b[8]) + (a[5] * b[7]) + (a[6] * b[6]) + (a[7] * b[5]) + (a[8] * b[4]) + (a[9] * b[3]) + (a[10] * b[2]) + (a[11] * b[1]) + (a[12] * b[0]);
        long c13 = (a[0] * b[13]) + (a[1] * b[12]) + (a[2] * b[11]) + (a[3] * b[10]) + (a[4] * b[9]) + (a[5] * b[8]) + (a[6] * b[7]) + (a[7] * b[6]) + (a[8] * b[5]) + (a[9] * b[4]) + (a[10] * b[3]) + (a[11] * b[2]) + (a[12] * b[1]) + (a[13] * b[0]);
        long c14 = (a[1] * b[13]) + (a[2] * b[12]) + (a[3] * b[11]) + (a[4] * b[10]) + (a[5] * b[9]) + (a[6] * b[8]) + (a[7] * b[7]) + (a[8] * b[6]) + (a[9] * b[5]) + (a[10] * b[4]) + (a[11] * b[3]) + (a[12] * b[2]) + (a[13] * b[1]);
        long c15 = (a[2] * b[13]) + (a[3] * b[12]) + (a[4] * b[11]) + (a[5] * b[10]) + (a[6] * b[9]) + (a[7] * b[8]) + (a[8] * b[7]) + (a[9] * b[6]) + (a[10] * b[5]) + (a[11] * b[4]) + (a[12] * b[3]) + (a[13] * b[2]);
        long c16 = (a[3] * b[13]) + (a[4] * b[12]) + (a[5] * b[11]) + (a[6] * b[10]) + (a[7] * b[9]) + (a[8] * b[8]) + (a[9] * b[7]) + (a[10] * b[6]) + (a[11] * b[5]) + (a[12] * b[4]) + (a[13] * b[3]);
        long c17 = (a[4] * b[13]) + (a[5] * b[12]) + (a[6] * b[11]) + (a[7] * b[10]) + (a[8] * b[9]) + (a[9] * b[8]) + (a[10] * b[7]) + (a[11] * b[6]) + (a[12] * b[5]) + (a[13] * b[4]);
        long c18 = (a[5] * b[13]) + (a[6] * b[12]) + (a[7] * b[11]) + (a[8] * b[10]) + (a[9] * b[9]) + (a[10] * b[8]) + (a[11] * b[7]) + (a[12] * b[6]) + (a[13] * b[5]);
        long c19 = (a[6] * b[13]) + (a[7] * b[12]) + (a[8] * b[11]) + (a[9] * b[10]) + (a[10] * b[9]) + (a[11] * b[8]) + (a[12] * b[7]) + (a[13] * b[6]);
        long c20 = (a[7] * b[13]) + (a[8] * b[12]) + (a[9] * b[11]) + (a[10] * b[10]) + (a[11] * b[9]) + (a[12] * b[8]) + (a[13] * b[7]);
        long c21 = (a[8] * b[13]) + (a[9] * b[12]) + (a[10] * b[11]) + (a[11] * b[10]) + (a[12] * b[9]) + (a[13] * b[8]);
        long c22 = (a[9] * b[13]) + (a[10] * b[12]) + (a[11] * b[11]) + (a[12] * b[10]) + (a[13] * b[9]);
        long c23 = (a[10] * b[13]) + (a[11] * b[12]) + (a[12] * b[11]) + (a[13] * b[10]);
        long c24 = (a[11] * b[13]) + (a[12] * b[12]) + (a[13] * b[11]);
        long c25 = (a[12] * b[13]) + (a[13] * b[12]);
        long c26 = (a[13] * b[13]);

        carryReduce(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26);
    }
    @Override
    protected void reduce(long[] a) {
        carryReduce(a, a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11], a[12], a[13]);
    }
    @Override
    protected void square(long[] a, long[] r) {
        long c0 = (a[0] * a[0]);
        long c1 = 2 * ((a[0] * a[1]));
        long c2 = 2 * ((a[0] * a[2])) + (a[1] * a[1]);
        long c3 = 2 * ((a[0] * a[3]) + (a[1] * a[2]));
        long c4 = 2 * ((a[0] * a[4]) + (a[1] * a[3])) + (a[2] * a[2]);
        long c5 = 2 * ((a[0] * a[5]) + (a[1] * a[4]) + (a[2] * a[3]));
        long c6 = 2 * ((a[0] * a[6]) + (a[1] * a[5]) + (a[2] * a[4])) + (a[3] * a[3]);
        long c7 = 2 * ((a[0] * a[7]) + (a[1] * a[6]) + (a[2] * a[5]) + (a[3] * a[4]));
        long c8 = 2 * ((a[0] * a[8]) + (a[1] * a[7]) + (a[2] * a[6]) + (a[3] * a[5])) + (a[4] * a[4]);
        long c9 = 2 * ((a[0] * a[9]) + (a[1] * a[8]) + (a[2] * a[7]) + (a[3] * a[6]) + (a[4] * a[5]));
        long c10 = 2 * ((a[0] * a[10]) + (a[1] * a[9]) + (a[2] * a[8]) + (a[3] * a[7]) + (a[4] * a[6])) + (a[5] * a[5]);
        long c11 = 2 * ((a[0] * a[11]) + (a[1] * a[10]) + (a[2] * a[9]) + (a[3] * a[8]) + (a[4] * a[7]) + (a[5] * a[6]));
        long c12 = 2 * ((a[0] * a[12]) + (a[1] * a[11]) + (a[2] * a[10]) + (a[3] * a[9]) + (a[4] * a[8]) + (a[5] * a[7])) + (a[6] * a[6]);
        long c13 = 2 * ((a[0] * a[13]) + (a[1] * a[12]) + (a[2] * a[11]) + (a[3] * a[10]) + (a[4] * a[9]) + (a[5] * a[8]) + (a[6] * a[7]));
        long c14 = 2 * ((a[1] * a[13]) + (a[2] * a[12]) + (a[3] * a[11]) + (a[4] * a[10]) + (a[5] * a[9]) + (a[6] * a[8])) + (a[7] * a[7]);
        long c15 = 2 * ((a[2] * a[13]) + (a[3] * a[12]) + (a[4] * a[11]) + (a[5] * a[10]) + (a[6] * a[9]) + (a[7] * a[8]));
        long c16 = 2 * ((a[3] * a[13]) + (a[4] * a[12]) + (a[5] * a[11]) + (a[6] * a[10]) + (a[7] * a[9])) + (a[8] * a[8]);
        long c17 = 2 * ((a[4] * a[13]) + (a[5] * a[12]) + (a[6] * a[11]) + (a[7] * a[10]) + (a[8] * a[9]));
        long c18 = 2 * ((a[5] * a[13]) + (a[6] * a[12]) + (a[7] * a[11]) + (a[8] * a[10])) + (a[9] * a[9]);
        long c19 = 2 * ((a[6] * a[13]) + (a[7] * a[12]) + (a[8] * a[11]) + (a[9] * a[10]));
        long c20 = 2 * ((a[7] * a[13]) + (a[8] * a[12]) + (a[9] * a[11])) + (a[10] * a[10]);
        long c21 = 2 * ((a[8] * a[13]) + (a[9] * a[12]) + (a[10] * a[11]));
        long c22 = 2 * ((a[9] * a[13]) + (a[10] * a[12])) + (a[11] * a[11]);
        long c23 = 2 * ((a[10] * a[13]) + (a[11] * a[12]));
        long c24 = 2 * ((a[11] * a[13])) + (a[12] * a[12]);
        long c25 = 2 * ((a[12] * a[13]));
        long c26 = (a[13] * a[13]);

        carryReduce(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26);
    }
}

