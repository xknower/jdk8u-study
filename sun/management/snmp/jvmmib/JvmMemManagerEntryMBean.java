package sun.management.snmp.jvmmib;

//
// Generated by mibgen version 5.0 (06/02/03) when compiling JVM-MANAGEMENT-MIB in standard metadata mode.
//


// jmx imports
//
import com.sun.jmx.snmp.SnmpStatusException;

/**
 * This interface is used for representing the remote management interface for the "JvmMemManagerEntry" MBean.
 */
public interface JvmMemManagerEntryMBean {

    /**
     * Getter for the "JvmMemManagerState" variable.
     */
    public EnumJvmMemManagerState getJvmMemManagerState() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemManagerName" variable.
     */
    public String getJvmMemManagerName() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemManagerIndex" variable.
     */
    public Integer getJvmMemManagerIndex() throws SnmpStatusException;

}
