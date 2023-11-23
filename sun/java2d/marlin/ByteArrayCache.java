package sun.java2d.marlin;

import java.util.ArrayDeque;
import java.util.Arrays;
import static sun.java2d.marlin.MarlinUtils.logException;
import static sun.java2d.marlin.MarlinUtils.logInfo;

final class ByteArrayCache implements MarlinConst {

    private final int arraySize;
    private final ArrayDeque<byte[]> byteArrays;
    // stats
    private int getOp = 0;
    private int createOp = 0;
    private int returnOp = 0;

    void dumpStats() {
        if (getOp > 0) {
            logInfo("ByteArrayCache[" + arraySize + "]: get: " + getOp
                    + " created: " + createOp + " - returned: " + returnOp
                    + " :: cache size: " + byteArrays.size());
        }
    }

    ByteArrayCache(final int arraySize) {
        this.arraySize = arraySize;
        // small but enough: almost 1 cache line
        this.byteArrays = new ArrayDeque<byte[]>(6);
    }

    byte[] getArray() {
        if (doStats) {
            getOp++;
        }

        // use cache:
        final byte[] array = byteArrays.pollLast();
        if (array != null) {
            return array;
        }

        if (doStats) {
            createOp++;
        }

        return new byte[arraySize];
    }

    void putDirtyArray(final byte[] array, final int length) {
        if (length != arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + length);
            }
            return;
        }
        if (doStats) {
            returnOp++;
        }

        // NO clean-up of array data = DIRTY ARRAY

        if (doCleanDirty) {
            // Force zero-fill dirty arrays:
            Arrays.fill(array, 0, array.length, BYTE_0);
        }

        // fill cache:
        byteArrays.addLast(array);
    }

    void putArray(final byte[] array, final int length,
                  final int fromIndex, final int toIndex)
    {
        if (length != arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + length);
            }
            return;
        }
        if (doStats) {
            returnOp++;
        }

        // clean-up array of dirty part[fromIndex; toIndex[
        fill(array, fromIndex, toIndex, BYTE_0);

        // fill cache:
        byteArrays.addLast(array);
    }

    static void fill(final byte[] array, final int fromIndex,
                     final int toIndex, final byte value)
    {
        // clear array data:
        /*
         * Arrays.fill is faster than System.arraycopy(empty array)
         * or Unsafe.setMemory(byte 0)
         */
        if (toIndex != 0) {
            Arrays.fill(array, fromIndex, toIndex, value);
        }

        if (doChecks) {
            check(array, fromIndex, toIndex, value);
        }
    }

    static void check(final byte[] array, final int fromIndex,
                      final int toIndex, final byte value)
    {
        if (doChecks) {
            // check zero on full array:
            for (int i = 0; i < array.length; i++) {
                if (array[i] != value) {
                    logException("Invalid value at: " + i + " = " + array[i]
                            + " from: " + fromIndex + " to: " + toIndex + "\n"
                            + Arrays.toString(array), new Throwable());

                    // ensure array is correctly filled:
                    Arrays.fill(array, value);

                    return;
                }
            }
        }
    }
}
