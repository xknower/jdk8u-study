package com.sun.org.apache.xml.internal.security.c14n;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/**
 * Class CanonicalizationException
 *
 */
public class CanonicalizationException extends XMLSecurityException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor CanonicalizationException
     *
     */
    public CanonicalizationException() {
        super();
    }

    public CanonicalizationException(Exception ex) {
        super(ex);
    }

    /**
     * Constructor CanonicalizationException
     *
     * @param msgID
     */
    public CanonicalizationException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor CanonicalizationException
     *
     * @param msgID
     * @param exArgs
     */
    public CanonicalizationException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor CanonicalizationException
     *
     * @param originalException
     * @param msgID
     */
    public CanonicalizationException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public CanonicalizationException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor CanonicalizationException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public CanonicalizationException(
        Exception originalException, String msgID, Object exArgs[]
    ) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public CanonicalizationException(String msgID, Object exArgs[], Exception originalException) {
        this(originalException, msgID, exArgs);
    }
}
