package com.sun.org.apache.xml.internal.security.signature;

/**
 * Raised if verifying a {@link com.sun.org.apache.xml.internal.security.signature.Reference} fails
 * because of an uninitialized {@link com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput}
 *
 */
public class ReferenceNotInitializedException extends XMLSignatureException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor ReferenceNotInitializedException
     *
     */
    public ReferenceNotInitializedException() {
        super();
    }

    public ReferenceNotInitializedException(Exception ex) {
        super(ex);
    }

    /**
     * Constructor ReferenceNotInitializedException
     *
     * @param msgID
     */
    public ReferenceNotInitializedException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor ReferenceNotInitializedException
     *
     * @param msgID
     * @param exArgs
     */
    public ReferenceNotInitializedException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor ReferenceNotInitializedException
     *
     * @param originalException
     * @param msgID
     */
    public ReferenceNotInitializedException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public ReferenceNotInitializedException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor ReferenceNotInitializedException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public ReferenceNotInitializedException(Exception originalException, String msgID, Object exArgs[]) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public ReferenceNotInitializedException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }
}
