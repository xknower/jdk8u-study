package com.sun.net.ssl;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import javax.security.cert.X509Certificate;

/**
 * HTTP URL connection with support for HTTPS-specific features. See
 * <A HREF="http://www.w3.org/pub/WWW/Protocols/"> the spec </A> for
 * details.
 *
 * @deprecated As of JDK 1.4, this implementation-specific class was
 *      replaced by {@link javax.net.ssl.HttpsURLConnection}.
 */
@Deprecated
abstract public
class HttpsURLConnection extends HttpURLConnection
{
    /*
     * Initialize an HTTPS URLConnection ... could check that the URL
     * is an "https" URL, and that the handler is also an HTTPS one,
     * but that's established by other code in this package.
     * @param url the URL
     */
    public HttpsURLConnection(URL url) throws IOException {
        super(url);
    }

    /**
     * Returns the cipher suite in use on this connection.
     * @return the cipher suite
     */
    public abstract String getCipherSuite();

    /**
     * Returns the server's X.509 certificate chain, or null if
     * the server did not authenticate.
     * @return the server certificate chain
     */
    public abstract X509Certificate [] getServerCertificateChain();

    /**
     * HostnameVerifier provides a callback mechanism so that
     * implementers of this interface can supply a policy for
     * handling the case where the host to connect to and
     * the server name from the certificate mismatch.
     *
     * The default implementation will deny such connections.
     */
    private static HostnameVerifier defaultHostnameVerifier =
        new HostnameVerifier() {
            public boolean verify(String urlHostname, String certHostname) {
                return false;
            }
        };

    protected HostnameVerifier hostnameVerifier = defaultHostnameVerifier;

    /**
     * Sets the default HostnameVerifier inherited when an instance
     * of this class is created.
     * @param v the default host name verifier
     */
    public static void setDefaultHostnameVerifier(HostnameVerifier v) {
        if (v == null) {
            throw new IllegalArgumentException(
                "no default HostnameVerifier specified");
        }

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SSLPermission("setHostnameVerifier"));
        }
        defaultHostnameVerifier = v;
    }

    /**
     * Gets the default HostnameVerifier.
     * @return the default host name verifier
     */
    public static HostnameVerifier getDefaultHostnameVerifier() {
        return defaultHostnameVerifier;
    }

    /**
     * Sets the HostnameVerifier.
     * @param v the host name verifier
     */
    public void setHostnameVerifier(HostnameVerifier v) {
        if (v == null) {
            throw new IllegalArgumentException(
                "no HostnameVerifier specified");
        }

        hostnameVerifier = v;
    }

    /**
     * Gets the HostnameVerifier.
     * @return the host name verifier
     */
    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    private static SSLSocketFactory defaultSSLSocketFactory = null;

    private SSLSocketFactory sslSocketFactory = getDefaultSSLSocketFactory();

    /**
     * Sets the default SSL socket factory inherited when an instance
     * of this class is created.
     * @param sf the default SSL socket factory
     */
    public static void setDefaultSSLSocketFactory(SSLSocketFactory sf) {
        if (sf == null) {
            throw new IllegalArgumentException(
                "no default SSLSocketFactory specified");
        }

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkSetFactory();
        }
        defaultSSLSocketFactory = sf;
    }

    /**
     * Gets the default SSL socket factory.
     * @return the default SSL socket factory
     */
    public static SSLSocketFactory getDefaultSSLSocketFactory() {
        if (defaultSSLSocketFactory == null) {
            defaultSSLSocketFactory =
                (SSLSocketFactory)SSLSocketFactory.getDefault();
        }
        return defaultSSLSocketFactory;
    }

    /**
     * Sets the SSL socket factory.
     * @param sf the SSL socket factory
     */
    public void setSSLSocketFactory(SSLSocketFactory sf) {
        if (sf == null) {
            throw new IllegalArgumentException(
                "no SSLSocketFactory specified");
        }

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkSetFactory();
        }

        sslSocketFactory = sf;
    }

    /**
     * Gets the SSL socket factory.
     * @return the SSL socket factory
     */
    public SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }
}
