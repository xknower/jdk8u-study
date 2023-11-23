package com.sun.media.sound;

/**
 * Soundfont instrument region.
 *
 * @author Karl Helgason
 */
public final class SF2InstrumentRegion extends SF2Region {

    SF2Layer layer;

    public SF2Layer getLayer() {
        return layer;
    }

    public void setLayer(SF2Layer layer) {
        this.layer = layer;
    }
}
