package javax.crypto;

import java.util.*;

import java.security.*;
import java.security.Provider.Service;
import java.security.spec.*;

import sun.security.util.Debug;
import sun.security.jca.*;
import sun.security.jca.GetInstance.Instance;

/**
 * This class provides the functionality of a key agreement (or key
 * exchange) protocol.
 * <p>
 * The keys involved in establishing a shared secret are created by one of the
 * key generators ({@code KeyPairGenerator} or
 * {@code KeyGenerator}), a {@code KeyFactory}, or as a result from
 * an intermediate phase of the key agreement protocol.
 *
 * <p> For each of the correspondents in the key exchange, {@code doPhase}
 * needs to be called. For example, if this key exchange is with one other
 * party, {@code doPhase} needs to be called once, with the
 * {@code lastPhase} flag set to {@code true}.
 * If this key exchange is
 * with two other parties, {@code doPhase} needs to be called twice,
 * the first time setting the {@code lastPhase} flag to
 * {@code false}, and the second time setting it to {@code true}.
 * There may be any number of parties involved in a key exchange.
 *
 * <p> Every implementation of the Java platform is required to support the
 * following standard {@code KeyAgreement} algorithm:
 * <ul>
 * <li>{@code DiffieHellman}</li>
 * </ul>
 * This algorithm is described in the <a href=
 * "{@docRoot}/../technotes/guides/security/StandardNames.html#KeyAgreement">
 * KeyAgreement section</a> of the
 * Java Cryptography Architecture Standard Algorithm Name Documentation.
 * Consult the release documentation for your implementation to see if any
 * other algorithms are supported.
 *
 * @author Jan Luehe
 *
 * @see KeyGenerator
 * @see SecretKey
 * @since 1.4
 */

public class KeyAgreement {

    private static final Debug debug =
                        Debug.getInstance("jca", "KeyAgreement");

    private static final Debug pdebug =
                        Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug =
        Debug.isOn("engine=") && !Debug.isOn("keyagreement");

    // The provider
    private Provider provider;

    // The provider implementation (delegate)
    private KeyAgreementSpi spi;

    // The name of the key agreement algorithm.
    private final String algorithm;

    // next service to try in provider selection
    // null once provider is selected
    private Service firstService;

    // remaining services to try in provider selection
    // null once provider is selected
    private Iterator<Service> serviceIterator;

    private final Object lock;

    /**
     * Creates a KeyAgreement object.
     *
     * @param keyAgreeSpi the delegate
     * @param provider the provider
     * @param algorithm the algorithm
     */
    protected KeyAgreement(KeyAgreementSpi keyAgreeSpi, Provider provider,
                           String algorithm) {
        this.spi = keyAgreeSpi;
        this.provider = provider;
        this.algorithm = algorithm;
        lock = null;
    }

    private KeyAgreement(Service s, Iterator<Service> t, String algorithm) {
        firstService = s;
        serviceIterator = t;
        this.algorithm = algorithm;
        lock = new Object();
    }

    /**
     * Returns the algorithm name of this {@code KeyAgreement} object.
     *
     * <p>This is the same name that was specified in one of the
     * {@code getInstance} calls that created this
     * {@code KeyAgreement} object.
     *
     * @return the algorithm name of this {@code KeyAgreement} object.
     */
    public final String getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Returns a {@code KeyAgreement} object that implements the
     * specified key agreement algorithm.
     *
     * <p> This method traverses the list of registered security Providers,
     * starting with the most preferred Provider.
     * A new KeyAgreement object encapsulating the
     * KeyAgreementSpi implementation from the first
     * Provider that supports the specified algorithm is returned.
     *
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the requested key agreement
     * algorithm.
     * See the KeyAgreement section in the <a href=
     * "{@docRoot}/../technotes/guides/security/StandardNames.html#KeyAgreement">
     * Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     * for information about standard algorithm names.
     *
     * @return the new {@code KeyAgreement} object.
     *
     * @exception NullPointerException if the specified algorithm
     *          is null.
     *
     * @exception NoSuchAlgorithmException if no Provider supports a
     *          KeyAgreementSpi implementation for the
     *          specified algorithm.
     *
     * @see java.security.Provider
     */
    public static final KeyAgreement getInstance(String algorithm)
            throws NoSuchAlgorithmException {
        List<Service> services =
                GetInstance.getServices("KeyAgreement", algorithm);
        // make sure there is at least one service from a signed provider
        Iterator<Service> t = services.iterator();
        while (t.hasNext()) {
            Service s = t.next();
            if (JceSecurity.canUseProvider(s.getProvider()) == false) {
                continue;
            }
            return new KeyAgreement(s, t, algorithm);
        }
        throw new NoSuchAlgorithmException
                                ("Algorithm " + algorithm + " not available");
    }

    /**
     * Returns a {@code KeyAgreement} object that implements the
     * specified key agreement algorithm.
     *
     * <p> A new KeyAgreement object encapsulating the
     * KeyAgreementSpi implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the security provider list.
     *
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the requested key agreement
     * algorithm.
     * See the KeyAgreement section in the <a href=
     * "{@docRoot}/../technotes/guides/security/StandardNames.html#KeyAgreement">
     * Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     * for information about standard algorithm names.
     *
     * @param provider the name of the provider.
     *
     * @return the new {@code KeyAgreement} object.
     *
     * @exception NullPointerException if the specified algorithm
     *          is null.
     *
     * @exception NoSuchAlgorithmException if a KeyAgreementSpi
     *          implementation for the specified algorithm is not
     *          available from the specified provider.
     *
     * @exception NoSuchProviderException if the specified provider is not
     *          registered in the security provider list.
     *
     * @exception IllegalArgumentException if the {@code provider}
     *          is null or empty.
     *
     * @see java.security.Provider
     */
    public static final KeyAgreement getInstance(String algorithm,
            String provider) throws NoSuchAlgorithmException,
            NoSuchProviderException {
        Instance instance = JceSecurity.getInstance
                ("KeyAgreement", KeyAgreementSpi.class, algorithm, provider);
        return new KeyAgreement((KeyAgreementSpi)instance.impl,
                instance.provider, algorithm);
    }

    /**
     * Returns a {@code KeyAgreement} object that implements the
     * specified key agreement algorithm.
     *
     * <p> A new KeyAgreement object encapsulating the
     * KeyAgreementSpi implementation from the specified Provider
     * object is returned.  Note that the specified Provider object
     * does not have to be registered in the provider list.
     *
     * @param algorithm the standard name of the requested key agreement
     * algorithm.
     * See the KeyAgreement section in the <a href=
     * "{@docRoot}/../technotes/guides/security/StandardNames.html#KeyAgreement">
     * Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     * for information about standard algorithm names.
     *
     * @param provider the provider.
     *
     * @return the new {@code KeyAgreement} object.
     *
     * @exception NullPointerException if the specified algorithm
     *          is null.
     *
     * @exception NoSuchAlgorithmException if a KeyAgreementSpi
     *          implementation for the specified algorithm is not available
     *          from the specified Provider object.
     *
     * @exception IllegalArgumentException if the {@code provider}
     *          is null.
     *
     * @see java.security.Provider
     */
    public static final KeyAgreement getInstance(String algorithm,
            Provider provider) throws NoSuchAlgorithmException {
        Instance instance = JceSecurity.getInstance
                ("KeyAgreement", KeyAgreementSpi.class, algorithm, provider);
        return new KeyAgreement((KeyAgreementSpi)instance.impl,
                instance.provider, algorithm);
    }

    // max number of debug warnings to print from chooseFirstProvider()
    private static int warnCount = 10;

    /**
     * Choose the Spi from the first provider available. Used if
     * delayed provider selection is not possible because init()
     * is not the first method called.
     */
    void chooseFirstProvider() {
        if (spi != null) {
            return;
        }
        synchronized (lock) {
            if (spi != null) {
                return;
            }
            if (debug != null) {
                int w = --warnCount;
                if (w >= 0) {
                    debug.println("KeyAgreement.init() not first method "
                        + "called, disabling delayed provider selection");
                    if (w == 0) {
                        debug.println("Further warnings of this type will "
                            + "be suppressed");
                    }
                    new Exception("Call trace").printStackTrace();
                }
            }
            Exception lastException = null;
            while ((firstService != null) || serviceIterator.hasNext()) {
                Service s;
                if (firstService != null) {
                    s = firstService;
                    firstService = null;
                } else {
                    s = serviceIterator.next();
                }
                if (JceSecurity.canUseProvider(s.getProvider()) == false) {
                    continue;
                }
                try {
                    Object obj = s.newInstance(null);
                    if (obj instanceof KeyAgreementSpi == false) {
                        continue;
                    }
                    spi = (KeyAgreementSpi)obj;
                    provider = s.getProvider();
                    // not needed any more
                    firstService = null;
                    serviceIterator = null;
                    return;
                } catch (Exception e) {
                    lastException = e;
                }
            }
            ProviderException e = new ProviderException
                    ("Could not construct KeyAgreementSpi instance");
            if (lastException != null) {
                e.initCause(lastException);
            }
            throw e;
        }
    }

    private final static int I_NO_PARAMS = 1;
    private final static int I_PARAMS    = 2;

    private void implInit(KeyAgreementSpi spi, int type, Key key,
            AlgorithmParameterSpec params, SecureRandom random)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (type == I_NO_PARAMS) {
            spi.engineInit(key, random);
        } else { // I_PARAMS
            spi.engineInit(key, params, random);
        }
    }

    private void chooseProvider(int initType, Key key,
            AlgorithmParameterSpec params, SecureRandom random)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        synchronized (lock) {
            if (spi != null) {
                implInit(spi, initType, key, params, random);
                return;
            }
            Exception lastException = null;
            while ((firstService != null) || serviceIterator.hasNext()) {
                Service s;
                if (firstService != null) {
                    s = firstService;
                    firstService = null;
                } else {
                    s = serviceIterator.next();
                }
                // if provider says it does not support this key, ignore it
                if (s.supportsParameter(key) == false) {
                    continue;
                }
                if (JceSecurity.canUseProvider(s.getProvider()) == false) {
                    continue;
                }
                try {
                    KeyAgreementSpi spi = (KeyAgreementSpi)s.newInstance(null);
                    implInit(spi, initType, key, params, random);
                    provider = s.getProvider();
                    this.spi = spi;
                    firstService = null;
                    serviceIterator = null;
                    return;
                } catch (Exception e) {
                    // NoSuchAlgorithmException from newInstance()
                    // InvalidKeyException from init()
                    // RuntimeException (ProviderException) from init()
                    if (lastException == null) {
                        lastException = e;
                    }
                }
            }
            // no working provider found, fail
            if (lastException instanceof InvalidKeyException) {
                throw (InvalidKeyException)lastException;
            }
            if (lastException instanceof InvalidAlgorithmParameterException) {
                throw (InvalidAlgorithmParameterException)lastException;
            }
            if (lastException instanceof RuntimeException) {
                throw (RuntimeException)lastException;
            }
            String kName = (key != null) ? key.getClass().getName() : "(null)";
            throw new InvalidKeyException
                ("No installed provider supports this key: "
                + kName, lastException);
        }
    }

    /**
     * Returns the provider of this {@code KeyAgreement} object.
     *
     * @return the provider of this {@code KeyAgreement} object
     */
    public final Provider getProvider() {
        chooseFirstProvider();
        return this.provider;
    }

    /**
     * Initializes this key agreement with the given key, which is required to
     * contain all the algorithm parameters required for this key agreement.
     *
     * <p> If this key agreement requires any random bytes, it will get
     * them using the
     * {@link java.security.SecureRandom}
     * implementation of the highest-priority
     * installed provider as the source of randomness.
     * (If none of the installed providers supply an implementation of
     * SecureRandom, a system-provided source of randomness will be used.)
     *
     * @param key the party's private information. For example, in the case
     * of the Diffie-Hellman key agreement, this would be the party's own
     * Diffie-Hellman private key.
     *
     * @exception InvalidKeyException if the given key is
     * inappropriate for this key agreement, e.g., is of the wrong type or
     * has an incompatible algorithm type.
     */
    public final void init(Key key) throws InvalidKeyException {
        init(key, JceSecurity.RANDOM);
    }

    /**
     * Initializes this key agreement with the given key and source of
     * randomness. The given key is required to contain all the algorithm
     * parameters required for this key agreement.
     *
     * <p> If the key agreement algorithm requires random bytes, it gets them
     * from the given source of randomness, {@code random}.
     * However, if the underlying
     * algorithm implementation does not require any random bytes,
     * {@code random} is ignored.
     *
     * @param key the party's private information. For example, in the case
     * of the Diffie-Hellman key agreement, this would be the party's own
     * Diffie-Hellman private key.
     * @param random the source of randomness
     *
     * @exception InvalidKeyException if the given key is
     * inappropriate for this key agreement, e.g., is of the wrong type or
     * has an incompatible algorithm type.
     */
    public final void init(Key key, SecureRandom random)
            throws InvalidKeyException {
        if (spi != null) {
            spi.engineInit(key, random);
        } else {
            try {
                chooseProvider(I_NO_PARAMS, key, null, random);
            } catch (InvalidAlgorithmParameterException e) {
                // should never occur
                throw new InvalidKeyException(e);
            }
        }

        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyAgreement." + algorithm + " algorithm from: " +
                this.provider.getName());
        }
    }

    /**
     * Initializes this key agreement with the given key and set of
     * algorithm parameters.
     *
     * <p> If this key agreement requires any random bytes, it will get
     * them using the
     * {@link java.security.SecureRandom}
     * implementation of the highest-priority
     * installed provider as the source of randomness.
     * (If none of the installed providers supply an implementation of
     * SecureRandom, a system-provided source of randomness will be used.)
     *
     * @param key the party's private information. For example, in the case
     * of the Diffie-Hellman key agreement, this would be the party's own
     * Diffie-Hellman private key.
     * @param params the key agreement parameters
     *
     * @exception InvalidKeyException if the given key is
     * inappropriate for this key agreement, e.g., is of the wrong type or
     * has an incompatible algorithm type.
     * @exception InvalidAlgorithmParameterException if the given parameters
     * are inappropriate for this key agreement.
     */
    public final void init(Key key, AlgorithmParameterSpec params)
        throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        init(key, params, JceSecurity.RANDOM);
    }

    /**
     * Initializes this key agreement with the given key, set of
     * algorithm parameters, and source of randomness.
     *
     * @param key the party's private information. For example, in the case
     * of the Diffie-Hellman key agreement, this would be the party's own
     * Diffie-Hellman private key.
     * @param params the key agreement parameters
     * @param random the source of randomness
     *
     * @exception InvalidKeyException if the given key is
     * inappropriate for this key agreement, e.g., is of the wrong type or
     * has an incompatible algorithm type.
     * @exception InvalidAlgorithmParameterException if the given parameters
     * are inappropriate for this key agreement.
     */
    public final void init(Key key, AlgorithmParameterSpec params,
                           SecureRandom random)
        throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        if (spi != null) {
            spi.engineInit(key, params, random);
        } else {
            chooseProvider(I_PARAMS, key, params, random);
        }

        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyAgreement." + algorithm + " algorithm from: " +
                this.provider.getName());
        }
    }

    /**
     * Executes the next phase of this key agreement with the given
     * key that was received from one of the other parties involved in this key
     * agreement.
     *
     * @param key the key for this phase. For example, in the case of
     * Diffie-Hellman between 2 parties, this would be the other party's
     * Diffie-Hellman public key.
     * @param lastPhase flag which indicates whether or not this is the last
     * phase of this key agreement.
     *
     * @return the (intermediate) key resulting from this phase, or null
     * if this phase does not yield a key
     *
     * @exception InvalidKeyException if the given key is inappropriate for
     * this phase.
     * @exception IllegalStateException if this key agreement has not been
     * initialized.
     */
    public final Key doPhase(Key key, boolean lastPhase)
        throws InvalidKeyException, IllegalStateException
    {
        chooseFirstProvider();
        return spi.engineDoPhase(key, lastPhase);
    }

    /**
     * Generates the shared secret and returns it in a new buffer.
     *
     * <p>This method resets this {@code KeyAgreement} object, so that it
     * can be reused for further key agreements. Unless this key agreement is
     * reinitialized with one of the {@code init} methods, the same
     * private information and algorithm parameters will be used for
     * subsequent key agreements.
     *
     * @return the new buffer with the shared secret
     *
     * @exception IllegalStateException if this key agreement has not been
     * completed yet
     */
    public final byte[] generateSecret() throws IllegalStateException {
        chooseFirstProvider();
        return spi.engineGenerateSecret();
    }

    /**
     * Generates the shared secret, and places it into the buffer
     * {@code sharedSecret}, beginning at {@code offset} inclusive.
     *
     * <p>If the {@code sharedSecret} buffer is too small to hold the
     * result, a {@code ShortBufferException} is thrown.
     * In this case, this call should be repeated with a larger output buffer.
     *
     * <p>This method resets this {@code KeyAgreement} object, so that it
     * can be reused for further key agreements. Unless this key agreement is
     * reinitialized with one of the {@code init} methods, the same
     * private information and algorithm parameters will be used for
     * subsequent key agreements.
     *
     * @param sharedSecret the buffer for the shared secret
     * @param offset the offset in {@code sharedSecret} where the
     * shared secret will be stored
     *
     * @return the number of bytes placed into {@code sharedSecret}
     *
     * @exception IllegalStateException if this key agreement has not been
     * completed yet
     * @exception ShortBufferException if the given output buffer is too small
     * to hold the secret
     */
    public final int generateSecret(byte[] sharedSecret, int offset)
        throws IllegalStateException, ShortBufferException
    {
        chooseFirstProvider();
        return spi.engineGenerateSecret(sharedSecret, offset);
    }

    /**
     * Creates the shared secret and returns it as a {@code SecretKey}
     * object of the specified algorithm.
     *
     * <p>This method resets this {@code KeyAgreement} object, so that it
     * can be reused for further key agreements. Unless this key agreement is
     * reinitialized with one of the {@code init} methods, the same
     * private information and algorithm parameters will be used for
     * subsequent key agreements.
     *
     * @param algorithm the requested secret-key algorithm
     *
     * @return the shared secret key
     *
     * @exception IllegalStateException if this key agreement has not been
     * completed yet
     * @exception NoSuchAlgorithmException if the specified secret-key
     * algorithm is not available
     * @exception InvalidKeyException if the shared secret-key material cannot
     * be used to generate a secret key of the specified algorithm (e.g.,
     * the key material is too short)
     */
    public final SecretKey generateSecret(String algorithm)
        throws IllegalStateException, NoSuchAlgorithmException,
            InvalidKeyException
    {
        chooseFirstProvider();
        return spi.engineGenerateSecret(algorithm);
    }
}
