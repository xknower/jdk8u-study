package sun.security.x509;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.security.auth.x500.X500Principal;

import sun.security.util.*;

/**
 * This class defines the X500Name attribute for the Certificate.
 *
 * @author Amit Kapoor
 * @author Hemma Prafullchandra
 * @see CertAttrSet
 */
public class CertificateIssuerName implements CertAttrSet<String> {
    /**
     * Identifier for this attribute, to be used with the
     * get, set, delete methods of Certificate, x509 type.
     */
    public static final String IDENT = "x509.info.issuer";
    /**
     * Sub attributes name for this CertAttrSet.
     */
    public static final String NAME = "issuer";
    public static final String DN_NAME = "dname";

    // accessor name for cached X500Principal only
    // do not allow a set() of this value, do not advertise with getElements()
    public static final String DN_PRINCIPAL = "x500principal";

    // Private data member
    private X500Name    dnName;

    // cached X500Principal version of the name
    private X500Principal dnPrincipal;

    /**
     * Default constructor for the certificate attribute.
     *
     * @param name the X500Name
     */
    public CertificateIssuerName(X500Name name) {
        this.dnName = name;
    }

    /**
     * Create the object, decoding the values from the passed DER stream.
     *
     * @param in the DerInputStream to read the X500Name from.
     * @exception IOException on decoding errors.
     */
    public CertificateIssuerName(DerInputStream in) throws IOException {
        dnName = new X500Name(in);
    }

    /**
     * Create the object, decoding the values from the passed stream.
     *
     * @param in the InputStream to read the X500Name from.
     * @exception IOException on decoding errors.
     */
    public CertificateIssuerName(InputStream in) throws IOException {
        DerValue derVal = new DerValue(in);
        dnName = new X500Name(derVal);
    }

    /**
     * Return the name as user readable string.
     */
    public String toString() {
        if (dnName == null) return "";
        return(dnName.toString());
    }

    /**
     * Encode the name in DER form to the stream.
     *
     * @param out the DerOutputStream to marshal the contents to.
     * @exception IOException on errors.
     */
    public void encode(OutputStream out) throws IOException {
        DerOutputStream tmp = new DerOutputStream();
        dnName.encode(tmp);

        out.write(tmp.toByteArray());
    }

    /**
     * Set the attribute value.
     */
    public void set(String name, Object obj) throws IOException {
        if (!(obj instanceof X500Name)) {
            throw new IOException("Attribute must be of type X500Name.");
        }
        if (name.equalsIgnoreCase(DN_NAME)) {
            this.dnName = (X500Name)obj;
            this.dnPrincipal = null;
        } else {
            throw new IOException("Attribute name not recognized by " +
                                  "CertAttrSet:CertificateIssuerName.");
        }
    }

    /**
     * Get the attribute value.
     */
    public Object get(String name) throws IOException {
        if (name.equalsIgnoreCase(DN_NAME)) {
            return(dnName);
        } else if (name.equalsIgnoreCase(DN_PRINCIPAL)) {
            if ((dnPrincipal == null) && (dnName != null)) {
                dnPrincipal = dnName.asX500Principal();
            }
            return dnPrincipal;
        } else {
            throw new IOException("Attribute name not recognized by " +
                                  "CertAttrSet:CertificateIssuerName.");
        }
    }

    /**
     * Delete the attribute value.
     */
    public void delete(String name) throws IOException {
        if (name.equalsIgnoreCase(DN_NAME)) {
            dnName = null;
            dnPrincipal = null;
        } else {
            throw new IOException("Attribute name not recognized by " +
                                  "CertAttrSet:CertificateIssuerName.");
        }
    }

    /**
     * Return an enumeration of names of attributes existing within this
     * attribute.
     */
    public Enumeration<String> getElements() {
        AttributeNameEnumeration elements = new AttributeNameEnumeration();
        elements.addElement(DN_NAME);

        return (elements.elements());
    }

    /**
     * Return the name of this attribute.
     */
    public String getName() {
        return(NAME);
    }
}
