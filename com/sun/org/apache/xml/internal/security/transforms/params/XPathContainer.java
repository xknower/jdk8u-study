package com.sun.org.apache.xml.internal.security.transforms.params;


import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * This Object serves both as namespace prefix resolver and as container for
 * the {@code ds:XPath} Element. It implements the {@link org.w3c.dom.Element} interface
 * and can be used directly in a DOM tree.
 *
 */
public class XPathContainer extends SignatureElementProxy implements TransformParam {

    /**
     * Constructor XPathContainer
     *
     * @param doc
     */
    public XPathContainer(Document doc) {
        super(doc);
    }

    /**
     * Sets the TEXT value of the {@code ds:XPath} Element.
     *
     * @param xpath
     */
    public void setXPath(String xpath) {
        Node childNode = getElement().getFirstChild();
        while (childNode != null) {
            Node nodeToBeRemoved = childNode;
            childNode = childNode.getNextSibling();
            getElement().removeChild(nodeToBeRemoved);
        }

        Text xpathText = createText(xpath);
        appendSelf(xpathText);
    }

    /**
     * Returns the TEXT value of the {@code ds:XPath} Element.
     *
     * @return the TEXT value of the {@code ds:XPath} Element.
     */
    public String getXPath() {
        return this.getTextFromTextChild();
    }

    /** {@inheritDoc} */
    public String getBaseLocalName() {
        return Constants._TAG_XPATH;
    }
}
