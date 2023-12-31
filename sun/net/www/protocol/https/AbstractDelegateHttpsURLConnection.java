package sun.net.www.protocol.https;

import java.net.URL;
import java.net.Proxy;
import java.net.SecureCacheResponse;
import java.security.Principal;
import java.io.IOException;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import sun.net.www.http.*;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * HTTPS URL connection support.
 * We need this delegate because HttpsURLConnection is a subclass of
 * java.net.HttpURLConnection. We will avoid copying over the code from
 * sun.net.www.protocol.http.HttpURLConnection by having this class
 *
 */
public abstract class AbstractDelegateHttpsURLConnection extends
        HttpURLConnection {

    protected AbstractDelegateHttpsURLConnection(URL url,
            sun.net.www.protocol.http.Handler handler) throws IOException {
        this(url, null, handler);
    }

    protected AbstractDelegateHttpsURLConnection(URL url, Proxy p,
            sun.net.www.protocol.http.Handler handler) throws IOException {
        super(url, p, handler);
    }

    protected abstract javax.net.ssl.SSLSocketFactory getSSLSocketFactory();

    protected abstract javax.net.ssl.HostnameVerifier getHostnameVerifier();

    /**
     * No user application is able to call these routines, as no one
     * should ever get access to an instance of
     * DelegateHttpsURLConnection (sun.* or com.*)
     */

    /**
     * Create a new HttpClient object, bypassing the cache of
     * HTTP client objects/connections.
     *
     * Note: this method is changed from protected to public because
     * the com.sun.ssl.internal.www.protocol.https handler reuses this
     * class for its actual implemantation
     *
     * @param url the URL being accessed
     */
    public void setNewClient (URL url)
        throws IOException {
        setNewClient (url, false);
    }

    /**
     * Obtain a HttpClient object. Use the cached copy if specified.
     *
     * Note: this method is changed from protected to public because
     * the com.sun.ssl.internal.www.protocol.https handler reuses this
     * class for its actual implemantation
     *
     * @param url       the URL being accessed
     * @param useCache  whether the cached connection should be used
     *        if present
     */
    public void setNewClient (URL url, boolean useCache)
        throws IOException {
        int readTimeout = getReadTimeout();
        http = HttpsClient.New (getSSLSocketFactory(),
                                url,
                                getHostnameVerifier(),
                                null,
                                -1,
                                useCache,
                                getConnectTimeout(),
                                this);
        http.setReadTimeout(readTimeout);
        ((HttpsClient)http).afterConnect();
    }

    /**
     * Create a new HttpClient object, set up so that it uses
     * per-instance proxying to the given HTTP proxy.  This
     * bypasses the cache of HTTP client objects/connections.
     *
     * Note: this method is changed from protected to public because
     * the com.sun.ssl.internal.www.protocol.https handler reuses this
     * class for its actual implemantation
     *
     * @param url       the URL being accessed
     * @param proxyHost the proxy host to use
     * @param proxyPort the proxy port to use
     */
    public void setProxiedClient (URL url, String proxyHost, int proxyPort)
            throws IOException {
        setProxiedClient(url, proxyHost, proxyPort, false);
    }

    /**
     * Obtain a HttpClient object, set up so that it uses per-instance
     * proxying to the given HTTP proxy. Use the cached copy of HTTP
     * client objects/connections if specified.
     *
     * Note: this method is changed from protected to public because
     * the com.sun.ssl.internal.www.protocol.https handler reuses this
     * class for its actual implemantation
     *
     * @param url       the URL being accessed
     * @param proxyHost the proxy host to use
     * @param proxyPort the proxy port to use
     * @param useCache  whether the cached connection should be used
     *        if present
     */
    public void setProxiedClient (URL url, String proxyHost, int proxyPort,
            boolean useCache) throws IOException {
        proxiedConnect(url, proxyHost, proxyPort, useCache);
        if (!http.isCachedConnection()) {
            doTunneling();
        }
        ((HttpsClient)http).afterConnect();
    }

    protected void proxiedConnect(URL url, String proxyHost, int proxyPort,
            boolean useCache) throws IOException {
        if (connected)
            return;
        int readTimeout = getReadTimeout();
        http = HttpsClient.New (getSSLSocketFactory(),
                                url,
                                getHostnameVerifier(),
                                proxyHost,
                                proxyPort,
                                useCache,
                                getConnectTimeout(),
                                this);
        http.setReadTimeout(readTimeout);
        connected = true;
    }

    /**
     * Used by subclass to access "connected" variable.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Used by subclass to access "connected" variable.
     */
    public void setConnected(boolean conn) {
        connected = conn;
    }

    /**
     * Implements the HTTP protocol handler's "connect" method,
     * establishing an SSL connection to the server as necessary.
     */
    public void connect() throws IOException {
        if (connected)
            return;
        plainConnect();
        if (cachedResponse != null) {
            // using cached response
            return;
        }
        if (!http.isCachedConnection() && http.needsTunneling()) {
            doTunneling();
        }
        ((HttpsClient)http).afterConnect();
    }

    // will try to use cached HttpsClient
    protected HttpClient getNewHttpClient(URL url, Proxy p, int connectTimeout)
        throws IOException {
        return HttpsClient.New(getSSLSocketFactory(), url,
                               getHostnameVerifier(), p, true, connectTimeout,
                               this);
    }

    // will open new connection
    protected HttpClient getNewHttpClient(URL url, Proxy p, int connectTimeout,
                                          boolean useCache)
        throws IOException {
        return HttpsClient.New(getSSLSocketFactory(), url,
                               getHostnameVerifier(), p,
                               useCache, connectTimeout, this);
    }

    /**
     * Returns the cipher suite in use on this connection.
     */
    public String getCipherSuite () {
        if (cachedResponse != null) {
            return ((SecureCacheResponse)cachedResponse).getCipherSuite();
        }
        if (http == null) {
            throw new IllegalStateException("connection not yet open");
        } else {
           return ((HttpsClient)http).getCipherSuite ();
        }
    }

    /**
     * Returns the certificate chain the client sent to the
     * server, or null if the client did not authenticate.
     */
    public java.security.cert.Certificate[] getLocalCertificates() {
        if (cachedResponse != null) {
            List<java.security.cert.Certificate> l = ((SecureCacheResponse)cachedResponse).getLocalCertificateChain();
            if (l == null) {
                return null;
            } else {
                return l.toArray(new java.security.cert.Certificate[0]);
            }
        }
        if (http == null) {
            throw new IllegalStateException("connection not yet open");
        } else {
            return (((HttpsClient)http).getLocalCertificates ());
        }
    }

    /**
     * Returns the server's certificate chain, or throws
     * SSLPeerUnverified Exception if
     * the server did not authenticate.
     */
    public java.security.cert.Certificate[] getServerCertificates()
            throws SSLPeerUnverifiedException {
        if (cachedResponse != null) {
            List<java.security.cert.Certificate> l = ((SecureCacheResponse)cachedResponse).getServerCertificateChain();
            if (l == null) {
                return null;
            } else {
                return l.toArray(new java.security.cert.Certificate[0]);
            }
        }

        if (http == null) {
            throw new IllegalStateException("connection not yet open");
        } else {
            return (((HttpsClient)http).getServerCertificates ());
        }
    }

    /**
     * Returns the server's X.509 certificate chain, or null if
     * the server did not authenticate.
     */
    public javax.security.cert.X509Certificate[] getServerCertificateChain()
            throws SSLPeerUnverifiedException {
        if (cachedResponse != null) {
            throw new UnsupportedOperationException("this method is not supported when using cache");
        }
        if (http == null) {
            throw new IllegalStateException("connection not yet open");
        } else {
            return ((HttpsClient)http).getServerCertificateChain ();
        }
    }

    /**
     * Returns the server's principal, or throws SSLPeerUnverifiedException
     * if the server did not authenticate.
     */
    Principal getPeerPrincipal()
            throws SSLPeerUnverifiedException
    {
        if (cachedResponse != null) {
            return ((SecureCacheResponse)cachedResponse).getPeerPrincipal();
        }

        if (http == null) {
            throw new IllegalStateException("connection not yet open");
        } else {
            return (((HttpsClient)http).getPeerPrincipal());
        }
    }

    /**
     * Returns the principal the client sent to the
     * server, or null if the client did not authenticate.
     */
    Principal getLocalPrincipal()
    {
        if (cachedResponse != null) {
            return ((SecureCacheResponse)cachedResponse).getLocalPrincipal();
        }

        if (http == null) {
            throw new IllegalStateException("connection not yet open");
        } else {
            return (((HttpsClient)http).getLocalPrincipal());
        }
    }

}
