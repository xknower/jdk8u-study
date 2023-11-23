package com.sun.jndi.ldap;


import javax.naming.*;
import javax.naming.ldap.LdapName;


class LdapNameParser implements NameParser {

    public LdapNameParser() {
    }

    public Name parse(String name) throws NamingException {
        return new LdapName(name);
    }
}
