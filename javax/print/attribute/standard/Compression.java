package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.DocAttribute;

/**
 * Class Compression is a printing attribute class, an enumeration, that
 * specifies how print data is compressed. Compression is an attribute of the
 * print data (the doc), not of the Print Job. If a Compression attribute is not
 * specified for a doc, the printer assumes the doc's print data is uncompressed
 * (i.e., the default Compression value is always {@link #NONE
 * NONE}).
 * <P>
 * <B>IPP Compatibility:</B> The category name returned by
 * <CODE>getName()</CODE> is the IPP attribute name.  The enumeration's
 * integer value is the IPP enum value.  The <code>toString()</code> method
 * returns the IPP string representation of the attribute value.
 * <P>
 *
 * @author  Alan Kaminsky
 */
public class Compression extends EnumSyntax implements DocAttribute {

    private static final long serialVersionUID = -5716748913324997674L;

    /**
     * No compression is used.
     */
    public static final Compression NONE = new Compression(0);

    /**
     * ZIP public domain inflate/deflate compression technology.
     */
    public static final Compression DEFLATE = new Compression(1);

    /**
     * GNU zip compression technology described in
     * <A HREF="http://www.ietf.org/rfc/rfc1952.txt">RFC 1952</A>.
     */
    public static final Compression GZIP = new Compression(2);

    /**
     * UNIX compression technology.
     */
    public static final Compression COMPRESS = new Compression(3);

    /**
     * Construct a new compression enumeration value with the given integer
     * value.
     *
     * @param  value  Integer value.
     */
    protected Compression(int value) {
        super(value);
    }


    private static final String[] myStringTable = {"none",
                                                   "deflate",
                                                   "gzip",
                                                   "compress"};

    private static final Compression[] myEnumValueTable = {NONE,
                                                           DEFLATE,
                                                           GZIP,
                                                           COMPRESS};

    /**
     * Returns the string table for class Compression.
     */
    protected String[] getStringTable() {
        return (String[])myStringTable.clone();
    }

    /**
     * Returns the enumeration value table for class Compression.
     */
    protected EnumSyntax[] getEnumValueTable() {
        return (EnumSyntax[])myEnumValueTable.clone();
    }

    /**
     * Get the printing attribute class which is to be used as the "category"
     * for this printing attribute value.
     * <P>
     * For class Compression and any vendor-defined subclasses, the category is
     * class Compression itself.
     *
     * @return  Printing attribute class (category), an instance of class
     *          {@link java.lang.Class java.lang.Class}.
     */
    public final Class<? extends Attribute> getCategory() {
        return Compression.class;
    }

    /**
     * Get the name of the category of which this attribute value is an
     * instance.
     * <P>
     * For class Compression and any vendor-defined subclasses, the category
     * name is <CODE>"compression"</CODE>.
     *
     * @return  Attribute category name.
     */
    public final String getName() {
        return "compression";
    }

}
