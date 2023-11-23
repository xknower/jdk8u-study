package sun.misc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.ObjectInputStream;

public interface JavaxCryptoSealedObjectAccess {
    ObjectInputStream getExtObjectInputStream(
            SealedObject sealed, Cipher cipher)
            throws BadPaddingException, IllegalBlockSizeException, IOException;
}
