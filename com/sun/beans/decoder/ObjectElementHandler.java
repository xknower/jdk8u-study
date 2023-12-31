package com.sun.beans.decoder;

import java.beans.Expression;

import static java.util.Locale.ENGLISH;

/**
 * This class is intended to handle &lt;object&gt; element.
 * This element looks like &lt;void&gt; element,
 * but its value is always used as an argument for element
 * that contains this one.
 * <p>The following attributes are supported:
 * <dl>
 * <dt>class
 * <dd>the type is used for static methods and fields
 * <dt>method
 * <dd>the method name
 * <dt>property
 * <dd>the property name
 * <dt>index
 * <dd>the property index
 * <dt>field
 * <dd>the field name
 * <dt>idref
 * <dd>the identifier to refer to the variable
 * <dt>id
 * <dd>the identifier of the variable that is intended to store the result
 * </dl>
 *
 * @since 1.7
 *
 * @author Sergey A. Malenkov
 */
class ObjectElementHandler extends NewElementHandler {
    private String idref;
    private String field;
    private Integer index;
    private String property;
    private String method;

    /**
     * Parses attributes of the element.
     * The following attributes are supported:
     * <dl>
     * <dt>class
     * <dd>the type is used for static methods and fields
     * <dt>method
     * <dd>the method name
     * <dt>property
     * <dd>the property name
     * <dt>index
     * <dd>the property index
     * <dt>field
     * <dd>the field name
     * <dt>idref
     * <dd>the identifier to refer to the variable
     * <dt>id
     * <dd>the identifier of the variable that is intended to store the result
     * </dl>
     *
     * @param name   the attribute name
     * @param value  the attribute value
     */
    @Override
    public final void addAttribute(String name, String value) {
        if (name.equals("idref")) { // NON-NLS: the attribute name
            this.idref = value;
        } else if (name.equals("field")) { // NON-NLS: the attribute name
            this.field = value;
        } else if (name.equals("index")) { // NON-NLS: the attribute name
            this.index = Integer.valueOf(value);
            addArgument(this.index); // hack for compatibility
        } else if (name.equals("property")) { // NON-NLS: the attribute name
            this.property = value;
        } else if (name.equals("method")) { // NON-NLS: the attribute name
            this.method = value;
        } else {
            super.addAttribute(name, value);
        }
    }

    /**
     * Calculates the value of this element
     * if the field attribute or the idref attribute is set.
     */
    @Override
    public final void startElement() {
        if ((this.field != null) || (this.idref != null)) {
            getValueObject();
        }
    }

    /**
     * Tests whether the value of this element can be used
     * as an argument of the element that contained in this one.
     *
     * @return {@code true} if the value of this element can be used
     *         as an argument of the element that contained in this one,
     *         {@code false} otherwise
     */
    @Override
    protected boolean isArgument() {
        return true; // hack for compatibility
    }

    /**
     * Creates the value of this element.
     *
     * @param type  the base class
     * @param args  the array of arguments
     * @return the value of this element
     * @throws Exception if calculation is failed
     */
    @Override
    protected final ValueObject getValueObject(Class<?> type, Object[] args) throws Exception {
        if (this.field != null) {
            return ValueObjectImpl.create(FieldElementHandler.getFieldValue(getContextBean(), this.field));
        }
        if (this.idref != null) {
            return ValueObjectImpl.create(getVariable(this.idref));
        }
        Object bean = getContextBean();
        String name;
        if (this.index != null) {
            name = (args.length == 2)
                    ? PropertyElementHandler.SETTER
                    : PropertyElementHandler.GETTER;
        } else if (this.property != null) {
            name = (args.length == 1)
                    ? PropertyElementHandler.SETTER
                    : PropertyElementHandler.GETTER;

            if (0 < this.property.length()) {
                name += this.property.substring(0, 1).toUpperCase(ENGLISH) + this.property.substring(1);
            }
        } else {
            name = (this.method != null) && (0 < this.method.length())
                    ? this.method
                    : "new"; // NON-NLS: the constructor marker
        }
        Expression expression = new Expression(bean, name, args);
        return ValueObjectImpl.create(expression.getValue());
    }
}
