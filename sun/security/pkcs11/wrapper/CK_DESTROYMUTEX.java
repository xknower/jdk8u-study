package sun.security.pkcs11.wrapper;



/**
 * interface CK_DESTROYMUTEX.<p>
 *
 * @author Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
 * @author Martin Schlaeffer <schlaeff@sbox.tugraz.at>
 */
public interface CK_DESTROYMUTEX {

    /**
     * Method CK_DESTROYMUTEX
     *
     * @param pMutex The mutex (lock) object.
     * @exception PKCS11Exception
     */
    public void CK_DESTROYMUTEX(Object pMutex) throws PKCS11Exception;

}