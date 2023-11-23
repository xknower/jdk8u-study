package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import sun.security.ssl.SSLHandshake.HandshakeMessage;

/**
 * Pack of the ServerHelloDone handshake message.
 */
final class ServerHelloDone {
    static final SSLConsumer handshakeConsumer =
        new ServerHelloDoneConsumer();
    static final HandshakeProducer handshakeProducer =
        new ServerHelloDoneProducer();

    /**
     * The ServerHelloDone handshake message.
     */
    static final class ServerHelloDoneMessage extends HandshakeMessage {
        ServerHelloDoneMessage(HandshakeContext handshakeContext) {
            super(handshakeContext);
        }

        ServerHelloDoneMessage(HandshakeContext handshakeContext,
                ByteBuffer m) throws IOException {
            super(handshakeContext);
            if (m.hasRemaining()) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER,
                    "Error parsing ServerHelloDone message: not empty");
            }
        }

        @Override
        public SSLHandshake handshakeType() {
            return SSLHandshake.SERVER_HELLO_DONE;
        }

        @Override
        public int messageLength() {
            return 0;
        }

        @Override
        public void send(HandshakeOutStream s) throws IOException {
            // empty, nothing to send
        }

        @Override
        public String toString() {
            return "<empty>";
        }
    }

    /**
     * The "ServerHelloDone" handshake message producer.
     */
    private static final
            class ServerHelloDoneProducer implements HandshakeProducer {
        // Prevent instantiation of this class.
        private ServerHelloDoneProducer() {
            // blank
        }

        @Override
        public byte[] produce(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The producing happens in server side only.
            ServerHandshakeContext shc = (ServerHandshakeContext)context;

            ServerHelloDoneMessage shdm = new ServerHelloDoneMessage(shc);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine(
                        "Produced ServerHelloDone handshake message", shdm);
            }

            // Output the handshake message.
            shdm.write(shc.handshakeOutput);
            shc.handshakeOutput.flush();

            //
            // update
            //
            shc.handshakeConsumers.put(SSLHandshake.CLIENT_KEY_EXCHANGE.id,
                    SSLHandshake.CLIENT_KEY_EXCHANGE);
            shc.conContext.consumers.put(ContentType.CHANGE_CIPHER_SPEC.id,
                    ChangeCipherSpec.t10Consumer);
            shc.handshakeConsumers.put(SSLHandshake.FINISHED.id,
                    SSLHandshake.FINISHED);

            // The handshake message has been delivered.
            return null;
        }
    }

    /**
     * The "ServerHelloDone" handshake message consumer.
     */
    private static final
            class ServerHelloDoneConsumer implements SSLConsumer {
        // Prevent instantiation of this class.
        private ServerHelloDoneConsumer() {
            // blank
        }

        @Override
        public void consume(ConnectionContext context,
                ByteBuffer message) throws IOException {
            // The consuming happens in client side only.
            ClientHandshakeContext chc = (ClientHandshakeContext)context;

            SSLConsumer certStatCons = chc.handshakeConsumers.remove(
                    SSLHandshake.CERTIFICATE_STATUS.id);
            if (certStatCons != null) {
                // Stapling was active but no certificate status message
                // was sent.  We need to run the absence handler which will
                // check the certificate chain.
                CertificateStatus.handshakeAbsence.absent(context, null);
            }

            // clean up this consumer
            chc.handshakeConsumers.clear();

            ServerHelloDoneMessage shdm =
                    new ServerHelloDoneMessage(chc, message);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine(
                        "Consuming ServerHelloDone handshake message", shdm);
            }

            //
            // validate
            //
            // blank

            //
            // update
            //
            chc.handshakeProducers.put(SSLHandshake.CLIENT_KEY_EXCHANGE.id,
                    SSLHandshake.CLIENT_KEY_EXCHANGE);
            chc.handshakeProducers.put(SSLHandshake.FINISHED.id,
                    SSLHandshake.FINISHED);
            //
            // produce
            //
            SSLHandshake[] probableHandshakeMessages = new SSLHandshake[] {
                // full handshake messages
                SSLHandshake.CERTIFICATE,
                SSLHandshake.CLIENT_KEY_EXCHANGE,
                SSLHandshake.CERTIFICATE_VERIFY,
                SSLHandshake.FINISHED
            };

            for (SSLHandshake hs : probableHandshakeMessages) {
                HandshakeProducer handshakeProducer =
                        chc.handshakeProducers.remove(hs.id);
                if (handshakeProducer != null) {
                    handshakeProducer.produce(context, null);
                }
            }
        }
    }
}
