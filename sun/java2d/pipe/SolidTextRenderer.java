package sun.java2d.pipe;

import java.awt.font.GlyphVector;
import sun.awt.SunHints;
import sun.java2d.SunGraphics2D;
import sun.font.GlyphList;

/**
 * A delegate pipe of SG2D for drawing text with
 * a solid source colour to an opaque destination.
 */

public class SolidTextRenderer extends GlyphListLoopPipe
    implements LoopBasedPipe
{
    protected void drawGlyphList(SunGraphics2D sg2d, GlyphList gl) {
        sg2d.loops.drawGlyphListLoop.DrawGlyphList(sg2d, sg2d.surfaceData, gl);
    }
}
