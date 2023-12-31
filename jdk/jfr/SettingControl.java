package jdk.jfr;

import java.security.AccessController;
import java.util.Set;

import jdk.jfr.internal.Control;

/**
 * Base class to extend to create setting controls.
 * <p>
 * The following example shows a naive implementation of a setting control for
 * regular expressions:
 *
 * <pre>
 * <code>
 * final class RegExpControl extends SettingControl {
 *   private Pattern pattern = Pattern.compile(".*");
 *
 *   {@literal @}Override
 *   public void setValue(String value) {
 *     this.pattern = Pattern.compile(value);
 *   }
 *
 *   {@literal @}Override
 *   public String combine(Set{@literal <}String{@literal >} values) {
 *     return String.join("|", values);
 *   }
 *
 *   {@literal @}Override
 *   public String getValue() {
 *     return pattern.toString();
 *   }
 *
 *   public String matches(String s) {
 *     return pattern.matcher(s).find();
 *   }
 * }
 * </code>
 * </pre>
 *
 * The {@code setValue(String)}, {@code getValue()} and
 * {@code combine(Set<String>)} methods are invoked when a setting value
 * changes, which typically happens when a recording is started or stopped. The
 * {@code combine(Set<String>)} method is invoked to resolve what value to use
 * when multiple recordings are running at the same time.
 * <p>
 * The setting control must have a default constructor that can be invoked when
 * the event is registered.
 * <p>
 * To use a setting control with an event, add a method that returns a
 * {@code boolean} value and takes the setting control as a parameter. Annotate
 * the method with the {@code @SettingDefinition} annotation. By default, the
 * method name is used as the setting name, but the name can be set explicitly
 * by using the {@code @Name} annotation. If the method returns {@code true},
 * the event will be committed.
 * <p>
 * It is recommended that the {@code setValue(String)} method updates an
 * efficient data structure that can be quickly checked when the event is
 * committed.
 * <p>
 * The following example shows how to create an event that uses the
 * regular expression filter defined above.
 *
 * <pre>
 * <code>
 * abstract class HTTPRequest extends Event {
 *   {@literal @}Label("Request URI")
 *   protected String uri;
 *
 *   {@literal @}Label("Servlet URI Filter")
 *   {@literal @}SettingDefinition
 *   protected boolean uriFilter(RegExpControl regExp) {
 *     return regExp.matches(uri);
 *   }
 * }
 *
 * {@literal @}Label("HTTP Get Request")
 * class HTTPGetRequest extends HTTPRequest {
 * }
 *
 * {@literal @}Label("HTTP Post Request")
 * class HTTPPostRequest extends HTTPRequest {
 * }
 *
 * class ExampleServlet extends HTTPServlet {
 *   protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
 *     HTTPGetRequest request = new HTTPGetRequest();
 *     request.begin();
 *     request.uri = req.getRequestURI();
 *     ...
 *     request.commit();
 *   }
 *
 *   protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
 *     HTTPPostRequest request = new HTTPPostRequest();
 *     request.begin();
 *     request.uri = req.getRequestURI();
 *     ...
 *     request.commit();
 *   }
 * }
 * </code>
 * </pre>
 *
 * The following example shows how an event can be filtered by assigning the
 * {@code "uriFilter"} setting with the specified regular expressions.
 *
 * <pre>
 * <code>
 * Recording r = new Recording();
 * r.enable("HTTPGetRequest").with("uriFilter", "https://www.example.com/list/.*");
 * r.enable("HTTPPostRequest").with("uriFilter", "https://www.example.com/login/.*");
 * r.start();
 * </code>
 * </pre>
 *
 *
 *
 * @see SettingDefinition
 *
 * @since 8
 */
@MetadataDefinition
public abstract class SettingControl extends Control {

    /**
     * Constructor for invocation by subclass constructors.
     */
    protected SettingControl() {
        super(AccessController.getContext());

    }

    /**
     * Combines the setting values for all running recordings into one value when
     * multiple recordings are running at the same time,
     * <p>
     * The semantics of how setting values are combined depends on the setting
     * control that is implemented, but all recordings should get at least all the
     * events they request.
     * <p>
     * This method should have no side effects, because the caller might cache values.
     * This method should never return {@code null} or throw an exception. If a
     * value is not valid for this setting control, the value should be ignored.
     * <p>
     * Examples:
     * <p>
     * if the setting control represents a threshold and three recordings are
     * running at the same time with the setting values {@code "10 ms"},
     * {@code "8 s"}, and {@code  "1 ms"}, this method returns {@code "1 ms"}
     * because it means that all recordings get at least all the requested data.
     * <p>
     * If the setting control represents a set of names and two recordings are
     * running at the same time with the setting values {@code "Smith, Jones"} and {@code "Jones,
     * Williams"} the returned value is {@code "Smith, Jones, Williams"} because all names would be accepted.
     * <p>
     * If the setting control represents a boolean condition and four recordings are
     * running at the same time with the following values {@code "true"}, {@code "false"}, {@code "false"}, and
     * {@code "incorrect"}, this method returns {@code "true"}, because all
     * recordings get at least all the requested data.
     *
     * @param settingValues the set of values, not {@code null}
     *
     * @return the value to use, not {@code null}
     */
    @Override
    public abstract String combine(Set<String> settingValues);

    /**
     * Sets the value for this setting.
     * <p>
     * If the setting value is not valid for this setting, this method
     * does not throw an exception. Instead, the value is ignored.
     *
     * @param settingValue the string value, not {@code null}
     */
    @Override
    public abstract void setValue(String settingValue);

    /**
     * Returns the currently used value for this setting, not {@code null}.
     * <p>
     * The value returned by this method is valid as an argument to both
     * the {@code setValue(String)} method and {@code combine(Set)} method.
     * <p>
     * This method is invoked when an event is registered to obtain the
     * default value. It is therefore important that a valid value can be
     * returned immediately after an instance of this class is created. It is
     * not valid to return {@code null}.
     *
     * @return the setting value, not {@code null}
     */
    @Override
    public abstract String getValue();
}
