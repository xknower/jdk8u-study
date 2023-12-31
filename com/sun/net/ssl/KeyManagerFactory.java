package com.sun.net.ssl;

import java.security.*;

/**
 * This class acts as a factory for key managers based on a
 * source of key material. Each key manager manages a specific
 * type of key material for use by secure sockets. The key
 * material is based on a KeyStore and/or provider specific sources.
 *
 * @deprecated As of JDK 1.4, this implementation-specific class was
 *      replaced by {@link javax.net.ssl.KeyManagerFactory}.
 */
@Deprecated
public class KeyManagerFactory {
    // The provider
    private Provider provider;

    // The provider implementation (delegate)
    private KeyManagerFactorySpi factorySpi;

    // The name of the key management algorithm.
    private String algorithm;

    /**
     * <p>The default KeyManager can be changed by setting the value of the
     * {@code sun.ssl.keymanager.type} security property to the desired name.
     *
     * @return the default type as specified by the
     * {@code sun.ssl.keymanager.type} security property, or an
     * implementation-specific default if no such property exists.
     *
     * @see java.security.Security security properties
     */
    public final static String getDefaultAlgorithm() {
        String type;
        type = AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return Security.getProperty("sun.ssl.keymanager.type");
            }
        });
        if (type == null) {
            type = "SunX509";
        }
        return type;

    }

    /**
     * Creates a KeyManagerFactory object.
     *
     * @param factorySpi the delegate
     * @param provider the provider
     * @param algorithm the algorithm
     */
    protected KeyManagerFactory(KeyManagerFactorySpi factorySpi,
                                Provider provider, String algorithm) {
        this.factorySpi = factorySpi;
        this.provider = provider;
        this.algorithm = algorithm;
    }

    /**
     * Returns the algorithm name of this <code>KeyManagerFactory</code> object.
     *
     * <p>This is the same name that was specified in one of the
     * <code>getInstance</code> calls that created this
     * <code>KeyManagerFactory</code> object.
     *
     * @return the algorithm name of this <code>KeyManagerFactory</code> object.
     */
    public final String getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Generates a <code>KeyManagerFactory</code> object that implements the
     * specified key management algorithm.
     * If the default provider package provides an implementation of the
     * requested key management algorithm, an instance of
     * <code>KeyManagerFactory</code> containing that implementation is
     * returned.  If the algorithm is not available in the default provider
     * package, other provider packages are searched.
     *
     * @param algorithm the standard name of the requested
     * algorithm.
     *
     * @return the new <code>KeyManagerFactory</code> object
     *
     * @exception NoSuchAlgorithmException if the specified algorithm is not
     * available in the default provider package or any of the other provider
     * packages that were searched.
     */
    public static final KeyManagerFactory getInstance(String algorithm)
        throws NoSuchAlgorithmException
    {
        try {
            Object[] objs = SSLSecurity.getImpl(algorithm, "KeyManagerFactory",
                                                (String) null);
            return new KeyManagerFactory((KeyManagerFactorySpi)objs[0],
                                    (Provider)objs[1],
                                    algorithm);
        } catch (NoSuchProviderException e) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
        }
    }

    /**
     * Generates a <code>KeyManagerFactory</code> object for the specified
     * key management algorithm from the specified provider.
     *
     * @param algorithm the standard name of the requested
     * algorithm.
     * @param provider the name of the provider
     *
     * @return the new <code>KeyManagerFactory</code> object
     *
     * @exception NoSuchAlgorithmException if the specified algorithm is not
     * available from the specified provider.
     * @exception NoSuchProviderException if the specified provider has not
     * been configured.
     */
    public static final KeyManagerFactory getInstance(String algorithm,
                                                 String provider)
        throws NoSuchAlgorithmException, NoSuchProviderException
    {
        if (provider == null || provider.length() == 0)
            throw new IllegalArgumentException("missing provider");
        Object[] objs = SSLSecurity.getImpl(algorithm, "KeyManagerFactory",
                                            provider);
        return new KeyManagerFactory((KeyManagerFactorySpi)objs[0],
                                        (Provider)objs[1], algorithm);
    }

    /**
     * Generates a <code>KeyManagerFactory</code> object for the specified
     * key management algorithm from the specified provider.
     *
     * @param algorithm the standard name of the requested
     * algorithm.
     * @param provider an instance of the provider
     *
     * @return the new <code>KeyManagerFactory</code> object
     *
     * @exception NoSuchAlgorithmException if the specified algorithm is not
     * available from the specified provider.
     */
    public static final KeyManagerFactory getInstance(String algorithm,
                                                 Provider provider)
        throws NoSuchAlgorithmException
    {
        if (provider == null)
            throw new IllegalArgumentException("missing provider");
        Object[] objs = SSLSecurity.getImpl(algorithm, "KeyManagerFactory",
                                            provider);
        return new KeyManagerFactory((KeyManagerFactorySpi)objs[0],
                                        (Provider)objs[1], algorithm);
    }

    /**
     * Returns the provider of this <code>KeyManagerFactory</code> object.
     *
     * @return the provider of this <code>KeyManagerFactory</code> object
     */
    public final Provider getProvider() {
        return this.provider;
    }


    /**
     * Initializes this factory with a source of key material. The
     * provider may also include a provider-specific source
     * of key material.
     *
     * @param ks the key store or null
     * @param password the password for recovering keys
     */
    public void init(KeyStore ks, char[] password)
        throws KeyStoreException, NoSuchAlgorithmException,
            UnrecoverableKeyException {
        factorySpi.engineInit(ks, password);
    }

    /**
     * Returns one key manager for each type of key material.
     * @return the key managers
     */
    public KeyManager[] getKeyManagers() {
        return factorySpi.engineGetKeyManagers();
    }
}
