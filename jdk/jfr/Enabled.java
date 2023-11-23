package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Event annotation, determines if an event should be enabled by default.
 * <p>
 * If an event doesn't have the annotation, then by default the event is enabled.
 *
 * @since 8
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@MetadataDefinition
public @interface Enabled {
    /**
     * Setting name {@code "enabled"}, signifies that the event should be
     * recorded.
     */
    public final static String NAME = "enabled";

    /**
     * Returns {@code true} if by default the event should be enabled, {@code false} otherwise.
     *
     * @return {@code true} if by default the event should be enabled by default, {@code false} otherwise
     */
    boolean value() default true;
}
