package sun.net.www.protocol.http;

import java.io.*;
import java.net.URL;
import java.net.ProtocolException;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Random;

import sun.net.www.HeaderParser;
import sun.net.NetProperties;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.AccessController;
import static sun.net.www.protocol.http.HttpURLConnection.HTTP_CONNECT;

/**
 * DigestAuthentication: Encapsulate an http server authentication using
 * the "Digest" scheme, as described in RFC2069 and updated in RFC2617
 *
 * @author Bill Foote
 */

class DigestAuthentication extends AuthenticationInfo {

    private static final long serialVersionUID = 100L;

    private String authMethod;

    private final static String compatPropName = "http.auth.digest." +
                "quoteParameters";

    // true if http.auth.digest.quoteParameters Net property is true
    private static final boolean delimCompatFlag;

    static {
        Boolean b = AccessController.doPrivileged(
            new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    return NetProperties.getBoolean(compatPropName);
                }
            }
        );
        delimCompatFlag = (b == null) ? false : b.booleanValue();
    }

    // Authentication parameters defined in RFC2617.
    // One instance of these may be shared among several DigestAuthentication
    // instances as a result of a single authorization (for multiple domains)

    static class Parameters implements java.io.Serializable {
        private static final long serialVersionUID = -3584543755194526252L;

        private boolean serverQop; // server proposed qop=auth
        private String opaque;
        private String cnonce;
        private String nonce;
        private String algorithm;
        private int NCcount=0;

        // The H(A1) string used for MD5-sess
        private String  cachedHA1;

        // Force the HA1 value to be recalculated because the nonce has changed
        private boolean redoCachedHA1 = true;

        private static final int cnonceRepeat = 5;

        private static final int cnoncelen = 40; /* number of characters in cnonce */

        private static Random   random;

        static {
            random = new Random();
        }

        Parameters () {
            serverQop = false;
            opaque = null;
            algorithm = null;
            cachedHA1 = null;
            nonce = null;
            setNewCnonce();
        }

        boolean authQop () {
            return serverQop;
        }
        synchronized void incrementNC() {
            NCcount ++;
        }
        synchronized int getNCCount () {
            return NCcount;
        }

        int cnonce_count = 0;

        /* each call increments the counter */
        synchronized String getCnonce () {
            if (cnonce_count >= cnonceRepeat) {
                setNewCnonce();
            }
            cnonce_count++;
            return cnonce;
        }
        synchronized void setNewCnonce () {
            byte bb[] = new byte [cnoncelen/2];
            char cc[] = new char [cnoncelen];
            random.nextBytes (bb);
            for (int  i=0; i<(cnoncelen/2); i++) {
                int x = bb[i] + 128;
                cc[i*2]= (char) ('A'+ x/16);
                cc[i*2+1]= (char) ('A'+ x%16);
            }
            cnonce = new String (cc, 0, cnoncelen);
            cnonce_count = 0;
            redoCachedHA1 = true;
        }

        synchronized void setQop (String qop) {
            if (qop != null) {
                StringTokenizer st = new StringTokenizer (qop, " ");
                while (st.hasMoreTokens()) {
                    if (st.nextToken().equalsIgnoreCase ("auth")) {
                        serverQop = true;
                        return;
                    }
                }
            }
            serverQop = false;
        }

        synchronized String getOpaque () { return opaque;}
        synchronized void setOpaque (String s) { opaque=s;}

        synchronized String getNonce () { return nonce;}

        synchronized void setNonce (String s) {
            if (!s.equals(nonce)) {
                nonce=s;
                NCcount = 0;
                redoCachedHA1 = true;
            }
        }

        synchronized String getCachedHA1 () {
            if (redoCachedHA1) {
                return null;
            } else {
                return cachedHA1;
            }
        }

        synchronized void setCachedHA1 (String s) {
            cachedHA1=s;
            redoCachedHA1=false;
        }

        synchronized String getAlgorithm () { return algorithm;}
        synchronized void setAlgorithm (String s) { algorithm=s;}
    }

    Parameters params;

    /**
     * Create a DigestAuthentication
     */
    public DigestAuthentication(boolean isProxy, URL url, String realm,
                                String authMethod, PasswordAuthentication pw,
                                Parameters params) {
        super(isProxy ? PROXY_AUTHENTICATION : SERVER_AUTHENTICATION,
              AuthScheme.DIGEST,
              url,
              realm);
        this.authMethod = authMethod;
        this.pw = pw;
        this.params = params;
    }

    public DigestAuthentication(boolean isProxy, String host, int port, String realm,
                                String authMethod, PasswordAuthentication pw,
                                Parameters params) {
        super(isProxy ? PROXY_AUTHENTICATION : SERVER_AUTHENTICATION,
              AuthScheme.DIGEST,
              host,
              port,
              realm);
        this.authMethod = authMethod;
        this.pw = pw;
        this.params = params;
    }

    /**
     * @return true if this authentication supports preemptive authorization
     */
    @Override
    public boolean supportsPreemptiveAuthorization() {
        return true;
    }

    /**
     * Reclaculates the request-digest and returns it.
     *
     * <P> Used in the common case where the requestURI is simply the
     * abs_path.
     *
     * @param  url
     *         the URL
     *
     * @param  method
     *         the HTTP method
     *
     * @return the value of the HTTP header this authentication wants set
     */
    @Override
    public String getHeaderValue(URL url, String method) {
        return getHeaderValueImpl(url.getFile(), method);
    }

    /**
     * Reclaculates the request-digest and returns it.
     *
     * <P> Used when the requestURI is not the abs_path. The exact
     * requestURI can be passed as a String.
     *
     * @param  requestURI
     *         the Request-URI from the HTTP request line
     *
     * @param  method
     *         the HTTP method
     *
     * @return the value of the HTTP header this authentication wants set
     */
    String getHeaderValue(String requestURI, String method) {
        return getHeaderValueImpl(requestURI, method);
    }

    /**
     * Check if the header indicates that the current auth. parameters are stale.
     * If so, then replace the relevant field with the new value
     * and return true. Otherwise return false.
     * returning true means the request can be retried with the same userid/password
     * returning false means we have to go back to the user to ask for a new
     * username password.
     */
    @Override
    public boolean isAuthorizationStale (String header) {
        HeaderParser p = new HeaderParser (header);
        String s = p.findValue ("stale");
        if (s == null || !s.equals("true"))
            return false;
        String newNonce = p.findValue ("nonce");
        if (newNonce == null || "".equals(newNonce)) {
            return false;
        }
        params.setNonce (newNonce);
        return true;
    }

    /**
     * Set header(s) on the given connection.
     * @param conn The connection to apply the header(s) to
     * @param p A source of header values for this connection, if needed.
     * @param raw Raw header values for this connection, if needed.
     * @return true if all goes well, false if no headers were set.
     */
    @Override
    public boolean setHeaders(HttpURLConnection conn, HeaderParser p, String raw) {
        params.setNonce (p.findValue("nonce"));
        params.setOpaque (p.findValue("opaque"));
        params.setQop (p.findValue("qop"));

        String uri="";
        String method;
        if (type == PROXY_AUTHENTICATION &&
                conn.tunnelState() == HttpURLConnection.TunnelState.SETUP) {
            uri = HttpURLConnection.connectRequestURI(conn.getURL());
            method = HTTP_CONNECT;
        } else {
            try {
                uri = conn.getRequestURI();
            } catch (IOException e) {}
            method = conn.getMethod();
        }

        if (params.nonce == null || authMethod == null || pw == null || realm == null) {
            return false;
        }
        if (authMethod.length() >= 1) {
            // Method seems to get converted to all lower case elsewhere.
            // It really does need to start with an upper case letter
            // here.
            authMethod = Character.toUpperCase(authMethod.charAt(0))
                        + authMethod.substring(1).toLowerCase();
        }
        String algorithm = p.findValue("algorithm");
        if (algorithm == null || "".equals(algorithm)) {
            algorithm = "MD5";  // The default, accoriding to rfc2069
        }
        params.setAlgorithm (algorithm);

        // If authQop is true, then the server is doing RFC2617 and
        // has offered qop=auth. We do not support any other modes
        // and if auth is not offered we fallback to the RFC2069 behavior

        if (params.authQop()) {
            params.setNewCnonce();
        }

        String value = getHeaderValueImpl (uri, method);
        if (value != null) {
            conn.setAuthenticationProperty(getHeaderName(), value);
            return true;
        } else {
            return false;
        }
    }

    /* Calculate the Authorization header field given the request URI
     * and based on the authorization information in params
     */
    private String getHeaderValueImpl (String uri, String method) {
        String response;
        char[] passwd = pw.getPassword();
        boolean qop = params.authQop();
        String opaque = params.getOpaque();
        String cnonce = params.getCnonce ();
        String nonce = params.getNonce ();
        String algorithm = params.getAlgorithm ();
        params.incrementNC ();
        int  nccount = params.getNCCount ();
        String ncstring=null;

        if (nccount != -1) {
            ncstring = Integer.toHexString (nccount).toLowerCase();
            int len = ncstring.length();
            if (len < 8)
                ncstring = zeroPad [len] + ncstring;
        }

        try {
            response = computeDigest(true, pw.getUserName(),passwd,realm,
                                        method, uri, nonce, cnonce, ncstring);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

        String ncfield = "\"";
        if (qop) {
            ncfield = "\", nc=" + ncstring;
        }

        String algoS, qopS;

        if (delimCompatFlag) {
            // Put quotes around these String value parameters
            algoS = ", algorithm=\"" + algorithm + "\"";
            qopS = ", qop=\"auth\"";
        } else {
            // Don't put quotes around them, per the RFC
            algoS = ", algorithm=" + algorithm;
            qopS = ", qop=auth";
        }

        String value = authMethod
                        + " username=\"" + pw.getUserName()
                        + "\", realm=\"" + realm
                        + "\", nonce=\"" + nonce
                        + ncfield
                        + ", uri=\"" + uri
                        + "\", response=\"" + response + "\""
                        + algoS;
        if (opaque != null) {
            value += ", opaque=\"" + opaque + "\"";
        }
        if (cnonce != null) {
            value += ", cnonce=\"" + cnonce + "\"";
        }
        if (qop) {
            value += qopS;
        }
        return value;
    }

    public void checkResponse (String header, String method, URL url)
                                                        throws IOException {
        checkResponse (header, method, url.getFile());
    }

    public void checkResponse (String header, String method, String uri)
                                                        throws IOException {
        char[] passwd = pw.getPassword();
        String username = pw.getUserName();
        boolean qop = params.authQop();
        String opaque = params.getOpaque();
        String cnonce = params.cnonce;
        String nonce = params.getNonce ();
        String algorithm = params.getAlgorithm ();
        int  nccount = params.getNCCount ();
        String ncstring=null;

        if (header == null) {
            throw new ProtocolException ("No authentication information in response");
        }

        if (nccount != -1) {
            ncstring = Integer.toHexString (nccount).toUpperCase();
            int len = ncstring.length();
            if (len < 8)
                ncstring = zeroPad [len] + ncstring;
        }
        try {
            String expected = computeDigest(false, username,passwd,realm,
                                        method, uri, nonce, cnonce, ncstring);
            HeaderParser p = new HeaderParser (header);
            String rspauth = p.findValue ("rspauth");
            if (rspauth == null) {
                throw new ProtocolException ("No digest in response");
            }
            if (!rspauth.equals (expected)) {
                throw new ProtocolException ("Response digest invalid");
            }
            /* Check if there is a nextnonce field */
            String nextnonce = p.findValue ("nextnonce");
            if (nextnonce != null && ! "".equals(nextnonce)) {
                params.setNonce (nextnonce);
            }

        } catch (NoSuchAlgorithmException ex) {
            throw new ProtocolException ("Unsupported algorithm in response");
        }
    }

    private String computeDigest(
                        boolean isRequest, String userName, char[] password,
                        String realm, String connMethod,
                        String requestURI, String nonceString,
                        String cnonce, String ncValue
                    ) throws NoSuchAlgorithmException
    {

        String A1, HashA1;
        String algorithm = params.getAlgorithm ();
        boolean md5sess = algorithm.equalsIgnoreCase ("MD5-sess");

        MessageDigest md = MessageDigest.getInstance(md5sess?"MD5":algorithm);

        if (md5sess) {
            if ((HashA1 = params.getCachedHA1 ()) == null) {
                String s = userName + ":" + realm + ":";
                String s1 = encode (s, password, md);
                A1 = s1 + ":" + nonceString + ":" + cnonce;
                HashA1 = encode(A1, null, md);
                params.setCachedHA1 (HashA1);
            }
        } else {
            A1 = userName + ":" + realm + ":";
            HashA1 = encode(A1, password, md);
        }

        String A2;
        if (isRequest) {
            A2 = connMethod + ":" + requestURI;
        } else {
            A2 = ":" + requestURI;
        }
        String HashA2 = encode(A2, null, md);
        String combo, finalHash;

        if (params.authQop()) { /* RRC2617 when qop=auth */
            combo = HashA1+ ":" + nonceString + ":" + ncValue + ":" +
                        cnonce + ":auth:" +HashA2;

        } else { /* for compatibility with RFC2069 */
            combo = HashA1 + ":" +
                       nonceString + ":" +
                       HashA2;
        }
        finalHash = encode(combo, null, md);
        return finalHash;
    }

    private final static char charArray[] = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private final static String zeroPad[] = {
        // 0         1          2         3        4       5      6     7
        "00000000", "0000000", "000000", "00000", "0000", "000", "00", "0"
    };

    private String encode(String src, char[] passwd, MessageDigest md) {
        try {
            md.update(src.getBytes("ISO-8859-1"));
        } catch (java.io.UnsupportedEncodingException uee) {
            assert false;
        }
        if (passwd != null) {
            byte[] passwdBytes = new byte[passwd.length];
            for (int i=0; i<passwd.length; i++)
                passwdBytes[i] = (byte)passwd[i];
            md.update(passwdBytes);
            Arrays.fill(passwdBytes, (byte)0x00);
        }
        byte[] digest = md.digest();

        StringBuffer res = new StringBuffer(digest.length * 2);
        for (int i = 0; i < digest.length; i++) {
            int hashchar = ((digest[i] >>> 4) & 0xf);
            res.append(charArray[hashchar]);
            hashchar = (digest[i] & 0xf);
            res.append(charArray[hashchar]);
        }
        return res.toString();
    }
}
