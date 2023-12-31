package sun.security.jgss.krb5;

import sun.security.krb5.JavaxSecurityAuthKerberosAccess;
import sun.security.krb5.KerberosSecrets;

import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.Subject;
import javax.security.auth.DestroyFailedException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KeyTab;

/**
 * This utility looks through the current Subject and retrieves private
 * credentials for the desired client/server principals.
 *
 * @author Ram Marti
 * @since 1.4.2
 */

class SubjectComber {

    private static final boolean DEBUG = Krb5Util.DEBUG;

    /**
     * Default constructor
     */
    private SubjectComber() {  // Cannot create one of these
    }

    static <T> T find(Subject subject, String serverPrincipal,
        String clientPrincipal, Class<T> credClass) {

        // findAux returns T if oneOnly.
        return credClass.cast(findAux(subject, serverPrincipal,
                                      clientPrincipal, credClass, true));
    }

    @SuppressWarnings("unchecked") // findAux returns List<T> if !oneOnly.
    static <T> List<T> findMany(Subject subject, String serverPrincipal,
        String clientPrincipal, Class<T> credClass) {

        return (List<T>)findAux(subject, serverPrincipal, clientPrincipal,
            credClass, false);
    }

    /**
     * Find private credentials for the specified client/server principals
     * in the subject. Returns null if the subject is null.
     *
     * @return the private credentials
     */
    // Returns T if oneOnly and List<T> if !oneOnly.
    private static <T> Object findAux(Subject subject, String serverPrincipal,
        String clientPrincipal, Class<T> credClass, boolean oneOnly) {

        if (subject == null) {
            return null;
        } else {
            List<T> answer = (oneOnly ? null : new ArrayList<T>());

            if (credClass == KeyTab.class) {
                Iterator<KeyTab> iterator =
                    subject.getPrivateCredentials(KeyTab.class).iterator();
                while (iterator.hasNext()) {
                    KeyTab t = iterator.next();
                    if (serverPrincipal != null && t.isBound()) {
                        KerberosPrincipal name = t.getPrincipal();
                        if (name != null) {
                            if (!serverPrincipal.equals(name.getName())) {
                                continue;
                            }
                        } else {
                            // legacy bound keytab. although we don't know who
                            // the bound principal is, it must be in allPrincs
                            boolean found = false;
                            for (KerberosPrincipal princ:
                                    subject.getPrincipals(KerberosPrincipal.class)) {
                                if (princ.getName().equals(serverPrincipal)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) continue;
                        }
                    }
                    // Check passed, we can add now
                    if (DEBUG) {
                        System.out.println("Found " + credClass.getSimpleName()
                                + " " + t);
                    }
                    if (oneOnly) {
                        return t;
                    } else {
                        answer.add(credClass.cast(t));
                    }
                }
            } else if (credClass == KerberosKey.class) {
                // We are looking for credentials for the serverPrincipal
                Iterator<KerberosKey> iterator =
                    subject.getPrivateCredentials(KerberosKey.class).iterator();
                while (iterator.hasNext()) {
                    KerberosKey t = iterator.next();
                    String name = t.getPrincipal().getName();
                    if (serverPrincipal == null || serverPrincipal.equals(name)) {
                         if (DEBUG) {
                             System.out.println("Found " +
                                     credClass.getSimpleName() + " for " + name);
                         }
                         if (oneOnly) {
                             return t;
                         } else {
                             answer.add(credClass.cast(t));
                         }
                    }
                }
            } else if (credClass == KerberosTicket.class) {
                // we are looking for a KerberosTicket credentials
                // for client-service principal pair
                Set<Object> pcs = subject.getPrivateCredentials();
                synchronized (pcs) {
                    Iterator<Object> iterator = pcs.iterator();
                    while (iterator.hasNext()) {
                        Object obj = iterator.next();
                        if (!(obj instanceof KerberosTicket)) {
                            continue;
                        }
                        @SuppressWarnings("unchecked")
                        KerberosTicket ticket = (KerberosTicket)obj;
                        if (DEBUG) {
                            System.out.println("Found ticket for "
                                                + ticket.getClient()
                                                + " to go to "
                                                + ticket.getServer()
                                                + " expiring on "
                                                + ticket.getEndTime());
                        }
                        if (!ticket.isCurrent()) {
                            // let us remove the ticket from the Subject
                            // Note that both TGT and service ticket will be
                            // removed  upon expiration
                            if (!subject.isReadOnly()) {
                                iterator.remove();
                                try {
                                    ticket.destroy();
                                    if (DEBUG) {
                                        System.out.println("Removed and destroyed "
                                                    + "the expired Ticket \n"
                                                    + ticket);

                                    }
                                } catch (DestroyFailedException dfe) {
                                    if (DEBUG) {
                                        System.out.println("Expired ticket not" +
                                                " detroyed successfully. " + dfe);
                                    }
                                }
                            }
                            continue;
                        }
                        String serverMatch = findServerMatch(serverPrincipal, ticket);
                        if (serverMatch != null) {
                            String clientMatch = findClientMatch(clientPrincipal, ticket);
                            if (clientMatch != null) {
                                if (oneOnly) {
                                    return ticket;
                                } else {
                                    // Record names so that tickets will
                                    // all belong to same principals
                                    if (clientPrincipal == null) {
                                        clientPrincipal = clientMatch;
                                    }
                                    if (serverPrincipal == null) {
                                        serverPrincipal = serverMatch;
                                    }
                                    answer.add(credClass.cast(ticket));
                                }
                            }
                        }
                    }
                }
            }
            return answer;
        }
    }

    private static String findServerMatch(String input, KerberosTicket ticket) {
        KerberosPrincipal serverAlias = KerberosSecrets
                .getJavaxSecurityAuthKerberosAccess()
                .kerberosTicketGetServerAlias(ticket);
        if (input != null) {
            return ((serverAlias != null && input.equals(serverAlias.getName())) ||
                    input.equals(ticket.getServer().getName()))
                    ? input : null;
        } else {
            return serverAlias != null
                    ? serverAlias.getName()
                    : ticket.getServer().getName();
        }
    }

    private static String findClientMatch(String input, KerberosTicket ticket) {
        JavaxSecurityAuthKerberosAccess access = KerberosSecrets
                .getJavaxSecurityAuthKerberosAccess();
        KerberosPrincipal clientAlias = access.kerberosTicketGetClientAlias(ticket);
        KerberosTicket proxy = access.kerberosTicketGetProxy(ticket);
        if (input != null) {
            return ((clientAlias != null && input.equals(clientAlias.getName())) ||
                    (proxy != null && input.equals(proxy.getClient().getName())) ||
                    (proxy == null && input.equals(ticket.getClient().getName())))
                    ? input : null;
        } else {
            if (clientAlias != null) {
                return clientAlias.getName();
            } else if (proxy != null) {
                return proxy.getClient().getName();
            } else {
                return ticket.getClient().getName();
            }
        }
    }
}
