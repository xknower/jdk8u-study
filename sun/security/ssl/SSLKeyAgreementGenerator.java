package sun.security.ssl;

import java.io.IOException;

interface SSLKeyAgreementGenerator {
    SSLKeyDerivation createKeyDerivation(
            HandshakeContext context) throws IOException;
}
