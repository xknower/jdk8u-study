package sun.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Implementation of the diagnostic MBean for Hotspot VM.
 */
public class HotSpotDiagnostic implements HotSpotDiagnosticMXBean {
    public HotSpotDiagnostic() {
    }

    @Override
    public void dumpHeap(String outputFile, boolean live) throws IOException {

        String propertyName = "jdk.management.heapdump.allowAnyFileSuffix";
        PrivilegedAction<Boolean> pa = () -> Boolean.parseBoolean(System.getProperty(propertyName, "false"));
        boolean allowAnyFileSuffix = AccessController.doPrivileged(pa);
        if (!allowAnyFileSuffix && !outputFile.endsWith(".hprof")) {
            throw new IllegalArgumentException("heapdump file must have .hprof extention");
        }

        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkWrite(outputFile);
            Util.checkControlAccess();
        }

        dumpHeap0(outputFile, live);
    }

    private native void dumpHeap0(String outputFile, boolean live) throws IOException;

    @Override
    public List<VMOption> getDiagnosticOptions() {
        List<Flag> allFlags = Flag.getAllFlags();
        List<VMOption> result = new ArrayList<>();
        for (Flag flag : allFlags) {
            if (flag.isWriteable() && flag.isExternal()) {
                result.add(flag.getVMOption());
            }
        }
        return result;
    }

    @Override
    public VMOption getVMOption(String name) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        Flag f = Flag.getFlag(name);
        if (f == null) {
            throw new IllegalArgumentException("VM option \"" +
                name + "\" does not exist");
        }
        return f.getVMOption();
    }

    @Override
    public void setVMOption(String name, String value) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }

        Util.checkControlAccess();
        Flag flag = Flag.getFlag(name);
        if (flag == null) {
            throw new IllegalArgumentException("VM option \"" +
                name + "\" does not exist");
        }
        if (!flag.isWriteable()){
            throw new IllegalArgumentException("VM Option \"" +
                name + "\" is not writeable");
        }

        // Check the type of the value
        Object v = flag.getValue();
        if (v instanceof Long) {
            try {
                long l = Long.parseLong(value);
                Flag.setLongValue(name, l);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value:" +
                        " VM Option \"" + name + "\"" +
                        " expects numeric value", e);
            }
        } else if (v instanceof Double) {
            try {
                double d = Double.parseDouble(value);
                Flag.setDoubleValue(name, d);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value:" +
                        " VM Option \"" + name + "\"" +
                        " expects numeric value", e);
            }
        } else if (v instanceof Boolean) {
            if (!value.equalsIgnoreCase("true") &&
                !value.equalsIgnoreCase("false")) {
                throw new IllegalArgumentException("Invalid value:" +
                    " VM Option \"" + name + "\"" +
                    " expects \"true\" or \"false\".");
            }
            Flag.setBooleanValue(name, Boolean.parseBoolean(value));
        } else if (v instanceof String) {
            Flag.setStringValue(name, value);
        } else {
            throw new IllegalArgumentException("VM Option \"" +
                name + "\" is of an unsupported type: " +
                v.getClass().getName());
        }
    }

    @Override
    public ObjectName getObjectName() {
        return Util.newObjectName("com.sun.management:type=HotSpotDiagnostic");
    }
}
