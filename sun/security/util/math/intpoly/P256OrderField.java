package sun.security.util.math.intpoly;

import java.math.BigInteger;
public class P256OrderField extends IntegerPolynomial {
    private static final int BITS_PER_LIMB = 26;
    private static final int NUM_LIMBS = 10;
    private static final int MAX_ADDS = 1;
    public static final BigInteger MODULUS = evaluateModulus();
    private static final long CARRY_ADD = 1 << 25;
    private static final int LIMB_MASK = -1 >>> (64 - BITS_PER_LIMB);
    public P256OrderField() {

        super(BITS_PER_LIMB, NUM_LIMBS, MAX_ADDS, MODULUS);

    }
    private static BigInteger evaluateModulus() {
        BigInteger result = BigInteger.valueOf(2).pow(256);
        result = result.add(BigInteger.valueOf(6497617));
        result = result.subtract(BigInteger.valueOf(2).pow(26).multiply(BigInteger.valueOf(26038081)));
        result = result.add(BigInteger.valueOf(2).pow(52).multiply(BigInteger.valueOf(32001852)));
        result = result.subtract(BigInteger.valueOf(2).pow(78).multiply(BigInteger.valueOf(21586850)));
        result = result.subtract(BigInteger.valueOf(2).pow(104).multiply(BigInteger.valueOf(4397317)));
        result = result.add(BigInteger.valueOf(2).pow(182).multiply(BigInteger.valueOf(1024)));
        result = result.subtract(BigInteger.valueOf(2).pow(208).multiply(BigInteger.valueOf(65536)));
        return result;
    }
    @Override
    protected void finalCarryReduceLast(long[] limbs) {
        long c = limbs[9] >> 22;
        limbs[9] -= c << 22;
        long t0 = -6497617 * c;
        limbs[0] += t0;
        t0 = 26038081 * c;
        limbs[1] += t0;
        t0 = -32001852 * c;
        limbs[2] += t0;
        t0 = 21586850 * c;
        limbs[3] += t0;
        t0 = 4397317 * c;
        limbs[4] += t0;
        t0 = -1024 * c;
        limbs[7] += t0;
        t0 = 65536 * c;
        limbs[8] += t0;
    }
    private void carryReduce(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18) {
        long c19 = 0;
        //carry from position 0
        long t0 = (c0 + CARRY_ADD) >> 26;
        c0 -= (t0 << 26);
        c1 += t0;
        //carry from position 1
        t0 = (c1 + CARRY_ADD) >> 26;
        c1 -= (t0 << 26);
        c2 += t0;
        //carry from position 2
        t0 = (c2 + CARRY_ADD) >> 26;
        c2 -= (t0 << 26);
        c3 += t0;
        //carry from position 3
        t0 = (c3 + CARRY_ADD) >> 26;
        c3 -= (t0 << 26);
        c4 += t0;
        //carry from position 4
        t0 = (c4 + CARRY_ADD) >> 26;
        c4 -= (t0 << 26);
        c5 += t0;
        //carry from position 5
        t0 = (c5 + CARRY_ADD) >> 26;
        c5 -= (t0 << 26);
        c6 += t0;
        //carry from position 6
        t0 = (c6 + CARRY_ADD) >> 26;
        c6 -= (t0 << 26);
        c7 += t0;
        //carry from position 7
        t0 = (c7 + CARRY_ADD) >> 26;
        c7 -= (t0 << 26);
        c8 += t0;
        //carry from position 8
        t0 = (c8 + CARRY_ADD) >> 26;
        c8 -= (t0 << 26);
        c9 += t0;
        //carry from position 9
        t0 = (c9 + CARRY_ADD) >> 26;
        c9 -= (t0 << 26);
        c10 += t0;
        //carry from position 10
        t0 = (c10 + CARRY_ADD) >> 26;
        c10 -= (t0 << 26);
        c11 += t0;
        //carry from position 11
        t0 = (c11 + CARRY_ADD) >> 26;
        c11 -= (t0 << 26);
        c12 += t0;
        //carry from position 12
        t0 = (c12 + CARRY_ADD) >> 26;
        c12 -= (t0 << 26);
        c13 += t0;
        //carry from position 13
        t0 = (c13 + CARRY_ADD) >> 26;
        c13 -= (t0 << 26);
        c14 += t0;
        //carry from position 14
        t0 = (c14 + CARRY_ADD) >> 26;
        c14 -= (t0 << 26);
        c15 += t0;
        //carry from position 15
        t0 = (c15 + CARRY_ADD) >> 26;
        c15 -= (t0 << 26);
        c16 += t0;
        //carry from position 16
        t0 = (c16 + CARRY_ADD) >> 26;
        c16 -= (t0 << 26);
        c17 += t0;
        //carry from position 17
        t0 = (c17 + CARRY_ADD) >> 26;
        c17 -= (t0 << 26);
        c18 += t0;
        //carry from position 18
        t0 = (c18 + CARRY_ADD) >> 26;
        c18 -= (t0 << 26);
        c19 += t0;

        carryReduce0(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19);
    }
    void carryReduce0(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19) {
        long t0;

        //reduce from position 19
        t0 = -6497617 * c19;
        c9 += (t0 << 4) & LIMB_MASK;
        c10 += t0 >> 22;
        t0 = 26038081 * c19;
        c10 += (t0 << 4) & LIMB_MASK;
        c11 += t0 >> 22;
        t0 = -32001852 * c19;
        c11 += (t0 << 4) & LIMB_MASK;
        c12 += t0 >> 22;
        t0 = 21586850 * c19;
        c12 += (t0 << 4) & LIMB_MASK;
        c13 += t0 >> 22;
        t0 = 4397317 * c19;
        c13 += (t0 << 4) & LIMB_MASK;
        c14 += t0 >> 22;
        t0 = -1024 * c19;
        c16 += (t0 << 4) & LIMB_MASK;
        c17 += t0 >> 22;
        t0 = 65536 * c19;
        c17 += (t0 << 4) & LIMB_MASK;
        c18 += t0 >> 22;
        //reduce from position 18
        t0 = -6497617 * c18;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        t0 = 26038081 * c18;
        c9 += (t0 << 4) & LIMB_MASK;
        c10 += t0 >> 22;
        t0 = -32001852 * c18;
        c10 += (t0 << 4) & LIMB_MASK;
        c11 += t0 >> 22;
        t0 = 21586850 * c18;
        c11 += (t0 << 4) & LIMB_MASK;
        c12 += t0 >> 22;
        t0 = 4397317 * c18;
        c12 += (t0 << 4) & LIMB_MASK;
        c13 += t0 >> 22;
        t0 = -1024 * c18;
        c15 += (t0 << 4) & LIMB_MASK;
        c16 += t0 >> 22;
        t0 = 65536 * c18;
        c16 += (t0 << 4) & LIMB_MASK;
        c17 += t0 >> 22;
        //reduce from position 17
        t0 = -6497617 * c17;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = 26038081 * c17;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        t0 = -32001852 * c17;
        c9 += (t0 << 4) & LIMB_MASK;
        c10 += t0 >> 22;
        t0 = 21586850 * c17;
        c10 += (t0 << 4) & LIMB_MASK;
        c11 += t0 >> 22;
        t0 = 4397317 * c17;
        c11 += (t0 << 4) & LIMB_MASK;
        c12 += t0 >> 22;
        t0 = -1024 * c17;
        c14 += (t0 << 4) & LIMB_MASK;
        c15 += t0 >> 22;
        t0 = 65536 * c17;
        c15 += (t0 << 4) & LIMB_MASK;
        c16 += t0 >> 22;
        //reduce from position 16
        t0 = -6497617 * c16;
        c6 += (t0 << 4) & LIMB_MASK;
        c7 += t0 >> 22;
        t0 = 26038081 * c16;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = -32001852 * c16;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        t0 = 21586850 * c16;
        c9 += (t0 << 4) & LIMB_MASK;
        c10 += t0 >> 22;
        t0 = 4397317 * c16;
        c10 += (t0 << 4) & LIMB_MASK;
        c11 += t0 >> 22;
        t0 = -1024 * c16;
        c13 += (t0 << 4) & LIMB_MASK;
        c14 += t0 >> 22;
        t0 = 65536 * c16;
        c14 += (t0 << 4) & LIMB_MASK;
        c15 += t0 >> 22;
        //reduce from position 15
        t0 = -6497617 * c15;
        c5 += (t0 << 4) & LIMB_MASK;
        c6 += t0 >> 22;
        t0 = 26038081 * c15;
        c6 += (t0 << 4) & LIMB_MASK;
        c7 += t0 >> 22;
        t0 = -32001852 * c15;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = 21586850 * c15;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        t0 = 4397317 * c15;
        c9 += (t0 << 4) & LIMB_MASK;
        c10 += t0 >> 22;
        t0 = -1024 * c15;
        c12 += (t0 << 4) & LIMB_MASK;
        c13 += t0 >> 22;
        t0 = 65536 * c15;
        c13 += (t0 << 4) & LIMB_MASK;
        c14 += t0 >> 22;
        //reduce from position 14
        t0 = -6497617 * c14;
        c4 += (t0 << 4) & LIMB_MASK;
        c5 += t0 >> 22;
        t0 = 26038081 * c14;
        c5 += (t0 << 4) & LIMB_MASK;
        c6 += t0 >> 22;
        t0 = -32001852 * c14;
        c6 += (t0 << 4) & LIMB_MASK;
        c7 += t0 >> 22;
        t0 = 21586850 * c14;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = 4397317 * c14;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        t0 = -1024 * c14;
        c11 += (t0 << 4) & LIMB_MASK;
        c12 += t0 >> 22;
        t0 = 65536 * c14;
        c12 += (t0 << 4) & LIMB_MASK;
        c13 += t0 >> 22;
        //reduce from position 13
        t0 = -6497617 * c13;
        c3 += (t0 << 4) & LIMB_MASK;
        c4 += t0 >> 22;
        t0 = 26038081 * c13;
        c4 += (t0 << 4) & LIMB_MASK;
        c5 += t0 >> 22;
        t0 = -32001852 * c13;
        c5 += (t0 << 4) & LIMB_MASK;
        c6 += t0 >> 22;
        t0 = 21586850 * c13;
        c6 += (t0 << 4) & LIMB_MASK;
        c7 += t0 >> 22;
        t0 = 4397317 * c13;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = -1024 * c13;
        c10 += (t0 << 4) & LIMB_MASK;
        c11 += t0 >> 22;
        t0 = 65536 * c13;
        c11 += (t0 << 4) & LIMB_MASK;
        c12 += t0 >> 22;
        //reduce from position 12
        t0 = -6497617 * c12;
        c2 += (t0 << 4) & LIMB_MASK;
        c3 += t0 >> 22;
        t0 = 26038081 * c12;
        c3 += (t0 << 4) & LIMB_MASK;
        c4 += t0 >> 22;
        t0 = -32001852 * c12;
        c4 += (t0 << 4) & LIMB_MASK;
        c5 += t0 >> 22;
        t0 = 21586850 * c12;
        c5 += (t0 << 4) & LIMB_MASK;
        c6 += t0 >> 22;
        t0 = 4397317 * c12;
        c6 += (t0 << 4) & LIMB_MASK;
        c7 += t0 >> 22;
        t0 = -1024 * c12;
        c9 += (t0 << 4) & LIMB_MASK;
        c10 += t0 >> 22;
        t0 = 65536 * c12;
        c10 += (t0 << 4) & LIMB_MASK;
        c11 += t0 >> 22;
        //reduce from position 11
        t0 = -6497617 * c11;
        c1 += (t0 << 4) & LIMB_MASK;
        c2 += t0 >> 22;
        t0 = 26038081 * c11;
        c2 += (t0 << 4) & LIMB_MASK;
        c3 += t0 >> 22;
        t0 = -32001852 * c11;
        c3 += (t0 << 4) & LIMB_MASK;
        c4 += t0 >> 22;
        t0 = 21586850 * c11;
        c4 += (t0 << 4) & LIMB_MASK;
        c5 += t0 >> 22;
        t0 = 4397317 * c11;
        c5 += (t0 << 4) & LIMB_MASK;
        c6 += t0 >> 22;
        t0 = -1024 * c11;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        t0 = 65536 * c11;
        c9 += (t0 << 4) & LIMB_MASK;
        c10 += t0 >> 22;
        //reduce from position 10
        t0 = -6497617 * c10;
        c0 += (t0 << 4) & LIMB_MASK;
        c1 += t0 >> 22;
        t0 = 26038081 * c10;
        c1 += (t0 << 4) & LIMB_MASK;
        c2 += t0 >> 22;
        t0 = -32001852 * c10;
        c2 += (t0 << 4) & LIMB_MASK;
        c3 += t0 >> 22;
        t0 = 21586850 * c10;
        c3 += (t0 << 4) & LIMB_MASK;
        c4 += t0 >> 22;
        t0 = 4397317 * c10;
        c4 += (t0 << 4) & LIMB_MASK;
        c5 += t0 >> 22;
        t0 = -1024 * c10;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = 65536 * c10;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        c10 = 0;

        carryReduce1(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19);
    }
    void carryReduce1(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19) {
        long t0;

        //carry from position 0
        t0 = (c0 + CARRY_ADD) >> 26;
        c0 -= (t0 << 26);
        c1 += t0;
        //carry from position 1
        t0 = (c1 + CARRY_ADD) >> 26;
        c1 -= (t0 << 26);
        c2 += t0;
        //carry from position 2
        t0 = (c2 + CARRY_ADD) >> 26;
        c2 -= (t0 << 26);
        c3 += t0;
        //carry from position 3
        t0 = (c3 + CARRY_ADD) >> 26;
        c3 -= (t0 << 26);
        c4 += t0;
        //carry from position 4
        t0 = (c4 + CARRY_ADD) >> 26;
        c4 -= (t0 << 26);
        c5 += t0;
        //carry from position 5
        t0 = (c5 + CARRY_ADD) >> 26;
        c5 -= (t0 << 26);
        c6 += t0;
        //carry from position 6
        t0 = (c6 + CARRY_ADD) >> 26;
        c6 -= (t0 << 26);
        c7 += t0;
        //carry from position 7
        t0 = (c7 + CARRY_ADD) >> 26;
        c7 -= (t0 << 26);
        c8 += t0;
        //carry from position 8
        t0 = (c8 + CARRY_ADD) >> 26;
        c8 -= (t0 << 26);
        c9 += t0;
        //carry from position 9
        t0 = (c9 + CARRY_ADD) >> 26;
        c9 -= (t0 << 26);
        c10 += t0;

        carryReduce2(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19);
    }
    void carryReduce2(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10, long c11, long c12, long c13, long c14, long c15, long c16, long c17, long c18, long c19) {
        long t0;

        //reduce from position 10
        t0 = -6497617 * c10;
        c0 += (t0 << 4) & LIMB_MASK;
        c1 += t0 >> 22;
        t0 = 26038081 * c10;
        c1 += (t0 << 4) & LIMB_MASK;
        c2 += t0 >> 22;
        t0 = -32001852 * c10;
        c2 += (t0 << 4) & LIMB_MASK;
        c3 += t0 >> 22;
        t0 = 21586850 * c10;
        c3 += (t0 << 4) & LIMB_MASK;
        c4 += t0 >> 22;
        t0 = 4397317 * c10;
        c4 += (t0 << 4) & LIMB_MASK;
        c5 += t0 >> 22;
        t0 = -1024 * c10;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = 65536 * c10;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        //carry from position 0
        t0 = (c0 + CARRY_ADD) >> 26;
        c0 -= (t0 << 26);
        c1 += t0;
        //carry from position 1
        t0 = (c1 + CARRY_ADD) >> 26;
        c1 -= (t0 << 26);
        c2 += t0;
        //carry from position 2
        t0 = (c2 + CARRY_ADD) >> 26;
        c2 -= (t0 << 26);
        c3 += t0;
        //carry from position 3
        t0 = (c3 + CARRY_ADD) >> 26;
        c3 -= (t0 << 26);
        c4 += t0;
        //carry from position 4
        t0 = (c4 + CARRY_ADD) >> 26;
        c4 -= (t0 << 26);
        c5 += t0;
        //carry from position 5
        t0 = (c5 + CARRY_ADD) >> 26;
        c5 -= (t0 << 26);
        c6 += t0;
        //carry from position 6
        t0 = (c6 + CARRY_ADD) >> 26;
        c6 -= (t0 << 26);
        c7 += t0;
        //carry from position 7
        t0 = (c7 + CARRY_ADD) >> 26;
        c7 -= (t0 << 26);
        c8 += t0;
        //carry from position 8
        t0 = (c8 + CARRY_ADD) >> 26;
        c8 -= (t0 << 26);
        c9 += t0;

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
    }
    private void carryReduce(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9) {
        long c10 = 0;
        //carry from position 0
        long t0 = (c0 + CARRY_ADD) >> 26;
        c0 -= (t0 << 26);
        c1 += t0;
        //carry from position 1
        t0 = (c1 + CARRY_ADD) >> 26;
        c1 -= (t0 << 26);
        c2 += t0;
        //carry from position 2
        t0 = (c2 + CARRY_ADD) >> 26;
        c2 -= (t0 << 26);
        c3 += t0;
        //carry from position 3
        t0 = (c3 + CARRY_ADD) >> 26;
        c3 -= (t0 << 26);
        c4 += t0;
        //carry from position 4
        t0 = (c4 + CARRY_ADD) >> 26;
        c4 -= (t0 << 26);
        c5 += t0;
        //carry from position 5
        t0 = (c5 + CARRY_ADD) >> 26;
        c5 -= (t0 << 26);
        c6 += t0;
        //carry from position 6
        t0 = (c6 + CARRY_ADD) >> 26;
        c6 -= (t0 << 26);
        c7 += t0;
        //carry from position 7
        t0 = (c7 + CARRY_ADD) >> 26;
        c7 -= (t0 << 26);
        c8 += t0;
        //carry from position 8
        t0 = (c8 + CARRY_ADD) >> 26;
        c8 -= (t0 << 26);
        c9 += t0;
        //carry from position 9
        t0 = (c9 + CARRY_ADD) >> 26;
        c9 -= (t0 << 26);
        c10 += t0;

        carryReduce0(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
    }
    void carryReduce0(long[] r, long c0, long c1, long c2, long c3, long c4, long c5, long c6, long c7, long c8, long c9, long c10) {
        long t0;

        //reduce from position 10
        t0 = -6497617 * c10;
        c0 += (t0 << 4) & LIMB_MASK;
        c1 += t0 >> 22;
        t0 = 26038081 * c10;
        c1 += (t0 << 4) & LIMB_MASK;
        c2 += t0 >> 22;
        t0 = -32001852 * c10;
        c2 += (t0 << 4) & LIMB_MASK;
        c3 += t0 >> 22;
        t0 = 21586850 * c10;
        c3 += (t0 << 4) & LIMB_MASK;
        c4 += t0 >> 22;
        t0 = 4397317 * c10;
        c4 += (t0 << 4) & LIMB_MASK;
        c5 += t0 >> 22;
        t0 = -1024 * c10;
        c7 += (t0 << 4) & LIMB_MASK;
        c8 += t0 >> 22;
        t0 = 65536 * c10;
        c8 += (t0 << 4) & LIMB_MASK;
        c9 += t0 >> 22;
        //carry from position 0
        t0 = (c0 + CARRY_ADD) >> 26;
        c0 -= (t0 << 26);
        c1 += t0;
        //carry from position 1
        t0 = (c1 + CARRY_ADD) >> 26;
        c1 -= (t0 << 26);
        c2 += t0;
        //carry from position 2
        t0 = (c2 + CARRY_ADD) >> 26;
        c2 -= (t0 << 26);
        c3 += t0;
        //carry from position 3
        t0 = (c3 + CARRY_ADD) >> 26;
        c3 -= (t0 << 26);
        c4 += t0;
        //carry from position 4
        t0 = (c4 + CARRY_ADD) >> 26;
        c4 -= (t0 << 26);
        c5 += t0;
        //carry from position 5
        t0 = (c5 + CARRY_ADD) >> 26;
        c5 -= (t0 << 26);
        c6 += t0;
        //carry from position 6
        t0 = (c6 + CARRY_ADD) >> 26;
        c6 -= (t0 << 26);
        c7 += t0;
        //carry from position 7
        t0 = (c7 + CARRY_ADD) >> 26;
        c7 -= (t0 << 26);
        c8 += t0;
        //carry from position 8
        t0 = (c8 + CARRY_ADD) >> 26;
        c8 -= (t0 << 26);
        c9 += t0;

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
        long c10 = (a[1] * b[9]) + (a[2] * b[8]) + (a[3] * b[7]) + (a[4] * b[6]) + (a[5] * b[5]) + (a[6] * b[4]) + (a[7] * b[3]) + (a[8] * b[2]) + (a[9] * b[1]);
        long c11 = (a[2] * b[9]) + (a[3] * b[8]) + (a[4] * b[7]) + (a[5] * b[6]) + (a[6] * b[5]) + (a[7] * b[4]) + (a[8] * b[3]) + (a[9] * b[2]);
        long c12 = (a[3] * b[9]) + (a[4] * b[8]) + (a[5] * b[7]) + (a[6] * b[6]) + (a[7] * b[5]) + (a[8] * b[4]) + (a[9] * b[3]);
        long c13 = (a[4] * b[9]) + (a[5] * b[8]) + (a[6] * b[7]) + (a[7] * b[6]) + (a[8] * b[5]) + (a[9] * b[4]);
        long c14 = (a[5] * b[9]) + (a[6] * b[8]) + (a[7] * b[7]) + (a[8] * b[6]) + (a[9] * b[5]);
        long c15 = (a[6] * b[9]) + (a[7] * b[8]) + (a[8] * b[7]) + (a[9] * b[6]);
        long c16 = (a[7] * b[9]) + (a[8] * b[8]) + (a[9] * b[7]);
        long c17 = (a[8] * b[9]) + (a[9] * b[8]);
        long c18 = (a[9] * b[9]);

        carryReduce(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18);
    }
    @Override
    protected void reduce(long[] a) {
        carryReduce(a, a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9]);
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
        long c10 = 2 * ((a[1] * a[9]) + (a[2] * a[8]) + (a[3] * a[7]) + (a[4] * a[6])) + (a[5] * a[5]);
        long c11 = 2 * ((a[2] * a[9]) + (a[3] * a[8]) + (a[4] * a[7]) + (a[5] * a[6]));
        long c12 = 2 * ((a[3] * a[9]) + (a[4] * a[8]) + (a[5] * a[7])) + (a[6] * a[6]);
        long c13 = 2 * ((a[4] * a[9]) + (a[5] * a[8]) + (a[6] * a[7]));
        long c14 = 2 * ((a[5] * a[9]) + (a[6] * a[8])) + (a[7] * a[7]);
        long c15 = 2 * ((a[6] * a[9]) + (a[7] * a[8]));
        long c16 = 2 * ((a[7] * a[9])) + (a[8] * a[8]);
        long c17 = 2 * ((a[8] * a[9]));
        long c18 = (a[9] * a[9]);

        carryReduce(r, c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18);
    }
}

