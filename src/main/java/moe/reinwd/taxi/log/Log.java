package moe.reinwd.taxi.log;

import moe.reinwd.taxi.util.ConcurrentUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * @author ReinWD 张巍 2016214874
 * @e-mail ReinWDD@gmail.com
 */
public class Log {
    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;
    private static int logLevel;
    private static BufferedWriter writer;
    private static File logFile;
    private static ExecutorService writerThread = ConcurrentUtil.getExecutor("LoggerThread", ConcurrentUtil.ExecutorType.SINGLE_THREAD);

    static {
        logFile = new File("./log");
        if (!logFile.exists()) {
            logFile.mkdir();
        }
        Date date = new Date();
        String format = DateFormat.getDateInstance().format(date);
        logFile = new File(logFile, format + ".log");
        try {
            writer = new BufferedWriter(new FileWriter(logFile,true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLogLevel(LogLevel logLevel) {
        Log.logLevel = logLevel.level;
    }

    public static void d(String s) {
        if (Log.logLevel <= 0) {
            log(LogLevel.DEBUG, s);
        }
    }

    private static void log(LogLevel level, String message) {
        writerThread.submit(()->{
            try {
                synchronized (writer) {
                    writer.newLine();
                    Date date = new Date();
                    String format = DateFormat.getTimeInstance().format(date);
                    writer.write("[");
                    writer.write(format);
                    writer.write("] [");
                    writer.write(level.name());
                    writer.write("] ");
                    writer.write(message);
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void i(String s) {
        if (Log.logLevel <= 1) {
            log(LogLevel.INFO,s);
        }
    }

    public static void w(String s) {
        if (Log.logLevel <= 2) {
            log(LogLevel.WARN,s);
        }
    }

    public static void e(String s) {
        if (Log.logLevel <= 3) {
            log(LogLevel.ERROR,s);
        }
    }

    enum LogLevel {
        DEBUG(0), INFO(1), WARN(2), ERROR(3);
        int level;

        LogLevel(int i) {
            this. level = i;
        }
    }
}
