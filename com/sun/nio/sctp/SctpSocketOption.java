package com.sun.nio.sctp;

import java.net.SocketOption;

/**
 * A socket option associated with an SCTP channel.
 *
 * @param   <T>     The type of the socket option value.
 *
 * @since 1.7
 *
 * @see SctpStandardSocketOptions
 */
@jdk.Exported
public interface SctpSocketOption<T> extends SocketOption<T> { }
