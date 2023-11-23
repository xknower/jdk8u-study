package sun.management;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.lang.management.PlatformManagedObject;
import java.lang.reflect.Method;

/**
 * Class to allow for an extended set of platform MXBeans
 */
public final class ExtendedPlatformComponent {
    private ExtendedPlatformComponent() {} // Don't create any instances

    /**
     * Get the extended set of platform MXBeans that should be registered in the
     * platform MBeanServer, or an empty list if there are no such MXBeans.
     */
    public static List<? extends PlatformManagedObject> getMXBeans() {
        PlatformManagedObject o = getFlightRecorderBean();
        if (o != null) {
            return Collections.singletonList(o);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Returns the extended platform MXBean implementing the given
     * mxbeanInterface, or null if there is no such MXBean.
     */
    public static <T extends PlatformManagedObject>
            T getMXBean(Class<T> mxbeanInterface) {

        if ("jdk.management.jfr.FlightRecorderMXBean".equals(mxbeanInterface.getName())) {
            return (T)getFlightRecorderBean();
        }
        return null;
    }

    private static PlatformManagedObject getFlightRecorderBean() {
        PlatformManagedObject object = null;
        try {
            Class provider = Class.forName("jdk.management.jfr.internal.FlightRecorderMXBeanProvider");
            Method m = provider.getDeclaredMethod("getFlightRecorderMXBean");

            object =  (PlatformManagedObject)m.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // no jfr?
        }
        return object;
    }
}
