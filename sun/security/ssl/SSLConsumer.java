package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;

interface SSLConsumer {
    void consume(ConnectionContext context,
            ByteBuffer message) throws IOException;
}

