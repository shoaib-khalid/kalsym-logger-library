package com.kalsym.log;

/**
 *
 * @author Taufik, enhanced by: zeeshan ali
 */
import org.apache.log4j.Category;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

/*
 * Does some thing in old style.
 *
 * @deprecated use logger instead.  
 */
@Deprecated
public class LogProperties extends java.util.Properties {

    //private Category log;
    /**
     * Used for logging directly, if you want to print the line number and class
     * name in stacktrace
     */
    public static Category log;
    static LogProperties logprop;
    private static final String defaultVersion = "NONVERSIONED";
    private static final String defaultlogPattern = "%d [%t] %L %-5p - %m%n";
    private static final String defaultFileSize = "50MB";
    private static final String defaultFileName = "logs";
    private static final String defaultLogFileExtension = "log";
    private static final int defaultMaxBackupFileCount = 100;
    private static KalsymLogLevel defaultLogLevel = KalsymLogLevel.DEBUG;
    private static KalsymLogLevel loglevel;

    /**
     * @return the KalsymLogLevel
     */
    public static KalsymLogLevel getLoglevel() {
        return loglevel;
    }

    /**
     * Creates a Default Logger with log files created in the
     * CurrentWorkingDirectory in which the jar file will be run, the fileName
     * will be "logs", the fileSize will be 50MB and the default log level will
     * be DEBUG and default pattern will be: %d [%t] %L %-5p - %m%n
     */
    public static void createDefaultLogger() throws Exception {
        if (logprop == null) {
            String currentWorkingDir = System.getProperty("user.dir");
            logprop = new LogProperties(defaultVersion, currentWorkingDir, defaultFileName, defaultlogPattern, defaultFileSize, defaultMaxBackupFileCount, defaultLogLevel);
        }
    }

    /**
     * Creates log file and adds rolling appenders
     *
     * @param version The version of the module which will be added in each log
     * line, providing an empty version will not add any
     * @param logDir The directory in which log files have to be created,
     * ideally the string should end in the slash separator, but it is appended
     * if not found
     */
    public static boolean createLogger(String version, String logDir) throws Exception {
        if (logprop == null) {
            logprop = new LogProperties(version, logDir, defaultFileName, defaultlogPattern, defaultFileSize, defaultMaxBackupFileCount, KalsymLogLevel.DEBUG);
        }
        return true;
    }

    /**
     * Creates log file and adds rolling appenders
     *
     * @param logDir The directory in which log files have to be created,
     * ideally the string should end in the slash separator, but it is appended
     * if not found
     * @param fileName The name of the log file which will be created in the
     * logDir
     * @param version The version of the module which will be added in each log
     * line, providing an empty version will not add any
     * @param fileSize The max size of the log file, after which it will be
     * rotated
     */
    public static boolean createLogger(String version, String logDir,
            String fileName, String fileSize) throws Exception {
        if (logprop == null) {
            logprop = new LogProperties(version, logDir, fileName, defaultlogPattern, fileSize, defaultMaxBackupFileCount, KalsymLogLevel.DEBUG);
        }
        return true;
    }

    /**
     * Creates the class instance for log properties
     *
     * @param version to add the version e.g. 3.0 in logs
     * @param logDir The directory in which log files have to be created,
     * ideally the string should end in the slash separator, but it is appended
     * if not found
     * @param fileName what should be the file name of log file Default log
     * pattern will be: 2013-07-31 00:17:56,701 [main] INFO Database Initialized
     * Default file size will be : 50MB
     * @param logLevel
     * @return
     */
    public static boolean createLogger(String version, String logDir,
            String fileName, KalsymLogLevel logLevel) throws Exception {
        if (logprop == null) {
            logprop = new LogProperties(version, logDir, fileName, defaultlogPattern, defaultFileSize, defaultMaxBackupFileCount, logLevel);
        }
        return true;
    }

    /**
     * Creates the class instance for log properties
     *
     * @param version to add the version e.g. 3.0 in logs
     * @param logDir The directory in which log files have to be created,
     * ideally the string should end in the slash separator, but it is appended
     * if not found
     * @param fileName what should be the file name of log file
     * @param logPattern what should be the pattern of logging Default filesize
     * will be: 50MB
     * @param logLevel
     * @return
     */
    public static boolean createLogger(String version, String logDir,
            String fileName, String logPattern, KalsymLogLevel logLevel) throws Exception {
        if (logprop == null) {
            logprop = new LogProperties(version, logDir, fileName, logPattern, defaultFileSize, defaultMaxBackupFileCount, logLevel);
        }
        return true;
    }

    /**
     * Creates the class instance for log properties
     *
     * @param version to add the version e.g. 3.0 in logs
     * @param logDir The directory in which log files have to be created,
     * ideally the string should end in the slash separator, but it is appended
     * if not found
     * @param fileName the file name of log file
     * @param logPattern the pattern with which logs will be written with
     * @param fileSize What should be the file size of log file, in case of
     * empty fileSize no size restriction on file size will be imposed
     * @param logLevel
     *
     * @return
     * @throws java.lang.Exception
     */
    public static boolean createLogger(String version, String logDir,
            String fileName, String logPattern, String fileSize,
            KalsymLogLevel logLevel) throws Exception {
        if (logprop == null) {
            createLogger(version, logDir, fileName, logPattern, fileSize, defaultMaxBackupFileCount, logLevel);
        }
        return true;
    }

    /**
     * Creates log file and adds rolling appenders
     *
     * @param logDir The directory in which log files have to be created,
     * ideally the string should end in the slash separator, but it is appended
     * if not found
     * @param fileName The name of the log file which will be created in the
     * logDir
     * @param version The version of the module which will be added in each log
     * line, providing an empty version will not add any
     * @param logPattern The pattern to write the logs with, passing an empty
     * string uses the default pattern
     * @param fileSize The max size of the log file, after which it will be
     * rotated
     * @param maxDailyBackupFilesCount The maximum log files' count for a day
     * @param logLevel The log level with which the logs will be written
     */
    public static boolean createLogger(String version, String logDir,
            String fileName, String logPattern, String fileSize,
            int maxDailyBackupFilesCount, KalsymLogLevel logLevel) throws Exception {
        if (maxDailyBackupFilesCount < 1) {
            throw new IndexOutOfBoundsException("maxBackupFilesCount must be greater than or equal to 1");
        }
        if (logprop == null) {
            logprop = new LogProperties(version, logDir, fileName, logPattern, fileSize, maxDailyBackupFilesCount, logLevel);
        }
        return true;

    }

    /**
     * Creates log file and adds rolling appenders
     *
     * @param logDir The directory in which log files have to be created,
     * ideally the string should end in the slash separator, but it is appended
     * if not found
     * @param fileName The name of the log file which will be created in the
     * logDir
     * @param version The version of the module which will be added in each log
     * line, providing an empty version will not add any
     * @param logPattern The pattern to write the logs with
     * @param fileSize The max size of the log file, after which it will be
     * rotated
     * @param logLevel The log level with which the logs will be written
     */
    private LogProperties(String version, String logDir, String fileName,
            String logPattern, String fileSize, int maxDailyBackupFilesCount,
            KalsymLogLevel logLevel) throws Exception {
        super();
        try {
            // Checking if version is either null or is an empty string
            if (version == null || version.trim().equals("")) {
                version = defaultVersion;
            }
            // if the default log pattern has been sent append version inside it
            if (defaultlogPattern.equals(logPattern)) {
                //logPattern = defaultlogPattern;
                logPattern = "%d [" + version + "] %-5p - %m%n";
            }
            if ("".equals(fileSize)) {
                fileSize = defaultFileSize;
            }

            this.loglevel = logLevel;

            String fileExtension = defaultLogFileExtension;

            fileName = fileName.trim();
            if (!"".equals(fileName)) {
                // Checking if extension is provided in fileName
                int startIndexOfExtenstion = fileName.lastIndexOf('.');
                if (startIndexOfExtenstion != -1) {
                    //Extension exists in fileName
                    fileExtension = fileName.substring(startIndexOfExtenstion);
                    //catering the condition when the dot is at the end of the fileName
                    if ("".equals(fileExtension)) {
                        fileExtension = defaultLogFileExtension;
                    }
                }
            }

            logDir = logDir.trim();
            String logFile = "";

            if (!(logDir.endsWith("/") || logDir.endsWith("\\"))) {
                logFile = logDir + "/" + fileName;
            } else {
                logFile = logDir + fileName;
            }

            String datePattern = "yyyy-MM-dd";

//            if (!"".equals(version.trim())) {
//                logPattern = "[" + version + "] " + logPattern;
//            }
            //Updating new pattern to remove the class name 
            PatternLayout rollingDateLayout = new PatternLayout(logPattern);//"%d [%t] %-5p (%F:%L) - %m%n"
            //%d [%t] %-5p (%F:%L) - %m%n gives the following formatted output
            //2013-07-31 00:17:56,701 [main] INFO  (NMSmsHandler.java:20) - Database Initialized

            log = Category.getInstance("application");

            DailyAndMaxSizeRollingFileAppender dmrfa
                    = new DailyAndMaxSizeRollingFileAppender(rollingDateLayout, logFile, fileExtension, datePattern, maxDailyBackupFilesCount, fileSize);

            log.addAppender(dmrfa);
            log.addAppender(new ConsoleAppender(new PatternLayout(logPattern))); //screen itself

        } catch (Exception ex) {
            System.out.println("Cannot create log files " + ex);
            throw ex;
        }
    }

    /**
     * @return the log
     */
    public Category getLog() {
        return log;
    }

    /**
     * Takes a KalsymLogLevel along with a <code>msg</code> string, message is
     * logged into file only if it passes a certain criteria, Following rules
     * are used to decide if log will be printed or not
     * ############################$$ Rule 1 $$################################
     * If you have initialized LogProperties using KalsymLogLevel.DEBUG. This
     * function will print @msg using any logging level passed in @logLevel
     * ############################$$ Rule 2 $$################################
     * If you have initialized LogProperties using KalsymLogLevel.INFO. This
     * function will print @msg using all logging level passed in @logLevel
     * except DEBUG log level ##############################Rule
     * 3$$################################ If you have initialized LogProperties
     * using KalsymLogLevel.ERROR. This function will print @msg using all
     * logging level passed in @logLevel except DEBUG
     *
     * @param loglevel parameter is level of logging your message should be
     * logged with, e.g if you want to print a debug log this parameter should
     * be debug, same way if you intend to use logging level info for this
     * message, you should pass INFO
     * @param msg message string to be logged
     */
    public static void writeLog(KalsymLogLevel loglevel, String msg) {
        KalsymLogLevel logType = getLoglevel();
        if (logType.equals(KalsymLogLevel.DEBUG)) {
            switch (loglevel) {
                case DEBUG:
                    logprop.log.debug(msg);
                    break;
                case INFO:
                    logprop.log.info(msg);
                    break;
                case ERROR:
                    logprop.log.error(msg);
                    break;
                default:
                    logprop.log.warn(msg);
            }
        } else if (logType.equals(KalsymLogLevel.INFO) && !loglevel.equals(KalsymLogLevel.DEBUG)) {
            switch (loglevel) {
                case INFO:
                    logprop.log.info(msg);
                    break;
                case ERROR:
                    logprop.log.error(msg);
                    break;
                default:
                    logprop.log.warn(msg);
            }
        } else if (logType.equals(KalsymLogLevel.ERROR) && !loglevel.equals(KalsymLogLevel.DEBUG)) {
            switch (loglevel) {
                case INFO:
                    logprop.log.info(msg);
                    break;
                case ERROR:
                    logprop.log.error(msg);
                    break;
                default:
                    logprop.log.warn(msg);
            }
        }
    }

    /**
     * Writes logs with LogLevel set by user
     *
     * @param msg The log which needs to be written
     */
    /**
     * Logs message into file only if it passes a certain criteria, Following
     * rules are used to decide if log will be printed or not
     * ############################$$ Rule 1 $$################################
     * If you have initialized LogProperties using KalsymLogLevel.DEBUG. This
     * function will print @msg using any logging level passed in @logLevel
     * ############################$$ Rule 2 $$################################
     * If you have initialized LogProperties using KalsymLogLevel.INFO. This
     * function will print @msg using all logging level passed in @logLevel
     * except DEBUG log level ##############################Rule
     * 3$$################################ If you have initialized LogProperties
     * using KalsymLogLevel.ERROR. This function will print @msg using all
     * logging level passed in @logLevel except DEBUG
     *
     * @param msg message string to be logged
     */
    public static void writeLog(String msg) {
        KalsymLogLevel logType = getLoglevel();
        switch (logType) {
            case DEBUG:
                logprop.log.debug(msg);
                break;
            case INFO:
                logprop.log.info(msg);
                break;
            case ERROR:
                logprop.log.error(msg);
                break;
            default:
                logprop.log.warn(msg);
                break;
        }
    }
}
