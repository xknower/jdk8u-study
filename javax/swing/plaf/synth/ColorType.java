package javax.swing.plaf.synth;

/**
 * A typesafe enumeration of colors that can be fetched from a style.
 * <p>
 * Each <code>SynthStyle</code> has a set of <code>ColorType</code>s that
 * are accessed by way of the
 * {@link SynthStyle#getColor(SynthContext, ColorType)} method.
 * <code>SynthStyle</code>'s <code>installDefaults</code> will install
 * the <code>FOREGROUND</code> color
 * as the foreground of
 * the Component, and the <code>BACKGROUND</code> color to the background of
 * the component (assuming that you have not explicitly specified a
 * foreground and background color). Some components
 * support more color based properties, for
 * example <code>JList</code> has the property
 * <code>selectionForeground</code> which will be mapped to
 * <code>FOREGROUND</code> with a component state of
 * <code>SynthConstants.SELECTED</code>.
 * <p>
 * The following example shows a custom <code>SynthStyle</code> that returns
 * a red Color for the <code>DISABLED</code> state, otherwise a black color.
 * <pre>
 * class MyStyle extends SynthStyle {
 *     private Color disabledColor = new ColorUIResource(Color.RED);
 *     private Color color = new ColorUIResource(Color.BLACK);
 *     protected Color getColorForState(SynthContext context, ColorType type){
 *         if (context.getComponentState() == SynthConstants.DISABLED) {
 *             return disabledColor;
 *         }
 *         return color;
 *     }
 * }
 * </pre>
 *
 * @since 1.5
 * @author Scott Violet
 */
public class ColorType {
    /**
     * ColorType for the foreground of a region.
     */
    public static final ColorType FOREGROUND = new ColorType("Foreground");

    /**
     * ColorType for the background of a region.
     */
    public static final ColorType BACKGROUND = new ColorType("Background");

    /**
     * ColorType for the foreground of a region.
     */
    public static final ColorType TEXT_FOREGROUND = new ColorType(
                                       "TextForeground");

    /**
     * ColorType for the background of a region.
     */
    public static final ColorType TEXT_BACKGROUND =new ColorType(
                                       "TextBackground");

    /**
     * ColorType for the focus.
     */
    public static final ColorType FOCUS = new ColorType("Focus");

    /**
     * Maximum number of <code>ColorType</code>s.
     */
    public static final int MAX_COUNT;

    private static int nextID;

    private String description;
    private int index;

    static {
        MAX_COUNT = Math.max(FOREGROUND.getID(), Math.max(
                                 BACKGROUND.getID(), FOCUS.getID())) + 1;
    }

    /**
     * Creates a new ColorType with the specified description.
     *
     * @param description String description of the ColorType.
     */
    protected ColorType(String description) {
        if (description == null) {
            throw new NullPointerException(
                          "ColorType must have a valid description");
        }
        this.description = description;
        synchronized(ColorType.class) {
            this.index = nextID++;
        }
    }

    /**
     * Returns a unique id, as an integer, for this ColorType.
     *
     * @return a unique id, as an integer, for this ColorType.
     */
    public final int getID() {
        return index;
    }

    /**
     * Returns the textual description of this <code>ColorType</code>.
     * This is the same value that the <code>ColorType</code> was created
     * with.
     *
     * @return the description of the string
     */
    public String toString() {
        return description;
    }
}
