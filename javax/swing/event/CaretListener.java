package javax.swing.event;


import java.util.EventListener;

/**
 * Listener for changes in the caret position of a text
 * component.
 *
 * @author  Timothy Prinzing
 */
public interface CaretListener extends EventListener {

    /**
     * Called when the caret position is updated.
     *
     * @param e the caret event
     */
    void caretUpdate(CaretEvent e);
}
