package com.sun.pept.ept;

import com.sun.pept.transport.Connection;
import java.util.*;

/**
 * <p>
 *
 * @author Dr. Harold Carr
 * </p>
 */
public interface ContactInfo extends EPTFactory {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a Connection with ...
 * </p><p>
 * @param messageInfo ...
 * </p>
 */
    public Connection getConnection(MessageInfo messageInfo);

} // end ContactInfo
