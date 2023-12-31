package sun.management.jdp;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnsupportedAddressTypeException;

/**
 * JdpBroadcaster is responsible for sending pre-built JDP packet across a Net
 *
 * <p> Multicast group address, port number and ttl have to be chosen on upper
 * level and passed to broadcaster constructor. Also it's possible to specify
 * source address to broadcast from. </p>
 *
 * <p>JdpBradcaster doesn't perform any validation on a supplied {@code port} and {@code ttl} because
 * the allowed values depend on an operating system setup</p>
 *
 */
public final class JdpBroadcaster {

    private final InetAddress addr;
    private final int port;
    private final DatagramChannel channel;

    /**
     * Create a new broadcaster
     *
     * @param address - multicast group address
     * @param srcAddress - address of interface we should use to broadcast.
     * @param port - udp port to use
     * @param ttl - packet ttl
     * @throws IOException
     */
    public JdpBroadcaster(InetAddress address, InetAddress srcAddress, int port, int ttl)
            throws IOException, JdpException {
        this.addr = address;
        this.port = port;

        ProtocolFamily family = (address instanceof Inet6Address)
                ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET;

        channel = DatagramChannel.open(family);
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, ttl);

        // with srcAddress equal to null, this constructor do exactly the same as
        // if srcAddress is not passed
        if (srcAddress != null) {
            // User requests particular interface to bind to
            NetworkInterface interf = NetworkInterface.getByInetAddress(srcAddress);
            try {
                channel.bind(new InetSocketAddress(srcAddress, 0));
            } catch (UnsupportedAddressTypeException ex) {
                throw new JdpException("Unable to bind to source address");
            }
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, interf);
        }
    }

    /**
     * Create a new broadcaster
     *
     * @param address - multicast group address
     * @param port - udp port to use
     * @param ttl - packet ttl
     * @throws IOException
     */
    public JdpBroadcaster(InetAddress address, int port, int ttl)
            throws IOException, JdpException {
        this(address, null, port, ttl);
    }

    /**
     * Broadcast pre-built packet
     *
     * @param packet - instance of JdpPacket
     * @throws IOException
     */
    public void sendPacket(JdpPacket packet)
            throws IOException {
        byte[] data = packet.getPacketData();
        // Unlike allocate/put wrap don't need a flip afterward
        ByteBuffer b = ByteBuffer.wrap(data);
        channel.send(b, new InetSocketAddress(addr, port));
    }

    /**
     * Shutdown broadcaster and close underlying socket channel
     *
     * @throws IOException
     */
    public void shutdown() throws IOException {
        channel.close();
    }
}
