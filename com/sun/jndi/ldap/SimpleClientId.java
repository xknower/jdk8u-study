package com.sun.jndi.ldap;

import java.util.Arrays;  // JDK1.2
import java.io.OutputStream;
import javax.naming.ldap.Control;

/**
 * Represents the identity of a 'simple' authenticated LDAP connection.
 * In addition to ClientId information, this class contains also the
 * username and password.
 *
 * @author Rosanna Lee
 */
class SimpleClientId extends ClientId {
    final private String username;
    final private Object passwd;
    final private int myHash;

    SimpleClientId(int version, String hostname, int port,
        String protocol, Control[] bindCtls, OutputStream trace,
        String socketFactory, String username, Object passwd) {

        super(version, hostname, port, protocol, bindCtls, trace,
                socketFactory);

        this.username = username;
        int pwdHashCode = 0;
        if (passwd == null) {
            this.passwd = null;
        } else if (passwd instanceof byte[]) {
            this.passwd = ((byte[])passwd).clone();
            pwdHashCode = Arrays.hashCode((byte[])passwd);
        } else if (passwd instanceof char[]) {
            this.passwd = ((char[])passwd).clone();
            pwdHashCode = Arrays.hashCode((char[])passwd);
        } else {
            this.passwd = passwd;
            pwdHashCode = passwd.hashCode();
        }

        myHash = super.hashCode()
            ^ (username != null ? username.hashCode() : 0)
            ^ pwdHashCode;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SimpleClientId)) {
            return false;
        }

        SimpleClientId other = (SimpleClientId)obj;

        return super.equals(obj)
            && (username == other.username // null OK
                || (username != null && username.equals(other.username)))
            && ((passwd == other.passwd)  // null OK
                || (passwd != null && other.passwd != null
                    && (((passwd instanceof String) && passwd.equals(other.passwd))
                        || ((passwd instanceof byte[])
                            && (other.passwd instanceof byte[])
                            && Arrays.equals((byte[])passwd, (byte[])other.passwd))
                        || ((passwd instanceof char[])
                            && (other.passwd instanceof char[])
                            && Arrays.equals((char[])passwd, (char[])other.passwd)))));

    }

    public int hashCode() {
        return myHash;
    }

    public String toString() {
        return super.toString() + ":" + username; // omit password for security
    }
}
