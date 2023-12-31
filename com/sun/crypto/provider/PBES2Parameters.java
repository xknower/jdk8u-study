package com.sun.crypto.provider;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.security.util.*;

/**
 * This class implements the parameter set used with password-based
 * encryption scheme 2 (PBES2), which is defined in PKCS#5 as follows:
 *
 * <pre>
 * -- PBES2
 *
 * PBES2Algorithms ALGORITHM-IDENTIFIER ::=
 *   { {PBES2-params IDENTIFIED BY id-PBES2}, ...}
 *
 * id-PBES2 OBJECT IDENTIFIER ::= {pkcs-5 13}
 *
 * PBES2-params ::= SEQUENCE {
 *   keyDerivationFunc AlgorithmIdentifier {{PBES2-KDFs}},
 *   encryptionScheme AlgorithmIdentifier {{PBES2-Encs}} }
 *
 * PBES2-KDFs ALGORITHM-IDENTIFIER ::=
 *   { {PBKDF2-params IDENTIFIED BY id-PBKDF2}, ... }
 *
 * PBES2-Encs ALGORITHM-IDENTIFIER ::= { ... }
 *
 * -- PBKDF2
 *
 * PBKDF2Algorithms ALGORITHM-IDENTIFIER ::=
 *   { {PBKDF2-params IDENTIFIED BY id-PBKDF2}, ...}
 *
 * id-PBKDF2 OBJECT IDENTIFIER ::= {pkcs-5 12}
 *
 * PBKDF2-params ::= SEQUENCE {
 *     salt CHOICE {
 *       specified OCTET STRING,
 *       otherSource AlgorithmIdentifier {{PBKDF2-SaltSources}}
 *     },
 *     iterationCount INTEGER (1..MAX),
 *     keyLength INTEGER (1..MAX) OPTIONAL,
 *     prf AlgorithmIdentifier {{PBKDF2-PRFs}} DEFAULT algid-hmacWithSHA1
 * }
 *
 * PBKDF2-SaltSources ALGORITHM-IDENTIFIER ::= { ... }
 *
 * PBKDF2-PRFs ALGORITHM-IDENTIFIER ::= {
 *     {NULL IDENTIFIED BY id-hmacWithSHA1} |
 *     {NULL IDENTIFIED BY id-hmacWithSHA224} |
 *     {NULL IDENTIFIED BY id-hmacWithSHA256} |
 *     {NULL IDENTIFIED BY id-hmacWithSHA384} |
 *     {NULL IDENTIFIED BY id-hmacWithSHA512}, ... }
 *
 * algid-hmacWithSHA1 AlgorithmIdentifier {{PBKDF2-PRFs}} ::=
 *     {algorithm id-hmacWithSHA1, parameters NULL : NULL}
 *
 * id-hmacWithSHA1 OBJECT IDENTIFIER ::= {digestAlgorithm 7}
 *
 * PBES2-Encs ALGORITHM-IDENTIFIER ::= { ... }
 *
 * </pre>
 */

abstract class PBES2Parameters extends AlgorithmParametersSpi {

    private static final int pkcs5PBKDF2[] =
                                        {1, 2, 840, 113549, 1, 5, 12};
    private static final int pkcs5PBES2[] =
                                        {1, 2, 840, 113549, 1, 5, 13};
    private static final int hmacWithSHA1[] =
                                        {1, 2, 840, 113549, 2, 7};
    private static final int hmacWithSHA224[] =
                                        {1, 2, 840, 113549, 2, 8};
    private static final int hmacWithSHA256[] =
                                        {1, 2, 840, 113549, 2, 9};
    private static final int hmacWithSHA384[] =
                                        {1, 2, 840, 113549, 2, 10};
    private static final int hmacWithSHA512[] =
                                        {1, 2, 840, 113549, 2, 11};
    private static final int aes128CBC[] =
                                        {2, 16, 840, 1, 101, 3, 4, 1, 2};
    private static final int aes192CBC[] =
                                        {2, 16, 840, 1, 101, 3, 4, 1, 22};
    private static final int aes256CBC[] =
                                        {2, 16, 840, 1, 101, 3, 4, 1, 42};

    private static ObjectIdentifier pkcs5PBKDF2_OID;
    private static ObjectIdentifier pkcs5PBES2_OID;
    private static ObjectIdentifier hmacWithSHA1_OID;
    private static ObjectIdentifier hmacWithSHA224_OID;
    private static ObjectIdentifier hmacWithSHA256_OID;
    private static ObjectIdentifier hmacWithSHA384_OID;
    private static ObjectIdentifier hmacWithSHA512_OID;
    private static ObjectIdentifier aes128CBC_OID;
    private static ObjectIdentifier aes192CBC_OID;
    private static ObjectIdentifier aes256CBC_OID;

    static {
        try {
            pkcs5PBKDF2_OID = new ObjectIdentifier(pkcs5PBKDF2);
            pkcs5PBES2_OID = new ObjectIdentifier(pkcs5PBES2);
            hmacWithSHA1_OID = new ObjectIdentifier(hmacWithSHA1);
            hmacWithSHA224_OID = new ObjectIdentifier(hmacWithSHA224);
            hmacWithSHA256_OID = new ObjectIdentifier(hmacWithSHA256);
            hmacWithSHA384_OID = new ObjectIdentifier(hmacWithSHA384);
            hmacWithSHA512_OID = new ObjectIdentifier(hmacWithSHA512);
            aes128CBC_OID = new ObjectIdentifier(aes128CBC);
            aes192CBC_OID = new ObjectIdentifier(aes192CBC);
            aes256CBC_OID = new ObjectIdentifier(aes256CBC);
        } catch (IOException ioe) {
            // should not happen
        }
    }

    // the PBES2 algorithm name
    private String pbes2AlgorithmName = null;

    // the salt
    private byte[] salt = null;

    // the iteration count
    private int iCount = 0;

    // the cipher parameter
    private AlgorithmParameterSpec cipherParam = null;

    // the key derivation function (default is HmacSHA1)
    private ObjectIdentifier kdfAlgo_OID = hmacWithSHA1_OID;

    // the encryption function
    private ObjectIdentifier cipherAlgo_OID = null;

    // the cipher keysize (in bits)
    private int keysize = -1;

    PBES2Parameters() {
        // KDF, encryption & keysize values are set later, in engineInit(byte[])
    }

    PBES2Parameters(String pbes2AlgorithmName) throws NoSuchAlgorithmException {
        int and;
        String kdfAlgo = null;
        String cipherAlgo = null;

        // Extract the KDF and encryption algorithm names
        this.pbes2AlgorithmName = pbes2AlgorithmName;
        if (pbes2AlgorithmName.startsWith("PBEWith") &&
            (and = pbes2AlgorithmName.indexOf("And", 7 + 1)) > 0) {
            kdfAlgo = pbes2AlgorithmName.substring(7, and);
            cipherAlgo = pbes2AlgorithmName.substring(and + 3);

            // Check for keysize
            int underscore;
            if ((underscore = cipherAlgo.indexOf('_')) > 0) {
                int slash;
                if ((slash = cipherAlgo.indexOf('/', underscore + 1)) > 0) {
                    keysize =
                        Integer.parseInt(cipherAlgo.substring(underscore + 1,
                            slash));
                } else {
                    keysize =
                        Integer.parseInt(cipherAlgo.substring(underscore + 1));
                }
                cipherAlgo = cipherAlgo.substring(0, underscore);
            }
        } else {
            throw new NoSuchAlgorithmException("No crypto implementation for " +
                pbes2AlgorithmName);
        }

        switch (kdfAlgo) {
        case "HmacSHA1":
            kdfAlgo_OID = hmacWithSHA1_OID;
            break;
        case "HmacSHA224":
            kdfAlgo_OID = hmacWithSHA224_OID;
            break;
        case "HmacSHA256":
            kdfAlgo_OID = hmacWithSHA256_OID;
            break;
        case "HmacSHA384":
            kdfAlgo_OID = hmacWithSHA384_OID;
            break;
        case "HmacSHA512":
            kdfAlgo_OID = hmacWithSHA512_OID;
            break;
        default:
            throw new NoSuchAlgorithmException(
                "No crypto implementation for " + kdfAlgo);
        }

        if (cipherAlgo.equals("AES")) {
            this.keysize = keysize;
            switch (keysize) {
            case 128:
                cipherAlgo_OID = aes128CBC_OID;
                break;
            case 256:
                cipherAlgo_OID = aes256CBC_OID;
                break;
            default:
                throw new NoSuchAlgorithmException(
                    "No Cipher implementation for " + keysize + "-bit " +
                        cipherAlgo);
            }
        } else {
            throw new NoSuchAlgorithmException("No Cipher implementation for " +
                cipherAlgo);
        }
    }

    protected void engineInit(AlgorithmParameterSpec paramSpec)
        throws InvalidParameterSpecException
    {
       if (!(paramSpec instanceof PBEParameterSpec)) {
           throw new InvalidParameterSpecException
               ("Inappropriate parameter specification");
       }
       this.salt = ((PBEParameterSpec)paramSpec).getSalt().clone();
       this.iCount = ((PBEParameterSpec)paramSpec).getIterationCount();
       this.cipherParam = ((PBEParameterSpec)paramSpec).getParameterSpec();
    }

    protected void engineInit(byte[] encoded)
        throws IOException
    {
        String kdfAlgo = null;
        String cipherAlgo = null;

        DerValue pBES2_params = new DerValue(encoded);
        if (pBES2_params.tag != DerValue.tag_Sequence) {
            throw new IOException("PBE parameter parsing error: "
                + "not an ASN.1 SEQUENCE tag");
        }
        DerValue kdf = pBES2_params.data.getDerValue();

        // Before JDK-8202837, PBES2-params was mistakenly encoded like
        // an AlgorithmId which is a sequence of its own OID and the real
        // PBES2-params. If the first DerValue is an OID instead of a
        // PBES2-KDFs (which should be a SEQUENCE), we are likely to be
        // dealing with this buggy encoding. Skip the OID and treat the
        // next DerValue as the real PBES2-params.
        if (kdf.getTag() == DerValue.tag_ObjectId) {
            pBES2_params = pBES2_params.data.getDerValue();
            kdf = pBES2_params.data.getDerValue();
        }

        kdfAlgo = parseKDF(kdf);

        if (pBES2_params.tag != DerValue.tag_Sequence) {
            throw new IOException("PBE parameter parsing error: "
                + "not an ASN.1 SEQUENCE tag");
        }
        cipherAlgo = parseES(pBES2_params.data.getDerValue());

        pbes2AlgorithmName = new StringBuilder().append("PBEWith")
            .append(kdfAlgo).append("And").append(cipherAlgo).toString();
    }

    private String parseKDF(DerValue keyDerivationFunc) throws IOException {

        if (!pkcs5PBKDF2_OID.equals(keyDerivationFunc.data.getOID())) {
            throw new IOException("PBE parameter parsing error: "
                + "expecting the object identifier for PBKDF2");
        }
        if (keyDerivationFunc.tag != DerValue.tag_Sequence) {
            throw new IOException("PBE parameter parsing error: "
                + "not an ASN.1 SEQUENCE tag");
        }
        DerValue pBKDF2_params = keyDerivationFunc.data.getDerValue();
        if (pBKDF2_params.tag != DerValue.tag_Sequence) {
            throw new IOException("PBE parameter parsing error: "
                + "not an ASN.1 SEQUENCE tag");
        }
        DerValue specified = pBKDF2_params.data.getDerValue();
        // the 'specified' ASN.1 CHOICE for 'salt' is supported
        if (specified.tag == DerValue.tag_OctetString) {
            salt = specified.getOctetString();
        } else {
            // the 'otherSource' ASN.1 CHOICE for 'salt' is not supported
            throw new IOException("PBE parameter parsing error: "
                + "not an ASN.1 OCTET STRING tag");
        }
        iCount = pBKDF2_params.data.getInteger();

        DerValue prf = null;
        // keyLength INTEGER (1..MAX) OPTIONAL,
        if (pBKDF2_params.data.available() > 0) {
            DerValue keyLength = pBKDF2_params.data.getDerValue();
            if (keyLength.tag == DerValue.tag_Integer) {
                keysize = keyLength.getInteger() * 8; // keysize (in bits)
            } else {
                // Should be the prf
                prf = keyLength;
            }
        }
        // prf AlgorithmIdentifier {{PBKDF2-PRFs}} DEFAULT algid-hmacWithSHA1
        String kdfAlgo = "HmacSHA1";
        if (prf == null) {
            if (pBKDF2_params.data.available() > 0) {
                prf = pBKDF2_params.data.getDerValue();
            }
        }
        if (prf != null) {
            kdfAlgo_OID = prf.data.getOID();
            if (hmacWithSHA1_OID.equals(kdfAlgo_OID)) {
                kdfAlgo = "HmacSHA1";
            } else if (hmacWithSHA224_OID.equals(kdfAlgo_OID)) {
                kdfAlgo = "HmacSHA224";
            } else if (hmacWithSHA256_OID.equals(kdfAlgo_OID)) {
                kdfAlgo = "HmacSHA256";
            } else if (hmacWithSHA384_OID.equals(kdfAlgo_OID)) {
                kdfAlgo = "HmacSHA384";
            } else if (hmacWithSHA512_OID.equals(kdfAlgo_OID)) {
                kdfAlgo = "HmacSHA512";
            } else {
                throw new IOException("PBE parameter parsing error: "
                        + "expecting the object identifier for a HmacSHA key "
                        + "derivation function");
            }
            if (prf.data.available() != 0) {
                // parameter is 'NULL' for all HmacSHA KDFs
                DerValue parameter = prf.data.getDerValue();
                if (parameter.tag != DerValue.tag_Null) {
                    throw new IOException("PBE parameter parsing error: "
                            + "not an ASN.1 NULL tag");
                }
            }
        }

        return kdfAlgo;
    }

    private String parseES(DerValue encryptionScheme) throws IOException {
        String cipherAlgo = null;

        cipherAlgo_OID = encryptionScheme.data.getOID();
        if (aes128CBC_OID.equals(cipherAlgo_OID)) {
            cipherAlgo = "AES_128";
            // parameter is AES-IV 'OCTET STRING (SIZE(16))'
            cipherParam =
                new IvParameterSpec(encryptionScheme.data.getOctetString());
            keysize = 128;
        } else if (aes256CBC_OID.equals(cipherAlgo_OID)) {
            cipherAlgo = "AES_256";
            // parameter is AES-IV 'OCTET STRING (SIZE(16))'
            cipherParam =
                new IvParameterSpec(encryptionScheme.data.getOctetString());
            keysize = 256;
        } else {
            throw new IOException("PBE parameter parsing error: "
                + "expecting the object identifier for AES cipher");
        }

        return cipherAlgo;
    }

    protected void engineInit(byte[] encoded, String decodingMethod)
        throws IOException
    {
        engineInit(encoded);
    }

    protected <T extends AlgorithmParameterSpec>
            T engineGetParameterSpec(Class<T> paramSpec)
        throws InvalidParameterSpecException
    {
        if (PBEParameterSpec.class.isAssignableFrom(paramSpec)) {
            return paramSpec.cast(
                new PBEParameterSpec(this.salt, this.iCount, this.cipherParam));
        } else {
            throw new InvalidParameterSpecException
                ("Inappropriate parameter specification");
        }
    }

    protected byte[] engineGetEncoded() throws IOException {
        DerOutputStream out = new DerOutputStream();

        DerOutputStream pBES2_params = new DerOutputStream();

        DerOutputStream keyDerivationFunc = new DerOutputStream();
        keyDerivationFunc.putOID(pkcs5PBKDF2_OID);

        DerOutputStream pBKDF2_params = new DerOutputStream();
        pBKDF2_params.putOctetString(salt); // choice: 'specified OCTET STRING'
        pBKDF2_params.putInteger(iCount);

        if (keysize > 0) {
            pBKDF2_params.putInteger(keysize / 8); // derived key length (in octets)
        }

        DerOutputStream prf = new DerOutputStream();
        // algorithm is id-hmacWithSHA1/SHA224/SHA256/SHA384/SHA512
        prf.putOID(kdfAlgo_OID);
        // parameters is 'NULL'
        prf.putNull();
        pBKDF2_params.write(DerValue.tag_Sequence, prf);

        keyDerivationFunc.write(DerValue.tag_Sequence, pBKDF2_params);
        pBES2_params.write(DerValue.tag_Sequence, keyDerivationFunc);

        DerOutputStream encryptionScheme = new DerOutputStream();
        // algorithm is id-aes128-CBC or id-aes256-CBC
        encryptionScheme.putOID(cipherAlgo_OID);
        // parameters is 'AES-IV ::= OCTET STRING (SIZE(16))'
        if (cipherParam != null && cipherParam instanceof IvParameterSpec) {
            encryptionScheme.putOctetString(
                ((IvParameterSpec)cipherParam).getIV());
        } else {
            throw new IOException("Wrong parameter type: IV expected");
        }
        pBES2_params.write(DerValue.tag_Sequence, encryptionScheme);

        out.write(DerValue.tag_Sequence, pBES2_params);

        return out.toByteArray();
    }

    protected byte[] engineGetEncoded(String encodingMethod)
        throws IOException
    {
        return engineGetEncoded();
    }

    /*
     * Returns a formatted string describing the parameters.
     *
     * The algorithn name pattern is: "PBEWith<prf>And<encryption>"
     * where <prf> is one of: HmacSHA1, HmacSHA224, HmacSHA256, HmacSHA384,
     * or HmacSHA512, and <encryption> is AES with a keysize suffix.
     */
    protected String engineToString() {
        return pbes2AlgorithmName;
    }

    public static final class General extends PBES2Parameters {
        public General() throws NoSuchAlgorithmException {
            super();
        }
    }

    public static final class HmacSHA1AndAES_128 extends PBES2Parameters {
        public HmacSHA1AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA1AndAES_128");
        }
    }

    public static final class HmacSHA224AndAES_128 extends PBES2Parameters {
        public HmacSHA224AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA224AndAES_128");
        }
    }

    public static final class HmacSHA256AndAES_128 extends PBES2Parameters {
        public HmacSHA256AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA256AndAES_128");
        }
    }

    public static final class HmacSHA384AndAES_128 extends PBES2Parameters {
        public HmacSHA384AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA384AndAES_128");
        }
    }

    public static final class HmacSHA512AndAES_128 extends PBES2Parameters {
        public HmacSHA512AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA512AndAES_128");
        }
    }

    public static final class HmacSHA1AndAES_256 extends PBES2Parameters {
        public HmacSHA1AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA1AndAES_256");
        }
    }

    public static final class HmacSHA224AndAES_256 extends PBES2Parameters {
        public HmacSHA224AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA224AndAES_256");
        }
    }

    public static final class HmacSHA256AndAES_256 extends PBES2Parameters {
        public HmacSHA256AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA256AndAES_256");
        }
    }

    public static final class HmacSHA384AndAES_256 extends PBES2Parameters {
        public HmacSHA384AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA384AndAES_256");
        }
    }

    public static final class HmacSHA512AndAES_256 extends PBES2Parameters {
        public HmacSHA512AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA512AndAES_256");
        }
    }
}
