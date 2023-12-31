package com.sun.beans.editors;

/**
 * Property editor for a java builtin "double" type.
 *
 */

import java.beans.*;

public class DoubleEditor extends NumberEditor {

    public void setAsText(String text) throws IllegalArgumentException {
        setValue((text == null) ? null : Double.valueOf(text));
    }

}
