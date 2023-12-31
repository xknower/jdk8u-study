package sun.security.krb5.internal;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import sun.security.krb5.Credentials;
import sun.security.krb5.PrincipalName;

/*
 * ReferralsCache class implements a cache scheme for referral TGTs as
 * described in RFC 6806 - 10. Caching Information. The goal is to optimize
 * resources (such as network traffic) when a client requests credentials for a
 * service principal to a given KDC. If a referral TGT was previously received,
 * cached information is used instead of issuing a new query. Once a referral
 * TGT expires, the corresponding referral entry in the cache is removed.
 */
final class ReferralsCache {

    private static Map<ReferralCacheKey, Map<String, ReferralCacheEntry>>
            referralsMap = new HashMap<>();

    static private final class ReferralCacheKey {
        private PrincipalName cname;
        private PrincipalName sname;
        private PrincipalName user; // S4U2Self only
        private byte[] userSvcTicketEnc; // S4U2Proxy only
        ReferralCacheKey (PrincipalName cname, PrincipalName sname,
                PrincipalName user, Ticket userSvcTicket) {
            this.cname = cname;
            this.sname = sname;
            this.user = user;
            if (userSvcTicket != null && userSvcTicket.encPart != null) {
                byte[] userSvcTicketEnc = userSvcTicket.encPart.getBytes();
                if (userSvcTicketEnc.length > 0) {
                    this.userSvcTicketEnc = userSvcTicketEnc;
                }
            }
        }
        public boolean equals(Object other) {
            if (!(other instanceof ReferralCacheKey))
                return false;
            ReferralCacheKey that = (ReferralCacheKey)other;
            return cname.equals(that.cname) &&
                    sname.equals(that.sname) &&
                    Objects.equals(user, that.user) &&
                    Arrays.equals(userSvcTicketEnc, that.userSvcTicketEnc);
        }
        public int hashCode() {
            return cname.hashCode() + sname.hashCode() +
                    Objects.hashCode(user) +
                    Arrays.hashCode(userSvcTicketEnc);
        }
    }

    static final class ReferralCacheEntry {
        private final Credentials creds;
        private final String toRealm;
        ReferralCacheEntry(Credentials creds, String toRealm) {
            this.creds = creds;
            this.toRealm = toRealm;
        }
        Credentials getCreds() {
            return creds;
        }
        String getToRealm() {
            return toRealm;
        }
    }

    /*
     * Add a new referral entry to the cache, including: client principal,
     * service principal, user principal (S4U2Self only), client service
     * ticket (S4U2Proxy only), source KDC realm, destination KDC realm and
     * referral TGT.
     *
     * If a loop is generated when adding the new referral, the first hop is
     * automatically removed. For example, let's assume that adding a
     * REALM-3.COM -> REALM-1.COM referral generates the following loop:
     * REALM-1.COM -> REALM-2.COM -> REALM-3.COM -> REALM-1.COM. Then,
     * REALM-1.COM -> REALM-2.COM referral entry is removed from the cache.
     */
    static synchronized void put(PrincipalName cname, PrincipalName service,
            PrincipalName user, Ticket[] userSvcTickets, String fromRealm,
            String toRealm, Credentials creds) {
        Ticket userSvcTicket = (userSvcTickets != null ?
                userSvcTickets[0] : null);
        ReferralCacheKey k = new ReferralCacheKey(cname, service,
                user, userSvcTicket);
        pruneExpired(k);
        if (creds.getEndTime().before(new Date())) {
            return;
        }
        Map<String, ReferralCacheEntry> entries = referralsMap.get(k);
        if (entries == null) {
            entries = new HashMap<String, ReferralCacheEntry>();
            referralsMap.put(k, entries);
        }
        entries.remove(fromRealm);
        ReferralCacheEntry newEntry = new ReferralCacheEntry(creds, toRealm);
        entries.put(fromRealm, newEntry);

        // Remove loops within the cache
        ReferralCacheEntry current = newEntry;
        List<ReferralCacheEntry> seen = new LinkedList<>();
        while (current != null) {
            if (seen.contains(current)) {
                // Loop found. Remove the first referral to cut the loop.
                entries.remove(newEntry.getToRealm());
                break;
            }
            seen.add(current);
            current = entries.get(current.getToRealm());
        }
    }

    /*
     * Obtain a referral entry from the cache given a client principal,
     * a service principal, a user principal (S4U2Self only), a client
     * service ticket (S4U2Proxy only) and a source KDC realm.
     */
    static synchronized ReferralCacheEntry get(PrincipalName cname,
            PrincipalName service, PrincipalName user,
            Ticket[] userSvcTickets, String fromRealm) {
        Ticket userSvcTicket = (userSvcTickets != null ?
                userSvcTickets[0] : null);
        ReferralCacheKey k = new ReferralCacheKey(cname, service,
                user, userSvcTicket);
        pruneExpired(k);
        Map<String, ReferralCacheEntry> entries = referralsMap.get(k);
        if (entries != null) {
            ReferralCacheEntry toRef = entries.get(fromRealm);
            if (toRef != null) {
                return toRef;
            }
        }
        return null;
    }

    /*
     * Remove referral entries from the cache when referral TGTs expire.
     */
    private static void pruneExpired(ReferralCacheKey k) {
        Date now = new Date();
        Map<String, ReferralCacheEntry> entries = referralsMap.get(k);
        if (entries != null) {
            for (Entry<String, ReferralCacheEntry> mapEntry :
                    entries.entrySet()) {
                if (mapEntry.getValue().getCreds().getEndTime().before(now)) {
                    entries.remove(mapEntry.getKey());
                }
            }
        }
    }
}
