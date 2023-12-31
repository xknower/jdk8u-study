package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension.ExtensionConsumer;
import sun.security.ssl.SSLExtension.SSLExtensionSpec;
import sun.security.ssl.SSLHandshake.HandshakeMessage;

/**
 * Pack of the "signature_algorithms" extensions [RFC 5246].
 */
final class SignatureAlgorithmsExtension {
    static final HandshakeProducer chNetworkProducer =
            new CHSignatureSchemesProducer();
    static final ExtensionConsumer chOnLoadConsumer =
            new CHSignatureSchemesConsumer();
    static final HandshakeAbsence chOnLoadAbsence =
            new CHSignatureSchemesOnLoadAbsence();
    static final HandshakeConsumer chOnTradeConsumer =
            new CHSignatureSchemesUpdate();
    static final HandshakeAbsence chOnTradeAbsence =
            new CHSignatureSchemesOnTradeAbsence();

    static final HandshakeProducer crNetworkProducer =
            new CRSignatureSchemesProducer();
    static final ExtensionConsumer crOnLoadConsumer =
            new CRSignatureSchemesConsumer();
    static final HandshakeAbsence crOnLoadAbsence =
            new CRSignatureSchemesAbsence();
    static final HandshakeConsumer crOnTradeConsumer =
            new CRSignatureSchemesUpdate();

    static final SSLStringizer ssStringizer =
            new SignatureSchemesStringizer();

    /**
     * The "signature_algorithms" extension.
     */
    static final class SignatureSchemesSpec implements SSLExtensionSpec {
        final int[] signatureSchemes;

        SignatureSchemesSpec(List<SignatureScheme> schemes) {
            if (schemes != null) {
                signatureSchemes = new int[schemes.size()];
                int i = 0;
                for (SignatureScheme scheme : schemes) {
                    signatureSchemes[i++] = scheme.id;
                }
            } else {
                this.signatureSchemes = new int[0];
            }
        }

        SignatureSchemesSpec(ByteBuffer buffer) throws IOException {
            if (buffer.remaining() < 2) {      // 2: the length of the list
                throw new SSLProtocolException(
                    "Invalid signature_algorithms: insufficient data");
            }

            byte[] algs = Record.getBytes16(buffer);
            if (buffer.hasRemaining()) {
                throw new SSLProtocolException(
                    "Invalid signature_algorithms: unknown extra data");
            }

            if (algs == null || algs.length == 0 || (algs.length & 0x01) != 0) {
                throw new SSLProtocolException(
                    "Invalid signature_algorithms: incomplete data");
            }

            int[] schemes = new int[algs.length / 2];
            for (int i = 0, j = 0; i < algs.length;) {
                byte hash = algs[i++];
                byte sign = algs[i++];
                schemes[j++] = ((hash & 0xFF) << 8) | (sign & 0xFF);
            }

            this.signatureSchemes = schemes;
        }

        @Override
        public String toString() {
            MessageFormat messageFormat = new MessageFormat(
                "\"signature schemes\": '['{0}']'", Locale.ENGLISH);

            if (signatureSchemes == null || signatureSchemes.length == 0) {
                Object[] messageFields = {
                        "<no supported signature schemes specified>"
                    };
                return messageFormat.format(messageFields);
            } else {
                StringBuilder builder = new StringBuilder(512);
                boolean isFirst = true;
                for (int pv : signatureSchemes) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        builder.append(", ");
                    }

                    builder.append(SignatureScheme.nameOf(pv));
                }

                Object[] messageFields = {
                        builder.toString()
                    };

                return messageFormat.format(messageFields);
            }
        }
    }

    private static final
            class SignatureSchemesStringizer implements SSLStringizer {
        @Override
        public String toString(ByteBuffer buffer) {
            try {
                return (new SignatureSchemesSpec(buffer)).toString();
            } catch (IOException ioe) {
                // For debug logging only, so please swallow exceptions.
                return ioe.getMessage();
            }
        }
    }

    /**
     * Network data producer of a "signature_algorithms" extension in
     * the ClientHello handshake message.
     */
    private static final
            class CHSignatureSchemesProducer implements HandshakeProducer {
        // Prevent instantiation of this class.
        private CHSignatureSchemesProducer() {
            // blank
        }

        @Override
        public byte[] produce(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The producing happens in client side only.
            ClientHandshakeContext chc = (ClientHandshakeContext)context;

            // Is it a supported and enabled extension?
            if (!chc.sslConfig.isAvailable(
                    SSLExtension.CH_SIGNATURE_ALGORITHMS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine(
                        "Ignore unavailable signature_algorithms extension");
                }
                return null;
            }

            // Produce the extension.
            if (chc.localSupportedSignAlgs == null) {
                chc.localSupportedSignAlgs =
                    SignatureScheme.getSupportedAlgorithms(
                            chc.sslConfig,
                            chc.algorithmConstraints, chc.activeProtocols);
            }

            int vectorLen = SignatureScheme.sizeInRecord() *
                    chc.localSupportedSignAlgs.size();
            byte[] extData = new byte[vectorLen + 2];
            ByteBuffer m = ByteBuffer.wrap(extData);
            Record.putInt16(m, vectorLen);
            for (SignatureScheme ss : chc.localSupportedSignAlgs) {
                Record.putInt16(m, ss.id);
            }

            // Update the context.
            chc.handshakeExtensions.put(
                    SSLExtension.CH_SIGNATURE_ALGORITHMS,
                    new SignatureSchemesSpec(chc.localSupportedSignAlgs));

            return extData;
        }
    }

    /**
     * Network data consumer of a "signature_algorithms" extension in
     * the ClientHello handshake message.
     */
    private static final
            class CHSignatureSchemesConsumer implements ExtensionConsumer {
        // Prevent instantiation of this class.
        private CHSignatureSchemesConsumer() {
            // blank
        }

        @Override
        public void consume(ConnectionContext context,
            HandshakeMessage message, ByteBuffer buffer) throws IOException {
            // The consuming happens in server side only.
            ServerHandshakeContext shc = (ServerHandshakeContext)context;

            // Is it a supported and enabled extension?
            if (!shc.sslConfig.isAvailable(
                    SSLExtension.CH_SIGNATURE_ALGORITHMS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine(
                        "Ignore unavailable signature_algorithms extension");
                }
                return;     // ignore the extension
            }

            // Parse the extension.
            SignatureSchemesSpec spec;
            try {
                spec = new SignatureSchemesSpec(buffer);
            } catch (IOException ioe) {
                throw shc.conContext.fatal(Alert.UNEXPECTED_MESSAGE, ioe);
            }

            // Update the context.
            shc.handshakeExtensions.put(
                    SSLExtension.CH_SIGNATURE_ALGORITHMS, spec);

            // No impact on session resumption.
        }
    }

    /**
     * After session creation consuming of a "signature_algorithms"
     * extension in the ClientHello handshake message.
     */
    private static final class CHSignatureSchemesUpdate
            implements HandshakeConsumer {
        // Prevent instantiation of this class.
        private CHSignatureSchemesUpdate() {
            // blank
        }

        @Override
        public void consume(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The consuming happens in server side only.
            ServerHandshakeContext shc = (ServerHandshakeContext)context;

            SignatureSchemesSpec spec =
                    (SignatureSchemesSpec)shc.handshakeExtensions.get(
                            SSLExtension.CH_SIGNATURE_ALGORITHMS);
            if (spec == null) {
                // Ignore, no "signature_algorithms" extension requested.
                return;
            }

            // update the context
            List<SignatureScheme> sss =
                    SignatureScheme.getSupportedAlgorithms(
                            shc.sslConfig,
                            shc.algorithmConstraints, shc.negotiatedProtocol,
                            spec.signatureSchemes);
            shc.peerRequestedSignatureSchemes = sss;

            // If no "signature_algorithms_cert" extension is present, then
            // the "signature_algorithms" extension also applies to
            // signatures appearing in certificates.
            SignatureSchemesSpec certSpec =
                    (SignatureSchemesSpec)shc.handshakeExtensions.get(
                            SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT);
            if (certSpec == null) {
                shc.peerRequestedCertSignSchemes = sss;
                shc.handshakeSession.setPeerSupportedSignatureAlgorithms(sss);
            }

            if (!shc.isResumption &&
                    shc.negotiatedProtocol.useTLS13PlusSpec()) {
                if (shc.sslConfig.clientAuthType !=
                        ClientAuthType.CLIENT_AUTH_NONE) {
                    shc.handshakeProducers.putIfAbsent(
                            SSLHandshake.CERTIFICATE_REQUEST.id,
                            SSLHandshake.CERTIFICATE_REQUEST);
                }
                shc.handshakeProducers.put(
                        SSLHandshake.CERTIFICATE.id,
                        SSLHandshake.CERTIFICATE);
                shc.handshakeProducers.putIfAbsent(
                        SSLHandshake.CERTIFICATE_VERIFY.id,
                        SSLHandshake.CERTIFICATE_VERIFY);
            }
        }
    }

    /**
     * The absence processing if a "signature_algorithms" extension is
     * not present in the ClientHello handshake message.
     */
    private static final
            class CHSignatureSchemesOnLoadAbsence implements HandshakeAbsence {
        @Override
        public void absent(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The consuming happens in server side only.
            ServerHandshakeContext shc = (ServerHandshakeContext)context;

            // This is a mandatory extension for certificate authentication
            // in TLS 1.3.
            //
            // We may support the server authentication other than X.509
            // certificate later.
            if (shc.negotiatedProtocol.useTLS13PlusSpec()) {
                throw shc.conContext.fatal(Alert.MISSING_EXTENSION,
                    "No mandatory signature_algorithms extension in the " +
                    "received CertificateRequest handshake message");
            }
        }
    }

    /**
     * The absence processing if a "signature_algorithms" extension is
     * not present in the ClientHello handshake message.
     */
    private static final
            class CHSignatureSchemesOnTradeAbsence implements HandshakeAbsence {
        @Override
        public void absent(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The consuming happens in server side only.
            ServerHandshakeContext shc = (ServerHandshakeContext)context;

            if (shc.negotiatedProtocol.useTLS12PlusSpec()) {
                // Use default hash and signature algorithm:
                //      {sha1,rsa}
                //      {sha1,dsa}
                //      {sha1,ecdsa}
                // Per RFC 5246, If the client supports only the default hash
                // and signature algorithms, it MAY omit the
                // signature_algorithms extension.  If the client does not
                // support the default algorithms, or supports other hash
                // and signature algorithms (and it is willing to use them
                // for verifying messages sent by the server, i.e., server
                // certificates and server key exchange), it MUST send the
                // signature_algorithms extension, listing the algorithms it
                // is willing to accept.
                List<SignatureScheme> schemes = Arrays.asList(
                        SignatureScheme.RSA_PKCS1_SHA1,
                        SignatureScheme.DSA_SHA1,
                        SignatureScheme.ECDSA_SHA1
                );

                shc.peerRequestedSignatureSchemes = schemes;
                if (shc.peerRequestedCertSignSchemes == null ||
                        shc.peerRequestedCertSignSchemes.isEmpty()) {
                    shc.peerRequestedCertSignSchemes = schemes;
                }

                // Use the default peer signature algorithms.
                shc.handshakeSession.setUseDefaultPeerSignAlgs();
            }
        }
    }

    /**
     * Network data producer of a "signature_algorithms" extension in
     * the CertificateRequest handshake message.
     */
    private static final
            class CRSignatureSchemesProducer implements HandshakeProducer {
        // Prevent instantiation of this class.
        private CRSignatureSchemesProducer() {
            // blank
        }

        @Override
        public byte[] produce(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The producing happens in server side only.
            ServerHandshakeContext shc = (ServerHandshakeContext)context;

            // Is it a supported and enabled extension?
            //
            // Note that this is a mandatory extension for CertificateRequest
            // handshake message in TLS 1.3.
            if (!shc.sslConfig.isAvailable(
                    SSLExtension.CR_SIGNATURE_ALGORITHMS)) {
                throw shc.conContext.fatal(Alert.MISSING_EXTENSION,
                        "No available signature_algorithms extension " +
                        "for client certificate authentication");
            }

            // Produce the extension.
            List<ProtocolVersion> protocols = Arrays.asList(shc.negotiatedProtocol);
            protocols = Collections.unmodifiableList(protocols);
            List<SignatureScheme> sigAlgs =
                    SignatureScheme.getSupportedAlgorithms(
                            shc.sslConfig,
                            shc.algorithmConstraints,
                            protocols);

            int vectorLen = SignatureScheme.sizeInRecord() * sigAlgs.size();
            byte[] extData = new byte[vectorLen + 2];
            ByteBuffer m = ByteBuffer.wrap(extData);
            Record.putInt16(m, vectorLen);
            for (SignatureScheme ss : sigAlgs) {
                Record.putInt16(m, ss.id);
            }

            // Update the context.
            shc.handshakeExtensions.put(
                    SSLExtension.CR_SIGNATURE_ALGORITHMS,
                    new SignatureSchemesSpec(shc.localSupportedSignAlgs));

            return extData;
        }
    }

    /**
     * Network data consumer of a "signature_algorithms" extension in
     * the CertificateRequest handshake message.
     */
    private static final
            class CRSignatureSchemesConsumer implements ExtensionConsumer {
        // Prevent instantiation of this class.
        private CRSignatureSchemesConsumer() {
            // blank
        }
        @Override
        public void consume(ConnectionContext context,
            HandshakeMessage message, ByteBuffer buffer) throws IOException {
            // The consuming happens in client side only.
            ClientHandshakeContext chc = (ClientHandshakeContext)context;

            // Is it a supported and enabled extension?
            //
            // Note that this is a mandatory extension for CertificateRequest
            // handshake message in TLS 1.3.
            if (!chc.sslConfig.isAvailable(
                    SSLExtension.CR_SIGNATURE_ALGORITHMS)) {
                throw chc.conContext.fatal(Alert.HANDSHAKE_FAILURE,
                        "No available signature_algorithms extension " +
                        "for client certificate authentication");
            }

            // Parse the extension.
            SignatureSchemesSpec spec;
            try {
                spec = new SignatureSchemesSpec(buffer);
            } catch (IOException ioe) {
                throw chc.conContext.fatal(Alert.UNEXPECTED_MESSAGE, ioe);
            }

            List<SignatureScheme> knownSignatureSchemes = new LinkedList<>();
            for (int id : spec.signatureSchemes) {
                SignatureScheme ss = SignatureScheme.valueOf(id);
                if (ss != null) {
                    knownSignatureSchemes.add(ss);
                }
            }

            // Update the context.
            // chc.peerRequestedSignatureSchemes = knownSignatureSchemes;
            chc.handshakeExtensions.put(
                    SSLExtension.CR_SIGNATURE_ALGORITHMS, spec);

            // No impact on session resumption.
        }
    }

    /**
     * After session creation consuming of a "signature_algorithms"
     * extension in the CertificateRequest handshake message.
     */
    private static final class CRSignatureSchemesUpdate
            implements HandshakeConsumer {
        // Prevent instantiation of this class.
        private CRSignatureSchemesUpdate() {
            // blank
        }

        @Override
        public void consume(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The consuming happens in client side only.
            ClientHandshakeContext chc = (ClientHandshakeContext)context;

            SignatureSchemesSpec spec =
                    (SignatureSchemesSpec)chc.handshakeExtensions.get(
                            SSLExtension.CR_SIGNATURE_ALGORITHMS);
            if (spec == null) {
                // Ignore, no "signature_algorithms" extension requested.
                return;
            }

            // update the context
            List<SignatureScheme> sss =
                    SignatureScheme.getSupportedAlgorithms(
                            chc.sslConfig,
                            chc.algorithmConstraints, chc.negotiatedProtocol,
                            spec.signatureSchemes);
            chc.peerRequestedSignatureSchemes = sss;

            // If no "signature_algorithms_cert" extension is present, then
            // the "signature_algorithms" extension also applies to
            // signatures appearing in certificates.
            SignatureSchemesSpec certSpec =
                    (SignatureSchemesSpec)chc.handshakeExtensions.get(
                            SSLExtension.CR_SIGNATURE_ALGORITHMS_CERT);
            if (certSpec == null) {
                chc.peerRequestedCertSignSchemes = sss;
                chc.handshakeSession.setPeerSupportedSignatureAlgorithms(sss);
            }
        }
    }

    /**
     * The absence processing if a "signature_algorithms" extension is
     * not present in the CertificateRequest handshake message.
     */
    private static final
            class CRSignatureSchemesAbsence implements HandshakeAbsence {
        @Override
        public void absent(ConnectionContext context,
                HandshakeMessage message) throws IOException {
            // The consuming happens in client side only.
            ClientHandshakeContext chc = (ClientHandshakeContext)context;

            // This is a mandatory extension for CertificateRequest handshake
            // message in TLS 1.3.
            throw chc.conContext.fatal(Alert.MISSING_EXTENSION,
                    "No mandatory signature_algorithms extension in the " +
                    "received CertificateRequest handshake message");
        }
    }
}
