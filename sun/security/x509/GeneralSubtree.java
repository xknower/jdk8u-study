package sun.security.x509;

import java.io.*;

import sun.security.util.*;

/**
 * Represent the GeneralSubtree ASN.1 object, whose syntax is:
 * <pre>
 * GeneralSubtree ::= SEQUENCE {
 *    base             GeneralName,
 *    minimum  [0]     BaseDistance DEFAULT 0,
 *    maximum  [1]     BaseDistance OPTIONAL
 * }
 * BaseDistance ::= INTEGER (0..MAX)
 * </pre>
 * @author Amit Kapoor
 * @author Hemma Prafullchandra
 */
public class GeneralSubtree {
    private static final byte TAG_MIN = 0;
    private static final byte TAG_MAX = 1;
    private static final int  MIN_DEFAULT = 0;

    private GeneralName name;
    private int         minimum = MIN_DEFAULT;
    private int         maximum = -1;

    private int myhash = -1;

    /**
     * The default constructor for the class.
     *
     * @params name the GeneralName
     * @params min the minimum BaseDistance
     * @params max the maximum BaseDistance
     */
    public GeneralSubtree(GeneralName name, int min, int max) {
        this.name = name;
        this.minimum = min;
        this.maximum = max;
    }

    /**
     * Create the object from its DER encoded form.
     *
     * @param val the DER encoded from of the same.
     */
    public GeneralSubtree(DerValue val) throws IOException {
        if (val.tag != DerValue.tag_Sequence) {
            throw new IOException("Invalid encoding for GeneralSubtree.");
        }
        name = new GeneralName(val.data.getDerValue(), true);

        // NB. this is always encoded with the IMPLICIT tag
        // The checks only make sense if we assume implicit tagging,
        // with explicit tagging the form is always constructed.
        while (val.data.available() != 0) {
            DerValue opt = val.data.getDerValue();

            if (opt.isContextSpecific(TAG_MIN) && !opt.isConstructed()) {
                opt.resetTag(DerValue.tag_Integer);
                minimum = opt.getInteger();

            } else if (opt.isContextSpecific(TAG_MAX) && !opt.isConstructed()) {
                opt.resetTag(DerValue.tag_Integer);
                maximum = opt.getInteger();
            } else
                throw new IOException("Invalid encoding of GeneralSubtree.");
        }
    }

    /**
     * Return the GeneralName.
     *
     * @return the GeneralName
     */
    public GeneralName getName() {
        //XXXX May want to consider cloning this
        return name;
    }

    /**
     * Return the minimum BaseDistance.
     *
     * @return the minimum BaseDistance. Default is 0 if not set.
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Return the maximum BaseDistance.
     *
     * @return the maximum BaseDistance, or -1 if not set.
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Return a printable string of the GeneralSubtree.
     */
    public String toString() {
        String s = "\n   GeneralSubtree: [\n" +
            "    GeneralName: " + ((name == null) ? "" : name.toString()) +
            "\n    Minimum: " + minimum;
            if (maximum == -1) {
                s += "\t    Maximum: undefined";
            } else
                s += "\t    Maximum: " + maximum;
            s += "    ]\n";
        return (s);
    }

    /**
     * Compare this GeneralSubtree with another
     *
     * @param other GeneralSubtree to compare to this
     * @returns true if match
     */
    public boolean equals(Object other) {
        if (!(other instanceof GeneralSubtree))
            return false;
        GeneralSubtree otherGS = (GeneralSubtree)other;
        if (this.name == null) {
            if (otherGS.name != null) {
                return false;
            }
        } else {
            if (!((this.name).equals(otherGS.name)))
                return false;
        }
        if (this.minimum != otherGS.minimum)
            return false;
        if (this.maximum != otherGS.maximum)
            return false;
        return true;
    }

    /**
     * Returns the hash code for this GeneralSubtree.
     *
     * @return a hash code value.
     */
    public int hashCode() {
        if (myhash == -1) {
            myhash = 17;
            if (name != null) {
                myhash = 37 * myhash + name.hashCode();
            }
            if (minimum != MIN_DEFAULT) {
                myhash = 37 * myhash + minimum;
            }
            if (maximum != -1) {
                myhash = 37 * myhash + maximum;
            }
        }
        return myhash;
    }

    /**
     * Encode the GeneralSubtree.
     *
     * @params out the DerOutputStream to encode this object to.
     */
    public void encode(DerOutputStream out) throws IOException {
        DerOutputStream seq = new DerOutputStream();

        name.encode(seq);

        if (minimum != MIN_DEFAULT) {
            DerOutputStream tmp = new DerOutputStream();
            tmp.putInteger(minimum);
            seq.writeImplicit(DerValue.createTag(DerValue.TAG_CONTEXT,
                              false, TAG_MIN), tmp);
        }
        if (maximum != -1) {
            DerOutputStream tmp = new DerOutputStream();
            tmp.putInteger(maximum);
            seq.writeImplicit(DerValue.createTag(DerValue.TAG_CONTEXT,
                              false, TAG_MAX), tmp);
        }
        out.write(DerValue.tag_Sequence, seq);
    }
}
