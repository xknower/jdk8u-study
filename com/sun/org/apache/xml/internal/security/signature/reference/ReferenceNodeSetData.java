package com.sun.org.apache.xml.internal.security.signature.reference;

import java.util.Iterator;

import org.w3c.dom.Node;

/**
 * An abstract representation of a {@code ReferenceData} type containing a node-set.
 */
public interface ReferenceNodeSetData extends ReferenceData {

    /**
     * Returns a read-only iterator over the nodes contained in this
     * {@code NodeSetData} in
     * <a href="http://www.w3.org/TR/1999/REC-xpath-19991116#dt-document-order">
     * document order</a>. Attempts to modify the returned iterator
     * via the {@code remove} method throw
     * {@code UnsupportedOperationException}.
     *
     * @return an {@code Iterator} over the nodes in this
     *    {@code NodeSetData} in document order
     */
    Iterator<Node> iterator();

}
