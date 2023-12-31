package com.sun.media.sound;

import javax.sound.midi.MidiDevice;


/**
 * MIDI input device provider.
 *
 * @author Kara Kytle
 * @author Florian Bomers
 */
public final class MidiInDeviceProvider extends AbstractMidiDeviceProvider {

    /** Cache of info objects for all MIDI output devices on the system. */
    private static Info[] infos = null;

    /** Cache of open MIDI input devices on the system. */
    private static MidiDevice[] devices = null;

    private static final boolean enabled;

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
    public MidiInDeviceProvider() {
        if (Printer.trace) Printer.trace("MidiInDeviceProvider: constructor");
    }

    // implementation of abstract methods in AbstractMidiDeviceProvider

    AbstractMidiDeviceProvider.Info createInfo(int index) {
        if (!enabled) {
            return null;
        }
        return new MidiInDeviceInfo(index, MidiInDeviceProvider.class);
    }

    MidiDevice createDevice(AbstractMidiDeviceProvider.Info info) {
        if (enabled && (info instanceof MidiInDeviceInfo)) {
            return new MidiInDevice(info);
        }
        return null;
    }

    int getNumDevices() {
        if (!enabled) {
            if (Printer.debug)Printer.debug("MidiInDevice not enabled, returning 0 devices");
            return 0;
        }
        int numDevices = nGetNumDevices();
        if (Printer.debug)Printer.debug("MidiInDeviceProvider.getNumDevices(): devices: " + numDevices);
        return numDevices;
    }

    MidiDevice[] getDeviceCache() { return devices; }
    void setDeviceCache(MidiDevice[] devices) { this.devices = devices; }
    Info[] getInfoCache() { return infos; }
    void setInfoCache(Info[] infos) { this.infos = infos; }


    // INNER CLASSES

    /**
     * Info class for MidiInDevices.  Adds the
     * provider's Class to keep the provider class from being
     * unloaded.  Otherwise, at least on JDK1.1.7 and 1.1.8,
     * the provider class can be unloaded.  Then, then the provider
     * is next invoked, the static block is executed again and a new
     * instance of the device object is created.  Even though the
     * previous instance may still exist and be open / in use / etc.,
     * the new instance will not reflect that state...
     */
    static final class MidiInDeviceInfo extends AbstractMidiDeviceProvider.Info {
        private final Class providerClass;

        private MidiInDeviceInfo(int index, Class providerClass) {
            super(nGetName(index), nGetVendor(index), nGetDescription(index), nGetVersion(index), index);
            this.providerClass = providerClass;
        }

    } // class MidiInDeviceInfo


    // NATIVE METHODS

    private static native int nGetNumDevices();
    private static native String nGetName(int index);
    private static native String nGetVendor(int index);
    private static native String nGetDescription(int index);
    private static native String nGetVersion(int index);
}
