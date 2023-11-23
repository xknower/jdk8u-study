package sun.awt.image;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import sun.net.util.URLUtil;

public class URLImageSource extends InputStreamImageSource {
    URL url;
    URLConnection conn;
    String actualHost;
    int actualPort;

    public URLImageSource(URL u) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                java.security.Permission perm =
                      URLUtil.getConnectPermission(u);
                if (perm != null) {
                    try {
                        sm.checkPermission(perm);
                    } catch (SecurityException se) {
                        // fallback to checkRead/checkConnect for pre 1.2
                        // security managers
                        if ((perm instanceof java.io.FilePermission) &&
                                perm.getActions().indexOf("read") != -1) {
                            sm.checkRead(perm.getName());
                        } else if ((perm instanceof
                                java.net.SocketPermission) &&
                                perm.getActions().indexOf("connect") != -1) {
                            sm.checkConnect(u.getHost(), u.getPort());
                        } else {
                            throw se;
                        }
                    }
                }
            } catch (java.io.IOException ioe) {
                    sm.checkConnect(u.getHost(), u.getPort());
            }
        }
        this.url = u;

    }

    public URLImageSource(String href) throws MalformedURLException {
        this(new URL(null, href));
    }

    public URLImageSource(URL u, URLConnection uc) {
        this(u);
        conn = uc;
    }

    public URLImageSource(URLConnection uc) {
        this(uc.getURL(), uc);
    }

    final boolean checkSecurity(Object context, boolean quiet) {
        // If actualHost is not null, then the host/port parameters that
        // the image was actually fetched from were different than the
        // host/port parameters the original URL specified for at least
        // one of the download attempts.  The original URL security was
        // checked when the applet got a handle to the image, so we only
        // need to check for the real host/port.
        if (actualHost != null) {
            try {
                SecurityManager security = System.getSecurityManager();
                if (security != null) {
                    security.checkConnect(actualHost, actualPort, context);
                }
            } catch (SecurityException e) {
                if (!quiet) {
                    throw e;
                }
                return false;
            }
        }
        return true;
    }

    private synchronized URLConnection getConnection() throws IOException {
        URLConnection c;
        if (conn != null) {
            c = conn;
            conn = null;
        } else {
            c = url.openConnection();
        }
        return c;
    }

    protected ImageDecoder getDecoder() {
        InputStream is = null;
        String type = null;
        URLConnection c = null;
        try {
            c = getConnection();
            is = c.getInputStream();
            type = c.getContentType();
            URL u = c.getURL();
            if (u != url && (!u.getHost().equals(url.getHost()) ||
                             u.getPort() != url.getPort()))
            {
                // The image is allowed to come from either the host/port
                // listed in the original URL, or it can come from one other
                // host/port that the URL is redirected to.  More than that
                // and we give up and just throw a SecurityException.
                if (actualHost != null && (!actualHost.equals(u.getHost()) ||
                                           actualPort != u.getPort()))
                {
                    throw new SecurityException("image moved!");
                }
                actualHost = u.getHost();
                actualPort = u.getPort();
            }
        } catch (IOException e) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2) {
                }
            } else if (c instanceof HttpURLConnection) {
                ((HttpURLConnection)c).disconnect();
            }
            return null;
        }

        ImageDecoder id = decoderForType(is, type);
        if (id == null) {
            id = getDecoder(is);
        }

        if (id == null) {
            // probably, no appropriate decoder
            if  (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            } else if (c instanceof HttpURLConnection) {
                ((HttpURLConnection)c).disconnect();
            }
        }
        return id;
    }
}
