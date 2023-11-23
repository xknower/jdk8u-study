package sun.awt.image;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import java.util.List;
import sun.misc.SoftCache;

public class MultiResolutionToolkitImage extends ToolkitImage implements MultiResolutionImage {

    Image resolutionVariant;

    public MultiResolutionToolkitImage(Image lowResolutionImage, Image resolutionVariant) {
        super(lowResolutionImage.getSource());
        this.resolutionVariant = resolutionVariant;
    }

    @Override
    public Image getResolutionVariant(int width, int height) {
        return ((width <= getWidth() && height <= getHeight()))
                ? this : resolutionVariant;
    }

    public Image getResolutionVariant() {
        return resolutionVariant;
    }

    @Override
    public List<Image> getResolutionVariants() {
        return Arrays.<Image>asList(this, resolutionVariant);
    }

    private static final int BITS_INFO = ImageObserver.SOMEBITS
            | ImageObserver.FRAMEBITS | ImageObserver.ALLBITS;

    private static class ObserverCache {

        static final SoftCache INSTANCE = new SoftCache();
    }

    public static ImageObserver getResolutionVariantObserver(
            final Image image, final ImageObserver observer,
            final int imgWidth, final int imgHeight,
            final int rvWidth, final int rvHeight) {
        return getResolutionVariantObserver(image, observer,
                imgWidth, imgHeight, rvWidth, rvHeight, false);
    }

    public static ImageObserver getResolutionVariantObserver(
            final Image image, final ImageObserver observer,
            final int imgWidth, final int imgHeight,
            final int rvWidth, final int rvHeight, boolean concatenateInfo) {

        if (observer == null) {
            return null;
        }

        synchronized (ObserverCache.INSTANCE) {
            ImageObserver o = (ImageObserver) ObserverCache.INSTANCE.get(observer);

            if (o == null) {

                o = (Image resolutionVariant, int flags,
                        int x, int y, int width, int height) -> {

                            if ((flags & (ImageObserver.WIDTH | BITS_INFO)) != 0) {
                                width = (width + 1) / 2;
                            }

                            if ((flags & (ImageObserver.HEIGHT | BITS_INFO)) != 0) {
                                height = (height + 1) / 2;
                            }

                            if ((flags & BITS_INFO) != 0) {
                                x /= 2;
                                y /= 2;
                            }

                            if(concatenateInfo){
                                flags &= ((ToolkitImage) image).
                                        getImageRep().check(null);
                            }

                            return observer.imageUpdate(
                                    image, flags, x, y, width, height);
                        };

                ObserverCache.INSTANCE.put(observer, o);
            }
            return o;
        }
    }
}
