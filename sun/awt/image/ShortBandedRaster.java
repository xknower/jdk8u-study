package sun.awt.image;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.BandedSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * This class defines a Raster with pixels consisting of multiple 16-bit
 * samples stored in separate arrays for each band.  Operations on
 * sets of pixels are performed on a given band of each pixel
 * in the set before moving on to the next band.  The arrays used
 * for storage may be distinct or shared between some or all of
 * the bands.
 * There is only one pixel stride and one scanline stride for all
 * bands.  This type of Raster can be used with a
 * ComponentColorModel. This class requires a BandedSampleModel.
 *
 */
public class ShortBandedRaster extends SunWritableRaster {

    /** Data offsets for each band of image data. */
    int[]         dataOffsets;

    /** Scanline stride of the image data contained in this Raster. */
    int           scanlineStride;

    /** The image data array. */
    short[][]     data;

    /** A cached copy of minX + width for use in bounds checks. */
    private int maxX;

    /** A cached copy of minY + height for use in bounds checks. */
    private int maxY;

    /**
     * Constructs a ShortBandedRaster with the given SampleModel.
     * The Raster's upper left corner is origin and it is the same
     * size as the SampleModel.  A DataBuffer large enough to describe the
     * Raster is automatically created.  SampleModel must be of type
     * BandedSampleModel.
     * @param sampleModel     The SampleModel that specifies the layout.
     * @param origin          The Point that specified the origin.
     */
    public ShortBandedRaster(SampleModel sampleModel,
                                Point origin) {
        this(sampleModel,
             sampleModel.createDataBuffer(),
             new Rectangle(origin.x,
                           origin.y,
                           sampleModel.getWidth(),
                           sampleModel.getHeight()),
             origin,
             null);
    }

    /**
     * Constructs a ShortBandedRaster with the given SampleModel
     * and DataBuffer.  The Raster's upper left corner is origin and
     * it is the same size as the SampleModel.  The DataBuffer is not
     * initialized and must be a DataBufferUShort compatible with SampleModel.
     * SampleModel must be of type BandedSampleModel.
     * @param sampleModel     The SampleModel that specifies the layout.
     * @param dataBuffer      The DataBufferUShort that contains the image data.
     * @param origin          The Point that specifies the origin.
     */
    public ShortBandedRaster(SampleModel sampleModel,
                                DataBuffer dataBuffer,
                                Point origin) {
        this(sampleModel, dataBuffer,
             new Rectangle(origin.x, origin.y,
                           sampleModel.getWidth(),
                           sampleModel.getHeight()),
             origin, null);
    }

    /**
     * Constructs a ShortBandedRaster with the given SampleModel,
     * DataBuffer, and parent.  DataBuffer must be a DataBufferUShort and
     * SampleModel must be of type BandedSampleModel.
     * When translated into the base Raster's
     * coordinate system, aRegion must be contained by the base Raster.
     * Origin is the coordinate in the new Raster's coordinate system of
     * the origin of the base Raster.  (The base Raster is the Raster's
     * ancestor which has no parent.)
     *
     * Note that this constructor should generally be called by other
     * constructors or create methods, it should not be used directly.
     * @param sampleModel     The SampleModel that specifies the layout.
     * @param dataBuffer      The DataBufferUShort that contains the image data.
     * @param aRegion         The Rectangle that specifies the image area.
     * @param origin          The Point that specifies the origin.
     * @param parent          The parent (if any) of this raster.
     */
    public ShortBandedRaster(SampleModel sampleModel,
                                DataBuffer dataBuffer,
                                Rectangle aRegion,
                                Point origin,
                                ShortBandedRaster parent) {

        super(sampleModel, dataBuffer, aRegion, origin, parent);
        this.maxX = minX + width;
        this.maxY = minY + height;
        if (!(dataBuffer instanceof DataBufferUShort)) {
           throw new RasterFormatException("ShortBandedRaster must have " +
                "ushort DataBuffers");
        }
        DataBufferUShort dbus = (DataBufferUShort)dataBuffer;

        if (sampleModel instanceof BandedSampleModel) {
            BandedSampleModel bsm = (BandedSampleModel)sampleModel;
            this.scanlineStride = bsm.getScanlineStride();
            int bankIndices[] = bsm.getBankIndices();
            int bandOffsets[] = bsm.getBandOffsets();
            int dOffsets[] = dbus.getOffsets();
            dataOffsets = new int[bankIndices.length];
            data = new short[bankIndices.length][];
            int xOffset = aRegion.x - origin.x;
            int yOffset = aRegion.y - origin.y;
            for (int i = 0; i < bankIndices.length; i++) {
               data[i] = stealData(dbus, bankIndices[i]);
               dataOffsets[i] = dOffsets[bankIndices[i]] +
                   xOffset + yOffset*scanlineStride + bandOffsets[i];
            }
        } else {
            throw new RasterFormatException("ShortBandedRasters must have "+
                "BandedSampleModels");
        }
        verify();
    }

    /**
     * Returns a copy of the data offsets array. For each band the data offset
     * is the index into the band's data array, of the first sample of the
     * band.
     */
    public int[] getDataOffsets() {
        return (int[]) dataOffsets.clone();
    }

    /**
     * Returns the data offset for the specified band.  The data offset
     * is the index into the band's data array
     * in which the first sample of the first scanline is stored.
     * @param   The band whose offset is returned.
     */
    public int getDataOffset(int band) {
        return dataOffsets[band];
    }

    /**
     * Returns the scanline stride -- the number of data array elements between
     * a given sample and the sample in the same column
     * of the next row in the same band.
     */
    public int getScanlineStride() {
        return scanlineStride;
    }

    /**
     * Returns the pixel stride, which is always equal to one for
     * a Raster with a BandedSampleModel.
     */
    public int getPixelStride() {
        return 1;
    }

    /**
     * Returns a reference to the entire data array.
     */
    public short[][] getDataStorage() {
        return data;
    }

    /**
     * Returns a reference to the specific band data array.
     */
    public short[] getDataStorage(int band) {
        return data[band];
    }

    /**
     * Returns the data elements for all bands at the specified
     * location.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinate is out of bounds.
     * A ClassCastException will be thrown if the input object is non null
     * and references anything other than an array of transferType.
     * @param x        The X coordinate of the pixel location.
     * @param y        The Y coordinate of the pixel location.
     * @param outData  An object reference to an array of type defined by
     *                 getTransferType() and length getNumDataElements().
     *                 If null an array of appropriate type and size will be
     *                 allocated.
     * @return         An object reference to an array of type defined by
     *                 getTransferType() with the request pixel data.
     */
    public Object getDataElements(int x, int y, Object obj) {
        if ((x < this.minX) || (y < this.minY) ||
            (x >= this.maxX) || (y >= this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        short outData[];
        if (obj == null) {
            outData = new short[numDataElements];
        } else {
            outData = (short[])obj;
        }

        int off = (y-minY)*scanlineStride + (x-minX);

        for (int band = 0; band < numDataElements; band++) {
            outData[band] = data[band][dataOffsets[band] + off];
        }

        return outData;
    }

    /**
     * Returns an array  of data elements from the specified rectangular
     * region.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinates are out of bounds.
     * <pre>
     *       short[] bandData = (short[])Raster.getDataElements(x, y, w, h, null);
     *       int numDataElements = Raster.getnumDataElements();
     *       short[] pixel = new short[numDataElements];
     *       // To find a data element at location (x2, y2)
     *       System.arraycopy(bandData, ((y2-y)*w + (x2-x))*numDataElements,
     *                        pixel, 0, numDataElements);
     * </pre>
     * @param x        The X coordinate of the upper left pixel location.
     * @param y        The Y coordinate of the upper left pixel location.
     * @param width    Width of the pixel rectangle.
     * @param height   Height of the pixel rectangle.
     * @param outData  An object reference to an array of type defined by
     *                 getTransferType() and length w*h*getNumDataElements().
     *                 If null an array of appropriate type and size will be
     *                 allocated.
     * @return         An object reference to an array of type defined by
     *                 getTransferType() with the request pixel data.
     */
    public Object getDataElements(int x, int y, int w, int h, Object obj) {
        if ((x < this.minX) || (y < this.minY) ||
            (x + w > this.maxX) || (y + h > this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        short outData[];
        if (obj == null) {
            outData = new short[numDataElements*w*h];
        } else {
            outData = (short[])obj;
        }
        int yoff = (y-minY)*scanlineStride + (x-minX);

        for (int c = 0; c < numDataElements; c++) {
            int off = c;
            short[] bank = data[c];
            int dataOffset = dataOffsets[c];

            int yoff2 = yoff;
            for (int ystart=0; ystart < h; ystart++, yoff2 += scanlineStride) {
                int xoff = dataOffset + yoff2;
                for (int xstart=0; xstart < w; xstart++) {
                    outData[off] = bank[xoff++];
                    off += numDataElements;
                }
            }
        }

        return outData;
    }

    /**
     * Returns a short array  of data elements from the specified rectangular
     * region for the specified band.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinates are out of bounds.
     * <pre>
     *       short[] bandData = Raster.getShortData(x, y, w, h, null);
     *       // To find the data element at location (x2, y2)
     *       short bandElement = bandData[((y2-y)*w + (x2-x))];
     * </pre>
     * @param x        The X coordinate of the upper left pixel location.
     * @param y        The Y coordinate of the upper left pixel location.
     * @param width    Width of the pixel rectangle.
     * @param height   Height of the pixel rectangle.
     * @param band     The band to return.
     * @param outData  If non-null, data elements for all bands
     *                 at the specified location are returned in this array.
     * @return         Data array with data elements for all bands.
     */
    public short[] getShortData(int x, int y, int w, int h,
                                      int band, short[] outData) {
        // Bounds check for 'band' will be performed automatically
        if ((x < this.minX) || (y < this.minY) ||
            (x + w > this.maxX) || (y + h > this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        if (outData == null) {
            outData = new short[scanlineStride*h];
        }
        int yoff = (y-minY)*scanlineStride + (x-minX) + dataOffsets[band];

        if (scanlineStride == w) {
            System.arraycopy(data[band], yoff, outData, 0, w*h);
        } else {
            int off = 0;
            for (int ystart=0; ystart < h; ystart++, yoff += scanlineStride) {
                System.arraycopy(data[band], yoff, outData, off, w);
                off += w;
            }
        }

        return outData;
    }

    /**
     * Returns a short array  of data elements from the specified rectangular
     * region.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinates are out of bounds.
     * <pre>
     *       short[] bandData = Raster.getShortData(x, y, w, h, null);
     *       int numDataElements = Raster.getnumDataElements();
     *       short[] pixel = new short[numDataElements];
     *       // To find a data element at location (x2, y2)
     *       System.arraycopy(bandData, ((y2-y)*w + (x2-x))*numDataElements,
     *                        pixel, 0, numDataElements);
     * </pre>
     * @param x        The X coordinate of the upper left pixel location.
     * @param y        The Y coordinate of the upper left pixel location.
     * @param width    Width of the pixel rectangle.
     * @param height   Height of the pixel rectangle.
     * @param outData  If non-null, data elements for all bands
     *                 at the specified location are returned in this array.
     * @return         Data array with data elements for all bands.
     */
    public short[] getShortData(int x, int y, int w, int h,
                                     short[] outData) {
        if ((x < this.minX) || (y < this.minY) ||
            (x + w > this.maxX) || (y + h > this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        if (outData == null) {
            outData = new short[numDataElements*scanlineStride*h];
        }
        int yoff = (y-minY)*scanlineStride + (x-minX);

        for (int c = 0; c < numDataElements; c++) {
            int off = c;
            short[] bank = data[c];
            int dataOffset = dataOffsets[c];

            int yoff2 = yoff;
            for (int ystart=0; ystart < h; ystart++, yoff2 += scanlineStride) {
                int xoff = dataOffset + yoff2;
                for (int xstart=0; xstart < w; xstart++) {
                    outData[off] = bank[xoff++];
                    off += numDataElements;
                }
            }
        }

        return outData;
    }

    /**
     * Stores the data element for all bands at the specified location.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinate is out of bounds.
     * A ClassCastException will be thrown if the input object is non null
     * and references anything other than an array of transferType.
     * @param x        The X coordinate of the pixel location.
     * @param y        The Y coordinate of the pixel location.
     * @param inData   An object reference to an array of type defined by
     *                 getTransferType() and length getNumDataElements()
     *                 containing the pixel data to place at x,y.
     */
    public void setDataElements(int x, int y, Object obj) {
        if ((x < this.minX) || (y < this.minY) ||
            (x >= this.maxX) || (y >= this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        short inData[] = (short[])obj;
        int off = (y-minY)*scanlineStride + (x-minX);
        for (int i = 0; i < numDataElements; i++) {
            data[i][dataOffsets[i] + off] = inData[i];
        }

        markDirty();
    }

    /**
     * Stores the Raster data at the specified location.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinates are out of bounds.
     * @param x          The X coordinate of the pixel location.
     * @param y          The Y coordinate of the pixel location.
     * @param inRaster   Raster of data to place at x,y location.
     */
    public void setDataElements(int x, int y, Raster inRaster) {
        int dstOffX = x + inRaster.getMinX();
        int dstOffY = y + inRaster.getMinY();
        int width  = inRaster.getWidth();
        int height = inRaster.getHeight();
        if ((dstOffX < this.minX) || (dstOffY < this.minY) ||
            (dstOffX + width > this.maxX) || (dstOffY + height > this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }

        setDataElements(dstOffX, dstOffY, width, height, inRaster);
    }

    /**
     * Stores the Raster data at the specified location.
     * @param dstX The absolute X coordinate of the destination pixel
     * that will receive a copy of the upper-left pixel of the
     * inRaster
     * @param dstY The absolute Y coordinate of the destination pixel
     * that will receive a copy of the upper-left pixel of the
     * inRaster
     * @param width      The number of pixels to store horizontally
     * @param height     The number of pixels to store vertically
     * @param inRaster   Raster of data to place at x,y location.
     */
    private void setDataElements(int dstX, int dstY,
                                 int width, int height,
                                 Raster inRaster) {
        // Assume bounds checking has been performed previously
        if (width <= 0 || height <= 0) {
            return;
        }

        // Write inRaster (minX, minY) to (dstX, dstY)

        int srcOffX = inRaster.getMinX();
        int srcOffY = inRaster.getMinY();
        Object tdata = null;

//      // REMIND: Do something faster!
//      if (inRaster instanceof ShortBandedRaster) {
//      }

        for (int startY=0; startY < height; startY++) {
            // Grab one scanline at a time
            tdata = inRaster.getDataElements(srcOffX, srcOffY+startY,
                                             width, 1, tdata);
            setDataElements(dstX, dstY + startY, width, 1, tdata);
        }
    }

    /**
     * Stores an array of data elements into the specified rectangular
     * region.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinates are out of bounds.
     * A ClassCastException will be thrown if the input object is non null
     * and references anything other than an array of transferType.
     * The data elements in the
     * data array are assumed to be packed.  That is, a data element
     * for the nth band at location (x2, y2) would be found at:
     * <pre>
     *      inData[((y2-y)*w + (x2-x))*numDataElements + n]
     * </pre>
     * @param x        The X coordinate of the upper left pixel location.
     * @param y        The Y coordinate of the upper left pixel location.
     * @param w        Width of the pixel rectangle.
     * @param h        Height of the pixel rectangle.
     * @param inData   An object reference to an array of type defined by
     *                 getTransferType() and length w*h*getNumDataElements()
     *                 containing the pixel data to place between x,y and
     *                 x+h, y+h.
     */
    public void setDataElements(int x, int y, int w, int h, Object obj) {
        if ((x < this.minX) || (y < this.minY) ||
            (x + w > this.maxX) || (y + h > this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        short inData[] = (short[])obj;
        int yoff = (y-minY)*scanlineStride + (x-minX);

        for (int c = 0; c < numDataElements; c++) {
            int off = c;
            short[] bank = data[c];
            int dataOffset = dataOffsets[c];

            int yoff2 = yoff;
            for (int ystart=0; ystart < h; ystart++, yoff2 += scanlineStride) {
                int xoff = dataOffset + yoff2;
                for (int xstart=0; xstart < w; xstart++) {
                    bank[xoff++] = inData[off];
                    off += numDataElements;
                }
            }
        }

        markDirty();
    }

    /**
     * Stores a short array of data elements into the specified
     * rectangular region for the specified band.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinates are out of bounds.
     * The data elements in the
     * data array are assumed to be packed.  That is, a data element
     * at location (x2, y2) would be found at:
     * <pre>
     *      inData[((y2-y)*w + (x2-x))]
     * </pre>
     * @param x        The X coordinate of the upper left pixel location.
     * @param y        The Y coordinate of the upper left pixel location.
     * @param w        Width of the pixel rectangle.
     * @param h        Height of the pixel rectangle.
     * @param band     The band to set.
     * @param inData   The data elements to be stored.
     */
    public void putShortData(int x, int y, int w, int h,
                                   int band, short[] inData) {
        // Bounds check for 'band' will be performed automatically
        if ((x < this.minX) || (y < this.minY) ||
            (x + w > this.maxX) || (y + h > this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        int yoff = (y-minY)*scanlineStride + (x-minX) + dataOffsets[band];
        int xoff;
        int off = 0;
        int xstart;
        int ystart;

        if (scanlineStride == w) {
            System.arraycopy(inData, 0, data[band], yoff, w*h);
        } else {
            for (ystart=0; ystart < h; ystart++, yoff += scanlineStride) {
                System.arraycopy(inData, off, data[band], yoff, w);
                off += w;
            }
        }

        markDirty();
    }

    /**
     * Stores a short integer array of data elements into the specified
     * rectangular region.
     * An ArrayIndexOutOfBounds exception will be thrown at runtime
     * if the pixel coordinates are out of bounds.
     * The data elements in the
     * data array are assumed to be packed.  That is, a data element
     * for the nth band at location (x2, y2) would be found at:
     * <pre>
     *      inData[((y2-y)*w + (x2-x))*numDataElements + n]
     * </pre>
     * @param x        The X coordinate of the upper left pixel location.
     * @param y        The Y coordinate of the upper left pixel location.
     * @param w        Width of the pixel rectangle.
     * @param h        Height of the pixel rectangle.
     * @param inData   The data elements to be stored.
     */
    public void putShortData(int x, int y, int w, int h, short[] inData) {
        if ((x < this.minX) || (y < this.minY) ||
            (x + w > this.maxX) || (y + h > this.maxY)) {
            throw new ArrayIndexOutOfBoundsException
                ("Coordinate out of bounds!");
        }
        int yoff = (y-minY)*scanlineStride + (x-minX);

        for (int c = 0; c < numDataElements; c++) {
            int off = c;
            short[] bank = data[c];
            int dataOffset = dataOffsets[c];

            int yoff2 = yoff;
            for (int ystart=0; ystart < h; ystart++, yoff2 += scanlineStride) {
                int xoff = dataOffset + yoff2;
                for (int xstart=0; xstart < w; xstart++) {
                    bank[xoff++] = inData[off];
                    off += numDataElements;
                }
            }
        }

        markDirty();
    }

    /**
     * Creates a Writable subRaster given a region of the Raster.  The x and y
     * coordinates specify the horizontal and vertical offsets
     * from the upper-left corner of this Raster to the upper-left corner
     * of the subRaster.  A subset of the bands of the parent Raster may
     * be specified.  If this is null, then all the bands are present in the
     * subRaster. A translation to the subRaster may also be specified.
     * Note that the subRaster will reference the same
     * DataBuffers as the parent Raster, but using different offsets.
     * @param x               X offset.
     * @param y               Y offset.
     * @param width           Width (in pixels) of the subraster.
     * @param height          Height (in pixels) of the subraster.
     * @param x0              Translated X origin of the subraster.
     * @param y0              Translated Y origin of the subraster.
     * @param bandList        Array of band indices.
     * @exception RasterFormatException
     *            if the specified bounding box is outside of the parent Raster.
     */
    public WritableRaster createWritableChild(int x, int y,
                                              int width, int height,
                                              int x0, int y0,
                                              int bandList[]) {

        if (x < this.minX) {
            throw new RasterFormatException("x lies outside raster");
        }
        if (y < this.minY) {
            throw new RasterFormatException("y lies outside raster");
        }
        if ((x+width < x) || (x+width > this.minX + this.width)) {
            throw new RasterFormatException("(x + width) is outside of Raster");
        }
        if ((y+height < y) || (y+height > this.minY + this.height)) {
            throw new RasterFormatException("(y + height) is outside of Raster");
        }

        SampleModel sm;

        if (bandList != null)
            sm = sampleModel.createSubsetSampleModel(bandList);
        else
            sm = sampleModel;

        int deltaX = x0 - x;
        int deltaY = y0 - y;

        return new ShortBandedRaster(sm,
                                     dataBuffer,
                                     new Rectangle(x0, y0, width, height),
                                     new Point(sampleModelTranslateX+deltaX,
                                               sampleModelTranslateY+deltaY),
                                     this);

    }

    /**
     * Creates a subraster given a region of the raster.  The x and y
     * coordinates specify the horizontal and vertical offsets
     * from the upper-left corner of this raster to the upper-left corner
     * of the subraster.  A subset of the bands of the parent Raster may
     * be specified.  If this is null, then all the bands are present in the
     * subRaster. A translation to the subRaster may also be specified.
     * Note that the subraster will reference the same
     * DataBuffers as the parent raster, but using different offsets.
     * @param x               X offset.
     * @param y               Y offset.
     * @param width           Width (in pixels) of the subraster.
     * @param height          Height (in pixels) of the subraster.
     * @param x0              Translated X origin of the subraster.
     * @param y0              Translated Y origin of the subraster.
     * @param bandList        Array of band indices.
     * @exception RasterFormatException
     *            if the specified bounding box is outside of the parent raster.
     */
    public Raster createChild (int x, int y,
                               int width, int height,
                               int x0, int y0,
                               int bandList[]) {
        return createWritableChild(x, y, width, height, x0, y0, bandList);
    }

    /**
     * Creates a Raster with the same layout but using a different
     * width and height, and with new zeroed data arrays.
     */
    public WritableRaster createCompatibleWritableRaster(int w, int h) {
        if (w <= 0 || h <=0) {
            throw new RasterFormatException("negative "+
                                            ((w <= 0) ? "width" : "height"));
        }

        SampleModel sm = sampleModel.createCompatibleSampleModel(w,h);

        return new ShortBandedRaster(sm, new Point(0,0));
    }

    /**
     * Creates a Raster with the same layout and the same
     * width and height, and with new zeroed data arrays.  If
     * the Raster is a subRaster, this will call
     * createCompatibleRaster(width, height).
     */
    public WritableRaster createCompatibleWritableRaster() {
       return createCompatibleWritableRaster(width,height);
    }

    /**
     * Verify that the layout parameters are consistent with the data.
     * Verifies whether the data buffer has enough data for the raster,
     * taking into account offsets, after ensuring all offsets are >=0.
     * @throws RasterFormatException if a problem is detected.
     */
    private void verify() {

        /* Need to re-verify the dimensions since a sample model may be
         * specified to the constructor
         */
        if (width <= 0 || height <= 0 ||
            height > (Integer.MAX_VALUE / width))
        {
            throw new RasterFormatException("Invalid raster dimension");
        }

        if (scanlineStride < 0 ||
            scanlineStride > (Integer.MAX_VALUE / height))
        {
            // integer overflow
            throw new RasterFormatException("Incorrect scanline stride: "
                    + scanlineStride);
        }

        if ((long)minX - sampleModelTranslateX < 0 ||
            (long)minY - sampleModelTranslateY < 0) {

            throw new RasterFormatException("Incorrect origin/translate: (" +
                    minX + ", " + minY + ") / (" +
                    sampleModelTranslateX + ", " + sampleModelTranslateY + ")");
        }

        if (height > 1 || minY - sampleModelTranslateY > 0) {
            // buffer should contain at least one scanline
            for (int i = 0; i < data.length; i++) {
                if (scanlineStride > data[i].length) {
                    throw new RasterFormatException("Incorrect scanline stride: "
                        + scanlineStride);
                }
            }
        }

        // Make sure data for Raster is in a legal range
        for (int i=0; i < dataOffsets.length; i++) {
            if (dataOffsets[i] < 0) {
                throw new RasterFormatException("Data offsets for band "+i+
                                                "("+dataOffsets[i]+
                                                ") must be >= 0");
            }
        }

        int lastScanOffset = (height - 1) * scanlineStride;
        if ((width - 1) > (Integer.MAX_VALUE - lastScanOffset)) {
            throw new RasterFormatException("Invalid raster dimension");
        }
        int lastPixelOffset = lastScanOffset + (width - 1);

        int maxIndex = 0;
        int index;

        for (int i=0; i < numDataElements; i++) {
            if (dataOffsets[i] > (Integer.MAX_VALUE - lastPixelOffset)) {
                throw new RasterFormatException("Invalid raster dimension");
            }
            index = lastPixelOffset + dataOffsets[i];
            if (index > maxIndex) {
                maxIndex = index;
            }
        }
        for (int i=0; i < numDataElements; i++) {
            if (data[i].length <= maxIndex) {
                throw new RasterFormatException("Data array too small " +
                      "(should be > "+ maxIndex+" )");
            }
        }
    }

    public String toString() {
        return new String ("ShortBandedRaster: width = "+width+" height = "
                           + height
                           +" #numBands " + numBands
                           +" #dataElements "+numDataElements);

    }

}
