package sun.net;

import java.net.SocketOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static jdk.net.ExtendedSocketOptions.TCP_KEEPCOUNT;
import static jdk.net.ExtendedSocketOptions.TCP_KEEPIDLE;
import static jdk.net.ExtendedSocketOptions.TCP_KEEPINTERVAL;

public class ExtendedOptionsHelper {

    private static final boolean keepAliveOptSupported =
            ExtendedOptionsImpl.keepAliveOptionsSupported();
    private static final Set<SocketOption<?>> extendedOptions = options();

    private static Set<SocketOption<?>> options() {
        Set<SocketOption<?>> options = new HashSet<>();
        if (keepAliveOptSupported) {
            options.add(TCP_KEEPCOUNT);
            options.add(TCP_KEEPIDLE);
            options.add(TCP_KEEPINTERVAL);
        }
        return Collections.unmodifiableSet(options);
    }

    public static Set<SocketOption<?>> keepAliveOptions() {
        return extendedOptions;
    }
}
