package com.sun.media.sound;

import javax.sound.midi.MidiDevice;


/**
 * MIDI output device provider.
 *
 * @author Kara Kytle
 * @author Florian Bomers
 */
public final class MidiOutDeviceProvider extends AbstractMidiDeviceProvider {

    /** Cache of info objects for all MIDI output devices on the system. */
    private static Info[] infos = null;

    /** Cache of open MIDI output devices on the system. */
    private static MidiDevice[] devices = null;

    private final static boolean enabled;

    // STATIC

    static {
        // initialize
        Platform.initialize();
        enabled = Platform.isMidiIOEnabled();
    }

    // CONSTRUCTOR

    /**
     * Required public no-arg constructor.
     */
    public MidiOutDeviceProvider() {
        if (Printer.trace) Printer.trace("MidiOutDeviceProvider: constructor");
    }

    // implementation of abstract methods in AbstractMidiDeviceProvider

    AbstractMidiDeviceProvider.Info createInfo(int index) {
        if (!enabled) {
            return null;
        }
        return new MidiOutDeviceInfo(index, MidiOutDeviceProvider.class);
    }

    MidiDevice createDevice(AbstractMidiDeviceProvider.Info info) {
        if (enabled && (info instanceof MidiOutDeviceInfo)) {
            return new MidiOutDevice(info);
        }
        return null;
    }

    int getNumDevices() {
        if (!enabled) {
            if (Printer.debug)Printer.debug("MidiOutDevice not enabled, returning 0 devices");
            return 0;
        }
        return nGetNumDevices();
    }

    MidiDevice[] getDeviceCache() { return devices; }
    void setDeviceCache(MidiDevice[] devices) { this.devices = devices; }
    Info[] getInfoCache() { return infos; }
    void setInfoCache(Info[] infos) { this.infos = infos; }


    // INNER CLASSES

    /**
     * Info class for MidiOutDevices.  Adds the
     * provider's Class to keep the provider class from being
     * unloaded.  Otherwise, at least on JDK1.1.7 and 1.1.8,
     * the provider class can be unloaded.  Then, then the provider
     * is next invoked, the static block is executed again and a new
     * instance of the device object is created.  Even though the
     * previous instance may still exist and be open / in use / etc.,
     * the new instance will not reflect that state...
     */
    static final class MidiOutDeviceInfo extends AbstractMidiDeviceProvider.Info {
        private final Class providerClass;

        private MidiOutDeviceInfo(int index, Class providerClass) {
            super(nGetName(index), nGetVendor(index), nGetDescription(index), nGetVersion(index), index);
            this.providerClass = providerClass;
        }

    } // class MidiOutDeviceInfo


    // NATIVE METHODS

    private static native int nGetNumDevices();
    private static native String nGetName(int index);
    private static native String nGetVendor(int index);
    private static native String nGetDescription(int index);
    private static native String nGetVersion(int index);
}
