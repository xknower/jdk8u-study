package com.sun.net.httpserver;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.security.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import javax.net.ssl.*;


/**
 * This class is used to configure the https parameters for each incoming
 * https connection on a HttpsServer. Applications need to override
 * the {@link #configure(HttpsParameters)} method in order to change
 * the default configuration.
 * <p>
 * The following <a name="example">example</a> shows how this may be done:
 * <p>
 * <pre><blockquote>
 * SSLContext sslContext = SSLContext.getInstance (....);
 * HttpsServer server = HttpsServer.create();
 *
 * server.setHttpsConfigurator (new HttpsConfigurator(sslContext) {
 *     public void configure (HttpsParameters params) {
 *
 *         // get the remote address if needed
 *         InetSocketAddress remote = params.getClientAddress();
 *
 *         SSLContext c = getSSLContext();
 *
 *         // get the default parameters
 *         SSLParameters sslparams = c.getDefaultSSLParameters();
 *         if (remote.equals (...) ) {
 *             // modify the default set for client x
 *         }
 *
 *         params.setSSLParameters(sslparams);
 *     }
 * });
 * </blockquote></pre>
 * @since 1.6
 */
@jdk.Exported
public class HttpsConfigurator {

    private SSLContext context;

    /**
     * Creates an Https configuration, with the given SSLContext.
     * @param context the SSLContext to use for this configurator
     * @throws NullPointerException if no SSLContext supplied
     */
    public HttpsConfigurator (SSLContext context) {
        if (context == null) {
            throw new NullPointerException ("null SSLContext");
        }
        this.context = context;
    }

    /**
     * Returns the SSLContext for this HttpsConfigurator.
     * @return the SSLContext
     */
    public SSLContext getSSLContext() {
        return context;
    }

//BEGIN_TIGER_EXCLUDE
   /**
    * Called by the HttpsServer to configure the parameters
    * for a https connection currently being established.
    * The implementation of configure() must call
    * {@link HttpsParameters#setSSLParameters(SSLParameters)}
    * in order to set the SSL parameters for the connection.
    * <p>
    * The default implementation of this method uses the
    * SSLParameters returned from <p>
    * <code>getSSLContext().getDefaultSSLParameters()</code>
    * <p>
    * configure() may be overridden in order to modify this behavior.
    * See, the example <a href="#example">above</a>.
    * @param params the HttpsParameters to be configured.
    *
    * @since 1.6
    */
    public void configure (HttpsParameters params) {
        params.setSSLParameters (getSSLContext().getDefaultSSLParameters());
    }
//END_TIGER_EXCLUDE
}
