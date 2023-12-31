package sun.security.krb5.internal;

import sun.security.util.*;
import sun.security.krb5.Asn1Exception;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Implements the ASN.1 TransitedEncoding type.
 *
 * <pre>{@code
 *  TransitedEncoding      ::= SEQUENCE {
 *         tr-type         [0] Int32 -- must be registered --,
 *         contents        [1] OCTET STRING
 *  }
 * }</pre>
 *
 * <p>
 * This definition reflects the Network Working Group RFC 4120
 * specification available at
 * <a href="http://www.ietf.org/rfc/rfc4120.txt">
 * http://www.ietf.org/rfc/rfc4120.txt</a>.
 */

public class TransitedEncoding {
    public int trType;
    public byte[] contents;

    public TransitedEncoding(int type, byte[] cont) {
        trType = type;
        contents = cont;
    }

    /**
     * Constructs a TransitedEncoding object.
     * @param encoding a Der-encoded data.
     * @exception Asn1Exception if an error occurs while decoding an ASN1 encoded data.
     * @exception IOException if an I/O error occurs while reading encoded data.
     */

    public TransitedEncoding(DerValue encoding) throws Asn1Exception, IOException {
        if (encoding.getTag() != DerValue.tag_Sequence) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue der;
        der = encoding.getData().getDerValue();
        if ((der.getTag() & 0x1F) == 0x00) {
            trType = der.getData().getBigInteger().intValue();
        }
        else
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        der = encoding.getData().getDerValue();

        if ((der.getTag() & 0x1F) == 0x01) {
            contents = der.getData().getOctetString();
        }
        else
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        if (der.getData().available() > 0)
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    /**
     * Encodes a TransitedEncoding object.
     * @return the byte array of the encoded TransitedEncoding object.
     * @exception Asn1Exception if an error occurs while decoding an ASN1 encoded data.
     * @exception IOException if an I/O error occurs while reading encoded data.
     */
    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream bytes = new DerOutputStream();
        DerOutputStream temp = new DerOutputStream();
        temp.putInteger(BigInteger.valueOf(trType));
        bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte)0x00), temp);
        temp = new DerOutputStream();
        temp.putOctetString(contents);
        bytes.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte)0x01), temp);
        temp = new DerOutputStream();
        temp.write(DerValue.tag_Sequence, bytes);
        return temp.toByteArray();
    }

    /**
     * Parse (unmarshal) a TransitedEncoding object from a DER input stream.  This form
     * parsing might be used when expanding a value which is part of
     * a constructed sequence and uses explicitly tagged type.
     *
     * @exception Asn1Exception on error.
     * @param data the Der input stream value, which contains one or more marshaled value.
     * @param explicitTag tag number.
     * @param optional indicate if this data field is optional
     * @return an instance of TransitedEncoding.
     *
     */
    public static TransitedEncoding parse(DerInputStream data, byte explicitTag, boolean optional) throws Asn1Exception, IOException {
        if ((optional) && (((byte)data.peekByte() & (byte)0x1F) != explicitTag))
            return null;
        DerValue der = data.getDerValue();
        if (explicitTag != (der.getTag() & (byte)0x1F))  {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        else {
            DerValue subDer = der.getData().getDerValue();
            return new TransitedEncoding(subDer);
        }
    }
}
