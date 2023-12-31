package com.sun.org.apache.xml.internal.security.utils;

import java.io.ByteArrayOutputStream;

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;

/**
 *
 */
public class SignerOutputStream extends ByteArrayOutputStream {
    private static final com.sun.org.slf4j.internal.Logger LOG =
        com.sun.org.slf4j.internal.LoggerFactory.getLogger(SignerOutputStream.class);

    final SignatureAlgorithm sa;

    /**
     * @param sa
     */
    public SignerOutputStream(SignatureAlgorithm sa) {
        this.sa = sa;
    }

    /** {@inheritDoc} */
    public void write(byte[] arg0)  {
        try {
            sa.update(arg0);
        } catch (XMLSignatureException e) {
            throw new RuntimeException("" + e);
        }
    }

    /** {@inheritDoc} */
    public void write(int arg0) {
        try {
            sa.update((byte)arg0);
        } catch (XMLSignatureException e) {
            throw new RuntimeException("" + e);
        }
    }

    /** {@inheritDoc} */
    public void write(byte[] arg0, int arg1, int arg2) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Canonicalized SignedInfo:");
            StringBuilder sb = new StringBuilder(arg2);
            for (int i = arg1; i < (arg1 + arg2); i++) {
                sb.append((char)arg0[i]);
            }
            LOG.debug(sb.toString());
        }
        try {
            sa.update(arg0, arg1, arg2);
        } catch (XMLSignatureException e) {
            throw new RuntimeException("" + e);
        }
    }
}
