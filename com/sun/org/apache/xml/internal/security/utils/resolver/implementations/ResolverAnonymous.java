package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverContext;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;

/**
 */
public class ResolverAnonymous extends ResourceResolverSpi {

    private InputStream inStream;

    @Override
    public boolean engineIsThreadSafe() {
        return true;
    }

    /**
     * @param filename
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ResolverAnonymous(String filename) throws FileNotFoundException, IOException {
        inStream = Files.newInputStream(Paths.get(filename));
    }

    /**
     * @param is
     */
    public ResolverAnonymous(InputStream is) {
        inStream = is;
    }

    /** {@inheritDoc} */
    @Override
    public XMLSignatureInput engineResolveURI(ResourceResolverContext context) {
        XMLSignatureInput input = new XMLSignatureInput(inStream);
        input.setSecureValidation(context.secureValidation);
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean engineCanResolveURI(ResourceResolverContext context) {
        if (context.uriToResolve == null) {
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    public String[] engineGetPropertyKeys() {
        return new String[0];
    }
}
