package com.sun.org.apache.xml.internal.security.exceptions;

public class AlgorithmAlreadyRegisteredException extends XMLSecurityException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor AlgorithmAlreadyRegisteredException
     *
     */
    public AlgorithmAlreadyRegisteredException() {
        super();
    }

    /**
     * Constructor AlgorithmAlreadyRegisteredException
     *
     * @param msgID
     */
    public AlgorithmAlreadyRegisteredException(String msgID) {
        super(msgID);
    }

    /**
     * Constructor AlgorithmAlreadyRegisteredException
     *
     * @param msgID
     * @param exArgs
     */
    public AlgorithmAlreadyRegisteredException(String msgID, Object exArgs[]) {
        super(msgID, exArgs);
    }

    /**
     * Constructor AlgorithmAlreadyRegisteredException
     *
     * @param originalException
     * @param msgID
     */
    public AlgorithmAlreadyRegisteredException(Exception originalException, String msgID) {
        super(originalException, msgID);
    }

    @Deprecated
    public AlgorithmAlreadyRegisteredException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor AlgorithmAlreadyRegisteredException
     *
     * @param originalException
     * @param msgID
     * @param exArgs
     */
    public AlgorithmAlreadyRegisteredException(
        Exception originalException, String msgID, Object exArgs[]
    ) {
        super(originalException, msgID, exArgs);
    }

    @Deprecated
    public AlgorithmAlreadyRegisteredException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }

}
