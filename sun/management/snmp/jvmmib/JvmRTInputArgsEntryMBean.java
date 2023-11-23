package sun.management.snmp.jvmmib;

//
// Generated by mibgen version 5.0 (06/02/03) when compiling JVM-MANAGEMENT-MIB in standard metadata mode.
//


// jmx imports
//
import com.sun.jmx.snmp.SnmpStatusException;

/**
 * This interface is used for representing the remote management interface for the "JvmRTInputArgsEntry" MBean.
 */
public interface JvmRTInputArgsEntryMBean {

    /**
     * Getter for the "JvmRTInputArgsItem" variable.
     */
    public String getJvmRTInputArgsItem() throws SnmpStatusException;

    /**
     * Getter for the "JvmRTInputArgsIndex" variable.
     */
    public Integer getJvmRTInputArgsIndex() throws SnmpStatusException;

}
