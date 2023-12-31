package jdk.jfr.internal;

import jdk.jfr.internal.SecuritySupport.SafePath;
import sun.misc.Unsafe;

/**
 * Options that control Flight Recorder.
 *
 * Can be set using JFR.configure
 *
 */
public final class Options {

    private final static JVM jvm = JVM.getJVM();
    private final static long WAIT_INTERVAL = 1000; // ms;

    private final static long MIN_MAX_CHUNKSIZE = 1024 * 1024;

    private static final long DEFAULT_GLOBAL_BUFFER_COUNT = 20;
    private static final long DEFAULT_GLOBAL_BUFFER_SIZE = 524288;
    private static final long DEFAULT_MEMORY_SIZE = DEFAULT_GLOBAL_BUFFER_COUNT * DEFAULT_GLOBAL_BUFFER_SIZE;
    private static long DEFAULT_THREAD_BUFFER_SIZE;
    private static final int DEFAULT_STACK_DEPTH = 64;
    private static final boolean DEFAULT_SAMPLE_THREADS = true;
    private static final long DEFAULT_MAX_CHUNK_SIZE = 12 * 1024 * 1024;
    private static final SafePath DEFAULT_DUMP_PATH = SecuritySupport.USER_HOME;

    private static long memorySize;
    private static long globalBufferSize;
    private static long globalBufferCount;
    private static long threadBufferSize;
    private static int stackDepth;
    private static boolean sampleThreads;
    private static long maxChunkSize;
    private static SafePath dumpPath;

    static {
        final long pageSize = Unsafe.getUnsafe().pageSize();
        DEFAULT_THREAD_BUFFER_SIZE = pageSize > 8 * 1024 ? pageSize : 8 * 1024;
        reset();
    }

    public static synchronized void setMaxChunkSize(long max) {
        if (max < MIN_MAX_CHUNKSIZE) {
            throw new IllegalArgumentException("Max chunk size must be at least " + MIN_MAX_CHUNKSIZE);
        }
        jvm.setFileNotification(max);
        maxChunkSize = max;
    }

    public static synchronized long getMaxChunkSize() {
        return maxChunkSize;
    }

    public static synchronized void setMemorySize(long memSize) {
        jvm.setMemorySize(memSize);
        memorySize = memSize;
    }

    public static synchronized long getMemorySize() {
        return memorySize;
    }

    public static synchronized void setThreadBufferSize(long threadBufSize) {
        jvm.setThreadBufferSize(threadBufSize);
        threadBufferSize = threadBufSize;
    }

    public static synchronized long getThreadBufferSize() {
        return threadBufferSize;
    }

    public static synchronized long getGlobalBufferSize() {
        return globalBufferSize;
    }

    public static synchronized void setGlobalBufferCount(long globalBufCount) {
        jvm.setGlobalBufferCount(globalBufCount);
        globalBufferCount = globalBufCount;
    }

    public static synchronized long getGlobalBufferCount() {
        return globalBufferCount;
    }

    public static synchronized void setGlobalBufferSize(long globalBufsize) {
        jvm.setGlobalBufferSize(globalBufsize);
        globalBufferSize = globalBufsize;
    }

    public static synchronized void setDumpPath(SafePath path) {
        dumpPath = path;
    }

    public static synchronized SafePath getDumpPath() {
        return dumpPath;
    }

    public static synchronized void setStackDepth(Integer stackTraceDepth) {
        jvm.setStackDepth(stackTraceDepth);
        stackDepth = stackTraceDepth;
    }

    public static synchronized int getStackDepth() {
        return stackDepth;
    }

    public static synchronized void setSampleThreads(Boolean sample) {
        jvm.setSampleThreads(sample);
        sampleThreads = sample;
    }

    public static synchronized boolean getSampleThreads() {
        return sampleThreads;
    }

    private static synchronized void reset() {
        setMaxChunkSize(DEFAULT_MAX_CHUNK_SIZE);
        setMemorySize(DEFAULT_MEMORY_SIZE);
        setGlobalBufferSize(DEFAULT_GLOBAL_BUFFER_SIZE);
        setGlobalBufferCount(DEFAULT_GLOBAL_BUFFER_COUNT);
        setDumpPath(DEFAULT_DUMP_PATH);
        setSampleThreads(DEFAULT_SAMPLE_THREADS);
        setStackDepth(DEFAULT_STACK_DEPTH);
        setThreadBufferSize(DEFAULT_THREAD_BUFFER_SIZE);
    }

    static synchronized long getWaitInterval() {
        return WAIT_INTERVAL;
    }

    static void ensureInitialized() {
        // trigger clinit which will setup JVM defaults.
    }


}
