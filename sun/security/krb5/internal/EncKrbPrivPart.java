package sun.security.krb5.internal;

import sun.security.util.*;
import sun.security.krb5.Asn1Exception;
import java.util.Vector;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Implements the ASN.1 EncKrbPrivPart type.
 *
 * <pre>{@code
 * EncKrbPrivPart  ::= [APPLICATION 28] SEQUENCE {
 *         user-data       [0] OCTET STRING,
 *         timestamp       [1] KerberosTime OPTIONAL,
 *         usec            [2] Microseconds OPTIONAL,
 *         seq-number      [3] UInt32 OPTIONAL,
 *         s-address       [4] HostAddress -- sender's addr --,
 *         r-address       [5] HostAddress OPTIONAL -- recip's addr
 * }
 * }</pre>
 *
 * <p>
 * This definition reflects the Network Working Group RFC 4120
 * specification available at
 * <a href="http://www.ietf.org/rfc/rfc4120.txt">
 * http://www.ietf.org/rfc/rfc4120.txt</a>.
 */
public class EncKrbPrivPart {

    public byte[] userData = null;
    public KerberosTime timestamp; //optional
    public Integer usec; //optional
    public Integer seqNumber; //optional
    public HostAddress sAddress; //optional
    public HostAddress rAddress; //optional

    public EncKrbPrivPart(
            byte[] new_userData,
            KerberosTime new_timestamp,
            Integer new_usec,
            Integer new_seqNumber,
            HostAddress new_sAddress,
            HostAddress new_rAddress) {
        if (new_userData != null) {
            userData = new_userData.clone();
        }
        timestamp = new_timestamp;
        usec = new_usec;
        seqNumber = new_seqNumber;
        sAddress = new_sAddress;
        rAddress = new_rAddress;
    }

    public EncKrbPrivPart(byte[] data) throws Asn1Exception, IOException {
        init(new DerValue(data));
    }

    public EncKrbPrivPart(DerValue encoding) throws Asn1Exception, IOException {
        init(encoding);
    }

    /**
     * Initializes an EncKrbPrivPart object.
     * @param encoding a single DER-encoded value.
     * @exception Asn1Exception if an error occurs while decoding an ASN1 encoded data.
     * @exception IOException if an I/O error occurs while reading encoded data.
     */
    private void init(DerValue encoding) throws Asn1Exception, IOException {
        DerValue der, subDer;
        if (((encoding.getTag() & (byte) 0x1F) != (byte) 0x1C)
                || (encoding.isApplication() != true)
                || (encoding.isConstructed() != true)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        der = encoding.getData().getDerValue();
        if (der.getTag() != DerValue.tag_Sequence) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        subDer = der.getData().getDerValue();
        if ((subDer.getTag() & (byte) 0x1F) == (byte) 0x00) {
            userData = subDer.getData().getOctetString();
        } else {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        timestamp = KerberosTime.parse(der.getData(), (byte) 0x01, true);
        if ((der.getData().peekByte() & 0x1F) == 0x02) {
            subDer = der.getData().getDerValue();
            usec = new Integer(subDer.getData().getBigInteger().intValue());
        } else {
            usec = null;
        }
        if ((der.getData().peekByte() & 0x1F) == 0x03) {
            subDer = der.getData().getDerValue();
            seqNumber = new Integer(subDer.getData().getBigInteger().intValue());
        } else {
            seqNumber = null;
        }
        sAddress = HostAddress.parse(der.getData(), (byte) 0x04, false);
        if (der.getData().available() > 0) {
            rAddress = HostAddress.parse(der.getData(), (byte) 0x05, true);
        }
        if (der.getData().available() > 0) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
    }

    /**
     * Encodes an EncKrbPrivPart object.
     * @return byte array of encoded EncKrbPrivPart object.
     * @exception Asn1Exception if an error occurs while decoding an ASN1 encoded data.
     * @exception IOException if an I/O error occurs while reading encoded data.
     */
    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream temp = new DerOutputStream();
        DerOutputStream bytes = new DerOutputStream();

        temp.putOctetString(userData);
        bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte) 0x00), temp);
        if (timestamp != null) {
            bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte) 0x01), timestamp.asn1Encode());
        }
        if (usec != null) {
            temp = new DerOutputStream();
            temp.putInteger(BigInteger.valueOf(usec.intValue()));
            bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte) 0x02), temp);
        }
        if (seqNumber != null) {
            temp = new DerOutputStream();
            // encode as an unsigned integer (UInt32)
            temp.putInteger(BigInteger.valueOf(seqNumber.longValue()));
            bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte) 0x03), temp);
        }
        bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte) 0x04), sAddress.asn1Encode());
        if (rAddress != null) {
            bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte) 0x05), rAddress.asn1Encode());
        }
        temp = new DerOutputStream();
        temp.write(DerValue.tag_Sequence, bytes);
        bytes = new DerOutputStream();
        bytes.write(DerValue.createTag(DerValue.TAG_APPLICATION, true, (byte) 0x1C), temp);
        return bytes.toByteArray();
    }
}
