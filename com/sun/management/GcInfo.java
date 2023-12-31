package com.sun.management;

import java.lang.management.MemoryUsage;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataView;
import javax.management.openmbean.CompositeType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import sun.management.GcInfoCompositeData;
import sun.management.GcInfoBuilder;

/**
 * Garbage collection information.  It contains the following
 * information for one garbage collection as well as GC-specific
 * attributes:
 * <blockquote>
 * <ul>
 *   <li>Start time</li>
 *   <li>End time</li>
 *   <li>Duration</li>
 *   <li>Memory usage before the collection starts</li>
 *   <li>Memory usage after the collection ends</li>
 * </ul>
 * </blockquote>
 *
 * <p>
 * <tt>GcInfo</tt> is a {@link CompositeData CompositeData}
 * The GC-specific attributes can be obtained via the CompositeData
 * interface.  This is a historical relic, and other classes should
 * not copy this pattern.  Use {@link CompositeDataView} instead.
 *
 * <h4>MXBean Mapping</h4>
 * <tt>GcInfo</tt> is mapped to a {@link CompositeData CompositeData}
 * with attributes as specified in the {@link #from from} method.
 *
 * @author  Mandy Chung
 * @since   1.5
 */
@jdk.Exported
public class GcInfo implements CompositeData, CompositeDataView {
    private final long index;
    private final long startTime;
    private final long endTime;
    private final Map<String, MemoryUsage> usageBeforeGc;
    private final Map<String, MemoryUsage> usageAfterGc;
    private final Object[] extAttributes;
    private final CompositeData cdata;
    private final GcInfoBuilder builder;

    private GcInfo(GcInfoBuilder builder,
                   long index, long startTime, long endTime,
                   MemoryUsage[] muBeforeGc,
                   MemoryUsage[] muAfterGc,
                   Object[] extAttributes) {
        this.builder       = builder;
        this.index         = index;
        this.startTime     = startTime;
        this.endTime       = endTime;
        String[] poolNames = builder.getPoolNames();
        this.usageBeforeGc = new HashMap<String, MemoryUsage>(poolNames.length);
        this.usageAfterGc = new HashMap<String, MemoryUsage>(poolNames.length);
        for (int i = 0; i < poolNames.length; i++) {
            this.usageBeforeGc.put(poolNames[i],  muBeforeGc[i]);
            this.usageAfterGc.put(poolNames[i],  muAfterGc[i]);
        }
        this.extAttributes = extAttributes;
        this.cdata = new GcInfoCompositeData(this, builder, extAttributes);
    }

    private GcInfo(CompositeData cd) {
        GcInfoCompositeData.validateCompositeData(cd);

        this.index         = GcInfoCompositeData.getId(cd);
        this.startTime     = GcInfoCompositeData.getStartTime(cd);
        this.endTime       = GcInfoCompositeData.getEndTime(cd);
        this.usageBeforeGc = GcInfoCompositeData.getMemoryUsageBeforeGc(cd);
        this.usageAfterGc  = GcInfoCompositeData.getMemoryUsageAfterGc(cd);
        this.extAttributes = null;
        this.builder       = null;
        this.cdata         = cd;
    }

    /**
     * Returns the identifier of this garbage collection which is
     * the number of collections that this collector has done.
     *
     * @return the identifier of this garbage collection which is
     * the number of collections that this collector has done.
     */
    public long getId() {
        return index;
    }

    /**
     * Returns the start time of this GC in milliseconds
     * since the Java virtual machine was started.
     *
     * @return the start time of this GC.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of this GC in milliseconds
     * since the Java virtual machine was started.
     *
     * @return the end time of this GC.
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Returns the elapsed time of this GC in milliseconds.
     *
     * @return the elapsed time of this GC in milliseconds.
     */
    public long getDuration() {
        return endTime - startTime;
    }

    /**
     * Returns the memory usage of all memory pools
     * at the beginning of this GC.
     * This method returns
     * a <tt>Map</tt> of the name of a memory pool
     * to the memory usage of the corresponding
     * memory pool before GC starts.
     *
     * @return a <tt>Map</tt> of memory pool names to the memory
     * usage of a memory pool before GC starts.
     */
    public Map<String, MemoryUsage> getMemoryUsageBeforeGc() {
        return Collections.unmodifiableMap(usageBeforeGc);
    }

    /**
     * Returns the memory usage of all memory pools
     * at the end of this GC.
     * This method returns
     * a <tt>Map</tt> of the name of a memory pool
     * to the memory usage of the corresponding
     * memory pool when GC finishes.
     *
     * @return a <tt>Map</tt> of memory pool names to the memory
     * usage of a memory pool when GC finishes.
     */
    public Map<String, MemoryUsage> getMemoryUsageAfterGc() {
        return Collections.unmodifiableMap(usageAfterGc);
    }

   /**
     * Returns a <tt>GcInfo</tt> object represented by the
     * given <tt>CompositeData</tt>. The given
     * <tt>CompositeData</tt> must contain
     * all the following attributes:
     *
     * <p>
     * <blockquote>
     * <table border>
     * <tr>
     *   <th align=left>Attribute Name</th>
     *   <th align=left>Type</th>
     * </tr>
     * <tr>
     *   <td>index</td>
     *   <td><tt>java.lang.Long</tt></td>
     * </tr>
     * <tr>
     *   <td>startTime</td>
     *   <td><tt>java.lang.Long</tt></td>
     * </tr>
     * <tr>
     *   <td>endTime</td>
     *   <td><tt>java.lang.Long</tt></td>
     * </tr>
     * <tr>
     *   <td>memoryUsageBeforeGc</td>
     *   <td><tt>javax.management.openmbean.TabularData</tt></td>
     * </tr>
     * <tr>
     *   <td>memoryUsageAfterGc</td>
     *   <td><tt>javax.management.openmbean.TabularData</tt></td>
     * </tr>
     * </table>
     * </blockquote>
     *
     * @throws IllegalArgumentException if <tt>cd</tt> does not
     *   represent a <tt>GcInfo</tt> object with the attributes
     *   described above.
     *
     * @return a <tt>GcInfo</tt> object represented by <tt>cd</tt>
     * if <tt>cd</tt> is not <tt>null</tt>; <tt>null</tt> otherwise.
     */
    public static GcInfo from(CompositeData cd) {
        if (cd == null) {
            return null;
        }

        if (cd instanceof GcInfoCompositeData) {
            return ((GcInfoCompositeData) cd).getGcInfo();
        } else {
            return new GcInfo(cd);
        }

    }

    // Implementation of the CompositeData interface
    public boolean containsKey(String key) {
        return cdata.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return cdata.containsValue(value);
    }

    public boolean equals(Object obj) {
        return cdata.equals(obj);
    }

    public Object get(String key) {
        return cdata.get(key);
    }

    public Object[] getAll(String[] keys) {
        return cdata.getAll(keys);
    }

    public CompositeType getCompositeType() {
        return cdata.getCompositeType();
    }

    public int hashCode() {
        return cdata.hashCode();
    }

    public String toString() {
        return cdata.toString();
    }

    public Collection values() {
        return cdata.values();
    }

    /**
     * <p>Return the {@code CompositeData} representation of this
     * {@code GcInfo}, including any GC-specific attributes.  The
     * returned value will have at least all the attributes described
     * in the {@link #from(CompositeData) from} method, plus optionally
     * other attributes.
     *
     * @param ct the {@code CompositeType} that the caller expects.
     * This parameter is ignored and can be null.
     *
     * @return the {@code CompositeData} representation.
     */
    public CompositeData toCompositeData(CompositeType ct) {
        return cdata;
    }
}
