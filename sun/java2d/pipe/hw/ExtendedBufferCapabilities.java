package sun.java2d.pipe.hw;

import java.awt.BufferCapabilities;
import java.awt.ImageCapabilities;

/**
 * Provides extended BufferStrategy capabilities, allowing to specify
 * the type of vertical refresh synchronization for a buffer strategy.
 *
 * This BS capability is always page flipping because v-sync is only relevant
 * to flipping buffer strategies.
 *
 * Note that asking for a v-synced BS doesn't necessarily guarantee that it will
 * be v-synced since the vsync capability may be disabled in the driver, or
 * there may be other restriction (like a number of v-synced buffer strategies
 * allowed per vm). Because of this {@code createBufferStrategy} doesn't
 * throw {@code AWTException} when a v-synced BS could not be created when
 * requested.
 *
 * @see java.awt.Canvas#createBufferStrategy(int, BufferCapabilities)
 * @see java.awt.Window#createBufferStrategy(int, BufferCapabilities)
 */
public class ExtendedBufferCapabilities extends BufferCapabilities {

    /**
     * Type of synchronization on vertical retrace.
     */
    public static enum VSyncType {
        /**
         * Use the default v-sync mode appropriate for given BufferStrategy
         * and situation.
         */
        VSYNC_DEFAULT(0),

        /**
         * Synchronize flip on vertical retrace.
         */
        VSYNC_ON(1),

        /**
         * Do not synchronize flip on vertical retrace.
         */
        VSYNC_OFF(2);

        /**
         * Used to identify the v-sync type (independent of the constants
         * order as opposed to {@code ordinal()}).
         */
        public int id() {
            return id;
        }

        private VSyncType(int id) {
            this.id = id;
        }
        private int id;
    }

    private VSyncType vsync;

    /**
     * Creates an ExtendedBufferCapabilities object with front/back/flip caps
     * from the passed cap, and VSYNC_DEFAULT v-sync mode.
     */
    public ExtendedBufferCapabilities(BufferCapabilities caps) {
        super(caps.getFrontBufferCapabilities(),
              caps.getBackBufferCapabilities(),
              caps.getFlipContents());

        this.vsync = VSyncType.VSYNC_DEFAULT;
    }

    /**
     * Creates an ExtendedBufferCapabilities instance with front/back/flip caps
     * from the passed caps, and VSYNC_DEFAULT v-sync mode.
     */
    public ExtendedBufferCapabilities(ImageCapabilities front,
                                      ImageCapabilities back, FlipContents flip)
    {
        super(front, back, flip);

        this.vsync = VSyncType.VSYNC_DEFAULT;
    }

    /**
     * Creates an ExtendedBufferCapabilities instance with front/back/flip caps
     * from the passed image/flip caps, and the v-sync type.
     */
    public ExtendedBufferCapabilities(ImageCapabilities front,
                                      ImageCapabilities back, FlipContents flip,
                                      VSyncType t)
    {
        super(front, back, flip);

        this.vsync = t;
    }

    /**
     * Creates an ExtendedBufferCapabilities instance with front/back/flip caps
     * from the passed cap, and the passed v-sync mode.
     */
    public ExtendedBufferCapabilities(BufferCapabilities caps, VSyncType t) {
        super(caps.getFrontBufferCapabilities(),
              caps.getBackBufferCapabilities(),
              caps.getFlipContents());

        this.vsync = t;
    }

    /**
     * Creates an ExtendedBufferCapabilities instance with front/back/flip caps
     * from the object, and passed v-sync mode.
     */
    public ExtendedBufferCapabilities derive(VSyncType t) {
        return new ExtendedBufferCapabilities(this, t);
    }

    /**
     * Returns the type of v-sync requested by this capabilities instance.
     */
    public VSyncType getVSync() {
        return vsync;
    }

    @Override
    public final boolean isPageFlipping() {
        return true;
    }
}
