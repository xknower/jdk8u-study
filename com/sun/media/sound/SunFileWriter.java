package com.sun.media.sound;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.DataInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.spi.AudioFileWriter;




/**
 * Abstract File Writer class.
 *
 * @author Jan Borgersen
 */
abstract class SunFileWriter extends AudioFileWriter {


    // buffer size for write
    protected static final int bufferSize = 16384;

    // buffer size for temporary input streams
    protected static final int bisBufferSize = 4096;


    final AudioFileFormat.Type types[];


    /**
     * Constructs a new SunParser object.
     */
    SunFileWriter(AudioFileFormat.Type types[]) {
        this.types = types;
    }



    // METHODS TO IMPLEMENT AudioFileWriter

    // new, 10.27.99

    public final AudioFileFormat.Type[] getAudioFileTypes(){
        AudioFileFormat.Type[] localArray = new AudioFileFormat.Type[types.length];
        System.arraycopy(types, 0, localArray, 0, types.length);
        return localArray;
    }


    public abstract AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream stream);

    public abstract int write(AudioInputStream stream, AudioFileFormat.Type fileType, OutputStream out) throws IOException;

    public abstract int write(AudioInputStream stream, AudioFileFormat.Type fileType, File out) throws IOException;


    // HELPER METHODS


    /**
     * rllong
     * Protected helper method to read 64 bits and changing the order of
     * each bytes.
     * @param DataInputStream
     * @return 32 bits swapped value.
     * @exception IOException
     */
    final int rllong(DataInputStream dis) throws IOException {

        int b1, b2, b3, b4 ;
        int i = 0;

        i = dis.readInt();

        b1 = ( i & 0xFF ) << 24 ;
        b2 = ( i & 0xFF00 ) << 8;
        b3 = ( i & 0xFF0000 ) >> 8;
        b4 = ( i & 0xFF000000 ) >>> 24;

        i = ( b1 | b2 | b3 | b4 );

        return i;
    }

    /**
     * big2little
     * Protected helper method to swap the order of bytes in a 32 bit int
     * @param int
     * @return 32 bits swapped value
     */
    final int big2little(int i) {

        int b1, b2, b3, b4 ;

        b1 = ( i & 0xFF ) << 24 ;
        b2 = ( i & 0xFF00 ) << 8;
        b3 = ( i & 0xFF0000 ) >> 8;
        b4 = ( i & 0xFF000000 ) >>> 24;

        i = ( b1 | b2 | b3 | b4 );

        return i;
    }

    /**
     * rlshort
     * Protected helper method to read 16 bits value. Swap high with low byte.
     * @param DataInputStream
     * @return the swapped value.
     * @exception IOException
     */
    final short rlshort(DataInputStream dis)  throws IOException {

        short s=0;
        short high, low;

        s = dis.readShort();

        high = (short)(( s & 0xFF ) << 8) ;
        low = (short)(( s & 0xFF00 ) >>> 8);

        s = (short)( high | low );

        return s;
    }

    /**
     * big2little
     * Protected helper method to swap the order of bytes in a 16 bit short
     * @param int
     * @return 16 bits swapped value
     */
    final short big2littleShort(short i) {

        short high, low;

        high = (short)(( i & 0xFF ) << 8) ;
        low = (short)(( i & 0xFF00 ) >>> 8);

        i = (short)( high | low );

        return i;
    }

    /**
     * InputStream wrapper class which prevent source stream from being closed.
     * The class is usefull for use with SequenceInputStream to prevent
     * closing of the source input streams.
     */
    final class NoCloseInputStream extends InputStream {
        private final InputStream in;

        NoCloseInputStream(InputStream in) {
            this.in = in;
        }

        @Override
        public int read() throws IOException {
            return in.read();
        }

        @Override
        public int read(byte b[]) throws IOException {
            return in.read(b);
        }

        @Override
        public int read(byte b[], int off, int len) throws IOException {
            return in.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            return in.skip(n);
        }

        @Override
        public int available() throws IOException {
            return in.available();
        }

        @Override
        public void close() throws IOException {
            // don't propagate the call
        }

        @Override
        public void mark(int readlimit) {
            in.mark(readlimit);
        }

        @Override
        public void reset() throws IOException {
            in.reset();
        }

        @Override
        public boolean markSupported() {
            return in.markSupported();
        }

    }
}
