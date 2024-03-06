package com.kalsym.log;

import org.apache.log4j.Category;
import org.apache.log4j.Level;

/**
 *
 * @author Ali Khan
 */
public class KLogger extends Category {

    /**
     * The fully qualified name of the Category class. See also the getFQCN
     * method.
     */
    private static final String FQCN = Category.class.getName();

    public KLogger(String name) {
        super(name);
    }

    /**
     * Log a message object with the {@link Level#DEBUG DEBUG} level.
     *
     * <p>
     * This method first checks if this category is <code>DEBUG</code> enabled
     * by comparing the level of this category with the {@link Level#DEBUG
     * DEBUG} level. If this category is <code>DEBUG</code> enabled, then it
     * converts the message object (passed as parameter) to a string by invoking
     * the appropriate {@link org.apache.log4j.or.ObjectRenderer}. It then
     * proceeds to call all the registered appenders in this category and also
     * higher in the hierarchy depending on the value of the additivity flag.
     * </p>
     *
     * <p>
     * <b>WARNING</b> Note that passing a {@link Throwable} to this method will
     * print the name of the <code>Throwable</code> but no stack trace. To print
     * a stack trace use the {@link #debug(Object, Throwable)} form instead.
     * </p>
     *
     * @param message the message object to log.
     */
    public void log(Object message) {
        if (repository.isDisabled(Level.DEBUG_INT)) {
            return;
        }

        if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, null);
        }
    }

    /**
     * Log a message object with the <code>DEBUG</code> level including the
     * stack trace of the {@link Throwable}<code>t</code> passed as parameter.
     *
     * <p>
     * See {@link #debug(Object)} form for more detailed information.
     * </p>
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void log(Object message, Throwable t) {
        if (repository.isDisabled(Level.DEBUG_INT)) {
            return;
        }

        if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, t);
        }
    }
}
