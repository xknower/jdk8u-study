package sun.awt.im;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Toolkit;
import sun.awt.AppContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InvocationEvent;
import java.awt.im.spi.InputMethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.Vector;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import sun.awt.InputMethodSupport;
import sun.awt.SunToolkit;

/**
 * <code>InputMethodManager</code> is an abstract class that manages the input
 * method environment of JVM. There is only one <code>InputMethodManager</code>
 * instance in JVM that is executed under a separate daemon thread.
 * <code>InputMethodManager</code> performs the following:
 * <UL>
 * <LI>
 * Keeps track of the current input context.</LI>
 *
 * <LI>
 * Provides a user interface to switch input methods and notifies the current
 * input context about changes made from the user interface.</LI>
 * </UL>
 *
 * The mechanism for supporting input method switch is as follows. (Note that
 * this may change in future releases.)
 *
 * <UL>
 * <LI>
 * One way is to use platform-dependent window manager's menu (known as the <I>Window
 * menu </I>in Motif and the <I>System menu</I> or <I>Control menu</I> in
 * Win32) on each window which is popped up by clicking the left top box of
 * a window (known as <I>Window menu button</I> in Motif and <I>System menu
 * button</I> in Win32). This happens to be common in both Motif and Win32.</LI>
 *
 * <LI>
 * When more than one input method descriptor can be found or the only input
 * method descriptor found supports multiple locales, a menu item
 * is added to the window (manager) menu. This item label is obtained invoking
 * <code>getTriggerMenuString()</code>. If null is returned by this method, it
 * means that there is only input method or none in the environment. Frame and Dialog
 * invoke this method.</LI>
 *
 * <LI>
 * This menu item means a trigger switch to the user to pop up a selection
 * menu.</LI>
 *
 * <LI>
 * When the menu item of the window (manager) menu has been selected by the
 * user, Frame/Dialog invokes <code>notifyChangeRequest()</code> to notify
 * <code>InputMethodManager</code> that the user wants to switch input methods.</LI>
 *
 * <LI>
 * <code>InputMethodManager</code> displays a pop-up menu to choose an input method.</LI>
 *
 * <LI>
 * <code>InputMethodManager</code> notifies the current <code>InputContext</code> of
 * the selected <code>InputMethod</code>.</LI>
 * </UL>
 *
 * <UL>
 * <LI>
 * The other way is to use user-defined hot key combination to show the pop-up menu to
 * choose an input method.  This is useful for the platforms which do not provide a
 * way to add a menu item in the window (manager) menu.</LI>
 *
 * <LI>
 * When the hot key combination is typed by the user, the component which has the input
 * focus invokes <code>notifyChangeRequestByHotKey()</code> to notify
 * <code>InputMethodManager</code> that the user wants to switch input methods.</LI>
 *
 * <LI>
 * This results in a popup menu and notification to the current input context,
 * as above.</LI>
 * </UL>
 *
 * @see java.awt.im.spi.InputMethod
 * @see sun.awt.im.InputContext
 * @see sun.awt.im.InputMethodAdapter
 * @author JavaSoft International
 */

public abstract class InputMethodManager {

    /**
     * InputMethodManager thread name
     */
    private static final String threadName = "AWT-InputMethodManager";

    /**
     * Object for global locking
     */
    private static final Object LOCK = new Object();

    /**
     * The InputMethodManager instance
     */
    private static InputMethodManager inputMethodManager;

    /**
     * Returns the instance of InputMethodManager. This method creates
     * the instance that is unique in the Java VM if it has not been
     * created yet.
     *
     * @return the InputMethodManager instance
     */
    public static final InputMethodManager getInstance() {
        if (inputMethodManager != null) {
            return inputMethodManager;
        }
        synchronized(LOCK) {
            if (inputMethodManager == null) {
                ExecutableInputMethodManager imm = new ExecutableInputMethodManager();

                // Initialize the input method manager and start a
                // daemon thread if the user has multiple input methods
                // to choose from. Otherwise, just keep the instance.
                if (imm.hasMultipleInputMethods()) {
                    imm.initialize();
                    Thread immThread = new Thread(imm, threadName);
                    immThread.setDaemon(true);
                    immThread.setPriority(Thread.NORM_PRIORITY + 1);
                    immThread.start();
                }
                inputMethodManager = imm;
            }
        }
        return inputMethodManager;
    }

    /**
     * Gets a string for the trigger menu item that should be added to
     * the window manager menu. If no need to display the trigger menu
     * item, null is returned.
     */
    public abstract String getTriggerMenuString();

    /**
     * Notifies InputMethodManager that input method change has been
     * requested by the user. This notification triggers a popup menu
     * for user selection.
     *
     * @param comp Component that has accepted the change
     * request. This component has to be a Frame or Dialog.
     */
    public abstract void notifyChangeRequest(Component comp);

    /**
     * Notifies InputMethodManager that input method change has been
     * requested by the user using the hot key combination. This
     * notification triggers a popup menu for user selection.
     *
     * @param comp Component that has accepted the change
     * request. This component has the input focus.
     */
    public abstract void notifyChangeRequestByHotKey(Component comp);

    /**
     * Sets the current input context so that it will be notified
     * of input method changes initiated from the user interface.
     * Set to real input context when activating; to null when
     * deactivating.
     */
    abstract void setInputContext(InputContext inputContext);

    /**
     * Tries to find an input method locator for the given locale.
     * Returns null if no available input method locator supports
     * the locale.
     */
    abstract InputMethodLocator findInputMethod(Locale forLocale);

    /**
     * Gets the default keyboard locale of the underlying operating system.
     */
    abstract Locale getDefaultKeyboardLocale();

    /**
     * Returns whether multiple input methods are available or not
     */
    abstract boolean hasMultipleInputMethods();

}
