package com.sun.media.sound;

import javax.sound.midi.*;



/**
 * MidiInDevice class representing functionality of MidiIn devices.
 *
 * @author David Rivas
 * @author Kara Kytle
 * @author Florian Bomers
 */
final class MidiInDevice extends AbstractMidiDevice implements Runnable {

    private volatile Thread midiInThread;

    // CONSTRUCTOR

    MidiInDevice(AbstractMidiDeviceProvider.Info info) {
        super(info);
        if(Printer.trace) Printer.trace("MidiInDevice CONSTRUCTOR");
    }


    // IMPLEMENTATION OF ABSTRACT MIDI DEVICE METHODS

    // $$kk: 06.24.99: i have this both opening and starting the midi in device.
    // may want to separate these??
    protected synchronized void implOpen() throws MidiUnavailableException {
        if (Printer.trace) Printer.trace("> MidiInDevice: implOpen()");

        int index = ((MidiInDeviceProvider.MidiInDeviceInfo)getDeviceInfo()).getIndex();
        id = nOpen(index); // can throw MidiUnavailableException

        if (id == 0) {
            throw new MidiUnavailableException("Unable to open native device");
        }

        // create / start a thread to get messages
        if (midiInThread == null) {
            midiInThread = JSSecurityManager.createThread(this,
                                                    "Java Sound MidiInDevice Thread",   // name
                                                    false,  // daemon
                                                    -1,    // priority
                                                    true); // doStart
        }

        nStart(id); // can throw MidiUnavailableException
        if (Printer.trace) Printer.trace("< MidiInDevice: implOpen() completed");
    }


    // $$kk: 06.24.99: i have this both stopping and closing the midi in device.
    // may want to separate these??
    protected synchronized void implClose() {
        if (Printer.trace) Printer.trace("> MidiInDevice: implClose()");
        long oldId = id;
        id = 0;

        super.implClose();

        // close the device
        nStop(oldId);
        if (midiInThread != null) {
            try {
                midiInThread.join(1000);
            } catch (InterruptedException e) {
                // IGNORE EXCEPTION
            }
        }
        nClose(oldId);
        if (Printer.trace) Printer.trace("< MidiInDevice: implClose() completed");
    }


    public long getMicrosecondPosition() {
        long timestamp = -1;
        if (isOpen()) {
            timestamp = nGetTimeStamp(id);
        }
        return timestamp;
    }


    // OVERRIDES OF ABSTRACT MIDI DEVICE METHODS


    protected boolean hasTransmitters() {
        return true;
    }


    protected Transmitter createTransmitter() {
        return new MidiInTransmitter();
    }

    /**
      * An own class to distinguish the class name from
      * the transmitter of other devices
      */
    private final class MidiInTransmitter extends BasicTransmitter {
        private MidiInTransmitter() {
            super();
        }
    }

    // RUNNABLE METHOD

    public void run() {
        // while the device is started, keep trying to get messages.
        // this thread returns from native code whenever stop() or close() is called
        while (id!=0) {
            // go into native code and retrieve messages
            nGetMessages(id);
            if (id!=0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
            }
        }
        if(Printer.verbose) Printer.verbose("MidiInDevice Thread exit");
        // let the thread exit
        midiInThread = null;
    }


    // CALLBACKS FROM NATIVE

    /**
     * Callback from native code when a short MIDI event is received from hardware.
     * @param packedMsg: status | data1 << 8 | data2 << 8
     * @param timeStamp time-stamp in microseconds
     */
    void callbackShortMessage(int packedMsg, long timeStamp) {
        if (packedMsg == 0 || id == 0) {
            return;
        }

        /*if(Printer.verbose) {
          int status = packedMsg & 0xFF;
          int data1 = (packedMsg & 0xFF00)>>8;
          int data2 = (packedMsg & 0xFF0000)>>16;
          Printer.verbose(">> MidiInDevice callbackShortMessage: status: " + status + " data1: " + data1 + " data2: " + data2 + " timeStamp: " + timeStamp);
          }*/

        getTransmitterList().sendMessage(packedMsg, timeStamp);
    }

    void callbackLongMessage(byte[] data, long timeStamp) {
        if (id == 0 || data == null) {
            return;
        }
        getTransmitterList().sendMessage(data, timeStamp);
    }

    // NATIVE METHODS

    private native long nOpen(int index) throws MidiUnavailableException;
    private native void nClose(long id);

    private native void nStart(long id) throws MidiUnavailableException;
    private native void nStop(long id);
    private native long nGetTimeStamp(long id);

    // go into native code and get messages. May be blocking
    private native void nGetMessages(long id);


}
