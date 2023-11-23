package com.sun.org.apache.xml.internal.security.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This {@link org.xml.sax.ErrorHandler} does absolutely nothing but LOG
 * the events.
 *
 */
public class IgnoreAllErrorHandler implements ErrorHandler {

    private static final com.sun.org.slf4j.internal.Logger LOG =
        com.sun.org.slf4j.internal.LoggerFactory.getLogger(IgnoreAllErrorHandler.class);

    /** Field throwExceptions */
    private static final boolean warnOnExceptions =
        getProperty("com.sun.org.apache.xml.internal.security.test.warn.on.exceptions");

    /** Field throwExceptions           */
    private static final boolean throwExceptions =
        getProperty("com.sun.org.apache.xml.internal.security.test.throw.exceptions");

    private static boolean getProperty(final String name) {
        return java.security.AccessController.doPrivileged(
            (java.security.PrivilegedAction<Boolean>) () -> Boolean.getBoolean(name));
    }

    /** {@inheritDoc} */
    @Override
    public void warning(SAXParseException ex) throws SAXException {
        if (IgnoreAllErrorHandler.warnOnExceptions) {
            LOG.warn("", ex);
        }
        if (IgnoreAllErrorHandler.throwExceptions) {
            throw ex;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void error(SAXParseException ex) throws SAXException {
        if (IgnoreAllErrorHandler.warnOnExceptions) {
            LOG.error("", ex);
        }
        if (IgnoreAllErrorHandler.throwExceptions) {
            throw ex;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void fatalError(SAXParseException ex) throws SAXException {
        if (IgnoreAllErrorHandler.warnOnExceptions) {
            LOG.warn("", ex);
        }
        if (IgnoreAllErrorHandler.throwExceptions) {
            throw ex;
        }
    }
}
