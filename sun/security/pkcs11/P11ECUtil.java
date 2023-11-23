package sun.security.pkcs11;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;

import sun.security.ec.ECPublicKeyImpl;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.x509.X509Key;

final class P11ECUtil {

    static ECPublicKey decodeX509ECPublicKey(byte[] encoded)
            throws InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

        return (ECPublicKey)ECGeneratePublic(keySpec);
    }

    static byte[] x509EncodeECPublicKey(ECPoint w,
            ECParameterSpec params) throws InvalidKeySpecException {
        ECPublicKeySpec keySpec = new ECPublicKeySpec(w, params);
        X509Key key = (X509Key)ECGeneratePublic(keySpec);

        return key.getEncoded();
    }

    static ECPrivateKey decodePKCS8ECPrivateKey(byte[] encoded)
            throws InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

        return (ECPrivateKey)ECGeneratePrivate(keySpec);
    }

    static ECPrivateKey generateECPrivateKey(BigInteger s,
            ECParameterSpec params) throws InvalidKeySpecException {
        ECPrivateKeySpec keySpec = new ECPrivateKeySpec(s, params);

        return (ECPrivateKey)ECGeneratePrivate(keySpec);
    }

    private static PublicKey ECGeneratePublic(KeySpec keySpec)
            throws InvalidKeySpecException {
        try {
            if (keySpec instanceof X509EncodedKeySpec) {
               X509EncodedKeySpec x509Spec = (X509EncodedKeySpec)keySpec;
                return new ECPublicKeyImpl(x509Spec.getEncoded());
            } else if (keySpec instanceof ECPublicKeySpec) {
                ECPublicKeySpec ecSpec = (ECPublicKeySpec)keySpec;
                return new ECPublicKeyImpl(
                    ecSpec.getW(),
                    ecSpec.getParams()
                );
            } else {
                throw new InvalidKeySpecException("Only ECPublicKeySpec "
                    + "and X509EncodedKeySpec supported for EC public keys");
            }
        } catch (InvalidKeySpecException e) {
            throw e;
        } catch (GeneralSecurityException e) {
            throw new InvalidKeySpecException(e);
        }
    }

    private static PrivateKey ECGeneratePrivate(KeySpec keySpec)
            throws InvalidKeySpecException {
        try {
            if (keySpec instanceof PKCS8EncodedKeySpec) {
                PKCS8EncodedKeySpec pkcsSpec = (PKCS8EncodedKeySpec)keySpec;
                return new ECPrivateKeyImpl(pkcsSpec.getEncoded());
            } else if (keySpec instanceof ECPrivateKeySpec) {
                ECPrivateKeySpec ecSpec = (ECPrivateKeySpec)keySpec;
                return new ECPrivateKeyImpl(ecSpec.getS(), ecSpec.getParams());
            } else {
                throw new InvalidKeySpecException("Only ECPrivateKeySpec "
                    + "and PKCS8EncodedKeySpec supported for EC private keys");
            }
        } catch (InvalidKeySpecException e) {
            throw e;
        } catch (GeneralSecurityException e) {
            throw new InvalidKeySpecException(e);
        }
    }

    private P11ECUtil() {}

}
