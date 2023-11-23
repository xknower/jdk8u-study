package com.sun.pept.ept;

import java.util.*;

/**
 * <p>
 *
 * @author Dr. Harold Carr
 * </p>
 */
public interface ContactInfoListIterator {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a boolean with ...
 * </p>
 */
    public boolean hasNext();
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a ContactInfo with ...
 * </p>
 */
    public ContactInfo next();

} // end ContactInfoListIterator
