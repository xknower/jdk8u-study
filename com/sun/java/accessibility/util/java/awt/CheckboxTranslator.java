package com.sun.java.accessibility.util.java.awt;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.accessibility.*;
import com.sun.java.accessibility.util.*;

/**
 * <p>The Translator class provides a translation to interface Accessible
 * for objects that do not implement interface Accessible.  Assistive
 * technologies can use the 'getAccessible' class method of Translator to
 * obtain an object that implements interface Accessible.  If the object
 * passed in already implements interface Accessible, getAccessible merely
 * returns the object.
 *
 * <p>An example of how an assistive technology might use the Translator
 * class is as follows:
 *
 * <PRE>
 *    Accessible accessible = Translator.getAccessible(someObj);
 *    // obtain information from the 'accessible' object.
 * </PRE>
 *
 * <P>This class extends the Translator class to provide specific support
 * for the Checkbox class.  Translator.getAccessible() will automatically
 * load this class when an assistive technology asks for an accessible
 * translator for Checkbox.
 *
 */
public class CheckboxTranslator extends Translator {

    /**
     * Get the state of this object.
     * @return an instance of AccessibleState containing the current state of the object
     * @see AccessibleState
     */
    public AccessibleStateSet getAccessibleStateSet() {
        AccessibleStateSet states = super.getAccessibleStateSet();
        if (((Checkbox) source).getState()) {
            states.add(AccessibleState.CHECKED);
        }
        return states;
    }

    /**
     * Get the name of this object.
     * @return the name of the object -- can be null if this object does
     * not have a name
     */
    public String getAccessibleName() {
        return ((Checkbox) source).getLabel();
    }

    /**
     * Set the name of this object.
     */
    public void setAccessibleName(String s) {
        ((Checkbox) source).setLabel(s);
    }

    public AccessibleRole getAccessibleRole() {
        return AccessibleRole.CHECK_BOX;
    }
}
