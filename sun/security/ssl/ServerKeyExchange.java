package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import sun.security.ssl.SSLHandshake.HandshakeMessage;

/**
 * Pack of the ServerKeyExchange handshake message.
 */
final class ServerKeyExchange {
    static final SSLConsumer handshakeConsumer =
        new ServerKeyExchangeConsumer();
    static final HandshakeProducer handshakeProducer =
        new ServerKeyExchangeProducer();

    /**
     * The "ServerKeyExchange" handshake message producer.
     */
    private static final
            class ServerKeyExchangeProducer implements HandshakeProducer {
        // Prevent instantiation of this class.
        private ServerKeyExchangeProducer() {
            // blank
        }

        @Override
        public byte[] produce(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The producing happens in server side only.
            ServerHandshakeContext shc = (ServerHandshakeContext)context;

            SSLKeyExchange ke = SSLKeyExchange.valueOf(
                    shc.negotiatedCipherSuite.keyExchange,
                    shc.negotiatedProtocol);
            if (ke != null) {
                for (Map.Entry<Byte, HandshakeProducer> hc :
                        ke.getHandshakeProducers(shc)) {
                    if (hc.getKey() == SSLHandshake.SERVER_KEY_EXCHANGE.id) {
                        return hc.getValue().produce(context, message);
                    }
                }
            }

            // not producer defined.
            throw shc.conContext.fatal(Alert.HANDSHAKE_FAILURE,
                    "No ServerKeyExchange handshake message can be produced.");
        }
    }

    /**
     * The "ServerKeyExchange" handshake message consumer.
     */
    private static final
            class ServerKeyExchangeConsumer implements SSLConsumer {
        // Prevent instantiation of this class.
        private ServerKeyExchangeConsumer() {
            // blank
        }

        @Override
        public void consume(ConnectionContext context,
                ByteBuffer message) throws IOException {
            // The consuming happens in client side only.
            ClientHandshakeContext chc = (ClientHandshakeContext)context;

            // clean up this consumer
            chc.handshakeConsumers.remove(SSLHandshake.SERVER_KEY_EXCHANGE.id);

            // Any receipt/consumption of the CertificateRequest before
            // ServerKeyExchange is a state machine violation.  We may not
            // know for sure if an early CR message is a violation though until
            // we have reached this point, due to other TLS features and
            // optional messages.
            if (chc.receivedCertReq) {
                chc.receivedCertReq = false;    // Reset flag
                throw chc.conContext.fatal(Alert.UNEXPECTED_MESSAGE,
                        "Unexpected ServerKeyExchange handshake message");
            }

            SSLConsumer certStatCons = chc.handshakeConsumers.remove(
                    SSLHandshake.CERTIFICATE_STATUS.id);
            if (certStatCons != null) {
                // Stapling was active but no certificate status message
                // was sent.  We need to run the absence handler which will
                // check the certificate chain.
                CertificateStatus.handshakeAbsence.absent(context, null);
            }

            SSLKeyExchange ke = SSLKeyExchange.valueOf(
                    chc.negotiatedCipherSuite.keyExchange,
                    chc.negotiatedProtocol);
            if (ke != null) {
                for (Map.Entry<Byte, SSLConsumer> hc :
                        ke.getHandshakeConsumers(chc)) {
                    if (hc.getKey() == SSLHandshake.SERVER_KEY_EXCHANGE.id) {
                        hc.getValue().consume(context, message);
                        return;
                    }
                }
            }

            // no consumer defined.
            throw chc.conContext.fatal(Alert.UNEXPECTED_MESSAGE,
                        "Unexpected ServerKeyExchange handshake message.");
        }
    }
}

