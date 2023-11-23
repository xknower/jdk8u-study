package com.sun.pept.ept;

import com.sun.pept.encoding.Decoder;
import com.sun.pept.encoding.Encoder;
import com.sun.pept.presentation.TargetFinder;
import com.sun.pept.protocol.Interceptors;
import com.sun.pept.protocol.MessageDispatcher;
import java.util.*;

/**
 * <p>
 *
 * @author Dr. Harold Carr
 * </p>
 */
public interface EPTFactory {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a MessageDispatcher with ...
 * </p><p>
 * @param messageInfo ...
 * </p>
 */
    public MessageDispatcher getMessageDispatcher(MessageInfo messageInfo);
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a Encoder with ...
 * </p><p>
 * @param messageInfo ...
 * </p>
 */
    public Encoder getEncoder(MessageInfo messageInfo);
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a Decoder with ...
 * </p><p>
 * @param messageInfo ...
 * </p>
 */
    public Decoder getDecoder(MessageInfo messageInfo);
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a Interceptors with ...
 * </p><p>
 * @param x ...
 * </p>
 */
    public Interceptors getInterceptors(MessageInfo x);
/**
 * <p>
 * Does ...
 * </p><p>
 *
 * @return a TargetFinder with ...
 * </p><p>
 * @param x ...
 * </p>
 */
    public TargetFinder getTargetFinder(MessageInfo x);

} // end EPTFactory
