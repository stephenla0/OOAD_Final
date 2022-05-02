package com.Final;

public interface LoggerWriter {
    default void loggerOutln(String msg, Logger logger) {
        logger.writeln(msg);
    }
    default void loggerOut(String msg, Logger logger) {
        logger.write(msg);
    }
}
