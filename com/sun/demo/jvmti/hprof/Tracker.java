package com.sun.demo.jvmti.hprof;

/* This class and it's methods are used by hprof when injecting bytecodes
 *   into class file images.
 *   See the directory src/share/demo/jvmti/hprof and the file README.txt
 *   for more details.
 */

public class Tracker {

    /* Master switch that activates calls to native functions. */

    private static int engaged = 0;

    /* To track memory allocated, we need to catch object init's and arrays. */

    /* At the beginning of java.jang.Object.<init>(), a call to
     *   Tracker.ObjectInit() is injected.
     */

    private static native void nativeObjectInit(Object thr, Object obj);

    public static void ObjectInit(Object obj)
    {
        if ( engaged != 0) {
            if (obj == null) {
                throw new IllegalArgumentException("Null object.");
            }
            nativeObjectInit(Thread.currentThread(), obj);
        }
    }

    /* Immediately following any of the newarray bytecodes, a call to
     *   Tracker.NewArray() is injected.
     */

    private static native void nativeNewArray(Object thr, Object obj);

    public static void NewArray(Object obj)
    {
        if ( engaged != 0) {
            if (obj == null) {
                throw new IllegalArgumentException("Null object.");
            }
            nativeNewArray(Thread.currentThread(), obj);
        }
    }

    /* For cpu time spent in methods, we need to inject for every method. */

    /* At the very beginning of every method, a call to
     *   Tracker.CallSite() is injected.
     */

    private static native void nativeCallSite(Object thr, int cnum, int mnum);

    public static void CallSite(int cnum, int mnum)
    {
        if ( engaged != 0 ) {
            if (cnum < 0) {
                throw new IllegalArgumentException("Negative class index");
            }

            if (mnum < 0) {
                throw new IllegalArgumentException("Negative method index");
            }

            nativeCallSite(Thread.currentThread(), cnum, mnum);
        }
    }

    /* Before any of the return bytecodes, a call to
     *   Tracker.ReturnSite() is injected.
     */

    private static native void nativeReturnSite(Object thr, int cnum, int mnum);

    public static void ReturnSite(int cnum, int mnum)
    {
        if ( engaged != 0 ) {
            if (cnum < 0) {
                throw new IllegalArgumentException("Negative class index");
            }

            if (mnum < 0) {
                throw new IllegalArgumentException("Negative method index");
            }

            nativeReturnSite(Thread.currentThread(), cnum, mnum);
        }
    }

}
