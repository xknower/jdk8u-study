package com.sun.org.apache.xml.internal.security.exceptions;

import java.text.MessageFormat;

import com.sun.org.apache.xml.internal.security.utils.I18n;

/**
 * The mother of all Exceptions in this bundle. It allows exceptions to have
 * their messages translated to the different locales.
 *
 * The {@code xmlsecurity_en.properties} file contains this line:
 * <pre>
 * xml.WrongElement = Can't create a {0} from a {1} element
 * </pre>
 *
 * Usage in the Java source is:
 * <pre>
 * {
 *    Object exArgs[] = { Constants._TAG_TRANSFORMS, "BadElement" };
 *
 *    throw new XMLSecurityException("xml.WrongElement", exArgs);
 * }
 * </pre>
 *
 * Additionally, if another Exception has been caught, we can supply it, too
 * <pre>
 * try {
 *    ...
 * } catch (Exception oldEx) {
 *    Object exArgs[] = { Constants._TAG_TRANSFORMS, "BadElement" };
 *
 *    throw new XMLSecurityException("xml.WrongElement", exArgs, oldEx);
 * }
 * </pre>
 *
 *
 */
public class XMLSecurityException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** Field msgID */
    protected String msgID;

    /**
     * Constructor XMLSecurityException
     *
     */
    public XMLSecurityException() {
        super("Missing message string");

        this.msgID = null;
    }

    /**
     * Constructor XMLSecurityException
     *
     * @param msgID
     */
    public XMLSecurityException(String msgID) {
        super(I18n.getExceptionMessage(msgID));

        this.msgID = msgID;
    }

    /**
     * Constructor XMLSecurityException
     *
     * @param msgID
     * @param exArgs
     */
    public XMLSecurityException(String msgID, Object[] exArgs) {

        super(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs));

        this.msgID = msgID;
    }

    /**
     * Constructor XMLSecurityException
     *
     * @param originalException
     */
    public XMLSecurityException(Exception originalException) {

        super(originalException.getMessage(), originalException);
    }

    /**
     * Constructor XMLSecurityException
     *
     * @param msgID
     * @param originalException
     */
    public XMLSecurityException(Exception originalException, String msgID) {
        super(I18n.getExceptionMessage(msgID, originalException), originalException);

        this.msgID = msgID;
    }

    @Deprecated
    public XMLSecurityException(String msgID, Exception originalException) {
        this(originalException, msgID);
    }

    /**
     * Constructor XMLSecurityException
     *
     * @param msgID
     * @param exArgs
     * @param originalException
     */
    public XMLSecurityException(Exception originalException, String msgID, Object[] exArgs) {
        super(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs), originalException);

        this.msgID = msgID;
    }

    @Deprecated
    public XMLSecurityException(String msgID, Object[] exArgs, Exception originalException) {
        this(originalException, msgID, exArgs);
    }


    /**
     * Method getMsgID
     *
     * @return the messageId
     */
    public String getMsgID() {
        if (msgID == null) {
            return "Missing message ID";
        }
        return msgID;
    }

    /** {@inheritDoc} */
    public String toString() {
        String s = this.getClass().getName();
        String message = super.getLocalizedMessage();

        if (message != null) {
            message = s + ": " + message;
        } else {
            message = s;
        }

        if (super.getCause() != null) {
            message = message + "\nOriginal Exception was " + super.getCause().toString();
        }

        return message;
    }

    /**
     * Method printStackTrace
     *
     */
    public void printStackTrace() {
        synchronized (System.err) {
            super.printStackTrace(System.err);
        }
    }

    /**
     * Method getOriginalException
     *
     * @return the original exception
     */
    public Exception getOriginalException() {
        if (this.getCause() instanceof Exception) {
            return (Exception)this.getCause();
        }
        return null;
    }
}
