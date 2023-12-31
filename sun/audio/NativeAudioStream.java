package sun.audio;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;

/**
 * A Sun-specific AudioStream that supports native audio formats.
 *
 */

 /*
 * note: this file used to do the real header reading and
 * format verification for .au files (the only kind supported).
 * now we are way more cool than that and don't need this
 * functionality here; i'm just gutting this class and letting
 * it contain an ACIS instead (so now it should work for
 * all the data types we support....).
 */

public
    class NativeAudioStream extends FilterInputStream {


        public NativeAudioStream(InputStream in) throws IOException {

            super(in);
        }

        public int getLength() {
            return 0;
        }
    }
