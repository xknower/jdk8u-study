package sun.security.krb5.internal.rcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.ReplayCache;

/**
 * This class stores replay caches. AuthTimeWithHash objects are categorized
 * into AuthLists keyed by the names of client and server.
 *
 * @author Yanni Zhang
 */
public class MemoryCache extends ReplayCache {

    // TODO: One day we'll need to read dynamic krb5.conf.
    private static final int lifespan = KerberosTime.getDefaultSkew();
    private static final boolean DEBUG = sun.security.krb5.internal.Krb5.DEBUG;

    private final Map<String,AuthList> content = new ConcurrentHashMap<>();

    @Override
    public synchronized void checkAndStore(KerberosTime currTime, AuthTimeWithHash time)
            throws KrbApErrException {
        String key = time.client + "|" + time.server;
        content.computeIfAbsent(key, k -> new AuthList(lifespan))
                .put(time, currTime);
        if (DEBUG) {
            System.out.println("MemoryCache: add " + time + " to " + key);
        }
        // TODO: clean up AuthList entries with only expired AuthTimeWithHash objects.
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (AuthList rc: content.values()) {
            sb.append(rc.toString());
        }
        return sb.toString();
    }
}
