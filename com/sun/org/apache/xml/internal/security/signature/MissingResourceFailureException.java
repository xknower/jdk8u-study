package com.sun.org.apache.xml.internal.security.signature;

/**
 * Thrown by {@link com.sun.org.apache.xml.internal.security.signature.SignedInfo#verify()} when
 * testing the signature fails because of uninitialized
 * {@link com.sun.org.apache.xml.internal.security.signature.Reference}s.
 *
 * @see ReferenceNotInitializedException
 */
public class MissingResourceFailureException extends XMLSignatureException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** Field uninitializedReference */
    private Reference uninitializedReference;

    /**
     * MissingKeyResourceFailureException constructor.
     * @param reference
     * @param msgID
     * @see #getReference
     */
    public MissingResourceFailureException(Reference reference, String msgID) {
        super(msgID);

        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(String msgID, Reference reference) {
        this(reference, msgID);
    }

    /**
     * Constructor MissingResourceFailureException
     *
     * @param reference
     * @param msgID
     * @param exArgs
     * @see #getReference
     */
    public MissingResourceFailureException(Reference reference, String msgID, Object exArgs[]) {
        super(msgID, exArgs);

        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(String msgID, Object exArgs[], Reference reference) {
        this(reference, msgID, exArgs);
    }

    /**
     * Constructor MissingResourceFailureException
     *
     * @param originalException
     * @param reference
     * @param msgID
     * @see #getReference
     */
    public MissingResourceFailureException(
        Exception originalException, Reference reference, String msgID
    ) {
        super(originalException, msgID);

        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(
        String msgID, Exception originalException, Reference reference
    ) {
        this(originalException, reference, msgID);
    }

    /**
     * Constructor MissingResourceFailureException
     *
     * @param originalException
     * @param reference
     * @param msgID
     * @param exArgs
     * @see #getReference
     */
    public MissingResourceFailureException(
        Exception originalException, Reference reference, String msgID, Object exArgs[]
    ) {
        super(originalException, msgID, exArgs);

        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(
        String msgID, Object exArgs[], Exception originalException, Reference reference
    ) {
        this(originalException, reference, msgID, exArgs);
    }

    /**
     * used to set the uninitialized {@link com.sun.org.apache.xml.internal.security.signature.Reference}
     *
     * @param reference the Reference object
     * @see #getReference
     */
    public void setReference(Reference reference) {
        this.uninitializedReference = reference;
    }

    /**
     * used to get the uninitialized {@link com.sun.org.apache.xml.internal.security.signature.Reference}
     *
     * This allows to supply the correct {@link com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput}
     * to the {@link com.sun.org.apache.xml.internal.security.signature.Reference} to try again verification.
     *
     * @return the Reference object
     * @see #setReference
     */
    public Reference getReference() {
        return this.uninitializedReference;
    }
}
