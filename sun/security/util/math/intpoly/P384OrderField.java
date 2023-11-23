package sun.security.util.math.intpoly;

import java.math.BigInteger;
public class P384OrderField extends IntegerPolynomial {
    private static final int BITS_PER_LIMB = 28;
    private static final int NUM_LIMBS = 14;
    private static final int MAX_ADDS = 1;
    public static final BigInteger MODULUS = evaluateModulus();
    private static final long CARRY_ADD = 1 << 27;
    private static final int LIMB_MASK = -1 >>> (64 - BITS_PER_LIMB);
    public P384OrderField() {

        super(BITS_PER_LIMB, NUM_LIMBS, MAX_ADDS, MODULUS);

    }
    private static BigInteger evaluateModulus() {
        BigInteger result = BigInteger.valueOf(2).pow(384);
        result = result.subtract(BigInteger.valueOf(54187661));
        result = result.subtract(BigInteger.valueOf(2).pow(28).multiply(BigInteger.valueOf(20867411)));
        result = result.add(BigInteger.valueOf(2).pow(56).multiply(BigInteger.valueOf(10975981)));
        result = result.add(BigInteger.valueOf(2).pow(84).multiply(BigInteger.valueOf(14361739)));
        result = result.subtract(BigInteger.valueOf(2).pow(112).multiply(BigInteger.valueOf(35694566)));
        result = result.subtract(BigInteger.valueOf(2).pow(140).multiply(BigInteger.valueOf(132168845)));
        result = result.subtract(BigInteger.valueOf(2).pow(168).multiply(BigInteger.valueOf(3710130)));
        return result;
    }
    @Override
    protected void finalCarryReduceLast(long[] limbs) {
        long c = limbs[13] >> 20;
        limbs[13] -= c << 20;
        long t0 = 54187661 * c;
        limbs[0] += t0;
        t0 = 20867411 * c;
        limbs[1] += t0;
        t0 = -10975981 * c;
        limbs[2] += t0;
        t0 = -14361739 * c;
        limbs[3] += t0;
        t0 = 35694566 * c;
        limbs[4] += t0;
        t0 = 132168845 * c;
        limbs[5] += t0;
        t0 = 3710130 * c;
        limbs[6] += t0;
    }
    private void carryReduce(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26) {
        long c27 = 0;
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

        carryReduce0(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27);
    }
    void carryReduce0(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27) {
        long t0;

        //reduce from position 27
        t0 = 54187661 * c27;
        c13 += (t0 << 8) & LIMB_MASK;
        c14 += t0 >> 20;
        t0 = 20867411 * c27;
        c14 += (t0 << 8) & LIMB_MASK;
        c15 += t0 >> 20;
        t0 = -10975981 * c27;
        c15 += (t0 << 8) & LIMB_MASK;
        c16 += t0 >> 20;
        t0 = -14361739 * c27;
        c16 += (t0 << 8) & LIMB_MASK;
        c17 += t0 >> 20;
        t0 = 35694566 * c27;
        c17 += (t0 << 8) & LIMB_MASK;
        c18 += t0 >> 20;
        t0 = 132168845 * c27;
        c18 += (t0 << 8) & LIMB_MASK;
        c19 += t0 >> 20;
        t0 = 3710130 * c27;
        c19 += (t0 << 8) & LIMB_MASK;
        c20 += t0 >> 20;
        //reduce from position 26
        t0 = 54187661 * c26;
        c12 += (t0 << 8) & LIMB_MASK;
        c13 += t0 >> 20;
        t0 = 20867411 * c26;
        c13 += (t0 << 8) & LIMB_MASK;
        c14 += t0 >> 20;
        t0 = -10975981 * c26;
        c14 += (t0 << 8) & LIMB_MASK;
        c15 += t0 >> 20;
        t0 = -14361739 * c26;
        c15 += (t0 << 8) & LIMB_MASK;
        c16 += t0 >> 20;
        t0 = 35694566 * c26;
        c16 += (t0 << 8) & LIMB_MASK;
        c17 += t0 >> 20;
        t0 = 132168845 * c26;
        c17 += (t0 << 8) & LIMB_MASK;
        c18 += t0 >> 20;
        t0 = 3710130 * c26;
        c18 += (t0 << 8) & LIMB_MASK;
        c19 += t0 >> 20;
        //reduce from position 25
        t0 = 54187661 * c25;
        c11 += (t0 << 8) & LIMB_MASK;
        c12 += t0 >> 20;
        t0 = 20867411 * c25;
        c12 += (t0 << 8) & LIMB_MASK;
        c13 += t0 >> 20;
        t0 = -10975981 * c25;
        c13 += (t0 << 8) & LIMB_MASK;
        c14 += t0 >> 20;
        t0 = -14361739 * c25;
        c14 += (t0 << 8) & LIMB_MASK;
        c15 += t0 >> 20;
        t0 = 35694566 * c25;
        c15 += (t0 << 8) & LIMB_MASK;
        c16 += t0 >> 20;
        t0 = 132168845 * c25;
        c16 += (t0 << 8) & LIMB_MASK;
        c17 += t0 >> 20;
        t0 = 3710130 * c25;
        c17 += (t0 << 8) & LIMB_MASK;
        c18 += t0 >> 20;
        //reduce from position 24
        t0 = 54187661 * c24;
        c10 += (t0 << 8) & LIMB_MASK;
        c11 += t0 >> 20;
        t0 = 20867411 * c24;
        c11 += (t0 << 8) & LIMB_MASK;
        c12 += t0 >> 20;
        t0 = -10975981 * c24;
        c12 += (t0 << 8) & LIMB_MASK;
        c13 += t0 >> 20;
        t0 = -14361739 * c24;
        c13 += (t0 << 8) & LIMB_MASK;
        c14 += t0 >> 20;
        t0 = 35694566 * c24;
        c14 += (t0 << 8) & LIMB_MASK;
        c15 += t0 >> 20;
        t0 = 132168845 * c24;
        c15 += (t0 << 8) & LIMB_MASK;
        c16 += t0 >> 20;
        t0 = 3710130 * c24;
        c16 += (t0 << 8) & LIMB_MASK;
        c17 += t0 >> 20;
        //reduce from position 23
        t0 = 54187661 * c23;
        c9 += (t0 << 8) & LIMB_MASK;
        c10 += t0 >> 20;
        t0 = 20867411 * c23;
        c10 += (t0 << 8) & LIMB_MASK;
        c11 += t0 >> 20;
        t0 = -10975981 * c23;
        c11 += (t0 << 8) & LIMB_MASK;
        c12 += t0 >> 20;
        t0 = -14361739 * c23;
        c12 += (t0 << 8) & LIMB_MASK;
        c13 += t0 >> 20;
        t0 = 35694566 * c23;
        c13 += (t0 << 8) & LIMB_MASK;
        c14 += t0 >> 20;
        t0 = 132168845 * c23;
        c14 += (t0 << 8) & LIMB_MASK;
        c15 += t0 >> 20;
        t0 = 3710130 * c23;
        c15 += (t0 << 8) & LIMB_MASK;
        c16 += t0 >> 20;
        //reduce from position 22
        t0 = 54187661 * c22;
        c8 += (t0 << 8) & LIMB_MASK;
        c9 += t0 >> 20;
        t0 = 20867411 * c22;
        c9 += (t0 << 8) & LIMB_MASK;
        c10 += t0 >> 20;
        t0 = -10975981 * c22;
        c10 += (t0 << 8) & LIMB_MASK;
        c11 += t0 >> 20;
        t0 = -14361739 * c22;
        c11 += (t0 << 8) & LIMB_MASK;
        c12 += t0 >> 20;
        t0 = 35694566 * c22;
        c12 += (t0 << 8) & LIMB_MASK;
        c13 += t0 >> 20;
        t0 = 132168845 * c22;
        c13 += (t0 << 8) & LIMB_MASK;
        c14 += t0 >> 20;
        t0 = 3710130 * c22;
        c14 += (t0 << 8) & LIMB_MASK;
        c15 += t0 >> 20;
        //reduce from position 21
        t0 = 54187661 * c21;
        c7 += (t0 << 8) & LIMB_MASK;
        c8 += t0 >> 20;
        t0 = 20867411 * c21;
        c8 += (t0 << 8) & LIMB_MASK;
        c9 += t0 >> 20;
        t0 = -10975981 * c21;
        c9 += (t0 << 8) & LIMB_MASK;
        c10 += t0 >> 20;
        t0 = -14361739 * c21;
        c10 += (t0 << 8) & LIMB_MASK;
        c11 += t0 >> 20;
        t0 = 35694566 * c21;
        c11 += (t0 << 8) & LIMB_MASK;
        c12 += t0 >> 20;
        t0 = 132168845 * c21;
        c12 += (t0 << 8) & LIMB_MASK;
        c13 += t0 >> 20;
        t0 = 3710130 * c21;
        c13 += (t0 << 8) & LIMB_MASK;
        c14 += t0 >> 20;
        //reduce from position 20
        t0 = 54187661 * c20;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
        t0 = 20867411 * c20;
        c7 += (t0 << 8) & LIMB_MASK;
        c8 += t0 >> 20;
        t0 = -10975981 * c20;
        c8 += (t0 << 8) & LIMB_MASK;
        c9 += t0 >> 20;
        t0 = -14361739 * c20;
        c9 += (t0 << 8) & LIMB_MASK;
        c10 += t0 >> 20;
        t0 = 35694566 * c20;
        c10 += (t0 << 8) & LIMB_MASK;
        c11 += t0 >> 20;
        t0 = 132168845 * c20;
        c11 += (t0 << 8) & LIMB_MASK;
        c12 += t0 >> 20;
        t0 = 3710130 * c20;
        c12 += (t0 << 8) & LIMB_MASK;
        c13 += t0 >> 20;
        //reduce from position 19
        t0 = 54187661 * c19;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = 20867411 * c19;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
        t0 = -10975981 * c19;
        c7 += (t0 << 8) & LIMB_MASK;
        c8 += t0 >> 20;
        t0 = -14361739 * c19;
        c8 += (t0 << 8) & LIMB_MASK;
        c9 += t0 >> 20;
        t0 = 35694566 * c19;
        c9 += (t0 << 8) & LIMB_MASK;
        c10 += t0 >> 20;
        t0 = 132168845 * c19;
        c10 += (t0 << 8) & LIMB_MASK;
        c11 += t0 >> 20;
        t0 = 3710130 * c19;
        c11 += (t0 << 8) & LIMB_MASK;
        c12 += t0 >> 20;
        //reduce from position 18
        t0 = 54187661 * c18;
        c4 += (t0 << 8) & LIMB_MASK;
        c5 += t0 >> 20;
        t0 = 20867411 * c18;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = -10975981 * c18;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
        t0 = -14361739 * c18;
        c7 += (t0 << 8) & LIMB_MASK;
        c8 += t0 >> 20;
        t0 = 35694566 * c18;
        c8 += (t0 << 8) & LIMB_MASK;
        c9 += t0 >> 20;
        t0 = 132168845 * c18;
        c9 += (t0 << 8) & LIMB_MASK;
        c10 += t0 >> 20;
        t0 = 3710130 * c18;
        c10 += (t0 << 8) & LIMB_MASK;
        c11 += t0 >> 20;
        //reduce from position 17
        t0 = 54187661 * c17;
        c3 += (t0 << 8) & LIMB_MASK;
        c4 += t0 >> 20;
        t0 = 20867411 * c17;
        c4 += (t0 << 8) & LIMB_MASK;
        c5 += t0 >> 20;
        t0 = -10975981 * c17;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = -14361739 * c17;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
        t0 = 35694566 * c17;
        c7 += (t0 << 8) & LIMB_MASK;
        c8 += t0 >> 20;
        t0 = 132168845 * c17;
        c8 += (t0 << 8) & LIMB_MASK;
        c9 += t0 >> 20;
        t0 = 3710130 * c17;
        c9 += (t0 << 8) & LIMB_MASK;
        c10 += t0 >> 20;
        //reduce from position 16
        t0 = 54187661 * c16;
        c2 += (t0 << 8) & LIMB_MASK;
        c3 += t0 >> 20;
        t0 = 20867411 * c16;
        c3 += (t0 << 8) & LIMB_MASK;
        c4 += t0 >> 20;
        t0 = -10975981 * c16;
        c4 += (t0 << 8) & LIMB_MASK;
        c5 += t0 >> 20;
        t0 = -14361739 * c16;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = 35694566 * c16;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
        t0 = 132168845 * c16;
        c7 += (t0 << 8) & LIMB_MASK;
        c8 += t0 >> 20;
        t0 = 3710130 * c16;
        c8 += (t0 << 8) & LIMB_MASK;
        c9 += t0 >> 20;
        //reduce from position 15
        t0 = 54187661 * c15;
        c1 += (t0 << 8) & LIMB_MASK;
        c2 += t0 >> 20;
        t0 = 20867411 * c15;
        c2 += (t0 << 8) & LIMB_MASK;
        c3 += t0 >> 20;
        t0 = -10975981 * c15;
        c3 += (t0 << 8) & LIMB_MASK;
        c4 += t0 >> 20;
        t0 = -14361739 * c15;
        c4 += (t0 << 8) & LIMB_MASK;
        c5 += t0 >> 20;
        t0 = 35694566 * c15;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = 132168845 * c15;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
        t0 = 3710130 * c15;
        c7 += (t0 << 8) & LIMB_MASK;
        c8 += t0 >> 20;
        //reduce from position 14
        t0 = 54187661 * c14;
        c0 += (t0 << 8) & LIMB_MASK;
        c1 += t0 >> 20;
        t0 = 20867411 * c14;
        c1 += (t0 << 8) & LIMB_MASK;
        c2 += t0 >> 20;
        t0 = -10975981 * c14;
        c2 += (t0 << 8) & LIMB_MASK;
        c3 += t0 >> 20;
        t0 = -14361739 * c14;
        c3 += (t0 << 8) & LIMB_MASK;
        c4 += t0 >> 20;
        t0 = 35694566 * c14;
        c4 += (t0 << 8) & LIMB_MASK;
        c5 += t0 >> 20;
        t0 = 132168845 * c14;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = 3710130 * c14;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
        c14 = 0;

        carryReduce1(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27);
    }
    void carryReduce1(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27) {
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

        carryReduce2(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27);
    }
    void carryReduce2(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19, long c20, long c21, long c22, long c23, long c24, long c25, long c26, long c27) {
        long t0;

        //reduce from position 14
        t0 = 54187661 * c14;
        c0 += (t0 << 8) & LIMB_MASK;
        c1 += t0 >> 20;
        t0 = 20867411 * c14;
        c1 += (t0 << 8) & LIMB_MASK;
        c2 += t0 >> 20;
        t0 = -10975981 * c14;
        c2 += (t0 << 8) & LIMB_MASK;
        c3 += t0 >> 20;
        t0 = -14361739 * c14;
        c3 += (t0 << 8) & LIMB_MASK;
        c4 += t0 >> 20;
        t0 = 35694566 * c14;
        c4 += (t0 << 8) & LIMB_MASK;
        c5 += t0 >> 20;
        t0 = 132168845 * c14;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = 3710130 * c14;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
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

        carryReduce0(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14);
    }
    void carryReduce0(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14) {
        long t0;

        //reduce from position 14
        t0 = 54187661 * c14;
        c0 += (t0 << 8) & LIMB_MASK;
        c1 += t0 >> 20;
        t0 = 20867411 * c14;
        c1 += (t0 << 8) & LIMB_MASK;
        c2 += t0 >> 20;
        t0 = -10975981 * c14;
        c2 += (t0 << 8) & LIMB_MASK;
        c3 += t0 >> 20;
        t0 = -14361739 * c14;
        c3 += (t0 << 8) & LIMB_MASK;
        c4 += t0 >> 20;
        t0 = 35694566 * c14;
        c4 += (t0 << 8) & LIMB_MASK;
        c5 += t0 >> 20;
        t0 = 132168845 * c14;
        c5 += (t0 << 8) & LIMB_MASK;
        c6 += t0 >> 20;
        t0 = 3710130 * c14;
        c6 += (t0 << 8) & LIMB_MASK;
        c7 += t0 >> 20;
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

