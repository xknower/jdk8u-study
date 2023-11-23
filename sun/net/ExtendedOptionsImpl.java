package sun.net;

import java.net.*;
import jdk.net.*;
import java.io.IOException;
import java.io.FileDescriptor;
import java.security.PrivilegedAction;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;

/**
 * Contains the native implementation for extended socket options
 * together with some other static utilities
 */
public class ExtendedOptionsImpl {

    static {
        AccessController.doPrivileged((PrivilegedAction<Void>)() -> {
            System.loadLibrary("net");
            return null;
        });
        init();
    }

    private ExtendedOptionsImpl() {}

    public static void checkSetOptionPermission(SocketOption<?> option) {
        SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            return;
        }
        String check = "setOption." + option.name();
        sm.checkPermission(new NetworkPermission(check));
    }

    public static void checkGetOptionPermission(SocketOption<?> option) {
        SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            return;
        }
        String check = "getOption." + option.name();
        sm.checkPermission(new NetworkPermission(check));
    }

    public static void checkValueType(Object value, Class<?> type) {
        if (!type.isAssignableFrom(value.getClass())) {
            String s = "Found: " + value.getClass().toString() + " Expected: "
                        + type.toString();
            throw new IllegalArgumentException(s);
        }
    }

    private static native void init();

    /*
     * Extension native implementations
     *
     * SO_FLOW_SLA
     */
    public static native void setFlowOption(FileDescriptor fd, SocketFlow f);
    public static native void getFlowOption(FileDescriptor fd, SocketFlow f);
    public static native boolean flowSupported();

    public static native void setTcpKeepAliveProbes(FileDescriptor fd, int value) throws SocketException;
    public static native void setTcpKeepAliveTime(FileDescriptor fd, int value) throws SocketException;
    public static native void setTcpKeepAliveIntvl(FileDescriptor fd, int value) throws SocketException;
    public static native int getTcpKeepAliveProbes(FileDescriptor fd) throws SocketException;
    public static native int getTcpKeepAliveTime(FileDescriptor fd) throws SocketException;
    public static native int getTcpKeepAliveIntvl(FileDescriptor fd) throws SocketException;
    public static native boolean keepAliveOptionsSupported();
}
