package com.sun.pept;

import com.sun.pept.presentation.MessageStruct;
import java.util.*;

/**
 * <p>
 *
 * @author Dr. Harold Carr
 * </p>
 */
public interface Delegate {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a MessageStruct with ...
 * </p>
 */
    public MessageStruct getMessageStruct();
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * </p><p>
 *
 * @param message ...
 * </p>
 */
    public void send(MessageStruct message);

} // end Delegate
