package com.sun.beans.decoder;

/**
 * This class is intended to handle &lt;byte&gt; element.
 * This element specifies {@code byte} values.
 * The class {@link Byte} is used as wrapper for these values.
 * The result value is created from text of the body of this element.
 * The body parsing is described in the class {@link StringElementHandler}.
 * For example:<pre>
 * &lt;byte&gt;127&lt;/byte&gt;</pre>
 * is shortcut to<pre>
 * &lt;method name="decode" class="java.lang.Byte"&gt;
 *     &lt;string&gt;127&lt;/string&gt;
 * &lt;/method&gt;</pre>
 * which is equivalent to {@code Byte.decode("127")} in Java code.
 * <p>The following attribute is supported:
 * <dl>
 * <dt>id
 * <dd>the identifier of the variable that is intended to store the result
 * </dl>
 *
 * @since 1.7
 *
 * @author Sergey A. Malenkov
 */
final class ByteElementHandler extends StringElementHandler {

    /**
     * Creates {@code byte} value from
     * the text of the body of this element.
     *
     * @param argument  the text of the body
     * @return evaluated {@code byte} value
     */
    @Override
    public Object getValue(String argument) {
        return Byte.decode(argument);
    }
}
