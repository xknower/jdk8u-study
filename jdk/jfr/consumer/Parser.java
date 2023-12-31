package jdk.jfr.consumer;

import java.io.IOException;

import jdk.jfr.internal.consumer.RecordingInput;

/**
 * Base class for parsing data from a {@link RecordingInput}.
 */
abstract class Parser {
    /**
     * Parses data from a {@link RecordingInput} and return an object.
     *
     * @param input input to read from
     * @return an object
     * @throws IOException if operation couldn't be completed due to I/O
     *         problems
     */
    abstract Object parse(RecordingInput input) throws IOException;
}
