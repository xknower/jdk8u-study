package com.sun.net.ssl;

import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.Principal;
import java.security.cert.X509Certificate;

/**
 * Instances of this interface manage which X509 certificate-based
 * key pairs are used to authenticate the local side of a secure
 * socket. The individual entries are identified by unique alias names.
 *
 * @deprecated As of JDK 1.4, this implementation-specific class was
 *      replaced by {@link javax.net.ssl.X509KeyManager}.
 */
@Deprecated
public interface X509KeyManager extends KeyManager {
    /**
     * Get the matching aliases for authenticating the client side of a secure
     * socket given the public key type and the list of
     * certificate issuer authorities recognized by the peer (if any).
     *
     * @param keyType the key algorithm type name
     * @param issuers the list of acceptable CA issuer subject names
     * @return the matching alias names
     */
    public String[] getClientAliases(String keyType, Principal[] issuers);

    /**
     * Choose an alias to authenticate the client side of a secure
     * socket given the public key type and the list of
     * certificate issuer authorities recognized by the peer (if any).
     *
     * @param keyType the key algorithm type name
     * @param issuers the list of acceptable CA issuer subject names
     * @return the alias name for the desired key
     */
    public String chooseClientAlias(String keyType, Principal[] issuers);

    /**
     * Get the matching aliases for authenticating the server side of a secure
     * socket given the public key type and the list of
     * certificate issuer authorities recognized by the peer (if any).
     *
     * @param keyType the key algorithm type name
     * @param issuers the list of acceptable CA issuer subject names
     * @return the matching alias names
     */
    public String[] getServerAliases(String keyType, Principal[] issuers);

    /**
     * Choose an alias to authenticate the server side of a secure
     * socket given the public key type and the list of
     * certificate issuer authorities recognized by the peer (if any).
     *
     * @param keyType the key algorithm type name
     * @param issuers the list of acceptable CA issuer subject names
     * @return the alias name for the desired key
     */
    public String chooseServerAlias(String keyType, Principal[] issuers);

    /**
     * Returns the certificate chain associated with the given alias.
     *
     * @param alias the alias name
     *
     * @return the certificate chain (ordered with the user's certificate first
     * and the root certificate authority last)
     */
    public X509Certificate[] getCertificateChain(String alias);

    /*
     * Returns the key associated with the given alias.
     *
     * @param alias the alias name
     *
     * @return the requested key
     */
    public PrivateKey getPrivateKey(String alias);
}
