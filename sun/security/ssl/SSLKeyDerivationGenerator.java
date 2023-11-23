package sun.security.ssl;

import java.io.IOException;
import javax.crypto.SecretKey;

interface SSLKeyDerivationGenerator {
    SSLKeyDerivation createKeyDerivation(HandshakeContext context,
            SecretKey secretKey) throws IOException;
}
