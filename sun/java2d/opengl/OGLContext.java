package sun.java2d.opengl;

import sun.java2d.pipe.BufferedContext;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.RenderQueue;
import sun.java2d.pipe.hw.ContextCapabilities;
import static sun.java2d.pipe.BufferedOpCodes.*;
import static sun.java2d.pipe.hw.ContextCapabilities.*;

import java.lang.annotation.Native;

/**
 * Note that the RenderQueue lock must be acquired before calling any of
 * the methods in this class.
 */
public class OGLContext extends BufferedContext {

    private final OGLGraphicsConfig config;

    OGLContext(RenderQueue rq, OGLGraphicsConfig config) {
        super(rq);
        this.config = config;
    }

    /**
     * Convenience method that delegates to setScratchSurface() below.
     */
    static void setScratchSurface(OGLGraphicsConfig gc) {
        setScratchSurface(gc.getNativeConfigInfo());
    }

    /**
     * Makes the given GraphicsConfig's context current to its associated
     * "scratch surface".  Each GraphicsConfig maintains a native context
     * (GLXContext on Unix, HGLRC on Windows) as well as a native pbuffer
     * known as the "scratch surface".  By making the context current to the
     * scratch surface, we are assured that we have a current context for
     * the relevant GraphicsConfig, and can therefore perform operations
     * depending on the capabilities of that GraphicsConfig.  For example,
     * if the GraphicsConfig supports the GL_ARB_texture_non_power_of_two
     * extension, then we should be able to make a non-pow2 texture for this
     * GraphicsConfig once we make the context current to the scratch surface.
     *
     * This method should be used for operations with an OpenGL texture
     * as the destination surface (e.g. a sw->texture blit loop), or in those
     * situations where we may not otherwise have a current context (e.g.
     * when disposing a texture-based surface).
     */
    static void setScratchSurface(long pConfigInfo) {
        // assert OGLRenderQueue.getInstance().lock.isHeldByCurrentThread();

        // invalidate the current context
        currentContext = null;

        // set the scratch context
        OGLRenderQueue rq = OGLRenderQueue.getInstance();
        RenderBuffer buf = rq.getBuffer();
        rq.ensureCapacityAndAlignment(12, 4);
        buf.putInt(SET_SCRATCH_SURFACE);
        buf.putLong(pConfigInfo);
    }

    /**
     * Invalidates the currentContext field to ensure that we properly
     * revalidate the OGLContext (make it current, etc.) next time through
     * the validate() method.  This is typically invoked from methods
     * that affect the current context state (e.g. disposing a context or
     * surface).
     */
    static void invalidateCurrentContext() {
        // assert OGLRenderQueue.getInstance().lock.isHeldByCurrentThread();

        // invalidate the current Java-level context so that we
        // revalidate everything the next time around
        if (currentContext != null) {
            currentContext.invalidateContext();
            currentContext = null;
        }

        // invalidate the context reference at the native level, and
        // then flush the queue so that we have no pending operations
        // dependent on the current context
        OGLRenderQueue rq = OGLRenderQueue.getInstance();
        rq.ensureCapacity(4);
        rq.getBuffer().putInt(INVALIDATE_CONTEXT);
        rq.flushNow();
    }

    public RenderQueue getRenderQueue() {
        return OGLRenderQueue.getInstance();
    }

    /**
     * Returns a string representing adapter id (vendor, renderer, version).
     * Must be called on the rendering thread.
     *
     * @return an id string for the adapter
     */
    static final native String getOGLIdString();

    @Override
    public void saveState() {
        // assert rq.lock.isHeldByCurrentThread();

        // reset all attributes of this and current contexts
        invalidateContext();
        invalidateCurrentContext();

        setScratchSurface(config);

        // save the state on the native level
        rq.ensureCapacity(4);
        buf.putInt(SAVE_STATE);
        rq.flushNow();
    }

    @Override
    public void restoreState() {
        // assert rq.lock.isHeldByCurrentThread();

        // reset all attributes of this and current contexts
        invalidateContext();
        invalidateCurrentContext();

        setScratchSurface(config);

        // restore the state on the native level
        rq.ensureCapacity(4);
        buf.putInt(RESTORE_STATE);
        rq.flushNow();
    }

    static class OGLContextCaps extends ContextCapabilities {
        /**
         * Indicates the presence of the GL_EXT_framebuffer_object extension.
         * This cap will only be set if the fbobject system property has been
         * enabled and we are able to create an FBO with depth buffer.
         */
        @Native
        static final int CAPS_EXT_FBOBJECT     =
                (CAPS_RT_TEXTURE_ALPHA | CAPS_RT_TEXTURE_OPAQUE);
        /** Indicates that the context is doublebuffered. */
        @Native
        static final int CAPS_DOUBLEBUFFERED   = (FIRST_PRIVATE_CAP << 0);
        /**
         * Indicates the presence of the GL_ARB_fragment_shader extension.
         * This cap will only be set if the lcdshader system property has been
         * enabled and the hardware supports the minimum number of texture units
         */
        @Native
        static final int CAPS_EXT_LCD_SHADER   = (FIRST_PRIVATE_CAP << 1);
        /**
         * Indicates the presence of the GL_ARB_fragment_shader extension.
         * This cap will only be set if the biopshader system property has been
         * enabled and the hardware meets our minimum requirements.
         */
        @Native
        static final int CAPS_EXT_BIOP_SHADER  = (FIRST_PRIVATE_CAP << 2);
        /**
         * Indicates the presence of the GL_ARB_fragment_shader extension.
         * This cap will only be set if the gradshader system property has been
         * enabled and the hardware meets our minimum requirements.
         */
        @Native
        static final int CAPS_EXT_GRAD_SHADER  = (FIRST_PRIVATE_CAP << 3);
        /** Indicates the presence of the GL_ARB_texture_rectangle extension. */
        @Native
        static final int CAPS_EXT_TEXRECT      = (FIRST_PRIVATE_CAP << 4);
        /** Indicates the presence of the GL_NV_texture_barrier extension. */
        @Native
        static final int CAPS_EXT_TEXBARRIER = (FIRST_PRIVATE_CAP << 5);


        OGLContextCaps(int caps, String adapterId) {
            super(caps, adapterId);
        }

        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer(super.toString());
            if ((caps & CAPS_EXT_FBOBJECT) != 0) {
                buf.append("CAPS_EXT_FBOBJECT|");
            }
            if ((caps & CAPS_DOUBLEBUFFERED) != 0) {
                buf.append("CAPS_DOUBLEBUFFERED|");
            }
            if ((caps & CAPS_EXT_LCD_SHADER) != 0) {
                buf.append("CAPS_EXT_LCD_SHADER|");
            }
            if ((caps & CAPS_EXT_BIOP_SHADER) != 0) {
                buf.append("CAPS_BIOP_SHADER|");
            }
            if ((caps & CAPS_EXT_GRAD_SHADER) != 0) {
                buf.append("CAPS_EXT_GRAD_SHADER|");
            }
            if ((caps & CAPS_EXT_TEXRECT) != 0) {
                buf.append("CAPS_EXT_TEXRECT|");
            }
            if ((caps & CAPS_EXT_TEXBARRIER) != 0) {
                buf.append("CAPS_EXT_TEXBARRIER|");
            }
            return buf.toString();
        }
    }
}
