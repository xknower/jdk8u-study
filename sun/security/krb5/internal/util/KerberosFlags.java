package sun.security.krb5.internal.util;

import java.io.IOException;
import java.util.Arrays;
import sun.security.krb5.internal.Krb5;
import sun.security.util.BitArray;
import sun.security.util.DerOutputStream;

/**
 * A wrapper class around sun.security.util.BitArray, so that KDCOptions,
 * TicketFlags and ApOptions in krb5 classes can utilize some functions
 * in BitArray classes.
 *
 * The data type is defined in RFC 4120 as:
 *
 * 5.2.8.  KerberosFlags
 *
 *  For several message types, a specific constrained bit string type,
 *  KerberosFlags, is used.
 *
 *  KerberosFlags   ::= BIT STRING (SIZE (32..MAX))
 *                      -- minimum number of bits shall be sent,
 *                      -- but no fewer than 32
 *
 * @author Yanni Zhang
 */
public class KerberosFlags {
    BitArray bits;

    // This constant is used by child classes.
    protected static final int BITS_PER_UNIT = 8;

    public KerberosFlags(int length) throws IllegalArgumentException {
        bits = new BitArray(length);
    }

    public KerberosFlags(int length, byte[] a) throws IllegalArgumentException {
        bits = new BitArray(length, a);
        if (length != Krb5.KRB_FLAGS_MAX+1) {
            bits = new BitArray(Arrays.copyOf(bits.toBooleanArray(), Krb5.KRB_FLAGS_MAX+1));
        }
    }

    public KerberosFlags(boolean[] bools) {
        bits = new BitArray((bools.length==Krb5.KRB_FLAGS_MAX+1)?
            bools:
            Arrays.copyOf(bools, Krb5.KRB_FLAGS_MAX+1));
    }

    public void set(int index, boolean value) {
        bits.set(index, value);
    }

    public boolean get(int index) {
        return bits.get(index);
    }

    public boolean[] toBooleanArray() {
        return bits.toBooleanArray();
    }

    /**
     * Writes the encoded data.
     *
     * @exception IOException if an I/O error occurs while reading encoded data.
     * @return an byte array of encoded KDCOptions.
     */
    public byte[] asn1Encode() throws IOException {
        DerOutputStream out = new DerOutputStream();
        out.putUnalignedBitString(bits);
        return out.toByteArray();
    }

    public String toString() {
        return bits.toString();
    }
}
