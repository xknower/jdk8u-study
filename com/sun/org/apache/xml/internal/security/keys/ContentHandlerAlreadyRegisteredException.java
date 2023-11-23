package com.sun.org.apache.xml.internal.security.keys;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

public class ContentHandlerAlreadyRegisteredException extends XMLSecurityException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor ContentHandlerAlreadyRegisteredException
     *
     */
    public ContentHandlerAlreadyRegisteredException() {
        super();
    }

    /**
     * Constructor ContentHandlerAlreadyRegisteredException
     *
     * @param msgID
     */
    public ContentHandlerAlreadyRegisteredException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor ContentHandlerAlreadyRegisteredException
     *
     * @param msgID
     * @param exArgs
     */
    public ContentHandlerAlreadyRegisteredException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor ContentHandlerAlreadyRegisteredException
     *
     * @param originalException
     * @param msgID
     */
    public ContentHandlerAlreadyRegisteredException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public ContentHandlerAlreadyRegisteredException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor ContentHandlerAlreadyRegisteredException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public ContentHandlerAlreadyRegisteredException(
        Exception originalException, String msgID, Object exArgs[]
    ) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public ContentHandlerAlreadyRegisteredException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }

}
