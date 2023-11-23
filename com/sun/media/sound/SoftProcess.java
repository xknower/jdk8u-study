package com.sun.media.sound;

/**
 * Control signal processor interface
 *
 * @author Karl Helgason
 */
public interface SoftProcess extends SoftControl {

    public void init(SoftSynthesizer synth);

    public double[] get(int instance, String name);

    public void processControlLogic();

    public void reset();
}
