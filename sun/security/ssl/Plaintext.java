package sun.security.ssl;

import java.nio.ByteBuffer;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

/*
 * Plaintext
 */
final class Plaintext {
    static final Plaintext PLAINTEXT_NULL = new Plaintext();

    final byte       contentType;
    final byte       majorVersion;
    final byte       minorVersion;
    final int        recordEpoch;     // increments on every cipher state change
    final long       recordSN;        // epoch | sequence number
    final ByteBuffer fragment;        // null if need to be reassembled

    HandshakeStatus  handshakeStatus; // null if not used or not handshaking

    private Plaintext() {
        this.contentType = 0;
        this.majorVersion = 0;
        this.minorVersion = 0;
        this.recordEpoch = -1;
        this.recordSN = -1;
        this.fragment = null;
        this.handshakeStatus = null;
    }

    Plaintext(byte contentType,
            byte majorVersion, byte minorVersion,
            int recordEpoch, long recordSN, ByteBuffer fragment) {

        this.contentType = contentType;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.recordEpoch = recordEpoch;
        this.recordSN = recordSN;
        this.fragment = fragment;

        this.handshakeStatus = null;
    }

    @Override
    public String toString() {
        return "contentType: " + contentType + "/" +
               "majorVersion: " + majorVersion + "/" +
               "minorVersion: " + minorVersion + "/" +
               "recordEpoch: " + recordEpoch + "/" +
               "recordSN: 0x" + Long.toHexString(recordSN) + "/" +
               "fragment: " + fragment;
    }
}
