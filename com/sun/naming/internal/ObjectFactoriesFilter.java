package com.sun.naming.internal;

import javax.naming.Reference;

import sun.misc.ObjectInputFilter;
import sun.misc.ObjectInputFilter.FilterInfo;
import sun.misc.ObjectInputFilter.Status;

import sun.security.util.SecurityProperties;

/**
 * This class implements the filter that validates object factories classes instantiated
 * during {@link Reference} lookups.
 * There is one system-wide filter instance per VM that can be set via
 * the {@code "jdk.jndi.object.factoriesFilter"} system property value, or via
 * setting the property in the security properties file. The system property value supersedes
 * the security property value. If none of the properties are specified the default
 * "*" value is used.
 * The filter is implemented as {@link ObjectInputFilter} with capabilities limited to the
 * validation of a factory's class types only ({@linkplain FilterInfo#serialClass()}).
 * Array length, number of object references, depth, and stream size filtering capabilities are
 * not supported by the filter.
 */
public final class ObjectFactoriesFilter {

    /**
     * Checks if serial filter configured with {@code "jdk.jndi.object.factoriesFilter"}
     * system property value allows instantiation of the specified objects factory class.
     * If the filter result is not {@linkplain Status#REJECTED REJECTED}, the filter will
     * allow the instantiation of objects factory class.
     *
     * @param factoryClass objects factory class
     * @return true - if the factory is allowed to be instantiated; false - otherwise
     */
    public static boolean canInstantiateObjectsFactory(Class<?> factoryClass) {
        return checkInput(() -> factoryClass);
    }

    private static boolean checkInput(FactoryInfo factoryInfo) {
        Status result = GLOBAL.checkInput(factoryInfo);
        return result != Status.REJECTED;
    }

    // FilterInfo to check if objects factory class is allowed by the system-wide
    // filter. Array length, number of object references, depth, and stream size
    // capabilities are ignored.
    @FunctionalInterface
    private interface FactoryInfo extends FilterInfo {
        @Override
        default long arrayLength() {
            return -1;
        }

        @Override
        default long depth() {
            return 1;
        }

        @Override
        default long references() {
            return 0;
        }

        @Override
        default long streamBytes() {
            return 0;
        }
    }

    // Prevent instantiation of the factories filter class
     private ObjectFactoriesFilter() {
         throw new InternalError("Not instantiable");
     }

    // System property name that contains the patterns to filter object factory names
    private static final String FACTORIES_FILTER_PROPNAME = "jdk.jndi.object.factoriesFilter";

    // Default system property value that allows the load of any object factory classes
    private static final String DEFAULT_SP_VALUE = "*";

    // System wide object factories filter constructed from the system property
    private static final ObjectInputFilter GLOBAL =
            ObjectInputFilter.Config.createFilter(getFilterPropertyValue());

    // Get security or system property value
    private static String getFilterPropertyValue() {
        String propVal = SecurityProperties.privilegedGetOverridable(FACTORIES_FILTER_PROPNAME);
        return propVal != null ? propVal : DEFAULT_SP_VALUE;
    }
}
