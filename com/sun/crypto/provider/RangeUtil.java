package com.sun.crypto.provider;

import java.util.List;
import java.util.function.BiFunction;
import java.security.*;

/**
 * This class holds the various utility methods for range checks.
 */

final class RangeUtil {

    private static final BiFunction<String, List<Integer>,
            ArrayIndexOutOfBoundsException> AIOOBE_SUPPLIER =
            Preconditions.outOfBoundsExceptionFormatter
            (ArrayIndexOutOfBoundsException::new);

    public static void blockSizeCheck(int len, int blockSize) {
        if ((len % blockSize) != 0) {
            throw new ProviderException("Internal error in input buffering");
        }
    }

    public static void nullAndBoundsCheck(byte[] array, int offset, int len) {
        // NPE is thrown when array is null
        Preconditions.checkFromIndexSize(offset, len, array.length, AIOOBE_SUPPLIER);
    }
}
