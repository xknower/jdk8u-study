package sun.security.pkcs11;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.*;
import javax.crypto.spec.*;

import sun.security.internal.spec.TlsMasterSecretParameterSpec;

import static sun.security.pkcs11.TemplateManager.*;
import sun.security.pkcs11.wrapper.*;
import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * KeyGenerator for the SSL/TLS master secret.
 *
 * @author  Andreas Sterbenz
 * @since   1.6
 */
public final class P11TlsMasterSecretGenerator extends KeyGeneratorSpi {

    private final static String MSG = "TlsMasterSecretGenerator must be "
        + "initialized using a TlsMasterSecretParameterSpec";

    // token instance
    private final Token token;

    // algorithm name
    private final String algorithm;

    // mechanism id
    private long mechanism;

    private TlsMasterSecretParameterSpec spec;
    private P11Key p11Key;

    int version;

    P11TlsMasterSecretGenerator(Token token, String algorithm, long mechanism)
            throws PKCS11Exception {
        super();
        this.token = token;
        this.algorithm = algorithm;
        this.mechanism = mechanism;
    }

    protected void engineInit(SecureRandom random) {
        throw new InvalidParameterException(MSG);
    }

    protected void engineInit(AlgorithmParameterSpec params,
            SecureRandom random) throws InvalidAlgorithmParameterException {
        if (params instanceof TlsMasterSecretParameterSpec == false) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsMasterSecretParameterSpec)params;
        SecretKey key = spec.getPremasterSecret();
        // algorithm should be either TlsRsaPremasterSecret or TlsPremasterSecret,
        // but we omit the check
        try {
            p11Key = P11SecretKeyFactory.convertKey(token, key, null);
        } catch (InvalidKeyException e) {
            throw new InvalidAlgorithmParameterException("init() failed", e);
        }
        version = (spec.getMajorVersion() << 8) | spec.getMinorVersion();
        if ((version < 0x0300) && (version > 0x0303)) {
            throw new InvalidAlgorithmParameterException("Only SSL 3.0," +
                    " TLS 1.0, TLS 1.1, and TLS 1.2 are supported");
        }
        // We assume the token supports the required mechanism. If it does not,
        // generateKey() will fail and the failover should take care of us.
    }

    protected void engineInit(int keysize, SecureRandom random) {
        throw new InvalidParameterException(MSG);
    }

    protected SecretKey engineGenerateKey() {
        if (spec == null) {
            throw new IllegalStateException
                ("TlsMasterSecretGenerator must be initialized");
        }
        final boolean isTlsRsaPremasterSecret =
                p11Key.getAlgorithm().equals("TlsRsaPremasterSecret");
        if (version == 0x0300) {
            mechanism = isTlsRsaPremasterSecret ?
                    CKM_SSL3_MASTER_KEY_DERIVE : CKM_SSL3_MASTER_KEY_DERIVE_DH;
        } else if (version == 0x0301 || version == 0x0302) {
            mechanism = isTlsRsaPremasterSecret ?
                    CKM_TLS_MASTER_KEY_DERIVE : CKM_TLS_MASTER_KEY_DERIVE_DH;
        } else if (version == 0x0303) {
            mechanism = isTlsRsaPremasterSecret ?
                    CKM_TLS12_MASTER_KEY_DERIVE : CKM_TLS12_MASTER_KEY_DERIVE_DH;
        }
        CK_VERSION ckVersion;
        if (isTlsRsaPremasterSecret) {
            ckVersion = new CK_VERSION(0, 0);
        } else {
            // Note: we use DH for all non-RSA premaster secrets. That includes
            // Kerberos. That should not be a problem because master secret
            // calculation is always a straightforward application of the
            // TLS PRF (or the SSL equivalent).
            // The only thing special about RSA master secret calculation is
            // that it extracts the version numbers from the premaster secret.
            ckVersion = null;
        }
        byte[] clientRandom = spec.getClientRandom();
        byte[] serverRandom = spec.getServerRandom();
        CK_SSL3_RANDOM_DATA random =
                new CK_SSL3_RANDOM_DATA(clientRandom, serverRandom);
        CK_MECHANISM ckMechanism = null;
        if (version < 0x0303) {
            CK_SSL3_MASTER_KEY_DERIVE_PARAMS params =
                    new CK_SSL3_MASTER_KEY_DERIVE_PARAMS(random, ckVersion);
            ckMechanism = new CK_MECHANISM(mechanism, params);
        } else if (version == 0x0303) {
            CK_TLS12_MASTER_KEY_DERIVE_PARAMS params =
                    new CK_TLS12_MASTER_KEY_DERIVE_PARAMS(random, ckVersion,
                            Functions.getHashMechId(spec.getPRFHashAlg()));
            ckMechanism = new CK_MECHANISM(mechanism, params);
        }

        Session session = null;
        long p11KeyID = p11Key.getKeyID();
        try {
            session = token.getObjSession();
            CK_ATTRIBUTE[] attributes = token.getAttributes(O_GENERATE,
                CKO_SECRET_KEY, CKK_GENERIC_SECRET, new CK_ATTRIBUTE[0]);
            long keyID = token.p11.C_DeriveKey(session.id(),
                    ckMechanism, p11KeyID, attributes);
            int major, minor;
            if (ckVersion == null) {
                major = -1;
                minor = -1;
            } else {
                major = ckVersion.major;
                minor = ckVersion.minor;
            }
            return P11Key.masterSecretKey(session, keyID,
                "TlsMasterSecret", 48 << 3, attributes, major, minor);
        } catch (Exception e) {
            throw new ProviderException("Could not generate key", e);
        } finally {
            p11Key.releaseKeyID();
            token.releaseSession(session);
        }
    }

}
