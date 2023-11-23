package sun.security.util.math.intpoly;

import java.math.BigInteger;
public class P521OrderField extends IntegerPolynomial {
    private static final int BITS_PER_LIMB = 28;
    private static final int NUM_LIMBS = 19;
    private static final int MAX_ADDS = 1;
    public static final BigInteger MODULUS = evaluateModulus();
    private static final long CARRY_ADD = 1 << 27;
    private static final int LIMB_MASK = -1 >>> (64 - BITS_PER_LIMB);
    public P521OrderField() {

        super(BITS_PER_LIMB, NUM_LIMBS, MAX_ADDS, MODULUS);

    }
    private static BigInteger evaluateModulus() {
        BigInteger result = BigInteger.valueOf(2).pow(521);
        result = result.add(BigInteger.valueOf(20472841));
        result = result.add(BigInteger.valueOf(2).pow(28).multiply(BigInteger.valueOf(117141993)));
        result = result.subtract(BigInteger.valueOf(2).pow(56).multiply(BigInteger.valueOf(62411077)));
        result = result.subtract(BigInteger.valueOf(2).pow(84).multiply(BigInteger.valueOf(56915814)));
        result = result.add(BigInteger.valueOf(2).pow(112).multiply(BigInteger.valueOf(97532854)));
        result = result.add(BigInteger.valueOf(2).pow(140).multiply(BigInteger.valueOf(76509338)));
        result = result.subtract(BigInteger.valueOf(2).pow(168).multiply(BigInteger.valueOf(75510783)));
        result = result.subtract(BigInteger.valueOf(2).pow(196).multiply(BigInteger.valueOf(67962521)));
        result = result.add(BigInteger.valueOf(2).pow(224).multiply(BigInteger.valueOf(25593732)));
        result = result.subtract(BigInteger.valueOf(2).pow(252).multiply(BigInteger.valueOf(91)));
        return result;
    }
    @Override
    protected void finalCarryReduceLast(long[] limbs) {
        long c = limbs[18] >> 17;
        limbs[18] -= c << 17;
        long t0 = -20472841 * c;
        limbs[0] += t0;
        t0 = -117141993 * c;
        limbs[1] += t0;
        t0 = 62411077 * c;
        limbs[2] += t0;
        t0 = 56915814 * c;
        limbs[3] += t0;
        t0 = -97532854 * c;
        limbs[4] += t0;
        t0 = -76509338 * c;
        limbs[5] += t0;
        t0 = 75510783 * c;
        limbs[6] += t0;
        t0 = 67962521 * c;
        limbs[7] += t0;
        t0 = -25593732 * c;
        limbs[8] += t0;
        t0 = 91 * c;
        limbs[9] += t0;
    }
    private void carryReduce(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27, long c28, long c29, long c30, long c31, long c32, long c33, long c34, long c35, long c36) {
        long c37 = 0;
        //carry from position 0
        long t0 = (c0 + CARRY_ADD) >> 28;
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
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 28;
        c13 -= (t0 << 28);
        c14 += t0;
        //carry from position 14
        t0 = (c14 + CARRY_ADD) >> 28;
        c14 -= (t0 << 28);
        c15 += t0;
        //carry from position 15
        t0 = (c15 + CARRY_ADD) >> 28;
        c15 -= (t0 << 28);
        c16 += t0;
        //carry from position 16
        t0 = (c16 + CARRY_ADD) >> 28;
        c16 -= (t0 << 28);
        c17 += t0;
        //carry from position 17
        t0 = (c17 + CARRY_ADD) >> 28;
        c17 -= (t0 << 28);
        c18 += t0;
        //carry from position 18
        t0 = (c18 + CARRY_ADD) >> 28;
        c18 -= (t0 << 28);
        c19 += t0;
        //carry from position 19
        t0 = (c19 + CARRY_ADD) >> 28;
        c19 -= (t0 << 28);
        c20 += t0;
        //carry from position 20
        t0 = (c20 + CARRY_ADD) >> 28;
        c20 -= (t0 << 28);
        c21 += t0;
        //carry from position 21
        t0 = (c21 + CARRY_ADD) >> 28;
        c21 -= (t0 << 28);
        c22 += t0;
        //carry from position 22
        t0 = (c22 + CARRY_ADD) >> 28;
        c22 -= (t0 << 28);
        c23 += t0;
        //carry from position 23
        t0 = (c23 + CARRY_ADD) >> 28;
        c23 -= (t0 << 28);
        c24 += t0;
        //carry from position 24
        t0 = (c24 + CARRY_ADD) >> 28;
        c24 -= (t0 << 28);
        c25 += t0;
        //carry from position 25
        t0 = (c25 + CARRY_ADD) >> 28;
        c25 -= (t0 << 28);
        c26 += t0;
        //carry from position 26
        t0 = (c26 + CARRY_ADD) >> 28;
        c26 -= (t0 << 28);
        c27 += t0;
        //carry from position 27
        t0 = (c27 + CARRY_ADD) >> 28;
        c27 -= (t0 << 28);
        c28 += t0;
        //carry from position 28
        t0 = (c28 + CARRY_ADD) >> 28;
        c28 -= (t0 << 28);
        c29 += t0;
        //carry from position 29
        t0 = (c29 + CARRY_ADD) >> 28;
        c29 -= (t0 << 28);
        c30 += t0;
        //carry from position 30
        t0 = (c30 + CARRY_ADD) >> 28;
        c30 -= (t0 << 28);
        c31 += t0;
        //carry from position 31
        t0 = (c31 + CARRY_ADD) >> 28;
        c31 -= (t0 << 28);
        c32 += t0;
        //carry from position 32
        t0 = (c32 + CARRY_ADD) >> 28;
        c32 -= (t0 << 28);
        c33 += t0;
        //carry from position 33
        t0 = (c33 + CARRY_ADD) >> 28;
        c33 -= (t0 << 28);
        c34 += t0;
        //carry from position 34
        t0 = (c34 + CARRY_ADD) >> 28;
        c34 -= (t0 << 28);
        c35 += t0;
        //carry from position 35
        t0 = (c35 + CARRY_ADD) >> 28;
        c35 -= (t0 << 28);
        c36 += t0;
        //carry from position 36
        t0 = (c36 + CARRY_ADD) >> 28;
        c36 -= (t0 << 28);
        c37 += t0;

        carryReduce0(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36, c37);
    }
    void carryReduce0(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27, long c28, long c29, long c30, long c31, long c32, long c33, long c34, long c35, long c36, long c37) {
        long t0;

        //reduce from position 37
        t0 = -20472841 * c37;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = -117141993 * c37;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = 62411077 * c37;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        t0 = 56915814 * c37;
        c21 += (t0 << 11) & LIMB_MASK;
        c22 += t0 >> 17;
        t0 = -97532854 * c37;
        c22 += (t0 << 11) & LIMB_MASK;
        c23 += t0 >> 17;
        t0 = -76509338 * c37;
        c23 += (t0 << 11) & LIMB_MASK;
        c24 += t0 >> 17;
        t0 = 75510783 * c37;
        c24 += (t0 << 11) & LIMB_MASK;
        c25 += t0 >> 17;
        t0 = 67962521 * c37;
        c25 += (t0 << 11) & LIMB_MASK;
        c26 += t0 >> 17;
        t0 = -25593732 * c37;
        c26 += (t0 << 11) & LIMB_MASK;
        c27 += t0 >> 17;
        t0 = 91 * c37;
        c27 += (t0 << 11) & LIMB_MASK;
        c28 += t0 >> 17;
        //reduce from position 36
        t0 = -20472841 * c36;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = -117141993 * c36;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = 62411077 * c36;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = 56915814 * c36;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        t0 = -97532854 * c36;
        c21 += (t0 << 11) & LIMB_MASK;
        c22 += t0 >> 17;
        t0 = -76509338 * c36;
        c22 += (t0 << 11) & LIMB_MASK;
        c23 += t0 >> 17;
        t0 = 75510783 * c36;
        c23 += (t0 << 11) & LIMB_MASK;
        c24 += t0 >> 17;
        t0 = 67962521 * c36;
        c24 += (t0 << 11) & LIMB_MASK;
        c25 += t0 >> 17;
        t0 = -25593732 * c36;
        c25 += (t0 << 11) & LIMB_MASK;
        c26 += t0 >> 17;
        t0 = 91 * c36;
        c26 += (t0 << 11) & LIMB_MASK;
        c27 += t0 >> 17;
        //reduce from position 35
        t0 = -20472841 * c35;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = -117141993 * c35;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = 62411077 * c35;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = 56915814 * c35;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = -97532854 * c35;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        t0 = -76509338 * c35;
        c21 += (t0 << 11) & LIMB_MASK;
        c22 += t0 >> 17;
        t0 = 75510783 * c35;
        c22 += (t0 << 11) & LIMB_MASK;
        c23 += t0 >> 17;
        t0 = 67962521 * c35;
        c23 += (t0 << 11) & LIMB_MASK;
        c24 += t0 >> 17;
        t0 = -25593732 * c35;
        c24 += (t0 << 11) & LIMB_MASK;
        c25 += t0 >> 17;
        t0 = 91 * c35;
        c25 += (t0 << 11) & LIMB_MASK;
        c26 += t0 >> 17;
        //reduce from position 34
        t0 = -20472841 * c34;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = -117141993 * c34;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = 62411077 * c34;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = 56915814 * c34;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = -97532854 * c34;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = -76509338 * c34;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        t0 = 75510783 * c34;
        c21 += (t0 << 11) & LIMB_MASK;
        c22 += t0 >> 17;
        t0 = 67962521 * c34;
        c22 += (t0 << 11) & LIMB_MASK;
        c23 += t0 >> 17;
        t0 = -25593732 * c34;
        c23 += (t0 << 11) & LIMB_MASK;
        c24 += t0 >> 17;
        t0 = 91 * c34;
        c24 += (t0 << 11) & LIMB_MASK;
        c25 += t0 >> 17;
        //reduce from position 33
        t0 = -20472841 * c33;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = -117141993 * c33;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = 62411077 * c33;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = 56915814 * c33;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = -97532854 * c33;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = -76509338 * c33;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = 75510783 * c33;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        t0 = 67962521 * c33;
        c21 += (t0 << 11) & LIMB_MASK;
        c22 += t0 >> 17;
        t0 = -25593732 * c33;
        c22 += (t0 << 11) & LIMB_MASK;
        c23 += t0 >> 17;
        t0 = 91 * c33;
        c23 += (t0 << 11) & LIMB_MASK;
        c24 += t0 >> 17;
        //reduce from position 32
        t0 = -20472841 * c32;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = -117141993 * c32;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = 62411077 * c32;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = 56915814 * c32;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = -97532854 * c32;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = -76509338 * c32;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = 75510783 * c32;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = 67962521 * c32;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        t0 = -25593732 * c32;
        c21 += (t0 << 11) & LIMB_MASK;
        c22 += t0 >> 17;
        t0 = 91 * c32;
        c22 += (t0 << 11) & LIMB_MASK;
        c23 += t0 >> 17;
        //reduce from position 31
        t0 = -20472841 * c31;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = -117141993 * c31;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = 62411077 * c31;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = 56915814 * c31;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = -97532854 * c31;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = -76509338 * c31;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = 75510783 * c31;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = 67962521 * c31;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = -25593732 * c31;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        t0 = 91 * c31;
        c21 += (t0 << 11) & LIMB_MASK;
        c22 += t0 >> 17;
        //reduce from position 30
        t0 = -20472841 * c30;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = -117141993 * c30;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = 62411077 * c30;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = 56915814 * c30;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = -97532854 * c30;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = -76509338 * c30;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = 75510783 * c30;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = 67962521 * c30;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = -25593732 * c30;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        t0 = 91 * c30;
        c20 += (t0 << 11) & LIMB_MASK;
        c21 += t0 >> 17;
        //reduce from position 29
        t0 = -20472841 * c29;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = -117141993 * c29;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = 62411077 * c29;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = 56915814 * c29;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = -97532854 * c29;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = -76509338 * c29;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = 75510783 * c29;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = 67962521 * c29;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = -25593732 * c29;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;
        t0 = 91 * c29;
        c19 += (t0 << 11) & LIMB_MASK;
        c20 += t0 >> 17;
        //reduce from position 28
        t0 = -20472841 * c28;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = -117141993 * c28;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = 62411077 * c28;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = 56915814 * c28;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = -97532854 * c28;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = -76509338 * c28;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = 75510783 * c28;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = 67962521 * c28;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = -25593732 * c28;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        t0 = 91 * c28;
        c18 += (t0 << 11) & LIMB_MASK;
        c19 += t0 >> 17;

        carryReduce1(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36, c37);
    }
    void carryReduce1(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27, long c28, long c29, long c30, long c31, long c32, long c33, long c34, long c35, long c36, long c37) {
        long t0;

        //carry from position 19
        t0 = (c19 + CARRY_ADD) >> 28;
        c19 -= (t0 << 28);
        c20 += t0;
        //carry from position 20
        t0 = (c20 + CARRY_ADD) >> 28;
        c20 -= (t0 << 28);
        c21 += t0;
        //carry from position 21
        t0 = (c21 + CARRY_ADD) >> 28;
        c21 -= (t0 << 28);
        c22 += t0;
        //carry from position 22
        t0 = (c22 + CARRY_ADD) >> 28;
        c22 -= (t0 << 28);
        c23 += t0;
        //carry from position 23
        t0 = (c23 + CARRY_ADD) >> 28;
        c23 -= (t0 << 28);
        c24 += t0;
        //carry from position 24
        t0 = (c24 + CARRY_ADD) >> 28;
        c24 -= (t0 << 28);
        c25 += t0;
        //carry from position 25
        t0 = (c25 + CARRY_ADD) >> 28;
        c25 -= (t0 << 28);
        c26 += t0;
        //carry from position 26
        t0 = (c26 + CARRY_ADD) >> 28;
        c26 -= (t0 << 28);
        c27 += t0;
        //reduce from position 27
        t0 = -20472841 * c27;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = -117141993 * c27;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = 62411077 * c27;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = 56915814 * c27;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = -97532854 * c27;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = -76509338 * c27;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = 75510783 * c27;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = 67962521 * c27;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = -25593732 * c27;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        t0 = 91 * c27;
        c17 += (t0 << 11) & LIMB_MASK;
        c18 += t0 >> 17;
        //reduce from position 26
        t0 = -20472841 * c26;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = -117141993 * c26;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = 62411077 * c26;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = 56915814 * c26;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = -97532854 * c26;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = -76509338 * c26;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = 75510783 * c26;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = 67962521 * c26;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = -25593732 * c26;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        t0 = 91 * c26;
        c16 += (t0 << 11) & LIMB_MASK;
        c17 += t0 >> 17;
        //reduce from position 25
        t0 = -20472841 * c25;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = -117141993 * c25;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = 62411077 * c25;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = 56915814 * c25;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = -97532854 * c25;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = -76509338 * c25;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = 75510783 * c25;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = 67962521 * c25;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = -25593732 * c25;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        t0 = 91 * c25;
        c15 += (t0 << 11) & LIMB_MASK;
        c16 += t0 >> 17;
        //reduce from position 24
        t0 = -20472841 * c24;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = -117141993 * c24;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = 62411077 * c24;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = 56915814 * c24;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = -97532854 * c24;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = -76509338 * c24;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = 75510783 * c24;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = 67962521 * c24;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = -25593732 * c24;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        t0 = 91 * c24;
        c14 += (t0 << 11) & LIMB_MASK;
        c15 += t0 >> 17;
        //reduce from position 23
        t0 = -20472841 * c23;
        c4 += (t0 << 11) & LIMB_MASK;
        c5 += t0 >> 17;
        t0 = -117141993 * c23;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = 62411077 * c23;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = 56915814 * c23;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = -97532854 * c23;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = -76509338 * c23;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = 75510783 * c23;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = 67962521 * c23;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = -25593732 * c23;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        t0 = 91 * c23;
        c13 += (t0 << 11) & LIMB_MASK;
        c14 += t0 >> 17;
        //reduce from position 22
        t0 = -20472841 * c22;
        c3 += (t0 << 11) & LIMB_MASK;
        c4 += t0 >> 17;
        t0 = -117141993 * c22;
        c4 += (t0 << 11) & LIMB_MASK;
        c5 += t0 >> 17;
        t0 = 62411077 * c22;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = 56915814 * c22;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = -97532854 * c22;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = -76509338 * c22;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = 75510783 * c22;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = 67962521 * c22;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = -25593732 * c22;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        t0 = 91 * c22;
        c12 += (t0 << 11) & LIMB_MASK;
        c13 += t0 >> 17;
        //reduce from position 21
        t0 = -20472841 * c21;
        c2 += (t0 << 11) & LIMB_MASK;
        c3 += t0 >> 17;
        t0 = -117141993 * c21;
        c3 += (t0 << 11) & LIMB_MASK;
        c4 += t0 >> 17;
        t0 = 62411077 * c21;
        c4 += (t0 << 11) & LIMB_MASK;
        c5 += t0 >> 17;
        t0 = 56915814 * c21;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = -97532854 * c21;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = -76509338 * c21;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = 75510783 * c21;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = 67962521 * c21;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = -25593732 * c21;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        t0 = 91 * c21;
        c11 += (t0 << 11) & LIMB_MASK;
        c12 += t0 >> 17;
        //reduce from position 20
        t0 = -20472841 * c20;
        c1 += (t0 << 11) & LIMB_MASK;
        c2 += t0 >> 17;
        t0 = -117141993 * c20;
        c2 += (t0 << 11) & LIMB_MASK;
        c3 += t0 >> 17;
        t0 = 62411077 * c20;
        c3 += (t0 << 11) & LIMB_MASK;
        c4 += t0 >> 17;
        t0 = 56915814 * c20;
        c4 += (t0 << 11) & LIMB_MASK;
        c5 += t0 >> 17;
        t0 = -97532854 * c20;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = -76509338 * c20;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = 75510783 * c20;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = 67962521 * c20;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = -25593732 * c20;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        t0 = 91 * c20;
        c10 += (t0 << 11) & LIMB_MASK;
        c11 += t0 >> 17;
        //reduce from position 19
        t0 = -20472841 * c19;
        c0 += (t0 << 11) & LIMB_MASK;
        c1 += t0 >> 17;
        t0 = -117141993 * c19;
        c1 += (t0 << 11) & LIMB_MASK;
        c2 += t0 >> 17;
        t0 = 62411077 * c19;
        c2 += (t0 << 11) & LIMB_MASK;
        c3 += t0 >> 17;
        t0 = 56915814 * c19;
        c3 += (t0 << 11) & LIMB_MASK;
        c4 += t0 >> 17;
        t0 = -97532854 * c19;
        c4 += (t0 << 11) & LIMB_MASK;
        c5 += t0 >> 17;
        t0 = -76509338 * c19;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = 75510783 * c19;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = 67962521 * c19;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = -25593732 * c19;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = 91 * c19;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
        c19 = 0;

        carryReduce2(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36, c37);
    }
    void carryReduce2(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27, long c28, long c29, long c30, long c31, long c32, long c33, long c34, long c35, long c36, long c37) {
        long t0;

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
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 28;
        c13 -= (t0 << 28);
        c14 += t0;
        //carry from position 14
        t0 = (c14 + CARRY_ADD) >> 28;
        c14 -= (t0 << 28);
        c15 += t0;
        //carry from position 15
        t0 = (c15 + CARRY_ADD) >> 28;
        c15 -= (t0 << 28);
        c16 += t0;
        //carry from position 16
        t0 = (c16 + CARRY_ADD) >> 28;
        c16 -= (t0 << 28);
        c17 += t0;
        //carry from position 17
        t0 = (c17 + CARRY_ADD) >> 28;
        c17 -= (t0 << 28);
        c18 += t0;
        //carry from position 18
        t0 = (c18 + CARRY_ADD) >> 28;
        c18 -= (t0 << 28);
        c19 += t0;

        carryReduce3(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36, c37);
    }
    void carryReduce3(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27, long c28, long c29, long c30, long c31, long c32, long c33, long c34, long c35, long c36, long c37) {
        long t0;

        //reduce from position 19
        t0 = -20472841 * c19;
        c0 += (t0 << 11) & LIMB_MASK;
        c1 += t0 >> 17;
        t0 = -117141993 * c19;
        c1 += (t0 << 11) & LIMB_MASK;
        c2 += t0 >> 17;
        t0 = 62411077 * c19;
        c2 += (t0 << 11) & LIMB_MASK;
        c3 += t0 >> 17;
        t0 = 56915814 * c19;
        c3 += (t0 << 11) & LIMB_MASK;
        c4 += t0 >> 17;
        t0 = -97532854 * c19;
        c4 += (t0 << 11) & LIMB_MASK;
        c5 += t0 >> 17;
        t0 = -76509338 * c19;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = 75510783 * c19;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = 67962521 * c19;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = -25593732 * c19;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = 91 * c19;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
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
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 28;
        c13 -= (t0 << 28);
        c14 += t0;
        //carry from position 14
        t0 = (c14 + CARRY_ADD) >> 28;
        c14 -= (t0 << 28);
        c15 += t0;
        //carry from position 15
        t0 = (c15 + CARRY_ADD) >> 28;
        c15 -= (t0 << 28);
        c16 += t0;
        //carry from position 16
        t0 = (c16 + CARRY_ADD) >> 28;
        c16 -= (t0 << 28);
        c17 += t0;
        //carry from position 17
        t0 = (c17 + CARRY_ADD) >> 28;
        c17 -= (t0 << 28);
        c18 += t0;

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
        r[14] = c14;
        r[15] = c15;
        r[16] = c16;
        r[17] = c17;
        r[18] = c18;
    }
    private void carryReduce(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18) {
        long c19 = 0;
        //carry from position 0
        long t0 = (c0 + CARRY_ADD) >> 28;
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
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 28;
        c13 -= (t0 << 28);
        c14 += t0;
        //carry from position 14
        t0 = (c14 + CARRY_ADD) >> 28;
        c14 -= (t0 << 28);
        c15 += t0;
        //carry from position 15
        t0 = (c15 + CARRY_ADD) >> 28;
        c15 -= (t0 << 28);
        c16 += t0;
        //carry from position 16
        t0 = (c16 + CARRY_ADD) >> 28;
        c16 -= (t0 << 28);
        c17 += t0;
        //carry from position 17
        t0 = (c17 + CARRY_ADD) >> 28;
        c17 -= (t0 << 28);
        c18 += t0;
        //carry from position 18
        t0 = (c18 + CARRY_ADD) >> 28;
        c18 -= (t0 << 28);
        c19 += t0;

        carryReduce0(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19);
    }
    void carryReduce0(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19) {
        long t0;

        //reduce from position 19
        t0 = -20472841 * c19;
        c0 += (t0 << 11) & LIMB_MASK;
        c1 += t0 >> 17;
        t0 = -117141993 * c19;
        c1 += (t0 << 11) & LIMB_MASK;
        c2 += t0 >> 17;
        t0 = 62411077 * c19;
        c2 += (t0 << 11) & LIMB_MASK;
        c3 += t0 >> 17;
        t0 = 56915814 * c19;
        c3 += (t0 << 11) & LIMB_MASK;
        c4 += t0 >> 17;
        t0 = -97532854 * c19;
        c4 += (t0 << 11) & LIMB_MASK;
        c5 += t0 >> 17;
        t0 = -76509338 * c19;
        c5 += (t0 << 11) & LIMB_MASK;
        c6 += t0 >> 17;
        t0 = 75510783 * c19;
        c6 += (t0 << 11) & LIMB_MASK;
        c7 += t0 >> 17;
        t0 = 67962521 * c19;
        c7 += (t0 << 11) & LIMB_MASK;
        c8 += t0 >> 17;
        t0 = -25593732 * c19;
        c8 += (t0 << 11) & LIMB_MASK;
        c9 += t0 >> 17;
        t0 = 91 * c19;
        c9 += (t0 << 11) & LIMB_MASK;
        c10 += t0 >> 17;
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
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 28;
        c13 -= (t0 << 28);
        c14 += t0;
        //carry from position 14
        t0 = (c14 + CARRY_ADD) >> 28;
        c14 -= (t0 << 28);
        c15 += t0;
        //carry from position 15
        t0 = (c15 + CARRY_ADD) >> 28;
        c15 -= (t0 << 28);
        c16 += t0;
        //carry from position 16
        t0 = (c16 + CARRY_ADD) >> 28;
        c16 -= (t0 << 28);
        c17 += t0;
        //carry from position 17
        t0 = (c17 + CARRY_ADD) >> 28;
        c17 -= (t0 << 28);
        c18 += t0;

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
        r[14] = c14;
        r[15] = c15;
        r[16] = c16;
        r[17] = c17;
        r[18] = c18;
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
        long c14 = (a[0] * b[14]) + (a[1] * b[13]) + (a[2] * b[12]) + (a[3] * b[11]) + (a[4] * b[10]) + (a[5] * b[9]) + (a[6] * b[8]) + (a[7] * b[7]) + (a[8] * b[6]) + (a[9] * b[5]) + (a[10] * b[4]) + (a[11] * b[3]) + (a[12] * b[2]) + (a[13] * b[1]) + (a[14] * b[0]);
        long c15 = (a[0] * b[15]) + (a[1] * b[14]) + (a[2] * b[13]) + (a[3] * b[12]) + (a[4] * b[11]) + (a[5] * b[10]) + (a[6] * b[9]) + (a[7] * b[8]) + (a[8] * b[7]) + (a[9] * b[6]) + (a[10] * b[5]) + (a[11] * b[4]) + (a[12] * b[3]) + (a[13] * b[2]) + (a[14] * b[1]) + (a[15] * b[0]);
        long c16 = (a[0] * b[16]) + (a[1] * b[15]) + (a[2] * b[14]) + (a[3] * b[13]) + (a[4] * b[12]) + (a[5] * b[11]) + (a[6] * b[10]) + (a[7] * b[9]) + (a[8] * b[8]) + (a[9] * b[7]) + (a[10] * b[6]) + (a[11] * b[5]) + (a[12] * b[4]) + (a[13] * b[3]) + (a[14] * b[2]) + (a[15] * b[1]) + (a[16] * b[0]);
        long c17 = (a[0] * b[17]) + (a[1] * b[16]) + (a[2] * b[15]) + (a[3] * b[14]) + (a[4] * b[13]) + (a[5] * b[12]) + (a[6] * b[11]) + (a[7] * b[10]) + (a[8] * b[9]) + (a[9] * b[8]) + (a[10] * b[7]) + (a[11] * b[6]) + (a[12] * b[5]) + (a[13] * b[4]) + (a[14] * b[3]) + (a[15] * b[2]) + (a[16] * b[1]) + (a[17] * b[0]);
        long c18 = (a[0] * b[18]) + (a[1] * b[17]) + (a[2] * b[16]) + (a[3] * b[15]) + (a[4] * b[14]) + (a[5] * b[13]) + (a[6] * b[12]) + (a[7] * b[11]) + (a[8] * b[10]) + (a[9] * b[9]) + (a[10] * b[8]) + (a[11] * b[7]) + (a[12] * b[6]) + (a[13] * b[5]) + (a[14] * b[4]) + (a[15] * b[3]) + (a[16] * b[2]) + (a[17] * b[1]) + (a[18] * b[0]);
        long c19 = (a[1] * b[18]) + (a[2] * b[17]) + (a[3] * b[16]) + (a[4] * b[15]) + (a[5] * b[14]) + (a[6] * b[13]) + (a[7] * b[12]) + (a[8] * b[11]) + (a[9] * b[10]) + (a[10] * b[9]) + (a[11] * b[8]) + (a[12] * b[7]) + (a[13] * b[6]) + (a[14] * b[5]) + (a[15] * b[4]) + (a[16] * b[3]) + (a[17] * b[2]) + (a[18] * b[1]);
        long c20 = (a[2] * b[18]) + (a[3] * b[17]) + (a[4] * b[16]) + (a[5] * b[15]) + (a[6] * b[14]) + (a[7] * b[13]) + (a[8] * b[12]) + (a[9] * b[11]) + (a[10] * b[10]) + (a[11] * b[9]) + (a[12] * b[8]) + (a[13] * b[7]) + (a[14] * b[6]) + (a[15] * b[5]) + (a[16] * b[4]) + (a[17] * b[3]) + (a[18] * b[2]);
        long c21 = (a[3] * b[18]) + (a[4] * b[17]) + (a[5] * b[16]) + (a[6] * b[15]) + (a[7] * b[14]) + (a[8] * b[13]) + (a[9] * b[12]) + (a[10] * b[11]) + (a[11] * b[10]) + (a[12] * b[9]) + (a[13] * b[8]) + (a[14] * b[7]) + (a[15] * b[6]) + (a[16] * b[5]) + (a[17] * b[4]) + (a[18] * b[3]);
        long c22 = (a[4] * b[18]) + (a[5] * b[17]) + (a[6] * b[16]) + (a[7] * b[15]) + (a[8] * b[14]) + (a[9] * b[13]) + (a[10] * b[12]) + (a[11] * b[11]) + (a[12] * b[10]) + (a[13] * b[9]) + (a[14] * b[8]) + (a[15] * b[7]) + (a[16] * b[6]) + (a[17] * b[5]) + (a[18] * b[4]);
        long c23 = (a[5] * b[18]) + (a[6] * b[17]) + (a[7] * b[16]) + (a[8] * b[15]) + (a[9] * b[14]) + (a[10] * b[13]) + (a[11] * b[12]) + (a[12] * b[11]) + (a[13] * b[10]) + (a[14] * b[9]) + (a[15] * b[8]) + (a[16] * b[7]) + (a[17] * b[6]) + (a[18] * b[5]);
        long c24 = (a[6] * b[18]) + (a[7] * b[17]) + (a[8] * b[16]) + (a[9] * b[15]) + (a[10] * b[14]) + (a[11] * b[13]) + (a[12] * b[12]) + (a[13] * b[11]) + (a[14] * b[10]) + (a[15] * b[9]) + (a[16] * b[8]) + (a[17] * b[7]) + (a[18] * b[6]);
        long c25 = (a[7] * b[18]) + (a[8] * b[17]) + (a[9] * b[16]) + (a[10] * b[15]) + (a[11] * b[14]) + (a[12] * b[13]) + (a[13] * b[12]) + (a[14] * b[11]) + (a[15] * b[10]) + (a[16] * b[9]) + (a[17] * b[8]) + (a[18] * b[7]);
        long c26 = (a[8] * b[18]) + (a[9] * b[17]) + (a[10] * b[16]) + (a[11] * b[15]) + (a[12] * b[14]) + (a[13] * b[13]) + (a[14] * b[12]) + (a[15] * b[11]) + (a[16] * b[10]) + (a[17] * b[9]) + (a[18] * b[8]);
        long c27 = (a[9] * b[18]) + (a[10] * b[17]) + (a[11] * b[16]) + (a[12] * b[15]) + (a[13] * b[14]) + (a[14] * b[13]) + (a[15] * b[12]) + (a[16] * b[11]) + (a[17] * b[10]) + (a[18] * b[9]);
        long c28 = (a[10] * b[18]) + (a[11] * b[17]) + (a[12] * b[16]) + (a[13] * b[15]) + (a[14] * b[14]) + (a[15] * b[13]) + (a[16] * b[12]) + (a[17] * b[11]) + (a[18] * b[10]);
        long c29 = (a[11] * b[18]) + (a[12] * b[17]) + (a[13] * b[16]) + (a[14] * b[15]) + (a[15] * b[14]) + (a[16] * b[13]) + (a[17] * b[12]) + (a[18] * b[11]);
        long c30 = (a[12] * b[18]) + (a[13] * b[17]) + (a[14] * b[16]) + (a[15] * b[15]) + (a[16] * b[14]) + (a[17] * b[13]) + (a[18] * b[12]);
        long c31 = (a[13] * b[18]) + (a[14] * b[17]) + (a[15] * b[16]) + (a[16] * b[15]) + (a[17] * b[14]) + (a[18] * b[13]);
        long c32 = (a[14] * b[18]) + (a[15] * b[17]) + (a[16] * b[16]) + (a[17] * b[15]) + (a[18] * b[14]);
        long c33 = (a[15] * b[18]) + (a[16] * b[17]) + (a[17] * b[16]) + (a[18] * b[15]);
        long c34 = (a[16] * b[18]) + (a[17] * b[17]) + (a[18] * b[16]);
        long c35 = (a[17] * b[18]) + (a[18] * b[17]);
        long c36 = (a[18] * b[18]);

        carryReduce(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36);
    }
    @Override
    protected void reduce(long[] a) {
        carryReduce(a, a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11], a[12], a[13], a[14], a[15], a[16], a[17], a[18]);
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
        long c14 = 2 * ((a[0] * a[14]) + (a[1] * a[13]) + (a[2] * a[12]) + (a[3] * a[11]) + (a[4] * a[10]) + (a[5] * a[9]) + (a[6] * a[8])) + (a[7] * a[7]);
        long c15 = 2 * ((a[0] * a[15]) + (a[1] * a[14]) + (a[2] * a[13]) + (a[3] * a[12]) + (a[4] * a[11]) + (a[5] * a[10]) + (a[6] * a[9]) + (a[7] * a[8]));
        long c16 = 2 * ((a[0] * a[16]) + (a[1] * a[15]) + (a[2] * a[14]) + (a[3] * a[13]) + (a[4] * a[12]) + (a[5] * a[11]) + (a[6] * a[10]) + (a[7] * a[9])) + (a[8] * a[8]);
        long c17 = 2 * ((a[0] * a[17]) + (a[1] * a[16]) + (a[2] * a[15]) + (a[3] * a[14]) + (a[4] * a[13]) + (a[5] * a[12]) + (a[6] * a[11]) + (a[7] * a[10]) + (a[8] * a[9]));
        long c18 = 2 * ((a[0] * a[18]) + (a[1] * a[17]) + (a[2] * a[16]) + (a[3] * a[15]) + (a[4] * a[14]) + (a[5] * a[13]) + (a[6] * a[12]) + (a[7] * a[11]) + (a[8] * a[10])) + (a[9] * a[9]);
        long c19 = 2 * ((a[1] * a[18]) + (a[2] * a[17]) + (a[3] * a[16]) + (a[4] * a[15]) + (a[5] * a[14]) + (a[6] * a[13]) + (a[7] * a[12]) + (a[8] * a[11]) + (a[9] * a[10]));
        long c20 = 2 * ((a[2] * a[18]) + (a[3] * a[17]) + (a[4] * a[16]) + (a[5] * a[15]) + (a[6] * a[14]) + (a[7] * a[13]) + (a[8] * a[12]) + (a[9] * a[11])) + (a[10] * a[10]);
        long c21 = 2 * ((a[3] * a[18]) + (a[4] * a[17]) + (a[5] * a[16]) + (a[6] * a[15]) + (a[7] * a[14]) + (a[8] * a[13]) + (a[9] * a[12]) + (a[10] * a[11]));
        long c22 = 2 * ((a[4] * a[18]) + (a[5] * a[17]) + (a[6] * a[16]) + (a[7] * a[15]) + (a[8] * a[14]) + (a[9] * a[13]) + (a[10] * a[12])) + (a[11] * a[11]);
        long c23 = 2 * ((a[5] * a[18]) + (a[6] * a[17]) + (a[7] * a[16]) + (a[8] * a[15]) + (a[9] * a[14]) + (a[10] * a[13]) + (a[11] * a[12]));
        long c24 = 2 * ((a[6] * a[18]) + (a[7] * a[17]) + (a[8] * a[16]) + (a[9] * a[15]) + (a[10] * a[14]) + (a[11] * a[13])) + (a[12] * a[12]);
        long c25 = 2 * ((a[7] * a[18]) + (a[8] * a[17]) + (a[9] * a[16]) + (a[10] * a[15]) + (a[11] * a[14]) + (a[12] * a[13]));
        long c26 = 2 * ((a[8] * a[18]) + (a[9] * a[17]) + (a[10] * a[16]) + (a[11] * a[15]) + (a[12] * a[14])) + (a[13] * a[13]);
        long c27 = 2 * ((a[9] * a[18]) + (a[10] * a[17]) + (a[11] * a[16]) + (a[12] * a[15]) + (a[13] * a[14]));
        long c28 = 2 * ((a[10] * a[18]) + (a[11] * a[17]) + (a[12] * a[16]) + (a[13] * a[15])) + (a[14] * a[14]);
        long c29 = 2 * ((a[11] * a[18]) + (a[12] * a[17]) + (a[13] * a[16]) + (a[14] * a[15]));
        long c30 = 2 * ((a[12] * a[18]) + (a[13] * a[17]) + (a[14] * a[16])) + (a[15] * a[15]);
        long c31 = 2 * ((a[13] * a[18]) + (a[14] * a[17]) + (a[15] * a[16]));
        long c32 = 2 * ((a[14] * a[18]) + (a[15] * a[17])) + (a[16] * a[16]);
        long c33 = 2 * ((a[15] * a[18]) + (a[16] * a[17]));
        long c34 = 2 * ((a[16] * a[18])) + (a[17] * a[17]);
        long c35 = 2 * ((a[17] * a[18]));
        long c36 = (a[18] * a[18]);

        carryReduce(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36);
    }
}

