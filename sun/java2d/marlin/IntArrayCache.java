package sun.java2d.marlin;

import java.util.ArrayDeque;
import java.util.Arrays;
import static sun.java2d.marlin.MarlinUtils.logException;
import static sun.java2d.marlin.MarlinUtils.logInfo;

final class IntArrayCache implements MarlinConst {

    private final int arraySize;
    private final ArrayDeque<int[]> intArrays;
    // stats
    private int getOp = 0;
    private int createOp = 0;
    private int returnOp = 0;

    void dumpStats() {
        if (getOp > 0) {
            logInfo("IntArrayCache[" + arraySize + "]: get: " + getOp
                    + " created: " + createOp + " - returned: " + returnOp
                    + " :: cache size: " + intArrays.size());
        }
    }

    IntArrayCache(final int arraySize) {
        this.arraySize = arraySize;
        // small but enough: almost 1 cache line
        this.intArrays = new ArrayDeque<int[]>(6);
    }

    int[] getArray() {
        if (doStats) {
            getOp++;
        }

        // use cache:
        final int[] array = intArrays.pollLast();
        if (array != null) {
            return array;
        }

        if (doStats) {
            createOp++;
        }

        return new int[arraySize];
    }

    void putDirtyArray(final int[] array, final int length) {
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
            Arrays.fill(array, 0, array.length, 0);
        }

        // fill cache:
        intArrays.addLast(array);
    }

    void putArray(final int[] array, final int length,
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
        fill(array, fromIndex, toIndex, 0);

        // fill cache:
        intArrays.addLast(array);
    }

    static void fill(final int[] array, final int fromIndex,
                     final int toIndex, final int value)
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

    static void check(final int[] array, final int fromIndex,
                      final int toIndex, final int value)
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
