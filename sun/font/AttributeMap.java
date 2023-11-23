package sun.font;

import java.awt.Paint;
import java.awt.font.GraphicAttribute;
import java.awt.font.NumericShaper;
import java.awt.font.TextAttribute;
import java.awt.font.TransformAttribute;
import java.awt.geom.AffineTransform;
import java.awt.im.InputMethodHighlight;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import static sun.font.AttributeValues.*;

public final class AttributeMap extends AbstractMap<TextAttribute, Object> {
    private AttributeValues values;
    private Map<TextAttribute, Object> delegateMap;

    public AttributeMap(AttributeValues values) {
        this.values = values;
    }

    public Set<Entry<TextAttribute, Object>> entrySet() {
        return delegate().entrySet();
    }

    public Object put(TextAttribute key, Object value) {
        return delegate().put(key, value);
    }

    // internal API
    public AttributeValues getValues() {
        return values;
    }

    private static boolean first = false; // debug
    private Map<TextAttribute, Object> delegate() {
        if (delegateMap == null) {
            if (first) {
                first = false;
                Thread.dumpStack();
            }
            delegateMap = values.toMap(new HashMap<TextAttribute, Object>(27));

            // nuke values, once map is accessible it might be mutated and values would
            // no longer reflect its contents
            values = null;
        }

        return delegateMap;
    }

    public String toString() {
        if (values != null) {
            return "map of " + values.toString();
        }
        return super.toString();
    }
}