package com.sun.org.apache.xml.internal.security.transforms.implementations;

import java.io.OutputStream;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;

/**
 * Implements the {@code http://www.w3.org/TR/2001/REC-xml-c14n-20010315}
 * transform.
 *
 */
public class TransformC14N extends TransformSpi {

    /** Field implementedTransformURI */
    public static final String implementedTransformURI =
        Transforms.TRANSFORM_C14N_OMIT_COMMENTS;

    /**
     * {@inheritDoc}
     */
    protected String engineGetURI() {
        return TransformC14N.implementedTransformURI;
    }

    protected XMLSignatureInput enginePerformTransform(
        XMLSignatureInput input, OutputStream os, Transform transformObject
    ) throws CanonicalizationException {
        Canonicalizer20010315OmitComments c14n = new Canonicalizer20010315OmitComments();
        c14n.setSecureValidation(secureValidation);
        if (os != null) {
            c14n.setWriter(os);
        }
        byte[] result = null;
        result = c14n.engineCanonicalize(input);
        XMLSignatureInput output = new XMLSignatureInput(result);
        output.setSecureValidation(secureValidation);
        if (os != null) {
            output.setOutputStream(os);
        }
        return output;
    }
}
