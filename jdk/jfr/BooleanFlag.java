package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Event field annotation, specifies that the value is a boolean flag, a {@code true} or
 * {@code false} value
 *
 * @since 8
 */
@MetadataDefinition
@ContentType
@Label("Flag")
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
public @interface BooleanFlag {
}
