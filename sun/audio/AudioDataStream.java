package sun.audio;

import java.io.*;

/**
 * An input stream to play AudioData.
 *
 * @see AudioPlayer
 * @see AudioData
 * @author Arthur van Hoff
 * @author Kara Kytle
 */
public class AudioDataStream extends ByteArrayInputStream {

    private final AudioData ad;

    /**
     * Constructor
     */
    public AudioDataStream(final AudioData data) {

        super(data.buffer);
        this.ad = data;
    }

    final AudioData getAudioData() {
        return ad;
    }
}
