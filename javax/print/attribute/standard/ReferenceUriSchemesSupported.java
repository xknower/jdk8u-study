package javax.print.attribute.standard;

import javax.print.attribute.EnumSyntax;
import javax.print.attribute.Attribute;

/**
 * Class ReferenceUriSchemesSupported is a printing attribute class
 * an enumeration, that indicates a "URI scheme," such as "http:" or "ftp:",
 * that a printer can use to retrieve print data stored at a URI location.
 * If a printer supports doc flavors with a print data representation class of
 * <CODE>"java.net.URL"</CODE>, the printer uses instances of class
 * ReferenceUriSchemesSupported to advertise the URI schemes it can accept.
 * The acceptable URI schemes are included as service attributes in the
 * lookup service; this lets clients search the
 * for printers that can get print data using a certain URI scheme. The
 * acceptable URI schemes can also be queried using the capability methods in
 * interface <code>PrintService</code>. However,
 * ReferenceUriSchemesSupported attributes are used solely for determining
 * acceptable URI schemes, they are never included in a doc's,
 * print request's, print job's, or print service's attribute set.
 * <P>
 * The Internet Assigned Numbers Authority maintains the
 * <A HREF="http://www.iana.org/assignments/uri-schemes.html">official
 * list of URI schemes</A>.
 * <p>
 * Class ReferenceUriSchemesSupported defines enumeration values for widely
 * used URI schemes. A printer that supports additional URI schemes
 * can define them in a subclass of class ReferenceUriSchemesSupported.
 * <P>
 * <B>IPP Compatibility:</B>  The category name returned by
 * <CODE>getName()</CODE> is the IPP attribute name.  The enumeration's
 * integer value is the IPP enum value.  The <code>toString()</code> method
 * returns the IPP string representation of the attribute value.
 * <P>
 *
 * @author  Alan Kaminsky
 */
public class ReferenceUriSchemesSupported
    extends EnumSyntax implements Attribute {

    private static final long serialVersionUID = -8989076942813442805L;

    /**
     * File Transfer Protocol (FTP).
     */
    public static final ReferenceUriSchemesSupported FTP =
        new ReferenceUriSchemesSupported(0);

    /**
     * HyperText Transfer Protocol (HTTP).
     */
    public static final ReferenceUriSchemesSupported HTTP = new ReferenceUriSchemesSupported(1);

    /**
     * Secure HyperText Transfer Protocol (HTTPS).
     */
    public static final ReferenceUriSchemesSupported HTTPS = new ReferenceUriSchemesSupported(2);

    /**
     * Gopher Protocol.
     */
    public static final ReferenceUriSchemesSupported GOPHER = new ReferenceUriSchemesSupported(3);

    /**
     * USENET news.
     */
    public static final ReferenceUriSchemesSupported NEWS = new ReferenceUriSchemesSupported(4);

    /**
     * USENET news using Network News Transfer Protocol (NNTP).
     */
    public static final ReferenceUriSchemesSupported NNTP = new ReferenceUriSchemesSupported(5);

    /**
     * Wide Area Information Server (WAIS) protocol.
     */
    public static final ReferenceUriSchemesSupported WAIS = new ReferenceUriSchemesSupported(6);

    /**
     * Host-specific file names.
     */
    public static final ReferenceUriSchemesSupported FILE = new ReferenceUriSchemesSupported(7);

    /**
     * Construct a new reference URI scheme enumeration value with the given
     * integer value.
     *
     * @param  value  Integer value.
     */
    protected ReferenceUriSchemesSupported(int value) {
        super (value);
    }

    private static final String[] myStringTable = {
        "ftp",
        "http",
        "https",
        "gopher",
        "news",
        "nntp",
        "wais",
        "file",
    };

    private static final ReferenceUriSchemesSupported[] myEnumValueTable = {
        FTP,
        HTTP,
        HTTPS,
        GOPHER,
        NEWS,
        NNTP,
        WAIS,
        FILE,
    };

    /**
     * Returns the string table for class ReferenceUriSchemesSupported.
     */
    protected String[] getStringTable() {
        return myStringTable.clone();
    }

    /**
     * Returns the enumeration value table for class
     * ReferenceUriSchemesSupported.
     */
    protected EnumSyntax[] getEnumValueTable() {
        return (EnumSyntax[])myEnumValueTable.clone();
    }

    /**
     * Get the printing attribute class which is to be used as the "category"
     * for this printing attribute value.
     * <P>
     * For class ReferenceUriSchemesSupported and any vendor-defined
     * subclasses, the category is class ReferenceUriSchemesSupported itself.
     *
     * @return  Printing attribute class (category), an instance of class
     *          {@link java.lang.Class java.lang.Class}.
     */
    public final Class<? extends Attribute> getCategory() {
        return ReferenceUriSchemesSupported.class;
    }

    /**
     * Get the name of the category of which this attribute value is an
     * instance.
     * <P>
     * For class ReferenceUriSchemesSupported and any vendor-defined
     * subclasses, the category name is
     * <CODE>"reference-uri-schemes-supported"</CODE>.
     *
     * @return  Attribute category name.
     */
    public final String getName() {
        return "reference-uri-schemes-supported";
    }

}
