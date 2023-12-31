package sun.security.krb5.internal.crypto;

import sun.security.krb5.Checksum;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.*;

public class DesCbcCrcEType extends DesCbcEType {

    public DesCbcCrcEType() {
    }

    public int eType() {
        return EncryptedData.ETYPE_DES_CBC_CRC;
    }

    public int minimumPadSize() {
        return 4;
    }

    public int confounderSize() {
        return 8;
    }

    public int checksumType() {
        return Checksum.CKSUMTYPE_RSA_MD5;
    }

    public int checksumSize() {
        return 4;
    }

    /**
     * Encrypts data using DES in CBC mode with CRC32.
     * @param data the data to be encrypted.
     * @param key  the secret key to encrypt the data. It is also used as initialization vector during cipher block chaining.
     * @return the buffer for cipher text.
     *
     * @written by Yanni Zhang, Dec 10, 1999
     */
    public byte[] encrypt(byte[] data, byte[] key, int usage)
         throws KrbCryptoException {
        return encrypt(data, key, key, usage);
    }

    /**
     * Decrypts data with provided key using DES in CBC mode with CRC32.
     * @param cipher the cipher text to be decrypted.
     * @param key  the secret key to decrypt the data.
     *
     * @written by Yanni Zhang, Dec 10, 1999
     */
    public byte[] decrypt(byte[] cipher, byte[] key, int usage)
         throws KrbApErrException, KrbCryptoException{
        return decrypt(cipher, key, key, usage);
    }

    protected byte[] calculateChecksum(byte[] data, int size) {
        return crc32.byte2crc32sum_bytes(data, size);
    }

}
