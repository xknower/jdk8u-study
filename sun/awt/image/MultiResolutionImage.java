package sun.awt.image;

import java.awt.Image;
import java.util.List;

/**
 * This interface is designed to provide a set of images at various resolutions.
 *
 * The <code>MultiResolutionImage</code> interface should be implemented by any
 * class whose instances are intended to provide image resolution variants
 * according to the given image width and height.
 *
 * For example,
 * <pre>
 * {@code
 *  public class ScaledImage extends BufferedImage
 *         implements MultiResolutionImage {
 *
 *    @Override
 *    public Image getResolutionVariant(int width, int height) {
 *      return ((width <= getWidth() && height <= getHeight()))
 *             ? this : highResolutionImage;
 *    }
 *
 *    @Override
 *    public List<Image> getResolutionVariants() {
 *        return Arrays.asList(this, highResolutionImage);
 *    }
 *  }
 * }</pre>
 *
 * It is recommended to cache image variants for performance reasons.
 *
 * <b>WARNING</b>: This class is an implementation detail. This API may change
 * between update release, and it may even be removed or be moved in some other
 * package(s)/class(es).
 */
public interface MultiResolutionImage {

    /**
     * Provides an image with necessary resolution which best fits to the given
     * image width and height.
     *
     * @param width the desired image resolution width.
     * @param height the desired image resolution height.
     * @return image resolution variant.
     *
     * @since JDK1.8
     */
    public Image getResolutionVariant(int width, int height);

    /**
     * Gets list of all resolution variants including the base image
     *
     * @return list of resolution variants.
     * @since JDK1.8
     */
    public List<Image> getResolutionVariants();
}
