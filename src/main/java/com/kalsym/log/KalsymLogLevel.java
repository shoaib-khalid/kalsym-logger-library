package com.kalsym.log;

/**
 *
 * @author Muhammad Waqar
 */
public enum KalsymLogLevel {

    DEBUG, INFO, ERROR, WARN;

    /**
     * Gets the KalsymLogLevel after parsing string, does not match case, returns DEBUG by default
     * @param loggingLevel
     * @return
     */
    public static KalsymLogLevel getLogLevel(String loggingLevel) {
        if ("INFO".equalsIgnoreCase(loggingLevel)) {
            return INFO;
        } else if ("DEBUG".equalsIgnoreCase(loggingLevel)) {
            return DEBUG;
        } else if ("WARN".equalsIgnoreCase(loggingLevel)) {
            return WARN;
        } else if ("ERROR".equalsIgnoreCase(loggingLevel)) {
            return ERROR;
        }
        return KalsymLogLevel.DEBUG;
    }
}
