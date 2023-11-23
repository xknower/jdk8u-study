package com.sun.org.slf4j.internal;

// Bridge to java.util.logging.
public class LoggerFactory {

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getName());
    }
}
