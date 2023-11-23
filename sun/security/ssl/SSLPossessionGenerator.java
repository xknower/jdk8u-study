package sun.security.ssl;

interface SSLPossessionGenerator {
    SSLPossession createPossession(HandshakeContext handshakeContext);
}
