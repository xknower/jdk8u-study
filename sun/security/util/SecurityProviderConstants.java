package sun.security.util;

import java.util.regex.PatternSyntaxException;
import java.security.InvalidParameterException;
import javax.crypto.spec.DHParameterSpec;
import sun.security.action.GetPropertyAction;

/**
 * Various constants such as version number, default key length, used by
 * the JDK security/crypto providers.
 */
public final class SecurityProviderConstants {
    private static final Debug debug =
        Debug.getInstance("jca", "ProviderConfig");

    // Cannot create one of these
    private SecurityProviderConstants () {
    }

    public static final int getDefDSASubprimeSize(int primeSize) {
        if (primeSize <= 1024) {
            return 160;
        } else if (primeSize == 2048) {
            return 224;
        } else if (primeSize == 3072) {
            return 256;
        } else {
            throw new InvalidParameterException("Invalid DSA Prime Size: " +
                primeSize);
        }
    }

    public static final int getDefDHPrivateExpSize(DHParameterSpec spec) {

        int dhGroupSize = spec.getP().bitLength();

        if (spec instanceof SafeDHParameterSpec) {
            // Known safe primes
            // use 2*security strength as default private exponent size
            // as in table 2 of NIST SP 800-57 part 1 rev 5, sec 5.6.1.1
            // and table 25 of NIST SP 800-56A rev 3, appendix D.
            if (dhGroupSize >= 15360) {
                return 512;
            } else if (dhGroupSize >= 8192) {
                return 400;
            } else if (dhGroupSize >= 7680) {
                return 384;
            } else if (dhGroupSize >= 6144) {
                return 352;
            } else if (dhGroupSize >= 4096) {
                return 304;
            } else if (dhGroupSize >= 3072) {
                return 256;
            } else if (dhGroupSize >= 2048) {
                return 224;
            } else {
                // min value for legacy key sizes
                return 160;
            }
        } else {
            // assume the worst and use groupSize/2 as private exp length
            // up to 1024-bit and use the same minimum 384 as before
            return Math.max((dhGroupSize >= 2048 ? 1024 : dhGroupSize >> 1),
                    384);
        }

    }

    public static final int DEF_DSA_KEY_SIZE;
    public static final int DEF_RSA_KEY_SIZE;
    public static final int DEF_RSASSA_PSS_KEY_SIZE;
    public static final int DEF_DH_KEY_SIZE;
    public static final int DEF_EC_KEY_SIZE;

    private static final String KEY_LENGTH_PROP =
        "jdk.security.defaultKeySize";
    static {
        String keyLengthStr = GetPropertyAction.privilegedGetProperty
            (KEY_LENGTH_PROP);
        int dsaKeySize = 2048;
        int rsaKeySize = 2048;
        int rsaSsaPssKeySize = rsaKeySize; // default to same value as RSA
        int dhKeySize = 2048;
        int ecKeySize = 256;

        if (keyLengthStr != null) {
            try {
                String[] pairs = keyLengthStr.split(",");
                for (String p : pairs) {
                    String[] algoAndValue = p.split(":");
                    if (algoAndValue.length != 2) {
                        // invalid pair, skip to next pair
                        if (debug != null) {
                            debug.println("Ignoring invalid pair in " +
                                KEY_LENGTH_PROP + " property: " + p);
                        }
                        continue;
                    }
                    String algoName = algoAndValue[0].trim().toUpperCase();
                    int value = -1;
                    try {
                        value = Integer.parseInt(algoAndValue[1].trim());
                    } catch (NumberFormatException nfe) {
                        // invalid value, skip to next pair
                        if (debug != null) {
                            debug.println("Ignoring invalid value in " +
                                KEY_LENGTH_PROP + " property: " + p);
                        }
                        continue;
                    }
                    if (algoName.equals("DSA")) {
                        dsaKeySize = value;
                    } else if (algoName.equals("RSA")) {
                        rsaKeySize = value;
                    } else if (algoName.equals("RSASSA-PSS")) {
                        rsaSsaPssKeySize = value;
                    } else if (algoName.equals("DH")) {
                        dhKeySize = value;
                    } else if (algoName.equals("EC")) {
                        ecKeySize = value;
                    } else {
                        if (debug != null) {
                            debug.println("Ignoring unsupported algo in " +
                                KEY_LENGTH_PROP + " property: " + p);
                        }
                        continue;
                    }
                    if (debug != null) {
                        debug.println("Overriding default " + algoName +
                            " keysize with value from " +
                            KEY_LENGTH_PROP + " property: " + value);
                    }
                }
            } catch (PatternSyntaxException pse) {
                // if property syntax is not followed correctly
                if (debug != null) {
                    debug.println("Unexpected exception while parsing " +
                        KEY_LENGTH_PROP + " property: " + pse);
                }
            }
        }
        DEF_DSA_KEY_SIZE = dsaKeySize;
        DEF_RSA_KEY_SIZE = rsaKeySize;
        DEF_RSASSA_PSS_KEY_SIZE = rsaSsaPssKeySize;
        DEF_DH_KEY_SIZE = dhKeySize;
        DEF_EC_KEY_SIZE = ecKeySize;
    }
}
