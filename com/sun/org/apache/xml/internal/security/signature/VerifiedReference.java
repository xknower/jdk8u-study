package com.sun.org.apache.xml.internal.security.signature;

import java.util.Collections;
import java.util.List;

/**
 * Holds the result of a Reference validation.
 */
public class VerifiedReference {

    private final boolean valid;
    private final String uri;
    private final List<VerifiedReference> manifestReferences;

    /**
     * @param valid Whether this Reference was successfully validated or not
     * @param uri The URI of this Reference
     * @param manifestReferences If this reference is a reference to a Manifest, this holds the list
     * of verified referenes associated with this Manifest
     */
    public VerifiedReference(boolean valid, String uri, List<VerifiedReference> manifestReferences) {
        this.valid = valid;
        this.uri = uri;
        if (manifestReferences != null) {
            this.manifestReferences = manifestReferences;
        } else {
            this.manifestReferences = Collections.emptyList();
        }
    }

    public boolean isValid() {
        return valid;
    }

    public String getUri() {
        return uri;
    }

    public List<VerifiedReference> getManifestReferences() {
        return Collections.unmodifiableList(manifestReferences);
    }
}
