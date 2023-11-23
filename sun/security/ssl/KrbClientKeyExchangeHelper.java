package sun.security.ssl;

import java.io.IOException;
import java.security.AccessControlContext;
import java.security.Principal;

/**
 * Helper interface for KrbClientKeyExchange SSL classes to access
 * the Kerberos implementation without static-linking. This enables
 * Java SE Embedded 8 compilation using 'compact1' profile -where
 * SSL classes are available but Kerberos are not-.
 */
public interface KrbClientKeyExchangeHelper {

    void init(byte[] preMaster, String serverName,
            AccessControlContext acc) throws IOException;

    void init(byte[] encodedTicket, byte[] preMasterEnc,
            Object serviceCreds, AccessControlContext acc)
                    throws IOException;

    byte[] getEncodedTicket();

    byte[] getEncryptedPreMasterSecret();

    byte[] getPlainPreMasterSecret();

    Principal getPeerPrincipal();

    Principal getLocalPrincipal();
}
