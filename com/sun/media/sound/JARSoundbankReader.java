package com.sun.media.sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Objects;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.spi.SoundbankReader;

import sun.reflect.misc.ReflectUtil;

/**
 * JarSoundbankReader is used to read soundbank object from jar files.
 *
 * @author Karl Helgason
 */
public final class JARSoundbankReader extends SoundbankReader {

    /*
     * Name of the system property that enables the Jar soundbank loading
     * true if jar sound bank is allowed to be loaded
     * default is false
     */
    private final static String JAR_SOUNDBANK_ENABLED = "jdk.sound.jarsoundbank";

    private static boolean isZIP(URL url) {
        boolean ok = false;
        try {
            InputStream stream = url.openStream();
            try {
                byte[] buff = new byte[4];
                ok = stream.read(buff) == 4;
                if (ok) {
                    ok =  (buff[0] == 0x50
                        && buff[1] == 0x4b
                        && buff[2] == 0x03
                        && buff[3] == 0x04);
                }
            } finally {
                stream.close();
            }
        } catch (IOException e) {
        }
        return ok;
    }

    public Soundbank getSoundbank(URL url)
            throws InvalidMidiDataException, IOException {
        Objects.requireNonNull(url);
        if (!Boolean.getBoolean(JAR_SOUNDBANK_ENABLED) || !isZIP(url))
            return null;

        ArrayList<Soundbank> soundbanks = new ArrayList<Soundbank>();
        URLClassLoader ucl = URLClassLoader.newInstance(new URL[]{url});
        InputStream stream = ucl.getResourceAsStream(
                "META-INF/services/javax.sound.midi.Soundbank");
        if (stream == null)
            return null;
        try
        {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            String line = r.readLine();
            while (line != null) {
                if (!line.startsWith("#")) {
                    try {
                        Class<?> c = Class.forName(line.trim(), false, ucl);
                        if (Soundbank.class.isAssignableFrom(c)) {
                            Object o = ReflectUtil.newInstance(c);
                            soundbanks.add((Soundbank) o);
                        }
                    } catch (ClassNotFoundException ignored) {
                    } catch (InstantiationException ignored) {
                    } catch (IllegalAccessException ignored) {
                    }
                }
                line = r.readLine();
            }
        }
        finally
        {
            stream.close();
        }
        if (soundbanks.size() == 0)
            return null;
        if (soundbanks.size() == 1)
            return soundbanks.get(0);
        SimpleSoundbank sbk = new SimpleSoundbank();
        for (Soundbank soundbank : soundbanks)
            sbk.addAllInstruments(soundbank);
        return sbk;
    }

    public Soundbank getSoundbank(InputStream stream)
            throws InvalidMidiDataException, IOException {
        return null;
    }

    public Soundbank getSoundbank(File file)
            throws InvalidMidiDataException, IOException {
        Objects.requireNonNull(file);
        return getSoundbank(file.toURI().toURL());
    }
}
