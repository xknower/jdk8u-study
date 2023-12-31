package sun.net.www.protocol.http.spnego;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import sun.net.www.protocol.http.HttpCallerInfo;
import sun.security.jgss.LoginConfigImpl;

/**
 * @since 1.6
 * Special callback handler used in JGSS for the HttpCaller.
 */
public class NegotiateCallbackHandler implements CallbackHandler {

    private String username;
    private char[] password;

    /**
     * Authenticator asks for username and password in a single prompt,
     * but CallbackHandler checks one by one. So, no matter which callback
     * gets handled first, make sure Authenticator is only called once.
     */
    private boolean answered;

    private final HttpCallerInfo hci;

    public NegotiateCallbackHandler(HttpCallerInfo hci) {
        this.hci = hci;
    }

    private void getAnswer() {
        if (!answered) {
            answered = true;

            if (LoginConfigImpl.HTTP_USE_GLOBAL_CREDS) {
                PasswordAuthentication passAuth =
                        Authenticator.requestPasswordAuthentication(
                                hci.host, hci.addr, hci.port, hci.protocol,
                                hci.prompt, hci.scheme, hci.url, hci.authType);
                /**
                 * To be compatible with existing callback handler implementations,
                 * when the underlying Authenticator is canceled, username and
                 * password are assigned null. No exception is thrown.
                 */
                if (passAuth != null) {
                    username = passAuth.getUserName();
                    password = passAuth.getPassword();
                }
            }
        }
    }

    public void handle(Callback[] callbacks) throws
            UnsupportedCallbackException, IOException {
        for (int i=0; i<callbacks.length; i++) {
            Callback callBack = callbacks[i];

            if (callBack instanceof NameCallback) {
                getAnswer();
                ((NameCallback)callBack).setName(username);
            } else if (callBack instanceof PasswordCallback) {
                getAnswer();
                ((PasswordCallback)callBack).setPassword(password);
                if (password != null) Arrays.fill(password, ' ');
            } else {
                throw new UnsupportedCallbackException(callBack,
                        "Call back not supported");
            }
        }
    }
}
