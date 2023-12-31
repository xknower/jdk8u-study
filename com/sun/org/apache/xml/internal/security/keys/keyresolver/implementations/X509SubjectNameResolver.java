package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Iterator;


import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SubjectName;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Element;

public class X509SubjectNameResolver extends KeyResolverSpi {

    private static final com.sun.org.slf4j.internal.Logger LOG =
        com.sun.org.slf4j.internal.LoggerFactory.getLogger(X509SubjectNameResolver.class);


    /**
     * Method engineResolvePublicKey
     *
     * @param element
     * @param baseURI
     * @param storage
     * @return null if no {@link PublicKey} could be obtained
     * @throws KeyResolverException
     */
    public PublicKey engineLookupAndResolvePublicKey(
        Element element, String baseURI, StorageResolver storage
    ) throws KeyResolverException {

        X509Certificate cert =
            this.engineLookupResolveX509Certificate(element, baseURI, storage);

        if (cert != null) {
            return cert.getPublicKey();
        }

        return null;
    }

    /**
     * Method engineResolveX509Certificate
     * {@inheritDoc}
     * @param element
     * @param baseURI
     * @param storage
     *
     * @throws KeyResolverException
     */
    public X509Certificate engineLookupResolveX509Certificate(
        Element element, String baseURI, StorageResolver storage
    ) throws KeyResolverException {
        LOG.debug("Can I resolve {}?", element.getTagName());
        Element[] x509childNodes = null;
        XMLX509SubjectName x509childObject[] = null;

        if (!XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_X509DATA)) {
            LOG.debug("I can't");
            return null;
        }
        x509childNodes =
            XMLUtils.selectDsNodes(element.getFirstChild(), Constants._TAG_X509SUBJECTNAME);

        if (!(x509childNodes != null && x509childNodes.length > 0)) {
            LOG.debug("I can't");
            return null;
        }

        try {
            if (storage == null) {
                Object exArgs[] = { Constants._TAG_X509SUBJECTNAME };
                KeyResolverException ex =
                    new KeyResolverException("KeyResolver.needStorageResolver", exArgs);

                LOG.debug("", ex);

                throw ex;
            }

            x509childObject = new XMLX509SubjectName[x509childNodes.length];

            for (int i = 0; i < x509childNodes.length; i++) {
                x509childObject[i] = new XMLX509SubjectName(x509childNodes[i], baseURI);
            }

            Iterator<Certificate> storageIterator = storage.getIterator();
            while (storageIterator.hasNext()) {
                X509Certificate cert = (X509Certificate)storageIterator.next();
                XMLX509SubjectName certSN =
                    new XMLX509SubjectName(element.getOwnerDocument(), cert);

                LOG.debug("Found Certificate SN: {}", certSN.getSubjectName());

                for (int i = 0; i < x509childObject.length; i++) {
                    LOG.debug("Found Element SN:     {}", x509childObject[i].getSubjectName());

                    if (certSN.equals(x509childObject[i])) {
                        LOG.debug("match !!! ");

                        return cert;
                    }
                    LOG.debug("no match...");
                }
            }

            return null;
        } catch (XMLSecurityException ex) {
            LOG.debug("XMLSecurityException", ex);

            throw new KeyResolverException(ex);
        }
    }

    /**
     * Method engineResolveSecretKey
     * {@inheritDoc}
     * @param element
     * @param baseURI
     * @param storage
     *
     */
    public javax.crypto.SecretKey engineLookupAndResolveSecretKey(
        Element element, String baseURI, StorageResolver storage
    ) {
        return null;
    }
}
