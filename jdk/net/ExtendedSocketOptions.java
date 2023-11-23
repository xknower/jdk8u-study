package jdk.net;

import java.net.SocketOption;

/**
 * Defines extended socket options, beyond those defined in
 * {@link java.net.StandardSocketOptions}. These options may be platform
 * specific.
 */
@jdk.Exported
public final class ExtendedSocketOptions {

    private static class ExtSocketOption<T> implements SocketOption<T> {
        private final String name;
        private final Class<T> type;
        ExtSocketOption(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }
        @Override public String name() { return name; }
        @Override public Class<T> type() { return type; }
        @Override public String toString() { return name; }
    }

    private ExtendedSocketOptions() {}

    /**
     * Service level properties. When a security manager is installed,
     * setting or getting this option requires a {@link NetworkPermission}
     * {@code ("setOption.SO_FLOW_SLA")} or {@code "getOption.SO_FLOW_SLA"}
     * respectively.
     */
    public static final SocketOption<SocketFlow> SO_FLOW_SLA = new
        ExtSocketOption<SocketFlow>("SO_FLOW_SLA", SocketFlow.class);

    /**
     * Keep-Alive idle time.
     *
     * <p>
     * The value of this socket option is an {@code Integer} that is the number
     * of seconds of idle time before keep-alive initiates a probe. The socket
     * option is specific to stream-oriented sockets using the TCP/IP protocol.
     * The exact semantics of this socket option are system dependent.
     *
     * <p>
     * When the {@link java.net.StandardSocketOptions#SO_KEEPALIVE
     * SO_KEEPALIVE} option is enabled, TCP probes a connection that has been
     * idle for some amount of time. The default value for this idle period is
     * system dependent, but is typically 2 hours. The {@code TCP_KEEPIDLE}
     * option can be used to affect this value for a given socket.
     */
    public static final SocketOption<Integer> TCP_KEEPIDLE
            = new ExtSocketOption<Integer>("TCP_KEEPIDLE", Integer.class);

    /**
     * Keep-Alive retransmission interval time.
     *
     * <p>
     * The value of this socket option is an {@code Integer} that is the number
     * of seconds to wait before retransmitting a keep-alive probe. The socket
     * option is specific to stream-oriented sockets using the TCP/IP protocol.
     * The exact semantics of this socket option are system dependent.
     *
     * <p>
     * When the {@link java.net.StandardSocketOptions#SO_KEEPALIVE
     * SO_KEEPALIVE} option is enabled, TCP probes a connection that has been
     * idle for some amount of time. If the remote system does not respond to a
     * keep-alive probe, TCP retransmits the probe after some amount of time.
     * The default value for this retransmission interval is system dependent,
     * but is typically 75 seconds. The {@code TCP_KEEPINTERVAL} option can be
     * used to affect this value for a given socket.
     */
    public static final SocketOption<Integer> TCP_KEEPINTERVAL
            = new ExtSocketOption<Integer>("TCP_KEEPINTERVAL", Integer.class);

    /**
     * Keep-Alive retransmission maximum limit.
     *
     * <p>
     * The value of this socket option is an {@code Integer} that is the maximum
     * number of keep-alive probes to be sent. The socket option is specific to
     * stream-oriented sockets using the TCP/IP protocol. The exact semantics of
     * this socket option are system dependent.
     *
     * <p>
     * When the {@link java.net.StandardSocketOptions#SO_KEEPALIVE
     * SO_KEEPALIVE} option is enabled, TCP probes a connection that has been
     * idle for some amount of time. If the remote system does not respond to a
     * keep-alive probe, TCP retransmits the probe a certain number of times
     * before a connection is considered to be broken. The default value for
     * this keep-alive probe retransmit limit is system dependent, but is
     * typically 8. The {@code TCP_KEEPCOUNT} option can be used to affect this
     * value for a given socket.
     */
    public static final SocketOption<Integer> TCP_KEEPCOUNT
            = new ExtSocketOption<Integer>("TCP_KEEPCOUNT", Integer.class);
}
