package com.sun.pept.presentation;

import com.sun.pept.ept.MessageInfo;
import java.util.*;

/**
 * <p>
 *
 * @author Dr. Harold Carr
 * </p>
 */
public interface Tie {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 *
 * </p><p>
 *
 * @param servant ...
 * </p>
 */
    public void _setServant(Object servant);
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a Object with ...
 * </p>
 */
    public Object _getServant();
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * </p><p>
 *
 * @param messageInfo ...
 * </p>
 */
    public void _invoke(MessageInfo messageInfo);

} // end Tie
