package java.awt.event;

import java.util.EventListener;

/**
 * The listener interface for receiving input method events. A text editing
 * component has to install an input method event listener in order to work
 * with input methods.
 *
 * <p>
 * The text editing component also has to provide an instance of InputMethodRequests.
 *
 * @author JavaSoft Asia/Pacific
 * @see InputMethodEvent
 * @see java.awt.im.InputMethodRequests
 * @since 1.2
 */

public interface InputMethodListener extends EventListener {

    /**
     * Invoked when the text entered through an input method has changed.
     */
    void inputMethodTextChanged(InputMethodEvent event);

    /**
     * Invoked when the caret within composed text has changed.
     */
    void caretPositionChanged(InputMethodEvent event);

}
