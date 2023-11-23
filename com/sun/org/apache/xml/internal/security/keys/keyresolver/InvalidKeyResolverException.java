package com.sun.org.apache.xml.internal.security.keys.keyresolver;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

public class InvalidKeyResolverException extends XMLSecurityException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor InvalidKeyResolverException
     *
     */
    public InvalidKeyResolverException() {
        super();
    }

    /**
     * Constructor InvalidKeyResolverException
     *
     * @param msgID
     */
    public InvalidKeyResolverException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor InvalidKeyResolverException
     *
     * @param msgID
     * @param exArgs
     */
    public InvalidKeyResolverException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor InvalidKeyResolverException
     *
     * @param originalException
     * @param msgID
     */
    public InvalidKeyResolverException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public InvalidKeyResolverException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor InvalidKeyResolverException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public InvalidKeyResolverException(Exception originalException, String msgID, Object exArgs[]) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public InvalidKeyResolverException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }
}
