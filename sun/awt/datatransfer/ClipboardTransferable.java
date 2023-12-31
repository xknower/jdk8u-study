package sun.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Reads all of the data from the system Clipboard which the data transfer
 * subsystem knows how to translate. This includes all text data, File Lists,
 * Serializable objects, Remote objects, and properly registered, arbitrary
 * data as InputStreams. The data is stored in byte format until requested
 * by client code. At that point, the data is converted, if necessary, into
 * the proper format to deliver to the application.
 *
 * This hybrid pre-fetch/delayed-rendering approach allows us to circumvent
 * the API restriction that client code cannot lock the Clipboard to discover
 * its formats before requesting data in a particular format, while avoiding
 * the overhead of fully rendering all data ahead of time.
 *
 * @author David Mendenhall
 * @author Danila Sinopalnikov
 *
 * @since 1.4 (appeared in modified form as FullyRenderedTransferable in 1.3.1)
 */
public class ClipboardTransferable implements Transferable {
    private final HashMap flavorsToData = new HashMap();
    private DataFlavor[] flavors = new DataFlavor[0];

    private final class DataFactory {
        final long format;
        final byte[] data;
        DataFactory(long format, byte[] data) {
            this.format = format;
            this.data   = data;
        }

        public Object getTransferData(DataFlavor flavor) throws IOException {
            return DataTransferer.getInstance().
                translateBytes(data, flavor, format,
                               ClipboardTransferable.this);
        }
    }

    public ClipboardTransferable(SunClipboard clipboard) {

        clipboard.openClipboard(null);

        try {
            long[] formats = clipboard.getClipboardFormats();

            if (formats != null && formats.length > 0) {
                // Since the SystemFlavorMap will specify many DataFlavors
                // which map to the same format, we should cache data as we
                // read it.
                HashMap cached_data = new HashMap(formats.length, 1.0f);

                Map flavorsForFormats = DataTransferer.getInstance().
                    getFlavorsForFormats(formats, SunClipboard.getDefaultFlavorTable());
                for (Iterator iter = flavorsForFormats.keySet().iterator();
                     iter.hasNext(); )
                {
                    DataFlavor flavor = (DataFlavor)iter.next();
                    Long lFormat = (Long)flavorsForFormats.get(flavor);

                    fetchOneFlavor(clipboard, flavor, lFormat, cached_data);
                }

                flavors = DataTransferer.getInstance().
                    setToSortedDataFlavorArray(flavorsToData.keySet());
            }
        } finally {
            clipboard.closeClipboard();
        }
    }

    private boolean fetchOneFlavor(SunClipboard clipboard, DataFlavor flavor,
                                   Long lFormat, HashMap cached_data)
    {
        if (!flavorsToData.containsKey(flavor)) {
            long format = lFormat.longValue();
            Object data = null;

            if (!cached_data.containsKey(lFormat)) {
                try {
                    data = clipboard.getClipboardData(format);
                } catch (IOException e) {
                    data = e;
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                // Cache this data, even if it's null, so we don't have to go
                // to native code again for this format.
                cached_data.put(lFormat, data);
            } else {
                data = cached_data.get(lFormat);
            }

            // Casting IOException to byte array causes ClassCastException.
            // We should handle IOException separately - do not wrap them into
            // DataFactory and report failure.
            if (data instanceof IOException) {
                flavorsToData.put(flavor, data);
                return false;
            } else if (data != null) {
                flavorsToData.put(flavor, new DataFactory(format,
                                                          (byte[])data));
                return true;
            }
        }

        return false;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[])flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavorsToData.containsKey(flavor);
    }

    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException, IOException
    {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        Object ret = flavorsToData.get(flavor);
        if (ret instanceof IOException) {
            // rethrow IOExceptions generated while fetching data
            throw (IOException)ret;
        } else if (ret instanceof DataFactory) {
            // Now we can render the data
            DataFactory factory = (DataFactory)ret;
            ret = factory.getTransferData(flavor);
        }
        return ret;
    }

}
