package com.sun.org.apache.xml.internal.security.transforms;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/**
 *
 */
public class InvalidTransformException extends XMLSecurityException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor InvalidTransformException
     *
     */
    public InvalidTransformException() {
        super();
    }

    /**
     * Constructor InvalidTransformException
     *
     * @param msgId
     */
    public InvalidTransformException(String msgId) {
        super(msgId);
    }

    /**
     * Constructor InvalidTransformException
     *
     * @param msgId
     * @param exArgs
     */
    public InvalidTransformException(String msgId, Object exArgs[]) {
        super(msgId, exArgs);
    }

    /**
     * Constructor InvalidTransformException
     *
     * @param msgId
     * @param originalException
     */
    public InvalidTransformException(Exception originalException, String msgId) {
        super(originalException, msgId);
    }

    @Deprecated
    public InvalidTransformException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor InvalidTransformException
     *
     * @param msgId
     * @param exArgs
     * @param originalException
     */
    public InvalidTransformException(Exception originalException, String msgId, Object exArgs[]) {
        super(originalException, msgId, exArgs);
    }

    @Deprecated
    public InvalidTransformException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }
}
