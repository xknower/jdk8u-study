package com.sun.org.apache.xml.internal.security.keys.storage;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;


public class StorageResolverException extends XMLSecurityException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor StorageResolverException
     *
     */
    public StorageResolverException() {
        super();
    }

    public StorageResolverException(Exception ex) {
        super(ex);
    }

    /**
     * Constructor StorageResolverException
     *
     * @param msgID
     */
    public StorageResolverException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor StorageResolverException
     *
     * @param msgID
     * @param exArgs
     */
    public StorageResolverException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor StorageResolverException
     *
     * @param originalException
     * @param msgID
     */
    public StorageResolverException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public StorageResolverException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor StorageResolverException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public StorageResolverException(Exception originalException, String msgID, Object exArgs[]) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public StorageResolverException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }
}
