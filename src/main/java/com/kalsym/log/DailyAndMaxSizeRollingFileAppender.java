package com.kalsym.log;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

public class DailyAndMaxSizeRollingFileAppender extends FileAppender {

    static final int TOP_OF_TROUBLE = -1;
    static final int TOP_OF_MINUTE = 0;
    static final int TOP_OF_HOUR = 1;
    static final int HALF_DAY = 2;
    static final int TOP_OF_DAY = 3;
    static final int TOP_OF_WEEK = 4;
    static final int TOP_OF_MONTH = 5;
    /**
     * @deprecated
     */
    public static final String DATE_PATTERN_OPTION = "DatePattern";
    private String datePattern = "'.'yyyy-MM-dd";
    private String scheduledFilename;
    private String fileExtension;
    private String filePathWithoutExtension;
    private long nextCheck = System.currentTimeMillis() - 1L;
    private int lastFileIndex;
    /**
     * Determines if the file is being rolled over the first time today, is set
     * to false in sizeRollOver() function
     */
    public static boolean firstTimeRollingToday = true;
    Date now = new Date();
    SimpleDateFormat sdf;
    RollingCalendar rc = new RollingCalendar();
    int checkPeriod = -1;
    /**
     * Size Properties
     */
    protected long maxFileSize = 10485760L;
    protected int maxBackupIndex = 1;

    public DailyAndMaxSizeRollingFileAppender() {
    }

    /**
     * Initializes the class with a custom pattern layout, fileName and a date
     * pattern. File Extension is used .log as default
     *
     * @param layout
     * @param fileName
     * @param datePattern
     * @throws IOException
     */
    public DailyAndMaxSizeRollingFileAppender(PatternLayout layout,
            String fileName, String datePattern)
            throws IOException {
        super(layout, fileName, true);
        this.datePattern = datePattern;
        this.fileExtension = "log";
        activateOptions();
    }

    /**
     * Initializes the class with a custom pattern layout, fileName and a date
     * pattern.
     *
     * @param layout
     * @param fileName
     * @param fileExtension
     * @param datePattern
     * @param maxBackupIndex
     * @param maxFileSize
     * @throws IOException
     */
    public DailyAndMaxSizeRollingFileAppender(PatternLayout layout,
            String fileName, String fileExtension, String datePattern,
            int maxBackupIndex, String maxFileSize)
            throws IOException {
        super(layout, fileName + "." + fileExtension, true);
        LogLog.debug("initializing appender");
        this.filePathWithoutExtension = fileName;//fileName.substring(0, filename.indexOf('.'));
        this.datePattern = datePattern;
        this.maxBackupIndex = maxBackupIndex;
        this.maxFileSize = OptionConverter.toFileSize(maxFileSize, this.maxFileSize + 1L);
        this.fileExtension = fileExtension;
        activateOptions();
    }

    public int getMaxBackupIndex() {
        return this.maxBackupIndex;
    }

    public long getMaximumFileSize() {
        return this.maxFileSize;
    }

    public void setMaxBackupIndex(int maxBackups) {
        this.maxBackupIndex = maxBackups;
    }

    /**
     * @deprecated
     */
    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setMaximumFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setMaxFileSize(String value) {
        this.maxFileSize = OptionConverter.toFileSize(value, this.maxFileSize + 1L);
    }

    protected void setQWForFiles(Writer writer) {
        this.qw = new CountingQuietWriter(writer, this.errorHandler);
    }

    public void setDatePattern(String pattern) {
        this.datePattern = pattern;
    }

    public String getDatePattern() {
        return this.datePattern;
    }

    /**
     * @deprecated
     */
//  public String[] getOptionStrings()
//  {
//    return OptionConverter.concatanateArrays(super.getOptionStrings(), new String[] { "DatePattern" });
//  }
    /**
     * @deprecated
     */
    public void setOption(String key, String value) {
        if (value == null) {
            return;
        }
//        super.setOption(key, value);

        if (key.equalsIgnoreCase("DatePattern")) {
            this.datePattern = value;
        }
    }

    public void activateOptions() {
        LogLog.debug("activateOptions entering");
        super.activateOptions();
        if ((this.datePattern != null) && (this.fileName != null)) {
            this.now.setTime(System.currentTimeMillis());
            this.sdf = new SimpleDateFormat(this.datePattern);
            //Disabling initialization of lastFileIndex, because it has the track of the current file's index which is rolled            
            //this.lastFileIndex = 1;
            int type = computeCheckPeriod();
            printPeriodicity(type);
            this.rc.setType(type);
            //this.scheduledFilename = (this.fileName + this.sdf.format(this.now));
            this.scheduledFilename = (this.filePathWithoutExtension + this.sdf.format(this.now));
        } else {
            LogLog.error("Either Filename or DatePattern options are not set for [" + this.name + "].");
        }
    }

    void printPeriodicity(int type) {
        switch (type) {
            case 0:
                LogLog.debug("Appender [" + this.name + "] to be rolled every minute.");
                break;
            case 1:
                LogLog.debug("Appender [" + this.name + "] to be rolled on top of every hour.");

                break;
            case 2:
                LogLog.debug("Appender [" + this.name + "] to be rolled at midday and midnight.");

                break;
            case 3:
                LogLog.debug("Appender [" + this.name + "] to be rolled at midnight.");

                break;
            case 4:
                LogLog.debug("Appender [" + this.name + "] to be rolled at start of week.");

                break;
            case 5:
                LogLog.debug("Appender [" + this.name + "] to be rolled at start of every month.");

                break;
            default:
                LogLog.warn("Unknown periodicity for appender [" + this.name + "].");
        }
    }

    int computeCheckPeriod() {
        RollingCalendar c = new RollingCalendar();

        Date epoch = new Date(0L);
        if (this.datePattern != null) {
            for (int i = 0; i <= 5; i++) {
                String r0 = this.sdf.format(epoch);
                c.setType(i);
                Date next = new Date(c.getNextCheckMillis(epoch));
                String r1 = this.sdf.format(next);

                if ((r0 != null) && (r1 != null) && (!r0.equals(r1))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Renames the current fileName to a new file with an incremented index. if
     * the maxBackupIndex value is reached, it deletes the file with the maximum
     * index and creates a new file with that index. Thus it keeps on replacing
     * the lastly created file only.
     */
    public void sizeRollOver() {
        LogLog.debug("rolling over count=" + ((CountingQuietWriter) this.qw).getCount());
        LogLog.debug("maxBackupIndex=" + this.maxBackupIndex);
        if (this.maxBackupIndex > 0) {
            //File file = new File(this.fileName + '.' + this.maxBackupIndex + '.' + this.fileExtension);
            File file = new File(this.filePathWithoutExtension + '.' + this.maxBackupIndex + '.' + this.fileExtension);
            // closeing the file so that the renaming in the loop can take place
            closeFile();
            if (file.exists()) {
                file.delete();
            }
            String newFileName = "";

            //String newSizeFilename = this.fileName + this.sdf.format(this.now);
            String newSizeFilename = this.filePathWithoutExtension + this.sdf.format(this.now);
            String targetFileName = "";
            String lastFileName = newSizeFilename + "." + this.maxBackupIndex + '.' + this.fileExtension;
            File lastFile = new File(lastFileName);
            for (int i = this.maxBackupIndex - 1; i >= 1; i--) {
                lastFileIndex = i;
                //file = new File(this.fileName + "." + i);
                targetFileName = newFileName = newSizeFilename + "." + i + '.' + this.fileExtension;
                //file = new File(newFileName);
                file = new File(this.fileName);
                //file = new File(this.filePathWithoutExtension); //wrong, need the actual file name
                File newFile = new File(newFileName);
                //if (file.exists()) {
                if (lastFile.exists()) {
                    lastFileIndex = i + 1;
                    targetFileName = newSizeFilename + '.' + (i + 1) + '.' + this.fileExtension;

                    File target = new File(targetFileName);
                    //Deleting the last index file so that the current file can be renamed to the last file, since the max backup index has been reached
                    if (target.exists()) {
                        target.delete();
                    }
                    LogLog.debug("Renaming file " + file + " to " + target);

                    file.renameTo(target);
                    break;
                }
                if (newFile.exists()) {
                    lastFileIndex = i + 1;
                    //File target = new File(this.fileName + '.' + (i + 1));
                    targetFileName = newSizeFilename + '.' + (i + 1) + '.' + this.fileExtension;

                    File target = new File(targetFileName);
                    LogLog.debug("Renaming file " + file + " to " + target);
                    file.renameTo(target);
                    break;
                }
            }
            // not incrementing the lastFileIndex so that if it enters dailyRollOver() the index should be the current index of the file which is just created
            //lastFileIndex++;

            this.scheduledFilename = targetFileName;

            //String newFileName = this.scheduledFilename + "." + 1;
            //this.scheduledFilename = newFileName;
            //File target = new File(this.fileName + "." + 1);
            File target = new File(targetFileName);

            closeFile();

            // Renaming the current file with the new "target" name
            file = new File(this.fileName);
            LogLog.debug("Renaming file " + file + " to " + target);
            file.renameTo(target);
            // Setting it to false because it is not the first time the file has been rolled, it is being rolled before day end becuase it has reached the maximum size
            this.firstTimeRollingToday = false;
        }
        try {
            this.setFile(this.fileName, false);
        } catch (IOException e) {
            LogLog.error("setFile(" + this.fileName + ", false) call failed.", e);
        }
    }

    /**
     * Renames the current fileName with the name in the scheduledFilename
     * variable, which is the name with the previous date. Increments the index
     * of the file name as well. If already backup count has reached
     * maxBackupIndex then it creates another file thus exceeding the count by 1
     *
     * @throws IOException
     */
    void dailyRollOver()
            throws IOException {
        LogLog.debug("dailyRollOver entering...");
        if (this.datePattern == null) {
            this.errorHandler.error("Missing DatePattern option in rollOver().");
            return;
        }

        //String datedFilename = this.fileName + this.sdf.format(this.now);
        String datedFilename = this.filePathWithoutExtension + this.sdf.format(this.now);

        if (this.scheduledFilename.equals(datedFilename)) {
            return;
        }

        closeFile();

        File target = null;
        if (firstTimeRollingToday) {
            //If its the first time the file is being rolled over today, then mark it as the first index entry
            target = new File(this.scheduledFilename + ".1." + this.fileExtension);
        } else {
            //If file has already been rolled over today then it must have been a size roll over which would have resulted in setting the lastFileIndex to the index of the last file created 
            target = new File(this.scheduledFilename + '.' + (lastFileIndex + 1) + '.' + this.fileExtension);
            //Setting this to true because if the max size doesnt reach today, then the file should be created with index = 1 
            firstTimeRollingToday = true;
        }
        lastFileIndex = 1;
        if (target.exists()) {
            target.delete();
        }
        // Creating a new File instance with the existing current file's path, so that it can be renamed to target name. Note that it does not create the file with "filename", it only instantiates the file with this abstract pathname, to create a file an explicit method createNewFile() has to be called
        File file = new File(this.fileName);
        file.renameTo(target);
        LogLog.debug(this.fileName + " -> " + this.scheduledFilename);
        try {
            setFile(this.fileName, false);
        } catch (IOException e) {
            this.errorHandler.error("setFile(" + this.fileName + ", false) call failed.");
        }
        this.scheduledFilename = datedFilename;
    }

//    @Override
    public void setFile(String fileName, boolean append)
            throws IOException {
        LogLog.debug("setFile called: " + fileName + ", " + append);

        reset();
//    Writer fw = Writer.createWriter(new FileOutputStream(fileName, append));
//    if(bufferedIO) {
//      fw = new BufferedWriter(fw, bufferSize);
//    }
//    this.setQWForFiles(fw);
        this.fileName = fileName;
        this.fileAppend = append;
        activateOptions();
//    this.bufferedIO = bufferedIO;
//    this.bufferSize = bufferSize;
//    writeHeader();
//    LogLog.debug("setFile ended");
    }

    protected void subAppend(LoggingEvent event) {
        long n = System.currentTimeMillis();
        //Combined both time and size condition
        if (((CountingQuietWriter) this.qw).getCount() >= this.maxFileSize) {
            sizeRollOver();
        }
        if (n >= this.nextCheck) {
            this.now.setTime(n);
            this.nextCheck = this.rc.getNextCheckMillis(this.now);
            try {
                dailyRollOver();
            } catch (IOException ioe) {
                LogLog.error("rollOver() failed.", ioe);
            }
        }
        super.subAppend(event);
    }

}
