package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta annotation, specifies that an annotation represents a content type, such
 * as a time span or a frequency.
 *
 * @since 8
 */
@MetadataDefinition
@Label("Content Type")
@Description("Semantic meaning of a value")
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentType {
}
