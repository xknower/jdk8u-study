package sun.awt.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;

/**
 * This class provides default implementations for the
 * <code>MultiResolutionImage</code> interface. The developer needs only
 * to subclass this abstract class and define the <code>getResolutionVariant</code>,
 * <code>getResolutionVariants</code>, and <code>getBaseImage</code> methods.
 *
 *
 * For example,
 * {@code
 * public class CustomMultiResolutionImage extends AbstractMultiResolutionImage {
 *
 *     int baseImageIndex;
 *     Image[] resolutionVariants;
 *
 *     public CustomMultiResolutionImage(int baseImageIndex,
 *             Image... resolutionVariants) {
 *          this.baseImageIndex = baseImageIndex;
 *          this.resolutionVariants = resolutionVariants;
 *     }
 *
 *     @Override
 *     public Image getResolutionVariant(float logicalDPIX, float logicalDPIY,
 *             float baseImageWidth, float baseImageHeight,
 *             float destImageWidth, float destImageHeight) {
 *         // return a resolution variant based on the given logical DPI,
 *         // base image size, or destination image size
 *     }
 *
 *     @Override
 *     public List<Image> getResolutionVariants() {
 *         return Arrays.asList(resolutionVariants);
 *     }
 *
 *     protected Image getBaseImage() {
 *         return resolutionVariants[baseImageIndex];
 *     }
 * }
 * }
 *
 * @see java.awt.Image
 * @see java.awt.image.MultiResolutionImage
 *
 */
public abstract class AbstractMultiResolutionImage extends java.awt.Image
        implements MultiResolutionImage {

    /**
     * @inheritDoc
     */
    @Override
    public int getWidth(ImageObserver observer) {
        return getBaseImage().getWidth(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getHeight(ImageObserver observer) {
        return getBaseImage().getHeight(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public ImageProducer getSource() {
        return getBaseImage().getSource();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Graphics getGraphics() {
        return getBaseImage().getGraphics();

    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return getBaseImage().getProperty(name, observer);
    }

    /**
     * @return base image
     */
    protected abstract Image getBaseImage();
}
