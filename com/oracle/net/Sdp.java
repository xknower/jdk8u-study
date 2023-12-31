package com.oracle.net;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.io.IOException;
import java.io.FileDescriptor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Constructor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

import sun.net.sdp.SdpSupport;

/**
 * This class consists exclusively of static methods that Sockets or Channels to
 * sockets that support the InfiniBand Sockets Direct Protocol (SDP).
 */

public final class Sdp {
    private Sdp() { }

    /**
     * The package-privage ServerSocket(SocketImpl) constructor
     */
    private static final Constructor<ServerSocket> serverSocketCtor;
    static {
        try {
            serverSocketCtor = (Constructor<ServerSocket>)
                ServerSocket.class.getDeclaredConstructor(SocketImpl.class);
            setAccessible(serverSocketCtor);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * The package-private SdpSocketImpl() constructor
     */
    private static final Constructor<SocketImpl> socketImplCtor;
    static {
        try {
            Class<?> cl = Class.forName("java.net.SdpSocketImpl", true, null);
            socketImplCtor = (Constructor<SocketImpl>)cl.getDeclaredConstructor();
            setAccessible(socketImplCtor);
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    private static void setAccessible(final AccessibleObject o) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                o.setAccessible(true);
                return null;
            }
        });
    }

    /**
     * SDP enabled Socket.
     */
    private static class SdpSocket extends Socket {
        SdpSocket(SocketImpl impl) throws SocketException {
            super(impl);
        }
    }

    /**
     * Creates a SDP enabled SocketImpl
     */
    private static SocketImpl createSocketImpl() {
        try {
            return socketImplCtor.newInstance();
        } catch (InstantiationException x) {
            throw new AssertionError(x);
        } catch (IllegalAccessException x) {
            throw new AssertionError(x);
        } catch (InvocationTargetException x) {
            throw new AssertionError(x);
        }
    }

    /**
     * Creates an unconnected and unbound SDP socket. The {@code Socket} is
     * associated with a {@link java.net.SocketImpl} of the system-default type.
     *
     * @return  a new Socket
     *
     * @throws  UnsupportedOperationException
     *          If SDP is not supported
     * @throws  IOException
     *          If an I/O error occurs
     */
    public static Socket openSocket() throws IOException {
        SocketImpl impl = createSocketImpl();
        return new SdpSocket(impl);
    }

    /**
     * Creates an unbound SDP server socket. The {@code ServerSocket} is
     * associated with a {@link java.net.SocketImpl} of the system-default type.
     *
     * @return  a new ServerSocket
     *
     * @throws  UnsupportedOperationException
     *          If SDP is not supported
     * @throws  IOException
     *          If an I/O error occurs
     */
    public static ServerSocket openServerSocket() throws IOException {
        // create ServerSocket via package-private constructor
        SocketImpl impl = createSocketImpl();
        try {
            return serverSocketCtor.newInstance(impl);
        } catch (IllegalAccessException x) {
            throw new AssertionError(x);
        } catch (InstantiationException x) {
            throw new AssertionError(x);
        } catch (InvocationTargetException x) {
            Throwable cause = x.getCause();
            if (cause instanceof IOException)
                throw (IOException)cause;
            if (cause instanceof RuntimeException)
                throw (RuntimeException)cause;
            throw new RuntimeException(x);
        }
    }

    /**
     * Opens a socket channel to a SDP socket.
     *
     * <p> The channel will be associated with the system-wide default
     * {@link java.nio.channels.spi.SelectorProvider SelectorProvider}.
     *
     * @return  a new SocketChannel
     *
     * @throws  UnsupportedOperationException
     *          If SDP is not supported or not supported by the default selector
     *          provider
     * @throws  IOException
     *          If an I/O error occurs.
     */
    public static SocketChannel openSocketChannel() throws IOException {
        FileDescriptor fd = SdpSupport.createSocket();
        return sun.nio.ch.Secrets.newSocketChannel(fd);
    }

    /**
     * Opens a socket channel to a SDP socket.
     *
     * <p> The channel will be associated with the system-wide default
     * {@link java.nio.channels.spi.SelectorProvider SelectorProvider}.
     *
     * @return  a new ServerSocketChannel
     *
     * @throws  UnsupportedOperationException
     *          If SDP is not supported or not supported by the default selector
     *          provider
     * @throws  IOException
     *          If an I/O error occurs
     */
    public static ServerSocketChannel openServerSocketChannel()
        throws IOException
    {
        FileDescriptor fd = SdpSupport.createSocket();
        return sun.nio.ch.Secrets.newServerSocketChannel(fd);
    }
}
