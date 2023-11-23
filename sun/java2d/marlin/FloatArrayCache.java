package sun.java2d.marlin;

import java.util.ArrayDeque;
import java.util.Arrays;
import static sun.java2d.marlin.MarlinUtils.logException;
import static sun.java2d.marlin.MarlinUtils.logInfo;

final class FloatArrayCache implements MarlinConst {

    private final int arraySize;
    private final ArrayDeque<float[]> floatArrays;
    // stats
    private int getOp = 0;
    private int createOp = 0;
    private int returnOp = 0;

    void dumpStats() {
        if (getOp > 0) {
            logInfo("FloatArrayCache[" + arraySize + "]: get: " + getOp
                    + " created: " + createOp + " - returned: " + returnOp
                    + " :: cache size: " + floatArrays.size());
        }
    }

    FloatArrayCache(final int arraySize) {
        this.arraySize = arraySize;
        // small but enough: almost 1 cache line
        this.floatArrays = new ArrayDeque<float[]>(6);
    }

    float[] getArray() {
        if (doStats) {
            getOp++;
        }

        // use cache
        final float[] array = floatArrays.pollLast();

        if (array != null) {
            return array;
        }

        if (doStats) {
            createOp++;
        }

        return new float[arraySize];
    }

    void putDirtyArray(final float[] array, final int length) {
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
            Arrays.fill(array, 0, array.length, 0f);
        }

        // fill cache:
        floatArrays.addLast(array);
    }

    void putArray(final float[] array, final int length,
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
        fill(array, fromIndex, toIndex, 0f);

        // fill cache:
        floatArrays.addLast(array);
    }

    static void fill(final float[] array, final int fromIndex,
                     final int toIndex, final float value)
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

    static void check(final float[] array, final int fromIndex,
                      final int toIndex, final float value)
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
