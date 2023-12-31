package sun.management;

/**
 * Diagnostic Command Argument information. It contains the description
 * of one parameter of the diagnostic command. A parameter can either be an
 * option or an argument. Options are identified by the option name while
 * arguments are identified by their position in the command line. The generic
 * syntax of a diagnostic command is:
 *  <blockquote>
 *    &lt;command name&gt; [&lt;option&gt;=&lt;value&gt;] [&lt;argument_value&gt;]
 * </blockquote>
 * Example:
 * <blockquote>
 * command_name option1=value1 option2=value argumentA argumentB argumentC
 * </blockquote>
 * In this command line, the diagnostic command receives five parameters, two
 * options named {@code option1} and {@code option2}, and three arguments.
 * argumentA's position is 0, argumentB's position is 1 and argumentC's
 * position is 2.
 *
 * @since 8
 */

class DiagnosticCommandArgumentInfo {
    private final String name;
    private final String description;
    private final String type;
    private final String defaultValue;
    private final boolean mandatory;
    private final boolean option;
    private final boolean multiple;
    private final int position;

    /**
     * Returns the argument name.
     *
     * @return the argument name
     */
    String getName() {
        return name;
    }

   /**
     * Returns the argument description.
     *
     * @return the argument description
     */
    String getDescription() {
        return description;
    }

    /**
     * Returns the argument type.
     *
     * @return the argument type
     */
    String getType() {
        return type;
    }

    /**
     * Returns the default value as a String if a default value
     * is defined, null otherwise.
     *
     * @return the default value as a String if a default value
     * is defined, null otherwise.
     */
    String getDefault() {
        return defaultValue;
    }

    /**
     * Returns {@code true} if the argument is mandatory,
     *         {@code false} otherwise.
     *
     * @return {@code true} if the argument is mandatory,
     *         {@code false} otherwise
     */
    boolean isMandatory() {
        return mandatory;
    }

    /**
     * Returns {@code true} if the argument is an option,
     *         {@code false} otherwise. Options have to be specified using the
     *         &lt;key&gt;=&lt;value&gt; syntax on the command line, while other
     *         arguments are specified with a single &lt;value&gt; field and are
     *         identified by their position on command line.
     *
     * @return {@code true} if the argument is an option,
     *         {@code false} otherwise
     */
    boolean isOption() {
        return option;
    }

    /**
     * Returns {@code true} if the argument can be specified multiple times,
     *         {@code false} otherwise.
     *
     * @return {@code true} if the argument can be specified multiple times,
     *         {@code false} otherwise
     */
    boolean isMultiple() {
        return multiple;
    }

    /**
     * Returns the expected position of this argument if it is not an option,
     *         -1 otherwise. Argument position if defined from left to right,
     *         starting at zero and ignoring the diagnostic command name and
     *         options.
     *
     * @return the expected position of this argument if it is not an option,
     *         -1 otherwise.
     */
    int getPosition() {
        return position;
    }

    DiagnosticCommandArgumentInfo(String name, String description,
                                         String type, String defaultValue,
                                         boolean mandatory, boolean option,
                                         boolean multiple, int position) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
        this.option = option;
        this.multiple = multiple;
        this.position = position;
    }
}
