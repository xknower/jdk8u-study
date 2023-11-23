package com.sun.jmx.snmp.IPAcl;

import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Vector;

class JDMAclBlock extends SimpleNode {
  JDMAclBlock(int id) {
    super(id);
  }

  JDMAclBlock(Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
      return new JDMAclBlock(id);
  }

  public static Node jjtCreate(Parser p, int id) {
      return new JDMAclBlock(p, id);
  }

  /**
   * Do no need to go through this part of the tree for
   * building TrapEntry.
   */
   @Override
   public void buildTrapEntries(Hashtable<InetAddress, Vector<String>> dest) {}

  /**
   * Do no need to go through this part of the tree for
   * building InformEntry.
   */
    @Override
   public void buildInformEntries(Hashtable<InetAddress, Vector<String>> dest) {}
}
