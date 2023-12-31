package jdk.jfr;

import java.time.Duration;
import java.util.Map;

/**
 * Convenience class for applying event settings to a recording.
 * <p>
 * An {@code EventSettings} object for a recording can be obtained by invoking
 * the {@link Recording#enable(String)} method which is configured using method
 * chaining.
 * <p>
 * The following example shows how to use the {@code EventSettings} class.
 * <pre>
 * {@code
 * Recording r = new Recording();
 * r.enable("jdk.CPULoad")
 *    .withPeriod(Duration.ofSeconds(1));
 * r.enable("jdk.FileWrite")
 *    .withoutStackTrace()
 *    .withThreshold(Duration.ofNanos(10));
 * r.start();
 * Thread.sleep(10_000);
 * r.stop();
 * r.dump(Files.createTempFile("recording", ".jfr"));
 *
 * }
 * </pre>
 * @since 8
 */
public abstract class EventSettings {

    // package private
    EventSettings() {
    }

    /**
     * Enables stack traces for the event that is associated with this event setting.
     * <p>
     * Equivalent to invoking the {@code with("stackTrace", "true")} method.
     *
     * @return event settings object for further configuration, not {@code null}
     */
    final public EventSettings withStackTrace() {
        return with(StackTrace.NAME, "true");
    }

    /**
     * Disables stack traces for the event that is associated with this event setting.
     * <p>
     * Equivalent to invoking the {@code with("stackTrace", "false")} method.
     *
     * @return event settings object for further configuration, not {@code null}
     */
    final public EventSettings withoutStackTrace() {
        return with(StackTrace.NAME, "false");
    }

    /**
     * Specifies that a threshold is not used.
     * <p>
     * This is a convenience method, equivalent to invoking the
     * {@code with("threshold", "0 s")} method.
     *
     * @return event settings object for further configuration, not {@code null}
     */
    final public EventSettings withoutThreshold() {
        return with(Threshold.NAME, "0 s");
    }

    /**
     * Sets the interval for the event that is associated with this event setting.
     *
     * @param duration the duration, not {@code null}
     *
     * @return event settings object for further configuration, not {@code null}
     */
    final public EventSettings withPeriod(Duration duration) {
        return with(Period.NAME, duration.toNanos() + " ns");
    }

    /**
     * Sets the threshold for the event that is associated with this event setting.
     *
     * @param duration the duration, or {@code null} if no duration is used
     *
     * @return event settings object for further configuration, not {@code null}
     */
    final public EventSettings withThreshold(Duration duration) {
        if (duration == null) {
            return with(Threshold.NAME, "0 ns");
        } else {
            return with(Threshold.NAME, duration.toNanos() + " ns");
        }
    }

    /**
     * Sets a setting value for the event that is associated with this event setting.
     *
     * @param name the name of the setting (for example, {@code "threshold"})
     *
     * @param value the value to set (for example {@code "20 ms"} not
     *        {@code null})
     *
     * @return event settings object for further configuration, not {@code null}
     */
    abstract public EventSettings with(String name, String value);

    /**
     * Creates a settings {@code Map} for the event that is associated with this
     * event setting.
     *
     * @return a settings {@code Map}, not {@code null}
     */
    abstract Map<String, String> toMap();
}
