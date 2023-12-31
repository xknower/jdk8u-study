package jdk.jfr;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jdk.jfr.internal.JVMSupport;
import jdk.jfr.internal.MetadataRepository;
import jdk.jfr.internal.PlatformEventType;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

/**
 * Describes an event, its fields, settings and annotations.
 *
 * @since 8
 */
public final class EventType {
    private final PlatformEventType platformEventType;
    private final List<String> UNCATEGORIZED = Collections.singletonList("Uncategorized");
    private Map<String, ValueDescriptor> cache; // create lazy to avoid memory overhead
    // helper constructor
    EventType(PlatformEventType platformEventType) {
        this.platformEventType = platformEventType;
    }

    /**
     * Returns an immutable list of descriptors that describe the event fields of
     * this event type.
     *
     * @return the list of field descriptors, not {@code null}
     */
    public List<ValueDescriptor> getFields() {
        return platformEventType.getFields();
    }

    /**
     * Returns the field with the specified name, or {@code null} if it doesn't
     * exist.
     *
     * @return a value descriptor that describes the field, or <code>null</code> if
     *         the field with the specified name doesn't exist
     *
     * @return a value descriptor, or <code>null</code> if it doesn't exist
     */
    public ValueDescriptor getField(String name) {
        Objects.requireNonNull(name);
        if (cache == null) {
            List<ValueDescriptor> fields = getFields();
            Map<String, ValueDescriptor> newCache = new LinkedHashMap<String, ValueDescriptor>(fields.size());
            for (ValueDescriptor v :fields) {
                newCache.put(v.getName(), v);
            }
            cache = newCache;
        }
        return cache.get(name);
    }

    /**
     * Returns an identifier for the event (for example,
     * {@code "jdk.CPULoad"}).
     * <p>
     * The identifier is the fully qualified name of the event class, if not set using
     * the {@link Name} annotation.
     *
     * @return the name, not {@code null}
     *
     * @see Name
     */
    public String getName() {
        return platformEventType.getName();
    }

    /**
     * Returns a human-readable name (for example, {@code "CPU Load"}).
     * <p>
     * The label of an event class can be set with {@link Label}.
     *
     * @return the label, or {@code null} if a label is not set
     *
     * @see Label
     */
    public String getLabel() {
        return platformEventType.getLabel();
    }

    /**
     * Returns a unique ID for this event type in the Java Virtual Machine (JVM).
     *
     * @return the ID that is used in the JVM
     */
    public long getId() {
        return platformEventType.getId();
    }

    /**
     * Returns an immutable list of annotation elements for this event type.
     *
     * @return an immutable list of annotations or an empty list if no
     *         annotations exists, not {@code null}
     */
    public List<AnnotationElement> getAnnotationElements() {
        return platformEventType.getAnnotationElements();
    }

    /**
     * Returns {@code true} if the event is enabled and at least one recording is
     * running, {@code false} otherwise.
     * <p>
     * By default, the event is enabled. The event can be enabled or disabled by
     * setting the enabled setting to {@code true} or {@code false}, programmatically or by using a
     * configuration file. The event can also be disabled by annotating event with
     * the {@code @Enabled(false)} annotation.
     *
     * @return true if event is enabled, false otherwise
     *
     * @see Enabled
     * @see Recording#enable(Class)
     */
    public boolean isEnabled() {
        return platformEventType.isEnabled();
    }

    /**
     * Returns a short sentence that describes the event class.
     * <p>
     * The description of an event class can be set with {@link Description}.
     *
     * @return the description, or {@code null} if no description exists
     *
     * @see Description
     */
    public String getDescription() {
        return platformEventType.getDescription();
    }

    /**
     * Returns the first annotation for the specified type if an annotation
     * element with the same name is directly present, otherwise {@code null}.
     *
     * @param <A> the type of the annotation to query for and return if present
     * @param annotationClass the {@code Class} object that corresponds to the
     *        annotation type, not {@code null}
     * @return this element's annotation for the specified annotation type if
     *         directly present, else {@code null}
     */
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        Objects.requireNonNull(annotationClass);
        return platformEventType.getAnnotation(annotationClass);
    }

    /**
     * Returns the event type for an event class, or {@code null} if it doesn't
     * exist.
     *
     * @param eventClass the event class, not {@code null}
     * @return the event class, or null if class doesn't exist
     *
     * @throws IllegalArgumentException if {@code eventClass} is an abstract class
     *
     * @throws IllegalStateException if the class is annotated with
     *         {@code Registered(false)}, but not manually registered
     */
    public static EventType getEventType(Class<? extends Event> eventClass) {
        Objects.requireNonNull(eventClass);
        Utils.ensureValidEventSubclass(eventClass);
        JVMSupport.ensureWithInternalError();
        return MetadataRepository.getInstance().getEventType(eventClass);
    }

    /**
     * Returns an immutable list of the setting descriptors that describe the available
     * event settings for this event type.
     *
     * @return the list of setting descriptors for this event type, not
     *         {@code null}
     */
    public List<SettingDescriptor> getSettingDescriptors() {
        return Collections.unmodifiableList(platformEventType.getSettings());
    }

    /**
     * Returns the list of human-readable names that makes up the categories for
     * this event type (for example, {@code "Java Application"}, {@code "Statistics"}).
     *
     * @return an immutable list of category names, or a list with the name
     *         {@code "Uncategorized"} if no category is set
     *
     * @see Category
     */
    public List<String> getCategoryNames() {
        Category c = platformEventType.getAnnotation(Category.class);
        if (c == null) {
            return UNCATEGORIZED;
        }
        return Collections.unmodifiableList(Arrays.asList(c.value()));
    }

    // package private
    Type getType() {
        return platformEventType;
    }

    // package private
    PlatformEventType getPlatformEventType() {
        return platformEventType;
    }
}
