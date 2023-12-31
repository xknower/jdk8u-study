package sun.security.jgss;

import org.ietf.jgss.*;
import sun.security.jgss.spi.*;
import java.security.Provider;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * This class provides the default implementation of the GSSManager
 * interface.
 */
public class GSSManagerImpl extends GSSManager {

    // Undocumented property
    private static final String USE_NATIVE_PROP =
        "sun.security.jgss.native";
    private static final Boolean USE_NATIVE;

    static {
        USE_NATIVE =
            AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                    public Boolean run() {
                        return Boolean.valueOf(System.getProperty
                                (USE_NATIVE_PROP));
                    }
            });

    }

    private ProviderList list;

    // Used by java SPNEGO impl to make sure native is disabled
    public GSSManagerImpl(GSSCaller caller, boolean useNative) {
        list = new ProviderList(caller, useNative);
    }

    // Used by HTTP/SPNEGO NegotiatorImpl
    public GSSManagerImpl(GSSCaller caller) {
        list = new ProviderList(caller, USE_NATIVE);
    }

    public GSSManagerImpl() {
        list = new ProviderList(GSSCaller.CALLER_UNKNOWN, USE_NATIVE);
    }

    public Oid[] getMechs(){
        return list.getMechs();
    }

    public Oid[] getNamesForMech(Oid mech)
        throws GSSException {
        MechanismFactory factory = list.getMechFactory(mech);
        return factory.getNameTypes().clone();
    }

    public Oid[] getMechsForName(Oid nameType){
        Oid[] mechs = list.getMechs();
        Oid[] retVal = new Oid[mechs.length];
        int pos = 0;

        // Compatibility with RFC 2853 old NT_HOSTBASED_SERVICE value.
        if (nameType.equals(GSSNameImpl.oldHostbasedServiceName)) {
            nameType = GSSName.NT_HOSTBASED_SERVICE;
        }

        // Iterate thru all mechs in GSS
        for (int i = 0; i < mechs.length; i++) {
            // what nametypes does this mech support?
            Oid mech = mechs[i];
            try {
                Oid[] namesForMech = getNamesForMech(mech);
                // Is the desired Oid present in that list?
                if (nameType.containedIn(namesForMech)) {
                    retVal[pos++] = mech;
                }
            } catch (GSSException e) {
                // Squelch it and just skip over this mechanism
                GSSUtil.debug("Skip " + mech +
                              ": error retrieving supported name types");
            }
        }

        // Trim the list if needed
        if (pos < retVal.length) {
            Oid[] temp = new Oid[pos];
            for (int i = 0; i < pos; i++)
                temp[i] = retVal[i];
            retVal = temp;
        }

        return retVal;
    }

    public GSSName createName(String nameStr, Oid nameType)
        throws GSSException {
        return new GSSNameImpl(this, nameStr, nameType);
    }

    public GSSName createName(byte name[], Oid nameType)
        throws GSSException {
        return new GSSNameImpl(this, name, nameType);
    }

    public GSSName createName(String nameStr, Oid nameType,
                              Oid mech) throws GSSException {
        return new GSSNameImpl(this, nameStr, nameType, mech);
    }

    public GSSName createName(byte name[], Oid nameType, Oid mech)
        throws GSSException {
        return new GSSNameImpl(this, name, nameType, mech);
    }

    public GSSCredential createCredential(int usage)
        throws GSSException {
        return new GSSCredentialImpl(this, usage);
    }

    public GSSCredential createCredential(GSSName aName,
                                          int lifetime, Oid mech, int usage)
        throws GSSException {
        return new GSSCredentialImpl(this, aName, lifetime, mech, usage);
    }

    public GSSCredential createCredential(GSSName aName,
                                          int lifetime, Oid mechs[], int usage)
        throws GSSException {
        return new GSSCredentialImpl(this, aName, lifetime, mechs, usage);
    }

    public GSSContext createContext(GSSName peer, Oid mech,
                                    GSSCredential myCred, int lifetime)
        throws GSSException {
        return new GSSContextImpl(this, peer, mech, myCred, lifetime);
    }

    public GSSContext createContext(GSSCredential myCred)
        throws GSSException {
        return new GSSContextImpl(this, myCred);
    }

    public GSSContext createContext(byte[] interProcessToken)
        throws GSSException {
        return new GSSContextImpl(this, interProcessToken);
    }

    public void addProviderAtFront(Provider p, Oid mech)
        throws GSSException {
        list.addProviderAtFront(p, mech);
    }

    public void addProviderAtEnd(Provider p, Oid mech)
        throws GSSException {
        list.addProviderAtEnd(p, mech);
    }

    public GSSCredentialSpi getCredentialElement(GSSNameSpi name, int initLifetime,
                                          int acceptLifetime, Oid mech, int usage)
        throws GSSException {
        MechanismFactory factory = list.getMechFactory(mech);
        return factory.getCredentialElement(name, initLifetime,
                                            acceptLifetime, usage);
    }

    // Used by java SPNEGO impl
    public GSSNameSpi getNameElement(String name, Oid nameType, Oid mech)
        throws GSSException {
        // Just use the most preferred MF impl assuming GSSNameSpi
        // objects are interoperable among providers
        MechanismFactory factory = list.getMechFactory(mech);
        return factory.getNameElement(name, nameType);
    }

    // Used by java SPNEGO impl
    public GSSNameSpi getNameElement(byte[] name, Oid nameType, Oid mech)
        throws GSSException {
        // Just use the most preferred MF impl assuming GSSNameSpi
        // objects are interoperable among providers
        MechanismFactory factory = list.getMechFactory(mech);
        return factory.getNameElement(name, nameType);
    }

    GSSContextSpi getMechanismContext(GSSNameSpi peer,
                                      GSSCredentialSpi myInitiatorCred,
                                      int lifetime, Oid mech)
        throws GSSException {
        Provider p = null;
        if (myInitiatorCred != null) {
            p = myInitiatorCred.getProvider();
        }
        MechanismFactory factory = list.getMechFactory(mech, p);
        return factory.getMechanismContext(peer, myInitiatorCred, lifetime);
    }

    GSSContextSpi getMechanismContext(GSSCredentialSpi myAcceptorCred,
                                      Oid mech)
        throws GSSException {
        Provider p = null;
        if (myAcceptorCred != null) {
            p = myAcceptorCred.getProvider();
        }
        MechanismFactory factory = list.getMechFactory(mech, p);
        return factory.getMechanismContext(myAcceptorCred);
    }

    GSSContextSpi getMechanismContext(byte[] exportedContext)
        throws GSSException {
        if ((exportedContext == null) || (exportedContext.length == 0)) {
            throw new GSSException(GSSException.NO_CONTEXT);
        }
        GSSContextSpi result = null;

        // Only allow context import with native provider since JGSS
        // still has not defined its own interprocess token format
        Oid[] mechs = list.getMechs();
        for (int i = 0; i < mechs.length; i++) {
            MechanismFactory factory = list.getMechFactory(mechs[i]);
            if (factory.getProvider().getName().equals("SunNativeGSS")) {
                result = factory.getMechanismContext(exportedContext);
                if (result != null) break;
            }
        }
        if (result == null) {
            throw new GSSException(GSSException.UNAVAILABLE);
        }
        return result;
    }
}
