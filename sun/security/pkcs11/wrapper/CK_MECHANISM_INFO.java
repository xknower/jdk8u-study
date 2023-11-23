package sun.security.pkcs11.wrapper;

import java.security.ProviderException;

/**
 * class CK_MECHANISM_INFO provides information about a particular mechanism.
 * <p>
 * <B>PKCS#11 structure:</B>
 * <PRE>
 * typedef struct CK_MECHANISM_INFO {&nbsp;&nbsp;
 *   CK_ULONG ulMinKeySize;&nbsp;&nbsp;
 *   CK_ULONG ulMaxKeySize;&nbsp;&nbsp;
 *   CK_FLAGS flags;&nbsp;&nbsp;
 * } CK_MECHANISM_INFO;
 * </PRE>
 *
 * @author Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
 * @author Martin Schlaeffer <schlaeff@sbox.tugraz.at>
 */
public class CK_MECHANISM_INFO {

    /**
     * <B>PKCS#11:</B>
     * <PRE>
     *   CK_ULONG ulMinKeySize;
     * </PRE>
     */
    public long ulMinKeySize;

    // the integer version of ulMinKeySize for doing the actual range
    // check in SunPKCS11 provider, defaults to 0
    public final int iMinKeySize;

    /**
     * <B>PKCS#11:</B>
     * <PRE>
     *   CK_ULONG ulMaxKeySize;
     * </PRE>
     */
    public long ulMaxKeySize;

    // the integer version of ulMaxKeySize for doing the actual range
    // check in SunPKCS11 provider, defaults to Integer.MAX_VALUE
    public final int iMaxKeySize;

    /**
     * <B>PKCS#11:</B>
     * <PRE>
     *   CK_FLAGS flags;
     * </PRE>
     */
    public long flags;

    public CK_MECHANISM_INFO(long minKeySize, long maxKeySize,
                             long flags) {
        this.ulMinKeySize = minKeySize;
        this.ulMaxKeySize = maxKeySize;
        this.iMinKeySize = ((minKeySize < Integer.MAX_VALUE && minKeySize > 0)?
                (int)minKeySize : 0);
        this.iMaxKeySize = ((maxKeySize < Integer.MAX_VALUE && maxKeySize > 0)?
                (int)maxKeySize : Integer.MAX_VALUE);
        this.flags = flags;
    }

    /**
     * Returns the string representation of CK_MECHANISM_INFO.
     *
     * @return the string representation of CK_MECHANISM_INFO
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(Constants.INDENT);
        buffer.append("ulMinKeySize: ");
        buffer.append(String.valueOf(ulMinKeySize));
        buffer.append(Constants.NEWLINE);

        buffer.append(Constants.INDENT);
        buffer.append("ulMaxKeySize: ");
        buffer.append(String.valueOf(ulMaxKeySize));
        buffer.append(Constants.NEWLINE);

        buffer.append(Constants.INDENT);
        buffer.append("flags: ");
        buffer.append(String.valueOf(flags));
        buffer.append(" = ");
        buffer.append(Functions.mechanismInfoFlagsToString(flags));
        //buffer.append(Constants.NEWLINE);

        return buffer.toString() ;
    }
}