package com.sun.jndi.ldap;

import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.SharedSecrets;

final class VersionHelper12 extends VersionHelper {

    // System property to control whether classes may be loaded from an
    // arbitrary URL code base.
    private static final String TRUST_URL_CODEBASE_PROPERTY =
        "com.sun.jndi.ldap.object.trustURLCodebase";

    // System property to control whether classes are allowed to be loaded from
    // 'javaSerializedData' attribute
    private static final String TRUST_SERIAL_DATA_PROPERTY =
        "com.sun.jndi.ldap.object.trustSerialData";

    /**
     * Determines whether objects may be deserialized from the content of
     * 'javaSerializedData' attribute.
     */
    private static final boolean trustSerialData;

    // Determine whether classes may be loaded from an arbitrary URL code base.
    private static final boolean trustURLCodebase;

    static {
        String trust = getPrivilegedProperty(TRUST_URL_CODEBASE_PROPERTY, "false");
        trustURLCodebase = "true".equalsIgnoreCase(trust);
        String trustSDString = getPrivilegedProperty(TRUST_SERIAL_DATA_PROPERTY, "true");
        trustSerialData = "true".equalsIgnoreCase(trustSDString);
    }

    private static String getPrivilegedProperty(String propertyName, String defaultVal) {
        PrivilegedAction<String> action = () -> System.getProperty(propertyName, defaultVal);
        if (System.getSecurityManager() == null) {
            return action.run();
        } else {
            return AccessController.doPrivileged(action);
        }
    }

    VersionHelper12() {} // Disallow external from creating one of these.

    /**
     * Returns true if deserialization of objects from 'javaSerializedData'
     * and 'javaReferenceAddress' LDAP attributes is allowed.
     *
     * @return true if deserialization is allowed; false - otherwise
     */
    public static boolean isSerialDataAllowed() {
        return trustSerialData;
    }

    ClassLoader getURLClassLoader(String[] url)
        throws MalformedURLException {
            ClassLoader parent = getContextClassLoader();
            /*
             * Classes may only be loaded from an arbitrary URL code base when
             * the system property com.sun.jndi.ldap.object.trustURLCodebase
             * has been set to "true".
             */
            if (url != null && trustURLCodebase) {
                return URLClassLoader.newInstance(getUrlArray(url), parent);
            } else {
                return parent;
            }
    }

    Class<?> loadClass(String className) throws ClassNotFoundException {
        ClassLoader cl = getContextClassLoader();
        return Class.forName(className, true, cl);
    }

    private ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged(
            new PrivilegedAction<ClassLoader>() {
                public ClassLoader run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            }
        );
    }

    Thread createThread(final Runnable r) {
        final AccessControlContext acc = AccessController.getContext();
        // 4290486: doPrivileged is needed to create a thread in
        // an environment that restricts "modifyThreadGroup".
        return AccessController.doPrivileged(
                new PrivilegedAction<Thread>() {
                    public Thread run() {
                        return SharedSecrets.getJavaLangAccess()
                                .newThreadWithAcc(r, acc);
                    }
                }
        );
    }
}
