package sun.security.pkcs11.wrapper;

import java.math.BigInteger;
import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * class CK_MECHANISM specifies a particular mechanism and any parameters it
 * requires.<p>
 * <B>PKCS#11 structure:</B>
 * <PRE>
 *  typedef struct CK_MECHANISM {&nbsp;&nbsp;
 *    CK_MECHANISM_TYPE mechanism;&nbsp;&nbsp;
 *    CK_VOID_PTR pParameter;&nbsp;&nbsp;
 *    CK_ULONG ulParameterLen;&nbsp;&nbsp;
 *  } CK_MECHANISM;
 * </PRE>
 *
 * @author Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
 * @author Martin Schlaeffer <schlaeff@sbox.tugraz.at>
 */
public class CK_MECHANISM {

    /**
     * <B>PKCS#11:</B>
     * <PRE>
     *   CK_MECHANISM_TYPE mechanism;
     * </PRE>
     */
    public long mechanism;

    /**
     * <B>PKCS#11:</B>
     * <PRE>
     *   CK_VOID_PTR pParameter;
     *   CK_ULONG ulParameterLen;
     * </PRE>
     */
    public Object pParameter = null;

    // pointer to native CK_MECHANISM structure
    // For mechanisms which have only mechanism id, the native structure
    // can be freed right after init and this field will not be used. However,
    // for mechanisms which have both mechanism id and parameters, it can
    // only be freed after operation is finished. Thus, the native pointer
    // will be stored here and then be explicitly freed by caller.
    private long pHandle = 0L;

    public CK_MECHANISM(long mechanism) {
        this.mechanism = mechanism;
    }

    // We don't have a (long,Object) constructor to force type checking.
    // This makes sure we don't accidentally pass a class that the native
    // code cannot handle.
    public CK_MECHANISM(long mechanism, byte[] pParameter) {
        init(mechanism, pParameter);
    }

    public CK_MECHANISM(long mechanism, BigInteger b) {
        init(mechanism, sun.security.pkcs11.P11Util.getMagnitude(b));
    }

    public CK_MECHANISM(long mechanism, CK_VERSION version) {
        init(mechanism, version);
    }

    public CK_MECHANISM(long mechanism, CK_SSL3_MASTER_KEY_DERIVE_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_TLS12_MASTER_KEY_DERIVE_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_SSL3_KEY_MAT_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_TLS12_KEY_MAT_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_TLS_PRF_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_TLS_MAC_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_ECDH1_DERIVE_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, Long params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_AES_CTR_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_GCM_PARAMS params) {
        init(mechanism, params);
    }

    public CK_MECHANISM(long mechanism, CK_CCM_PARAMS params) {
        init(mechanism, params);
    }

    // For PSS. the parameter may be set multiple times, use the
    // CK_MECHANISM(long) constructor and setParameter(CK_RSA_PKCS_PSS_PARAMS)
    // methods instead of creating yet another constructor
    public void setParameter(CK_RSA_PKCS_PSS_PARAMS params) {
        assert(this.mechanism == CKM_RSA_PKCS_PSS);
        assert(params != null);
        if (this.pParameter != null && this.pParameter.equals(params)) {
            return;
        }
        freeHandle();
        this.pParameter = params;
    }

    public void freeHandle() {
        if (this.pHandle != 0L) {
            this.pHandle = PKCS11.freeMechanism(pHandle);
        }
    }

    private void init(long mechanism, Object pParameter) {
        this.mechanism = mechanism;
        this.pParameter = pParameter;
    }

    /**
     * Returns the string representation of CK_MECHANISM.
     *
     * @return the string representation of CK_MECHANISM
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(Constants.INDENT);
        buffer.append("mechanism: ");
        buffer.append(mechanism);
        buffer.append(Constants.NEWLINE);

        buffer.append(Constants.INDENT);
        buffer.append("pParameter: ");
        buffer.append(pParameter.toString());
        buffer.append(Constants.NEWLINE);

        /*
        buffer.append(Constants.INDENT);
        buffer.append("ulParameterLen: ??");
        buffer.append(Constants.NEWLINE);
        */
        if (pHandle != 0L) {
            buffer.append(Constants.INDENT);
            buffer.append("pHandle: ");
            buffer.append(pHandle);
            buffer.append(Constants.NEWLINE);
        }
        return buffer.toString() ;
    }
}