package sun.swing;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;

/**
 * The interface by means of which the {@link JLightweightFrame} class
 * communicates to its client application.
 * <p>
 * The client application implements this interface so it can response
 * to requests and process notifications from {@code JLightweightFrame}.
 * An implementation of this interface is associated with a {@code
 * JLightweightFrame} instance via the {@link JLightweightFrame#setContent}
 * method.
 *
 * A hierarchy of components contained in the {@code JComponent} instance
 * returned by the {@link #getComponent} method should not contain any
 * heavyweight components, otherwise {@code JLightweightFrame} may fail
 * to paint it.
 *
 * @author Artem Ananiev
 * @author Anton Tarasov
 * @author Jim Graham
 */
public interface LightweightContent {

    /**
     * The client application overrides this method to return the {@code
     * JComponent} instance which the {@code JLightweightFrame} container
     * will paint as its lightweight content. A hierarchy of components
     * contained in this component should not contain any heavyweight objects.
     *
     * @return the component to paint
     */
    public JComponent getComponent();

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that it acquires the paint lock. The client application
     * should implement the locking mechanism in order to synchronize access
     * to the content image data, shared between {@code JLightweightFrame}
     * and the client application.
     *
     * @see #paintUnlock
     */
    public void paintLock();

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that it releases the paint lock. The client application
     * should implement the locking mechanism in order to synchronize access
     * to the content image data, shared between {@code JLightweightFrame}
     * and the client application.
     *
     * @see #paintLock
     */
    public void paintUnlock();

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that a new data buffer has been set as a content pixel
     * buffer. Typically this occurs when a buffer of a larger size is
     * created in response to a content resize event.
     * <p>
     * The method reports a reference to the pixel data buffer, the content
     * image bounds within the buffer and the line stride of the buffer.
     * These values have the following correlation.
     * The {@code width} and {@code height} matches the layout size of the content
     * (the component returned from the {@link #getComponent} method). The
     * {@code x} and {@code y} is the origin of the content, {@code (0, 0)}
     * in the layout coordinate space of the content, appearing at
     * {@code data[y * scale * linestride + x * scale]} in the buffer.
     * A pixel with indices {@code (i, j)}, where {@code (0 <= i < width)} and
     * {@code (0 <= j < height)}, in the layout coordinate space of the content
     * is represented by a {@code scale^2} square of pixels in the physical
     * coordinate space of the buffer. The top-left corner of the square has the
     * following physical coordinate in the buffer:
     * {@code data[(y + j) * scale * linestride + (x + i) * scale]}.
     *
     * @param data the content pixel data buffer of INT_ARGB_PRE type
     * @param x the logical x coordinate of the image
     * @param y the logical y coordinate of the image
     * @param width the logical width of the image
     * @param height the logical height of the image
     * @param linestride the line stride of the pixel buffer
     * @param scale the scale factor of the pixel buffer
     */
    default public void imageBufferReset(int[] data,
                                 int x, int y,
                                 int width, int height,
                                 int linestride,
                                 int scale)
    {
        imageBufferReset(data, x, y, width, height, linestride);
    }

    /**
     * The default implementation for #imageBufferReset uses a hard-coded value
     * of 1 for the scale factor. Both the old and the new methods provide
     * default implementations in order to allow a client application to run
     * with any JDK version without breaking backward compatibility.
     */
    default public void imageBufferReset(int[] data,
                                 int x, int y,
                                 int width, int height,
                                 int linestride)
    {
        imageBufferReset(data, x, y, width, height, linestride, 1);
    }

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that the content image bounds have been changed within the
     * image's pixel buffer.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param width the width of the image
     * @param height the height of the image
     *
     * @see #imageBufferReset
     */
    public void imageReshaped(int x, int y, int width, int height);

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that a part of the content image, or the whole image has
     * been updated. The method reports bounds of the rectangular dirty region.
     * The {@code dirtyX} and {@code dirtyY} is the origin of the dirty
     * rectangle, which is relative to the origin of the content, appearing
     * at {@code data[(y + dirtyY] * linestride + (x + dirtyX)]} in the pixel
     * buffer (see {@link #imageBufferReset}). All indices
     * {@code data[(y + dirtyY + j) * linestride + (x + dirtyX + i)]} where
     * {@code (0 <= i < dirtyWidth)} and {@code (0 <= j < dirtyHeight)}
     * will represent valid pixel data, {@code (i, j)} in the coordinate space
     * of the dirty rectangle.
     *
     * @param dirtyX the x coordinate of the dirty rectangle,
     *        relative to the image origin
     * @param dirtyY the y coordinate of the dirty rectangle,
     *        relative to the image origin
     * @param dirtyWidth the width of the dirty rectangle
     * @param dirtyHeight the height of the dirty rectangle
     *
     * @see #imageBufferReset
     * @see #imageReshaped
     */
    public void imageUpdated(int dirtyX, int dirtyY,
                             int dirtyWidth, int dirtyHeight);

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that the frame has grabbed focus.
     */
    public void focusGrabbed();

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that the frame has ungrabbed focus.
     */
    public void focusUngrabbed();

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that the content preferred size has changed.
     */
    public void preferredSizeChanged(int width, int height);

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that the content maximum size has changed.
     */
    public void maximumSizeChanged(int width, int height);

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that the content minimum size has changed.
     */
    public void minimumSizeChanged(int width, int height);

    /**
     * {@code JLightweightFrame} calls this method to notify the client
     * application that in needs to set a cursor
     * @param cursor a cursor to set
     */
    default public void setCursor(Cursor cursor) { }

    /**
     * Create a drag gesture recognizer for the lightweight frame.
     */
    default public <T extends DragGestureRecognizer> T createDragGestureRecognizer(
            Class<T> abstractRecognizerClass,
            DragSource ds, Component c, int srcActions,
            DragGestureListener dgl)
    {
        return null;
    }

    /**
     * Create a drag source context peer for the lightweight frame.
     */
    default public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dge) throws InvalidDnDOperationException
    {
        return null;
    }

    /**
     * Adds a drop target to the lightweight frame.
     */
    default public void addDropTarget(DropTarget dt) {}

    /**
     * Removes a drop target from the lightweight frame.
     */
    default public void removeDropTarget(DropTarget dt) {}
}
