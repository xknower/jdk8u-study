package sun.security.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.security.KeyStore;

import java.security.cert.X509Certificate;
import java.text.Collator;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p> This class provides several utilities to <code>KeyStore</code>.
 *
 * @since 1.6.0
 */
public class KeyStoreUtil {

    private KeyStoreUtil() {
        // this class is not meant to be instantiated
    }

    private static final String JKS = "jks";

    /**
     * Returns true if the certificate is self-signed, false otherwise.
     */
    public static boolean isSelfSigned(X509Certificate cert) {
        return signedBy(cert, cert);
    }

    public static boolean signedBy(X509Certificate end, X509Certificate ca) {
        if (!ca.getSubjectX500Principal().equals(end.getIssuerX500Principal())) {
            return false;
        }
        try {
            end.verify(ca.getPublicKey());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if KeyStore has a password. This is true except for
     * MSCAPI KeyStores
     */
    public static boolean isWindowsKeyStore(String storetype) {
        return storetype != null
                && (storetype.equalsIgnoreCase("Windows-MY")
                    || storetype.equalsIgnoreCase("Windows-ROOT"));
    }

    /**
     * Returns standard-looking names for storetype
     */
    public static String niceStoreTypeName(String storetype) {
        if (storetype.equalsIgnoreCase("Windows-MY")) {
            return "Windows-MY";
        } else if(storetype.equalsIgnoreCase("Windows-ROOT")) {
            return "Windows-ROOT";
        } else {
            return storetype.toUpperCase(Locale.ENGLISH);
        }
    }

    /**
     * Returns the keystore with the configured CA certificates.
     */
    public static KeyStore getCacertsKeyStore()
        throws Exception
    {
        String sep = File.separator;
        File file = new File(System.getProperty("java.home") + sep
                             + "lib" + sep + "security" + sep
                             + "cacerts");
        if (!file.exists()) {
            return null;
        }
        KeyStore caks = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            caks = KeyStore.getInstance(JKS);
            caks.load(fis, null);
        }
        return caks;
    }

    public static char[] getPassWithModifier(String modifier, String arg,
                                             ResourceBundle rb,
                                             Collator collator) {
        if (modifier == null) {
            return arg.toCharArray();
        } else if (collator.compare(modifier, "env") == 0) {
            String value = System.getenv(arg);
            if (value == null) {
                System.err.println(rb.getString(
                        "Cannot.find.environment.variable.") + arg);
                return null;
            } else {
                return value.toCharArray();
            }
        } else if (collator.compare(modifier, "file") == 0) {
            try {
                URL url = null;
                try {
                    url = new URL(arg);
                } catch (java.net.MalformedURLException mue) {
                    File f = new File(arg);
                    if (f.exists()) {
                        url = f.toURI().toURL();
                    } else {
                        System.err.println(rb.getString(
                                "Cannot.find.file.") + arg);
                        return null;
                    }
                }

                try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(
                         url.openStream()))) {
                    String value = br.readLine();

                    if (value == null) {
                        return new char[0];
                    }

                    return value.toCharArray();
                }
            } catch (IOException ioe) {
                System.err.println(ioe);
                return null;
            }
        } else {
            System.err.println(rb.getString("Unknown.password.type.") +
                    modifier);
            return null;
        }
    }
}
