package sun.management;

import java.util.regex.*;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import sun.management.counter.*;

/**
 * Implementation class of HotspotCompilationMBean interface.
 *
 * Internal, uncommitted management interface for Hotspot compilation
 * system.
 *
 */
class HotspotCompilation
    implements HotspotCompilationMBean {

    private VMManagement jvm;

    /**
     * Constructor of HotspotRuntime class.
     */
    HotspotCompilation(VMManagement vm) {
        jvm = vm;
        initCompilerCounters();
    }

    // Performance counter support
    private static final String JAVA_CI    = "java.ci.";
    private static final String COM_SUN_CI = "com.sun.ci.";
    private static final String SUN_CI     = "sun.ci.";
    private static final String CI_COUNTER_NAME_PATTERN =
        JAVA_CI + "|" + COM_SUN_CI + "|" + SUN_CI;

    private LongCounter compilerThreads;
    private LongCounter totalCompiles;
    private LongCounter totalBailouts;
    private LongCounter totalInvalidates;
    private LongCounter nmethodCodeSize;
    private LongCounter nmethodSize;
    private StringCounter lastMethod;
    private LongCounter lastSize;
    private LongCounter lastType;
    private StringCounter lastFailedMethod;
    private LongCounter lastFailedType;
    private StringCounter lastInvalidatedMethod;
    private LongCounter lastInvalidatedType;

    private class CompilerThreadInfo {
        int index;
        String name;
        StringCounter method;
        LongCounter type;
        LongCounter compiles;
        LongCounter time;
        CompilerThreadInfo(String bname, int index) {
            String basename = bname + "." + index + ".";
            this.name = bname + "-" + index;
            this.method = (StringCounter) lookup(basename + "method");
            this.type = (LongCounter) lookup(basename + "type");
            this.compiles = (LongCounter) lookup(basename + "compiles");
            this.time = (LongCounter) lookup(basename + "time");
        }
        CompilerThreadInfo(String bname) {
            String basename = bname + ".";
            this.name = bname;
            this.method = (StringCounter) lookup(basename + "method");
            this.type = (LongCounter) lookup(basename + "type");
            this.compiles = (LongCounter) lookup(basename + "compiles");
            this.time = (LongCounter) lookup(basename + "time");
        }

        CompilerThreadStat getCompilerThreadStat() {
            MethodInfo minfo = new MethodInfo(method.stringValue(),
                                              (int) type.longValue(),
                                              -1);
            return new CompilerThreadStat(name,
                                          compiles.longValue(),
                                          time.longValue(),
                                          minfo);
        }
    }
    private CompilerThreadInfo[] threads;
    private int numActiveThreads; // number of active compiler threads

    private Map<String, Counter> counters;
    private Counter lookup(String name) {
        Counter c = null;

        // Only one counter exists with the specified name in the
        // current implementation.  We first look up in the SUN_CI namespace
        // since most counters are in SUN_CI namespace.

        if ((c = counters.get(SUN_CI + name)) != null) {
            return c;
        }
        if ((c = counters.get(COM_SUN_CI + name)) != null) {
            return c;
        }
        if ((c = counters.get(JAVA_CI + name)) != null) {
            return c;
        }

        // FIXME: should tolerate if counter doesn't exist
        throw new AssertionError("Counter " + name + " does not exist");
    }

    private void initCompilerCounters() {
        // Build a tree map of the current list of performance counters
        counters = new TreeMap<>();
        for (Counter c: getInternalCompilerCounters()) {
            counters.put(c.getName(), c);
        }

        compilerThreads = (LongCounter) lookup("threads");
        totalCompiles = (LongCounter) lookup("totalCompiles");
        totalBailouts = (LongCounter) lookup("totalBailouts");
        totalInvalidates = (LongCounter) lookup("totalInvalidates");
        nmethodCodeSize = (LongCounter) lookup("nmethodCodeSize");
        nmethodSize = (LongCounter) lookup("nmethodSize");
        lastMethod = (StringCounter) lookup("lastMethod");
        lastSize = (LongCounter) lookup("lastSize");
        lastType = (LongCounter) lookup("lastType");
        lastFailedMethod = (StringCounter) lookup("lastFailedMethod");
        lastFailedType = (LongCounter) lookup("lastFailedType");
        lastInvalidatedMethod = (StringCounter) lookup("lastInvalidatedMethod");
        lastInvalidatedType = (LongCounter) lookup("lastInvalidatedType");

        numActiveThreads = (int) compilerThreads.longValue();

        // Allocate CompilerThreadInfo for compilerThread and adaptorThread
        threads = new CompilerThreadInfo[numActiveThreads+1];

        // AdaptorThread has index 0
        if (counters.containsKey(SUN_CI + "adapterThread.compiles")) {
            threads[0] = new CompilerThreadInfo("adapterThread", 0);
            numActiveThreads++;
        } else {
            threads[0] = null;
        }

        for (int i = 1; i < threads.length; i++) {
            threads[i] = new CompilerThreadInfo("compilerThread", i-1);
        }
    }

    public int getCompilerThreadCount() {
        return numActiveThreads;
    }

    public long getTotalCompileCount() {
        return totalCompiles.longValue();
    }

    public long getBailoutCompileCount() {
        return totalBailouts.longValue();
    }

    public long getInvalidatedCompileCount() {
        return totalInvalidates.longValue();
    }

    public long getCompiledMethodCodeSize() {
        return nmethodCodeSize.longValue();
    }

    public long getCompiledMethodSize() {
        return nmethodSize.longValue();
    }

    public java.util.List<CompilerThreadStat> getCompilerThreadStats() {
        List<CompilerThreadStat> list = new ArrayList<>(threads.length);
        int i = 0;
        if (threads[0] == null) {
            // no adaptor thread
            i = 1;
        }
        for (; i < threads.length; i++) {
            list.add(threads[i].getCompilerThreadStat());
        }
        return list;
    }

    public MethodInfo getLastCompile() {
        return new MethodInfo(lastMethod.stringValue(),
                              (int) lastType.longValue(),
                              (int) lastSize.longValue());
    }

    public MethodInfo getFailedCompile() {
        return new MethodInfo(lastFailedMethod.stringValue(),
                              (int) lastFailedType.longValue(),
                              -1);
    }

    public MethodInfo getInvalidatedCompile() {
        return new MethodInfo(lastInvalidatedMethod.stringValue(),
                              (int) lastInvalidatedType.longValue(),
                              -1);
    }

    public java.util.List<Counter> getInternalCompilerCounters() {
        return jvm.getInternalCounters(CI_COUNTER_NAME_PATTERN);
    }
}
