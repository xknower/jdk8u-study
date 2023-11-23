package sun.security.ssl;

import java.net.Socket;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import javax.net.ssl.*;
import sun.security.util.AnchorCertificates;
import sun.security.util.HostnameChecker;
import sun.security.validator.*;

/**
 * This class implements the SunJSSE X.509 trust manager using the internal
 * validator API in J2SE core. The logic in this class is minimal.<p>
 * <p>
 * This class supports both the Simple validation algorithm from previous
 * JSSE versions and PKIX validation. Currently, it is not possible for the
 * application to specify PKIX parameters other than trust anchors. This will
 * be fixed in a future release using new APIs. When that happens, it may also
 * make sense to separate the Simple and PKIX trust managers into separate
 * classes.
 *
 * @author Andreas Sterbenz
 */
final class X509TrustManagerImpl extends X509ExtendedTrustManager
        implements X509TrustManager {

    private final String validatorType;

    /**
     * The Set of trusted X509Certificates.
     */
    private final Collection<X509Certificate> trustedCerts;

    private final PKIXBuilderParameters pkixParams;

    // note that we need separate validator for client and server due to
    // the different extension checks. They are initialized lazily on demand.
    private volatile Validator clientValidator, serverValidator;

    X509TrustManagerImpl(String validatorType,
            Collection<X509Certificate> trustedCerts) {

        this.validatorType = validatorType;
        this.pkixParams = null;

        if (trustedCerts == null) {
            trustedCerts = Collections.<X509Certificate>emptySet();
        }

        this.trustedCerts = trustedCerts;

        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("adding as trusted certificates",
                    (Object[])trustedCerts.toArray(new X509Certificate[0]));
        }
    }

    X509TrustManagerImpl(String validatorType, PKIXBuilderParameters params) {
        this.validatorType = validatorType;
        this.pkixParams = params;
        // create server validator eagerly so that we can conveniently
        // get the trusted certificates
        // clients need it anyway eventually, and servers will not mind
        // the little extra footprint
        Validator v = getValidator(Validator.VAR_TLS_SERVER);
        trustedCerts = v.getTrustedCertificates();
        serverValidator = v;

        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("adding as trusted certificates",
                    (Object[])trustedCerts.toArray(new X509Certificate[0]));
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        checkTrusted(chain, authType, (Socket)null, true);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        checkTrusted(chain, authType, (Socket)null, false);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] certsArray = new X509Certificate[trustedCerts.size()];
        trustedCerts.toArray(certsArray);
        return certsArray;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType,
                Socket socket) throws CertificateException {
        checkTrusted(chain, authType, socket, true);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType,
            Socket socket) throws CertificateException {
        checkTrusted(chain, authType, socket, false);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType,
            SSLEngine engine) throws CertificateException {
        checkTrusted(chain, authType, engine, true);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType,
            SSLEngine engine) throws CertificateException {
        checkTrusted(chain, authType, engine, false);
    }

    private Validator checkTrustedInit(X509Certificate[] chain,
            String authType, boolean checkClientTrusted) {
        if (chain == null || chain.length == 0) {
            throw new IllegalArgumentException(
                "null or zero-length certificate chain");
        }

        if (authType == null || authType.isEmpty()) {
            throw new IllegalArgumentException(
                "null or zero-length authentication type");
        }

        Validator v = null;
        if (checkClientTrusted) {
            v = clientValidator;
            if (v == null) {
                synchronized (this) {
                    v = clientValidator;
                    if (v == null) {
                        v = getValidator(Validator.VAR_TLS_CLIENT);
                        clientValidator = v;
                    }
                }
            }
        } else {
            // assume double checked locking with a volatile flag works
            // (guaranteed under the new Tiger memory model)
            v = serverValidator;
            if (v == null) {
                synchronized (this) {
                    v = serverValidator;
                    if (v == null) {
                        v = getValidator(Validator.VAR_TLS_SERVER);
                        serverValidator = v;
                    }
                }
            }
        }

        return v;
    }

    private void checkTrusted(X509Certificate[] chain,
            String authType, Socket socket,
            boolean checkClientTrusted) throws CertificateException {
        Validator v = checkTrustedInit(chain, authType, checkClientTrusted);

        X509Certificate[] trustedChain = null;
        if ((socket != null) && socket.isConnected() &&
                                        (socket instanceof SSLSocket)) {

            SSLSocket sslSocket = (SSLSocket)socket;
            SSLSession session = sslSocket.getHandshakeSession();
            if (session == null) {
                throw new CertificateException("No handshake session");
            }

            // create the algorithm constraints
            boolean isExtSession = (session instanceof ExtendedSSLSession);
            AlgorithmConstraints constraints;
            if (isExtSession &&
                    ProtocolVersion.useTLS12PlusSpec(session.getProtocol())) {
                ExtendedSSLSession extSession = (ExtendedSSLSession)session;
                String[] localSupportedSignAlgs =
                        extSession.getLocalSupportedSignatureAlgorithms();

                constraints = new SSLAlgorithmConstraints(
                                sslSocket, localSupportedSignAlgs, false);
            } else {
                constraints = new SSLAlgorithmConstraints(sslSocket, false);
            }

            // Grab any stapled OCSP responses for use in validation
            List<byte[]> responseList = Collections.emptyList();
            if (!checkClientTrusted && isExtSession &&
                    session instanceof SSLSessionImpl) {
                responseList =
                        ((SSLSessionImpl)session).getStatusResponses();
            }
             trustedChain = validate(v, chain, responseList,
                    constraints, checkClientTrusted ? null : authType);

            // check endpoint identity
            String identityAlg = sslSocket.getSSLParameters().
                    getEndpointIdentificationAlgorithm();
            if (identityAlg != null && !identityAlg.isEmpty()) {
                checkIdentity(session,
                        trustedChain, identityAlg, checkClientTrusted);
            }
        } else {
            trustedChain = validate(v, chain, Collections.emptyList(),
                    null, checkClientTrusted ? null : authType);
        }

        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("Found trusted certificate",
                    trustedChain[trustedChain.length - 1]);
        }
    }

    private void checkTrusted(X509Certificate[] chain,
            String authType, SSLEngine engine,
            boolean checkClientTrusted) throws CertificateException {
        Validator v = checkTrustedInit(chain, authType, checkClientTrusted);

        X509Certificate[] trustedChain = null;
        if (engine != null) {
            SSLSession session = engine.getHandshakeSession();
            if (session == null) {
                throw new CertificateException("No handshake session");
            }

            // create the algorithm constraints
            boolean isExtSession = (session instanceof ExtendedSSLSession);
            AlgorithmConstraints constraints;
            if (isExtSession &&
                    ProtocolVersion.useTLS12PlusSpec(session.getProtocol())) {
                ExtendedSSLSession extSession = (ExtendedSSLSession)session;
                String[] localSupportedSignAlgs =
                        extSession.getLocalSupportedSignatureAlgorithms();

                constraints = new SSLAlgorithmConstraints(
                                engine, localSupportedSignAlgs, false);
            } else {
                constraints = new SSLAlgorithmConstraints(engine, false);
            }

            // Grab any stapled OCSP responses for use in validation
            List<byte[]> responseList = Collections.emptyList();
            if (!checkClientTrusted && isExtSession &&
                session instanceof SSLSessionImpl) {
                responseList =
                        ((SSLSessionImpl)session).getStatusResponses();
            }
             trustedChain = validate(v, chain, responseList,
                    constraints, checkClientTrusted ? null : authType);

            // check endpoint identity
            String identityAlg = engine.getSSLParameters().
                    getEndpointIdentificationAlgorithm();
            if (identityAlg != null && !identityAlg.isEmpty()) {
                checkIdentity(session, trustedChain,
                        identityAlg, checkClientTrusted);
            }
        } else {
            trustedChain = validate(v, chain, Collections.emptyList(),
                    null, checkClientTrusted ? null : authType);
        }

        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("Found trusted certificate",
                    trustedChain[trustedChain.length - 1]);
        }
    }

    private Validator getValidator(String variant) {
        Validator v;
        if (pkixParams == null) {
            v = Validator.getInstance(validatorType, variant, trustedCerts);
        } else {
            v = Validator.getInstance(validatorType, variant, pkixParams);
        }
        return v;
    }

    private static X509Certificate[] validate(Validator v,
            X509Certificate[] chain, List<byte[]> responseList,
            AlgorithmConstraints constraints, String authType)
            throws CertificateException {
        Object o = JsseJce.beginFipsProvider();
        try {
            return v.validate(chain, null, responseList, constraints, authType);
        } finally {
            JsseJce.endFipsProvider(o);
        }
    }

    // Get string representation of HostName from a list of server names.
    //
    // We are only accepting host_name name type in the list.
    private static String getHostNameInSNI(List<SNIServerName> sniNames) {

        SNIHostName hostname = null;
        for (SNIServerName sniName : sniNames) {
            if (sniName.getType() != StandardConstants.SNI_HOST_NAME) {
                continue;
            }

            if (sniName instanceof SNIHostName) {
                hostname = (SNIHostName)sniName;
            } else {
                try {
                    hostname = new SNIHostName(sniName.getEncoded());
                } catch (IllegalArgumentException iae) {
                    // unlikely to happen, just in case ...
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
                        SSLLogger.fine("Illegal server name: " + sniName);
                    }
                }
            }

            // no more than server name of the same name type
            break;
        }

        if (hostname != null) {
            return hostname.getAsciiName();
        }

        return null;
    }

    // Also used by X509KeyManagerImpl
    static List<SNIServerName> getRequestedServerNames(Socket socket) {
        if (socket != null && socket.isConnected() &&
                                        socket instanceof SSLSocket) {
            return getRequestedServerNames(
                    ((SSLSocket)socket).getHandshakeSession());
        }

        return Collections.<SNIServerName>emptyList();
    }

    // Also used by X509KeyManagerImpl
    static List<SNIServerName> getRequestedServerNames(SSLEngine engine) {
        if (engine != null) {
            return getRequestedServerNames(engine.getHandshakeSession());
        }

        return Collections.<SNIServerName>emptyList();
    }

    private static List<SNIServerName> getRequestedServerNames(
            SSLSession session) {
        if (session != null && (session instanceof ExtendedSSLSession)) {
            return ((ExtendedSSLSession)session).getRequestedServerNames();
        }

        return Collections.<SNIServerName>emptyList();
    }

    /*
     * Per RFC 6066, if an application negotiates a server name using an
     * application protocol and then upgrades to TLS, and if a server_name
     * extension is sent, then the extension SHOULD contain the same name
     * that was negotiated in the application protocol.  If the server_name
     * is established in the TLS session handshake, the client SHOULD NOT
     * attempt to request a different server name at the application layer.
     *
     * According to the above spec, we only need to check either the identity
     * in server_name extension or the peer host of the connection.  Peer host
     * is not always a reliable fully qualified domain name. The HostName in
     * server_name extension is more reliable than peer host. So we prefer
     * the identity checking aginst the server_name extension if present, and
     * may failove to peer host checking.
     */
    static void checkIdentity(SSLSession session,
            X509Certificate[] trustedChain,
            String algorithm,
            boolean checkClientTrusted) throws CertificateException {

        // check if EE certificate chains to a public root CA (as
        // pre-installed in cacerts)
        boolean chainsToPublicCA = AnchorCertificates.contains(
                trustedChain[trustedChain.length - 1]);

        boolean identifiable = false;
        String peerHost = session.getPeerHost();
        // Is it a Fully-Qualified Domain Names (FQDN) ending with a dot?
        if (peerHost != null && peerHost.endsWith(".")) {
            // Remove the ending dot, which is not allowed in SNIHostName.
            peerHost = peerHost.substring(0, peerHost.length() - 1);
        }

        if (!checkClientTrusted) {
            List<SNIServerName> sniNames = getRequestedServerNames(session);
            String sniHostName = getHostNameInSNI(sniNames);
            if (sniHostName != null) {
                try {
                    checkIdentity(sniHostName,
                            trustedChain[0], algorithm, chainsToPublicCA);
                    identifiable = true;
                } catch (CertificateException ce) {
                    if (sniHostName.equalsIgnoreCase(peerHost)) {
                        throw ce;
                    }

                    // otherwisw, failover to check peer host
                }
            }
        }

        if (!identifiable) {
            checkIdentity(peerHost,
                    trustedChain[0], algorithm, chainsToPublicCA);
        }
    }

    /*
     * Identify the peer by its certificate and hostname.
     *
     * Lifted from sun.net.www.protocol.https.HttpsClient.
     */
    static void checkIdentity(String hostname, X509Certificate cert,
            String algorithm) throws CertificateException {
        checkIdentity(hostname, cert, algorithm, false);
    }

    private static void checkIdentity(String hostname, X509Certificate cert,
            String algorithm, boolean chainsToPublicCA)
            throws CertificateException {
        if (algorithm != null && !algorithm.isEmpty()) {
            // if IPv6 strip off the "[]"
            if ((hostname != null) && hostname.startsWith("[") &&
                    hostname.endsWith("]")) {
                hostname = hostname.substring(1, hostname.length() - 1);
            }

            if (algorithm.equalsIgnoreCase("HTTPS")) {
                HostnameChecker.getInstance(HostnameChecker.TYPE_TLS).match(
                        hostname, cert, chainsToPublicCA);
            } else if (algorithm.equalsIgnoreCase("LDAP") ||
                    algorithm.equalsIgnoreCase("LDAPS")) {
                HostnameChecker.getInstance(HostnameChecker.TYPE_LDAP).match(
                        hostname, cert, chainsToPublicCA);
            } else {
                throw new CertificateException(
                        "Unknown identification algorithm: " + algorithm);
            }
        }
    }
}

