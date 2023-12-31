package sun.audio;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;


/**
 * This class provides an interface to play audio streams.
 *
 * To play an audio stream use:
 * <pre>
 *      AudioPlayer.player.start(audiostream);
 * </pre>
 * To stop playing an audio stream use:
 * <pre>
 *      AudioPlayer.player.stop(audiostream);
 * </pre>
 * To play an audio stream from a URL use:
 * <pre>
 *      AudioStream audiostream = new AudioStream(url.openStream());
 *      AudioPlayer.player.start(audiostream);
 * </pre>
 * To play a continuous sound you first have to
 * create an AudioData instance and use it to construct a
 * ContinuousAudioDataStream.
 * For example:
 * <pre>
 *      AudioData data = new AudioStream(url.openStream()).getData();
 *      ContinuousAudioDataStream audiostream = new ContinuousAudioDataStream(data);
 *      AudioPlayer.player.start(audiostream);
 * </pre>
 *
 * @see AudioData
 * @see AudioDataStream
 * @see AudioDevice
 * @see AudioStream
 * @author Arthur van Hoff, Thomas Ball
 */

public final class AudioPlayer extends Thread {

        private final AudioDevice devAudio;
        private final static boolean DEBUG = false /*true*/;

        /**
         * The default audio player. This audio player is initialized
         * automatically.
         */
        public static final AudioPlayer player = getAudioPlayer();

        private static ThreadGroup getAudioThreadGroup() {

            if(DEBUG) { System.out.println("AudioPlayer.getAudioThreadGroup()"); }
            ThreadGroup g = currentThread().getThreadGroup();
            while ((g.getParent() != null) &&
                   (g.getParent().getParent() != null)) {
                g = g.getParent();
            }
            return g;
        }

        /**
         * Create an AudioPlayer thread in a privileged block.
         */

        private static AudioPlayer getAudioPlayer() {

            if(DEBUG) { System.out.println("> AudioPlayer.getAudioPlayer()"); }
            AudioPlayer audioPlayer;
            PrivilegedAction action = new PrivilegedAction() {
                    public Object run() {
                        Thread t = new AudioPlayer();
                        t.setPriority(MAX_PRIORITY);
                        t.setDaemon(true);
                        t.start();
                        return t;
                    }
                };
            audioPlayer = (AudioPlayer) AccessController.doPrivileged(action);
            return audioPlayer;
        }

        /**
         * Construct an AudioPlayer.
         */
        private AudioPlayer() {

            super(getAudioThreadGroup(), "Audio Player");
            if(DEBUG) { System.out.println("> AudioPlayer private constructor"); }
            devAudio = AudioDevice.device;
            devAudio.open();
            if(DEBUG) { System.out.println("< AudioPlayer private constructor completed"); }
        }


        /**
         * Start playing a stream. The stream will continue to play
         * until the stream runs out of data, or it is stopped.
         * @see AudioPlayer#stop
         */
        public synchronized void start(InputStream in) {

            if(DEBUG) {
                System.out.println("> AudioPlayer.start");
                System.out.println("  InputStream = " + in);
            }
            devAudio.openChannel(in);
            notify();
            if(DEBUG) {
                System.out.println("< AudioPlayer.start completed");
            }
        }

        /**
         * Stop playing a stream. The stream will stop playing,
         * nothing happens if the stream wasn't playing in the
         * first place.
         * @see AudioPlayer#start
         */
        public synchronized void stop(InputStream in) {

            if(DEBUG) {
                System.out.println("> AudioPlayer.stop");
            }

            devAudio.closeChannel(in);
            if(DEBUG) {
                System.out.println("< AudioPlayer.stop completed");
            }
        }

        /**
         * Main mixing loop. This is called automatically when the AudioPlayer
         * is created.
         */
        public void run() {

            // $$jb: 06.24.99: With the JS API, mixing is no longer done by AudioPlayer
            // or AudioDevice ... it's done by the API itself, so this bit of legacy
            // code does nothing.
            // $$jb: 10.21.99: But it appears that some legacy applications
            // check to see if this thread is alive or not, so we need to spin here.

            devAudio.play();
            if(DEBUG) {
                System.out.println("AudioPlayer mixing loop.");
            }
            while(true) {
                try{
                    Thread.sleep(5000);
                    //wait();
                } catch(Exception e) {
                    break;
                    // interrupted
                }
            }
            if(DEBUG) {
                System.out.println("AudioPlayer exited.");
            }

        }
    }
