package jdk.internal.util;

/**
 * System Property access for internal use only.
 * Read-only access to System property values initialized during Phase 1
 * are cached.  Setting, clearing, or modifying the value using
 * {@link System#setProperty) or {@link System#getProperties()} is ignored.
 * <strong>{@link SecurityManager#checkPropertyAccess} is NOT checked
 * in these access methods. The caller of these methods should take care to ensure
 * that the returned property is not made accessible to untrusted code.</strong>
 */
public final class StaticProperty {

    // The class static initialization is triggered to initialize these final
    // fields during init Phase 1 and before a security manager is set.
    private static final String JDK_SERIAL_FILTER = System.getProperty("jdk.serialFilter");

    private StaticProperty() {}

    /**
     *
     * Return the {@code jdk.serialFilter} system property.
     *
     * <strong>{@link SecurityManager#checkPropertyAccess} is NOT checked
     * in this method. The caller of this method should take care to ensure
     * that the returned property is not made accessible to untrusted code.</strong>
     *
     * @return the {@code user.name} system property
     */
    public static String jdkSerialFilter() {
        return JDK_SERIAL_FILTER;
    }
}
