package com.sun.org.apache.xml.internal.security.signature;

/**
 * Raised if testing the signature value over <i>DigestValue</i> fails because of invalid signature.
 *
 * @see InvalidDigestValueException  MissingKeyFailureException  MissingResourceFailureException
 */
public class InvalidSignatureValueException extends XMLSignatureException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor InvalidSignatureValueException
     *
     */
    public InvalidSignatureValueException() {
        super();
    }

    /**
     * Constructor InvalidSignatureValueException
     *
     * @param msgID
     */
    public InvalidSignatureValueException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor InvalidSignatureValueException
     *
     * @param msgID
     * @param exArgs
     */
    public InvalidSignatureValueException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor InvalidSignatureValueException
     *
     * @param originalException
     * @param msgID
     */
    public InvalidSignatureValueException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public InvalidSignatureValueException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor InvalidSignatureValueException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public InvalidSignatureValueException(Exception originalException, String msgID, Object exArgs[]) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public InvalidSignatureValueException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }
}
