package com.sun.net.httpserver;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.*;

/**
 * This class encapsulates a HTTPS request received and a
 * response to be generated in one exchange and defines
 * the extensions to HttpExchange that are specific to the HTTPS protocol.
 * @since 1.6
 */

@jdk.Exported
public abstract class HttpsExchange extends HttpExchange {

    protected HttpsExchange () {
    }

    /**
     * Get the SSLSession for this exchange.
     * @return the SSLSession
     */
    public abstract SSLSession getSSLSession ();
}
