package sun.net.sdp;

import java.io.IOException;
import java.io.FileDescriptor;
import java.security.AccessController;

import sun.misc.SharedSecrets;
import sun.misc.JavaIOFileDescriptorAccess;


/**
 * This class defines methods for creating SDP sockets or "converting" existing
 * file descriptors, referencing (unbound) TCP sockets, to SDP.
 */

public final class SdpSupport {
    private static final String os = AccessController
        .doPrivileged(new sun.security.action.GetPropertyAction("os.name"));
    private static final boolean isSupported = (os.equals("SunOS") || (os.equals("Linux")));
    private static final JavaIOFileDescriptorAccess fdAccess =
        SharedSecrets.getJavaIOFileDescriptorAccess();

    private SdpSupport() { }

    /**
     * Creates a SDP socket, returning file descriptor referencing the socket.
     */
    public static FileDescriptor createSocket() throws IOException {
        if (!isSupported)
            throw new UnsupportedOperationException("SDP not supported on this platform");
        int fdVal = create0();
        FileDescriptor fd = new FileDescriptor();
        fdAccess.set(fd, fdVal);
        return fd;
    }

    /**
     * Converts an existing file descriptor, that references an unbound TCP socket,
     * to SDP.
     */
    public static void convertSocket(FileDescriptor fd) throws IOException {
        if (!isSupported)
            throw new UnsupportedOperationException("SDP not supported on this platform");
        int fdVal = fdAccess.get(fd);
        convert0(fdVal);
    }

    private static native int create0() throws IOException;

    private static native void convert0(int fd) throws IOException;

    static {
        AccessController.doPrivileged(
            new java.security.PrivilegedAction<Void>() {
                public Void run() {
                    System.loadLibrary("net");
                    return null;
                }
            });
    }
}
