package sun.misc;

import java.security.PermissionCollection;
import java.security.ProtectionDomain;

public interface JavaSecurityProtectionDomainAccess {
    interface ProtectionDomainCache {
        void put(ProtectionDomain pd, PermissionCollection pc);
        PermissionCollection get(ProtectionDomain pd);
    }
    /**
     * Returns the ProtectionDomainCache.
     */
    ProtectionDomainCache getProtectionDomainCache();

    /**
     * Returns the staticPermissions field of the specified object
     */
    boolean getStaticPermissionsField(ProtectionDomain pd);
}
