package com.kalsym.cdr;

import com.kalsym.log.DailyAndMaxSizeRollingFileAppender;
import org.apache.log4j.Category;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author zeeshan
 */
public class CdrWriter extends java.util.Properties {

    public static Category cdr;
    static CdrWriter cdrWriter;
    private static final String defaultCdrPattern = "%d{yyyy-MM-dd,HH:mm:ss.SSS},%m%n";
    private static final String defaultFileSize = "50MB";

    /**
     * @param dir the directory in which logs should be made
     * @param filename what should be the file name of log file Default log
     * pattern will be: 2013-07-31 00:17:56,701 [main] INFO
     * (NMSmsHandler.java:20) - Database Initialized Default file size will be :
     * 50MB
     * @return
     */
    public static boolean createCdr(String dir, String filename) {
        if (cdrWriter == null) {
            cdrWriter = new CdrWriter(dir, filename, defaultCdrPattern, defaultFileSize);
        }
        return true;
    }

    /**
     *
     * @param dir the directory in which logs should be made
     * @param filename what should be the file name of log file
     * @param cdrPattern what should be the pattern of logging Default filesize
     * will be: 50MB
     * @return
     */
    public static boolean createCdr(String dir, String filename,
            String cdrPattern) {
        if (cdrWriter == null) {
            if (cdrPattern.equals("")) {
                cdrPattern = defaultCdrPattern;
            }
            cdrWriter = new CdrWriter(dir, filename, cdrPattern, defaultFileSize);
        }
        return true;
    }

    /**
     *
     * @param dir the directory in which logs should be made
     * @param filename what should be the file name of log file
     * @param cdrPattern what should be the pattern of logging
     * @param filesize What should be the file size of log file, in case of
     * empty filesize no size restriction on file size will be imposed
     * @return
     */
    public static boolean createCdr(String dir, String filename,
            String cdrPattern, String filesize) {
        if (cdrWriter == null) {
            if (cdrPattern.equals("")) {
                cdrPattern = defaultCdrPattern;
            }
            if (filesize.equals("")) {
                filesize = defaultFileSize;
            }
            cdrWriter = new CdrWriter(dir, filename, cdrPattern, filesize);
        }
        return true;
    }

    /**
     * Created log file and adds rolling appenders
     */
    private CdrWriter(String dir, String filename, String cdrPattern,
            String filesize) {
        super();
        try {
            String logFile = dir + filename;

            String datePattern = "yyyy-MM-dd";
            //Updating new pattern to remove the class name 
            PatternLayout rollingDateLayout = new PatternLayout(cdrPattern);//"%d [%t] %-5p (%F:%L) - %m%n"
            //%d [%t] %-5p (%F:%L) - %m%n gives the following formatted output
            //2013-07-31 00:17:56,701 [main] INFO  (NMSmsHandler.java:20) - Database Initialized

            cdr = Category.getInstance("cdr");
            String fileExtension = "log";
            DailyAndMaxSizeRollingFileAppender dmrfa
                    = new DailyAndMaxSizeRollingFileAppender(rollingDateLayout, logFile, fileExtension, datePattern, 100, filesize);
            cdr.addAppender(dmrfa);
            cdr.addAppender(new ConsoleAppender(new PatternLayout(cdrPattern))); //screen itself

        } catch (Exception ex) {
            System.out.println("Cannot create cdr files" + ex);
        }
    }
}
