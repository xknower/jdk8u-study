package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/**
 * All XML Signature related exceptions inherit herefrom.
 *
 * @see MissingResourceFailureException InvalidDigestValueException InvalidSignatureValueException
 */
public class XMLSignatureException extends XMLSecurityException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor XMLSignatureException
     *
     */
    public XMLSignatureException() {
        super();
    }

    public XMLSignatureException(Exception ex) {
        super(ex);
    }

    /**
     * Constructor XMLSignatureException
     *
     * @param msgID
     */
    public XMLSignatureException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor XMLSignatureException
     *
     * @param msgID
     * @param exArgs
     */
    public XMLSignatureException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor XMLSignatureException
     *
     * @param originalException
     * @param msgID
     */
    public XMLSignatureException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public XMLSignatureException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor XMLSignatureException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public XMLSignatureException(Exception originalException, String msgID, Object exArgs[]) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public XMLSignatureException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }
}
