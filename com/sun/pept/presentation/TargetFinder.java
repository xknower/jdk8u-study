package com.sun.pept.presentation;

import com.sun.pept.ept.MessageInfo;
import java.util.*;

/**
 * <p>
 *
 * @author Dr. Harold Carr
 * </p>
 */
public interface TargetFinder {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a Tie with ...
 * </p><p>
 * @param x ...
 * </p>
 */
    public Tie findTarget(MessageInfo x);

} // end TargetFinder
