package sun.java2d.loops;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.lang.ref.WeakReference;
import sun.java2d.SurfaceData;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.Region;

/**
 * TransformBlit
 * 1) applies an AffineTransform to a rectangle of pixels while copying
 *    from one surface to another
 * 2) performs compositing of colors based upon a Composite
 *    parameter
 *
 * precise behavior is undefined if the source surface
 * and the destination surface are the same surface
 * with overlapping regions of pixels
 */

public class TransformBlit extends GraphicsPrimitive
{
    public static final String methodSignature =
        "TransformBlit(...)".toString();

    public static final int primTypeID = makePrimTypeID();

    private static RenderCache blitcache = new RenderCache(10);

    public static TransformBlit locate(SurfaceType srctype,
                                       CompositeType comptype,
                                       SurfaceType dsttype)
    {
        return (TransformBlit)
            GraphicsPrimitiveMgr.locate(primTypeID,
                                        srctype, comptype, dsttype);
    }

    public static TransformBlit getFromCache(SurfaceType src,
                                             CompositeType comp,
                                             SurfaceType dst)
    {
        Object o = blitcache.get(src, comp, dst);
        if (o != null) {
            return (TransformBlit) o;
        }
        TransformBlit blit = locate(src, comp, dst);
        if (blit == null) {
            /*
            System.out.println("blit loop not found for:");
            System.out.println("src:  "+src);
            System.out.println("comp: "+comp);
            System.out.println("dst:  "+dst);
            */
        } else {
            blitcache.put(src, comp, dst, blit);
        }
        return blit;
    }

    protected TransformBlit(SurfaceType srctype,
                            CompositeType comptype,
                            SurfaceType dsttype)
    {
        super(methodSignature, primTypeID, srctype, comptype, dsttype);
    }

    public TransformBlit(long pNativePrim,
                         SurfaceType srctype,
                         CompositeType comptype,
                         SurfaceType dsttype)
    {
        super(pNativePrim, methodSignature, primTypeID,
              srctype, comptype, dsttype);
    }

    public native void Transform(SurfaceData src, SurfaceData dst,
                                 Composite comp, Region clip,
                                 AffineTransform at, int hint,
                                 int srcx, int srcy, int dstx, int dsty,
                                 int width, int height);

    // REMIND: do we have a general loop?
    static {
        GraphicsPrimitiveMgr.registerGeneral(new TransformBlit(null, null,
                                                               null));
    }

    public GraphicsPrimitive makePrimitive(SurfaceType srctype,
                                           CompositeType comptype,
                                           SurfaceType dsttype)
    {
        /*
        System.out.println("Constructing general blit for:");
        System.out.println("src:  "+srctype);
        System.out.println("comp: "+comptype);
        System.out.println("dst:  "+dsttype);
        */
        return null;
    }

    public GraphicsPrimitive traceWrap() {
        return new TraceTransformBlit(this);
    }

    private static class TraceTransformBlit extends TransformBlit {
        TransformBlit target;

        public TraceTransformBlit(TransformBlit target) {
            super(target.getSourceType(),
                  target.getCompositeType(),
                  target.getDestType());
            this.target = target;
        }

        public GraphicsPrimitive traceWrap() {
            return this;
        }

        public void Transform(SurfaceData src, SurfaceData dst,
                              Composite comp, Region clip,
                              AffineTransform at, int hint,
                              int srcx, int srcy, int dstx, int dsty,
                              int width, int height)
        {
            tracePrimitive(target);
            target.Transform(src, dst, comp, clip, at, hint,
                             srcx, srcy, dstx, dsty, width, height);
        }
    }
}
