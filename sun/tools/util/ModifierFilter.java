package sun.tools.util;

import sun.tools.java.*;


/**
 *   A class whose instances are filters over Modifier bits.
 *   Filtering is done by returning boolean values.
 *   Classes, methods and fields can be filtered, or filtering
 *   can be done directly on modifier bits.
 *
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 *
 *   @see java.lang.reflect.Modifier
 *   @author Robert Field
 */

public
class ModifierFilter extends java.lang.reflect.Modifier {

    /**
    * Package private access.
    * A "pseudo-" modifier bit that can be used in the
    * constructors of this class to specify package private
    * access. This is needed since there is no Modifier.PACKAGE.
    */
    public static final long PACKAGE = 0x8000000000000000L;

    /**
    * All access modifiers.
    * A short-hand set of modifier bits that can be used in the
    * constructors of this class to specify all access modifiers,
    * Same as PRIVATE | PROTECTED | PUBLIC | PACKAGE.
    */
    public static final long ALL_ACCESS =
                PRIVATE | PROTECTED | PUBLIC | PACKAGE;

    private long oneOf;
    private long must;
    private long cannot;

    private static final int ACCESS_BITS = PRIVATE | PROTECTED | PUBLIC;

    /**
     * Constructor - Specify a filter.
     *
     * @param   oneOf   If zero, everything passes the filter.
     *                  If non-zero, at least one of the specified
     *                  bits must be on in the modifier bits to
     *                  pass the filter.
     */
    public
    ModifierFilter(long oneOf) {
        this(oneOf, 0, 0);
    }

    /**
     * Constructor - Specify a filter.
     * For example, the filter below  will only pass synchronized
     * methods that are private or package private access and are
     * not native or static.
     * <pre>
     * ModifierFilter(  Modifier.PRIVATE | ModifierFilter.PACKAGE,
     *                  Modifier.SYNCHRONIZED,
     *                  Modifier.NATIVE | Modifier.STATIC)
     * </pre><p>
     * Each of the three arguments must either be
     * zero or the or'ed combination of the bits specified in the
     * class Modifier or this class. During filtering, these values
     * are compared against the modifier bits as follows:
     *
     * @param   oneOf   If zero, ignore this argument.
     *                  If non-zero, at least one of the bits must be on.
     * @param   must    All bits specified must be on.
     * @param   cannot  None of the bits specified can be on.
     */
    public
    ModifierFilter(long oneOf, long must, long cannot) {
        this.oneOf = oneOf;
        this.must = must;
        this.cannot = cannot;
    }

    /**
     * Filter on modifier bits.
     *
     * @param   modifierBits    Bits as specified in the Modifier class
     *
     * @return                  Whether the modifierBits pass this filter.
     */
    public boolean checkModifier(int modifierBits) {
        // Add in the "pseudo-" modifier bit PACKAGE, if needed
        long fmod = ((modifierBits & ACCESS_BITS) == 0) ?
                        modifierBits | PACKAGE :
                        modifierBits;
        return ((oneOf == 0) || ((oneOf & fmod) != 0)) &&
                ((must & fmod) == must) &&
                ((cannot & fmod) == 0);
    }

    /**
     * Filter a MemberDefinition.
     *
     * @param   field           A MemberDefinition
     *
     * @return                  Whether the modifier of the field
     *                          passes this filter.
     *
     * @see sun.tools.MemberDefinition
     */
    public boolean checkMember(MemberDefinition field) {
        return checkModifier(field.getModifiers());
    }

    /**
     * Filter a ClassDefinition.
     *
     * @param   cdef            A ClassDefinition
     *
     * @return                  Whether the modifier of the class
     *                          passes this filter.
     *
     * @see sun.tools.ClassDefinition
     */
    public boolean checkClass(ClassDefinition cdef) {
        return checkModifier(cdef.getModifiers());
    }

} // end ModifierFilter
