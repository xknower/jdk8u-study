package com.sun.management;

/**
 * Platform-specific management interface for the operating system
 * on which the Java virtual machine is running.
 *
 * <p>
 * This interface provides information about the operating environment
 * on which the Java virtual machine is running. That might be a native
 * operating system, a virtualized operating system environment, or a
 * container-managed environment.
 *
 * <p>
 * The <tt>OperatingSystemMXBean</tt> object returned by
 * {@link java.lang.management.ManagementFactory#getOperatingSystemMXBean()}
 * is an instance of the implementation class of this interface
 * or {@link UnixOperatingSystemMXBean} interface depending on
 * its underlying operating system.
 *
 * @author  Mandy Chung
 * @since   1.5
 */
@jdk.Exported
public interface OperatingSystemMXBean extends
    java.lang.management.OperatingSystemMXBean {

    /**
     * Returns the amount of virtual memory that is guaranteed to
     * be available to the running process in bytes,
     * or <tt>-1</tt> if this operation is not supported.
     *
     * @return the amount of virtual memory that is guaranteed to
     * be available to the running process in bytes,
     * or <tt>-1</tt> if this operation is not supported.
     */
    public long getCommittedVirtualMemorySize();

    /**
     * Returns the total amount of swap space in bytes.
     *
     * @return the total amount of swap space in bytes.
     */
    public long getTotalSwapSpaceSize();

    /**
     * Returns the amount of free swap space in bytes.
     *
     * @return the amount of free swap space in bytes.
     */
    public long getFreeSwapSpaceSize();

    /**
     * Returns the CPU time used by the process on which the Java
     * virtual machine is running in nanoseconds.  The returned value
     * is of nanoseconds precision but not necessarily nanoseconds
     * accuracy.  This method returns <tt>-1</tt> if the
     * the platform does not support this operation.
     *
     * @return the CPU time used by the process in nanoseconds,
     * or <tt>-1</tt> if this operation is not supported.
     */
    public long getProcessCpuTime();

    /**
     * Returns the amount of free physical memory in bytes.
     *
     * @return the amount of free physical memory in bytes.
     */
    public long getFreePhysicalMemorySize();

    /**
     * Returns the total amount of physical memory in bytes.
     *
     * @return the total amount of physical memory in  bytes.
     */
    public long getTotalPhysicalMemorySize();

    /**
     * Returns the "recent cpu usage" for the whole system. This value is a
     * double in the [0.0,1.0] interval. A value of 0.0 means that all CPUs
     * were idle during the recent period of time observed, while a value
     * of 1.0 means that all CPUs were actively running 100% of the time
     * during the recent period being observed. All values betweens 0.0 and
     * 1.0 are possible depending of the activities going on in the system.
     * If the system recent cpu usage is not available, the method returns a
     * negative value.
     *
     * @return the "recent cpu usage" for the whole system; a negative
     * value if not available.
     * @since   1.7
     */
    public double getSystemCpuLoad();

    /**
     * Returns the "recent cpu usage" for the Java Virtual Machine process.
     * This value is a double in the [0.0,1.0] interval. A value of 0.0 means
     * that none of the CPUs were running threads from the JVM process during
     * the recent period of time observed, while a value of 1.0 means that all
     * CPUs were actively running threads from the JVM 100% of the time
     * during the recent period being observed. Threads from the JVM include
     * the application threads as well as the JVM internal threads. All values
     * betweens 0.0 and 1.0 are possible depending of the activities going on
     * in the JVM process and the whole system. If the Java Virtual Machine
     * recent CPU usage is not available, the method returns a negative value.
     *
     * @return the "recent cpu usage" for the Java Virtual Machine process;
     * a negative value if not available.
     * @since   1.7
     */
    public double getProcessCpuLoad();

}
