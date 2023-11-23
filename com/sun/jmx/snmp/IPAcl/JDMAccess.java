package com.sun.jmx.snmp.IPAcl;


class JDMAccess extends SimpleNode {
  protected int access= -1;

  JDMAccess(int id) {
    super(id);
  }

  JDMAccess(Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
      return new JDMAccess(id);
  }

  public static Node jjtCreate(Parser p, int id) {
      return new JDMAccess(p, id);
  }

  protected void putPermission(AclEntryImpl entry) {
    if (access == ParserConstants.RO) {
       // We have a read-only access.
       //
       entry.addPermission(com.sun.jmx.snmp.IPAcl.SnmpAcl.getREAD());
    }
    if (access == ParserConstants.RW) {
       // We have a read-write access.
       //
       entry.addPermission(com.sun.jmx.snmp.IPAcl.SnmpAcl.getREAD());
       entry.addPermission(com.sun.jmx.snmp.IPAcl.SnmpAcl.getWRITE());
    }
  }
}
