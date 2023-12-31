package sun.font;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;

import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* FontScaler is "internal interface" to font rasterizer library.
 *
 * Access to native rasterizers without going through this interface is
 * strongly discouraged. In particular, this is important because native
 * data could be disposed due to runtime font processing error at any time.
 *
 * FontScaler represents combination of particular rasterizer implementation
 * and particular font. It does not include rasterization attributes such as
 * transform. These attributes are part of native scalerContext object.
 * This approach allows to share same scaler for different requests related
 * to the same font file.
 *
 * Note that scaler may throw FontScalerException on any operation.
 * Generally this means that runtime error had happened and scaler is not
 * usable.  Subsequent calls to this scaler should not cause crash but will
 * likely cause exceptions to be thrown again.
 *
 * It is recommended that callee should replace its reference to the scaler
 * with something else. For instance it could be FontManager.getNullScaler().
 * Note that NullScaler is trivial and will not actually rasterize anything.
 *
 * Alternatively, callee can use more sophisticated error recovery strategies
 * and for instance try to substitute failed scaler with new scaler instance
 * using another font.
 *
 * Note that in case of error there is no need to call dispose(). Moreover,
 * dispose() generally is called by Disposer thread and explicit calls to
 * dispose might have unexpected sideeffects because scaler can be shared.
 *
 * Current disposing logic is the following:
 *   - scaler is registered in the Disposer by the FontManager (on creation)
 *   - scalers are disposed when associated Font2D object (e.g. TruetypeFont)
 *     is garbage collected. That's why this object implements DisposerRecord
 *     interface directly (as it is not used as indicator when it is safe
 *     to release native state) and that's why we have to use WeakReference
 *     to Font internally.
 *   - Majority of Font2D objects are linked from various mapping arrays
 *     (e.g. FontManager.localeFullNamesToFont). So, they are not collected.
 *     This logic only works for fonts created with Font.createFont()
 *
 *  Notes:
 *   - Eventually we may consider releasing some of the scaler resources if
 *     it was not used for a while but we do not want to be too aggressive on
 *     this (and this is probably more important for Type1 fonts).
 */
public abstract class FontScaler implements DisposerRecord {

    private static FontScaler nullScaler = null;
    private static Constructor<FontScaler> scalerConstructor = null;

    //Find preferred font scaler
    //
    //NB: we can allow property based preferences
    //   (theoretically logic can be font type specific)
    static {
        Class scalerClass = null;
        Class arglst[] = new Class[] {Font2D.class, int.class,
        boolean.class, int.class};

        try {
            if (FontUtilities.isOpenJDK) {
                scalerClass = Class.forName("sun.font.FreetypeFontScaler");
            } else {
                scalerClass = Class.forName("sun.font.T2KFontScaler");
            }
        } catch (ClassNotFoundException e) {
                scalerClass = NullFontScaler.class;
        }

        //NB: rewrite using factory? constructor is ugly way
        try {
            scalerConstructor = scalerClass.getConstructor(arglst);
        } catch (NoSuchMethodException e) {
            //should not happen
        }
    }

    /* This is the only place to instantiate new FontScaler.
     * Therefore this is very convinient place to register
     * scaler with Disposer as well as trigger deregistring bad font
     * in case when scaler reports this.
     */
    public static FontScaler getScaler(Font2D font,
                                int indexInCollection,
                                boolean supportsCJK,
                                int filesize) {
        FontScaler scaler = null;

        try {
            Object args[] = new Object[] {font, indexInCollection,
                                          supportsCJK, filesize};
            scaler = scalerConstructor.newInstance(args);
            Disposer.addObjectRecord(font, scaler);
        } catch (Throwable e) {
            scaler = nullScaler;

            //if we can not instantiate scaler assume bad font
            //NB: technically it could be also because of internal scaler
            //    error but here we are assuming scaler is ok.
            FontManager fm = FontManagerFactory.getInstance();
            fm.deRegisterBadFont(font);
        }
        return scaler;
    }

    /*
     * At the moment it is harmless to create 2 null scalers so, technically,
     * syncronized keyword is not needed.
     *
     * But it is safer to keep it to avoid subtle problems if we will be adding
     * checks like whether scaler is null scaler.
     */
    public static synchronized FontScaler getNullScaler() {
        if (nullScaler == null) {
            nullScaler = new NullFontScaler();
        }
        return nullScaler;
    }

    protected WeakReference<Font2D> font = null;
    protected long nativeScaler = 0; //used by decendants
                                     //that have native state
    protected boolean disposed = false;

    abstract StrikeMetrics getFontMetrics(long pScalerContext)
                throws FontScalerException;

    abstract float getGlyphAdvance(long pScalerContext, int glyphCode)
                throws FontScalerException;

    abstract void getGlyphMetrics(long pScalerContext, int glyphCode,
                                  Point2D.Float metrics)
                throws FontScalerException;

    /*
     *  Returns pointer to native GlyphInfo object.
     *  Callee is responsible for freeing this memory.
     *
     *  Note:
     *   currently this method has to return not 0L but pointer to valid
     *   GlyphInfo object. Because Strike and drawing releated logic does
     *   expect that.
     *   In the future we may want to rework this to allow 0L here.
     */
    abstract long getGlyphImage(long pScalerContext, int glyphCode)
                throws FontScalerException;

    abstract Rectangle2D.Float getGlyphOutlineBounds(long pContext,
                                                     int glyphCode)
                throws FontScalerException;

    abstract GeneralPath getGlyphOutline(long pScalerContext, int glyphCode,
                                         float x, float y)
                throws FontScalerException;

    abstract GeneralPath getGlyphVectorOutline(long pScalerContext, int[] glyphs,
                                               int numGlyphs, float x, float y)
                throws FontScalerException;

    /* Used by Java2D disposer to ensure native resources are released.
       Note: this method does not release any of created
             scaler context objects! */
    public void dispose() {}

    /**
     * Used when the native resources held by the scaler need
     * to be released before the 2D disposer runs.
     */
    public void disposeScaler() {}

    /* At the moment these 3 methods are needed for Type1 fonts only.
     * For Truetype fonts we extract required info outside of scaler
     * on java layer.
     */
    abstract int getNumGlyphs() throws FontScalerException;
    abstract int getMissingGlyphCode() throws FontScalerException;
    abstract int getGlyphCode(char charCode) throws FontScalerException;

    /* This method returns table cache used by native layout engine.
     * This cache is essentially just small collection of
     * pointers to various truetype tables. See definition of TTLayoutTableCache
     * in the fontscalerdefs.h for more details.
     *
     * Note that tables themselves have same format as defined in the truetype
     * specification, i.e. font scaler do not need to perform any preprocessing.
     *
     * Probably it is better to have API to request pointers to each table
     * separately instead of requesting pointer to some native structure.
     * (then there is not need to share its definition by different
     * implementations of scaler).
     * However, this means multiple JNI calls and potential impact on performance.
     *
     * Note: return value 0 is legal.
     *   This means tables are not available (e.g. type1 font).
     */
    abstract long getLayoutTableCache() throws FontScalerException;

    /* Used by the OpenType engine for mark positioning. */
    abstract Point2D.Float getGlyphPoint(long pScalerContext,
                                int glyphCode, int ptNumber)
        throws FontScalerException;

    abstract long getUnitsPerEm();

    /* Returns pointer to native structure describing rasterization attributes.
       Format of this structure is scaler-specific.

       Callee is responsible for freeing scaler context (using free()).

       Note:
         Context is tightly associated with strike and it is actually
        freed when corresponding strike is being released.
     */
    abstract long createScalerContext(double[] matrix,
                                      int aa, int fm,
                                      float boldness, float italic,
                                      boolean disableHinting);

    /* Marks context as invalid because native scaler is invalid.
       Notes:
         - pointer itself is still valid and has to be released
         - if pointer to native scaler was cached it
           should not be neither disposed nor used.
           it is very likely it is already disposed by this moment. */
    abstract void invalidateScalerContext(long ppScalerContext);
}
