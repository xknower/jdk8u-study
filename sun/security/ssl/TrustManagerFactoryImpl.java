package sun.security.ssl;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import javax.net.ssl.*;
import sun.security.validator.TrustStoreUtil;
import sun.security.validator.Validator;

abstract class TrustManagerFactoryImpl extends TrustManagerFactorySpi {

    private X509TrustManager trustManager = null;
    private boolean isInitialized = false;

    TrustManagerFactoryImpl() {
        // empty
    }

    @Override
    protected void engineInit(KeyStore ks) throws KeyStoreException {
        if (ks == null) {
            try {
                trustManager = getInstance(TrustStoreManager.getTrustedCerts());
            } catch (SecurityException se) {
                // eat security exceptions but report other throwables
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine(
                            "SunX509: skip default keystore", se);
                }
            } catch (Error err) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine(
                        "SunX509: skip default keystore", err);
                }
                throw err;
            } catch (RuntimeException re) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine(
                        "SunX509: skip default keystor", re);
                }
                throw re;
            } catch (Exception e) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine(
                        "SunX509: skip default keystore", e);
                }
                throw new KeyStoreException(
                    "problem accessing trust store", e);
            }
        } else {
            trustManager = getInstance(TrustStoreUtil.getTrustedCerts(ks));
        }

        isInitialized = true;
    }

    abstract X509TrustManager getInstance(
            Collection<X509Certificate> trustedCerts);

    abstract X509TrustManager getInstance(ManagerFactoryParameters spec)
            throws InvalidAlgorithmParameterException;

    @Override
    protected void engineInit(ManagerFactoryParameters spec) throws
            InvalidAlgorithmParameterException {
        trustManager = getInstance(spec);
        isInitialized = true;
    }

    /**
     * Returns one trust manager for each type of trust material.
     */
    @Override
    protected TrustManager[] engineGetTrustManagers() {
        if (!isInitialized) {
            throw new IllegalStateException(
                        "TrustManagerFactoryImpl is not initialized");
        }
        return new TrustManager[] { trustManager };
    }

    /*
     * Try to get an InputStream based on the file we pass in.
     */
    private static FileInputStream getFileInputStream(final File file)
            throws Exception {
        return AccessController.doPrivileged(
                new PrivilegedExceptionAction<FileInputStream>() {
                    @Override
                    public FileInputStream run() throws Exception {
                        try {
                            if (file.exists()) {
                                return new FileInputStream(file);
                            } else {
                                return null;
                            }
                        } catch (FileNotFoundException e) {
                            // couldn't find it, oh well.
                            return null;
                        }
                    }
                });
    }

    public static final class SimpleFactory extends TrustManagerFactoryImpl {
        @Override
        X509TrustManager getInstance(
                Collection<X509Certificate> trustedCerts) {
            return new X509TrustManagerImpl(
                    Validator.TYPE_SIMPLE, trustedCerts);
        }

        @Override
        X509TrustManager getInstance(ManagerFactoryParameters spec)
                throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException
                ("SunX509 TrustManagerFactory does not use "
                + "ManagerFactoryParameters");
        }
    }

    public static final class PKIXFactory extends TrustManagerFactoryImpl {
        @Override
        X509TrustManager getInstance(
                Collection<X509Certificate> trustedCerts) {
            return new X509TrustManagerImpl(Validator.TYPE_PKIX, trustedCerts);
        }

        @Override
        X509TrustManager getInstance(ManagerFactoryParameters spec)
                throws InvalidAlgorithmParameterException {
            if (spec instanceof CertPathTrustManagerParameters == false) {
                throw new InvalidAlgorithmParameterException
                    ("Parameters must be CertPathTrustManagerParameters");
            }
            CertPathParameters params =
                ((CertPathTrustManagerParameters)spec).getParameters();
            if (params instanceof PKIXBuilderParameters == false) {
                throw new InvalidAlgorithmParameterException
                    ("Encapsulated parameters must be PKIXBuilderParameters");
            }
            PKIXBuilderParameters pkixParams = (PKIXBuilderParameters)params;
            return new X509TrustManagerImpl(Validator.TYPE_PKIX, pkixParams);
        }
    }
}
