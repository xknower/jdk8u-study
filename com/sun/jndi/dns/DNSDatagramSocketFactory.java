package com.sun.jndi.dns;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ProtocolFamily;
import java.net.SocketException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.Random;

class DNSDatagramSocketFactory {
    static final int DEVIATION = 3;
    static final int THRESHOLD = 6;
    static final int BIT_DEVIATION = 2;
    static final int HISTORY = 32;
    static final int MAX_RANDOM_TRIES = 5;
    /**
     * The dynamic allocation port range (aka ephemeral ports), as configured
     * on the system. Use nested class for lazy evaluation.
     */
    static final class EphemeralPortRange {
        private EphemeralPortRange() {}
        static final int LOWER = sun.net.PortConfig.getLower();
        static final int UPPER = sun.net.PortConfig.getUpper();
        static final int RANGE = UPPER - LOWER + 1;
    }

    private static int findFirstFreePort() {
        PrivilegedExceptionAction<DatagramSocket> action = () -> new DatagramSocket(0);
        int port;
        try {
            @SuppressWarnings({"deprecated", "removal"})
            DatagramSocket ds = AccessController.doPrivileged(action);
            try (DatagramSocket ds1 = ds) {
                port = ds1.getLocalPort();
            }
        } catch (Exception x) {
            port = 0;
        }
        return port;
    }

    // Records a subset of max {@code capacity} previously used ports
    static final class PortHistory {
        final int capacity;
        final int[] ports;
        final Random random;
        int index;
        PortHistory(int capacity, Random random) {
            this.random = random;
            this.capacity = capacity;
            this.ports = new int[capacity];
        }
        // returns true if the history contains the specified port.
        public boolean contains(int port) {
            int p = 0;
            for (int i=0; i<capacity; i++) {
                if ((p = ports[i]) == 0 || p == port) break;
            }
            return p == port;
        }
        // Adds the port to the history - doesn't check whether the port
        // is already present. Always adds the port and always return true.
        public boolean add(int port) {
            if (ports[index] != 0) { // at max capacity
                // remove one port at random and store the new port there
                // don't remove the last port
                int remove = random.nextInt(capacity);
                if ((remove +1) % capacity == index) remove = index;
                ports[index = remove] = port;
            } else { // there's a free slot
                ports[index] = port;
            }
            if (++index == capacity) index = 0;
            return true;
        }
        // Adds the port to the history if not already present.
        // Return true if the port was added, false if the port was already
        // present.
        public boolean offer(int port) {
            if (contains(port)) return false;
            else return add(port);
        }
    }

    int lastport = findFirstFreePort();
    int lastSystemAllocated = lastport;
    int suitablePortCount;
    int unsuitablePortCount;
    final ProtocolFamily family; // null (default) means dual stack
    final int thresholdCount; // decision point
    final int deviation;
    final Random random;
    final PortHistory history;

    DNSDatagramSocketFactory() {
        this(new Random());
    }

    DNSDatagramSocketFactory(Random random) {
        this(Objects.requireNonNull(random), null, DEVIATION, THRESHOLD);
    }
    DNSDatagramSocketFactory(Random random,
                             ProtocolFamily family,
                             int deviation,
                             int threshold) {
        this.random = Objects.requireNonNull(random);
        this.history = new PortHistory(HISTORY, random);
        this.family = family;
        this.deviation = Math.max(1, deviation);
        this.thresholdCount = Math.max(2, threshold);
    }

    /**
     * Opens a datagram socket listening to the wildcard address on a
     * random port. If the underlying OS supports UDP port randomization
     * out of the box (if binding a socket to port 0 binds it to a random
     * port) then the underlying OS implementation is used. Otherwise, this
     * method will allocate and bind a socket on a randomly selected ephemeral
     * port in the dynamic range.
     * @return A new DatagramSocket bound to a random port.
     * @throws SocketException if the socket cannot be created.
     */
    public synchronized DatagramSocket open() throws SocketException {
        int lastseen = lastport;
        DatagramSocket s;

        boolean thresholdCrossed = unsuitablePortCount > thresholdCount;
        if (thresholdCrossed) {
            // Underlying stack does not support random UDP port out of the box.
            // Use our own algorithm to allocate a random UDP port
            s = openRandom();
            if (s != null) return s;

            // couldn't allocate a random port: reset all counters and fall
            // through.
            unsuitablePortCount = 0; suitablePortCount = 0; lastseen = 0;
        }

        // Allocate an ephemeral port (port 0)
        s = openDefault();
        lastport = s.getLocalPort();
        if (lastseen == 0) {
            lastSystemAllocated = lastport;
            history.offer(lastport);
            return s;
        }

        thresholdCrossed = suitablePortCount > thresholdCount;
        boolean farEnough = farEnough(lastseen);
        if (farEnough && lastSystemAllocated > 0) {
            farEnough = farEnough(lastSystemAllocated);
        }
        boolean recycled = history.contains(lastport);
        boolean suitable = (thresholdCrossed || farEnough && !recycled);
        if (suitable && !recycled) history.add(lastport);

        if (suitable) {
            if (!thresholdCrossed) {
                suitablePortCount++;
            } else if (!farEnough || recycled) {
                unsuitablePortCount = 1;
                suitablePortCount = thresholdCount/2;
            }
            // Either the underlying stack supports random UDP port allocation,
            // or the new port is sufficiently distant from last port to make
            // it look like it is. Let's use it.
            lastSystemAllocated = lastport;
            return s;
        }

        // Undecided... the new port was too close. Let's allocate a random
        // port using our own algorithm
        assert !thresholdCrossed;
        DatagramSocket ss = openRandom();
        if (ss == null) return s;
        unsuitablePortCount++;
        s.close();
        return ss;
    }

    private DatagramSocket openDefault() throws SocketException {
        if (family != null) {
            try {
                DatagramChannel c = DatagramChannel.open(family);
                try {
                    DatagramSocket s = c.socket();
                    s.bind(null);
                    return s;
                } catch (Throwable x) {
                    c.close();
                    throw x;
                }
            } catch (SocketException x) {
                throw x;
            } catch (IOException x) {
                SocketException e = new SocketException(x.getMessage());
                e.initCause(x);
                throw e;
            }
        }
        return new DatagramSocket();
    }

    synchronized boolean isUsingNativePortRandomization() {
        return  unsuitablePortCount <= thresholdCount
                && suitablePortCount > thresholdCount;
    }

    synchronized boolean isUsingJavaPortRandomization() {
        return unsuitablePortCount > thresholdCount ;
    }

    synchronized boolean isUndecided() {
        return !isUsingJavaPortRandomization()
                && !isUsingNativePortRandomization();
    }

    private boolean farEnough(int port) {
        return Integer.bitCount(port ^ lastport) > BIT_DEVIATION
                && Math.abs(port - lastport) > deviation;
    }

    private DatagramSocket openRandom() {
        int maxtries = MAX_RANDOM_TRIES;
        while (maxtries-- > 0) {
            int port;
            boolean suitable;
            boolean recycled;
            int maxrandom = MAX_RANDOM_TRIES;
            do {
                port = EphemeralPortRange.LOWER
                        + random.nextInt(EphemeralPortRange.RANGE);
                recycled = history.contains(port);
                suitable = lastport == 0 || (farEnough(port) && !recycled);
            } while (maxrandom-- > 0 && !suitable);

            // if no suitable port was found, try again
            // this means we might call random MAX_RANDOM_TRIES x MAX_RANDOM_TRIES
            // times - but that should be OK with MAX_RANDOM_TRIES = 5.
            if (!suitable) continue;

            try {
                if (family != null) {
                    DatagramChannel c = DatagramChannel.open(family);
                    try {
                        DatagramSocket s = c.socket();
                        s.bind(new InetSocketAddress(port));
                        lastport = s.getLocalPort();
                        if (!recycled) history.add(port);
                        return s;
                    } catch (Throwable x) {
                        c.close();
                        throw x;
                    }
                }
                DatagramSocket s = new DatagramSocket(port);
                lastport = s.getLocalPort();
                if (!recycled) history.add(port);
                return s;
            } catch (IOException x) {
                // try again until maxtries == 0;
            }
        }
        return null;
    }

}
