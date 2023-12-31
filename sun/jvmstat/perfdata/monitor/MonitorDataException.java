package sun.jvmstat.perfdata.monitor;

import sun.jvmstat.monitor.MonitorException;

/**
 * Exception indicating that improperly formatted data was encountered
 * while parsing a HotSpot PerfData buffer.
 *
 * @author Brian Doherty
 * @since 1.5
 */
public class MonitorDataException extends MonitorException {

    /**
     * Constructs a <code>MonitorDataException</code> with <code>
     * null</code> as its error detail message.
     */
     public MonitorDataException() {
       super();
       }

    /**
     * Constructs an <code>MonitorDataException</code> with the specified
     * detail message. The error message string <code>s</code> can later be
     * retrieved by the <code>{@link java.lang.Throwable#getMessage}</code>
     * method of class <code>java.lang.Throwable</code>.
     *
     * @param s the detail message.
     */
    public MonitorDataException(String s) {
        super(s);
    }
}
