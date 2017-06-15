package com.tiangles.storm.analytics.logger;

import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class FileLogger{
    final Logger logger;
    private String logDir;
    private Formatter formatter;
    private int expiredPeriod;

    private HandlerThread fileLoggerThread;
    private Handler handler;
    private String tag;

    public FileLogger(String tag) {
        this.tag = tag;
        fileLoggerThread = new HandlerThread(FileLogger.class.getSimpleName());
        fileLoggerThread.start();
        handler = new Handler(fileLoggerThread.getLooper());

        this.logger = Logger.getLogger(tag);
        logger.setUseParentHandlers(false);
    }


    public void setFilePathAndFormatter(final String dir, Formatter formatter, final int expiredPeriod) {
        this.logDir = dir;
        this.formatter = formatter;
        this.expiredPeriod = expiredPeriod;

        if (!logDir.endsWith("/"))
            logDir += "/";

        String name = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        File file = new File(logDir + name + ".txt");

        FileHandler fh;
        try {
            File d = new File(logDir);
            if(!d.exists()){
                d.mkdir();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            fh = new FileHandler(file.toString(), true);
            fh.setFormatter(formatter);

            logger.addHandler(fh);
        } catch (IOException e) {
            //unused
            error(this.getClass().getSimpleName(), e);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public String logDirectory() {
        return this.logDir;
    }

    public Formatter fileFormatter() {
        return this.formatter;
    }

    public int expiredPeriod() {
        return this.expiredPeriod;
    }


    public void setLevel(LoggerLevel level) {
        if (LoggerLevel.DEBUG == level)
            logger.setLevel(Level.FINE);
        else if (LoggerLevel.INFO == level)
            logger.setLevel(Level.INFO);
        else if (LoggerLevel.WARN == level)
            logger.setLevel(Level.WARNING);
        else if (LoggerLevel.ERROR == level)
            logger.setLevel(Level.SEVERE);
        else if (LoggerLevel.OFF == level)
            logger.setLevel(Level.OFF);
    }

    private synchronized void log(Level level, String msg, Throwable t) {
        LogRecord record = new LogRecord(level, msg);
        record.setLoggerName(tag);
        record.setThrown(t);
        logger.log(record);
    }

    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    public void debug(final String msg) {
        if (!isDebugEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.FINE, msg, null);
            }
        });
    }

    public void debug(final String subTag, final String msg) {
        if (!isDebugEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.FINE, TagFormatter.format(subTag, msg), null);
            }
        });
    }

    public void debug(final String subTag, final String format, final Object arg) {
        if (!isDebugEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.FINE, TagFormatter.format(subTag, format, arg), null);
            }
        });
    }

    public void debug(final String subTag, final String format, final Object argA, final Object argB) {
        if (!isDebugEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.FINE, TagFormatter.format(subTag, format, argA, argB), null);
            }
        });
    }

    public void debug(final String subTag, final String format, final Object... arguments) {
        if (!isDebugEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.FINE, TagFormatter.format(subTag, format, arguments), null);
            }
        });
    }

    public void debug(String subTag, Throwable t) {
        if (!isDebugEnabled())
            return;
        log(Level.FINE, subTag, t);
    }

    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    public void info(final String msg) {
        if (!isInfoEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.INFO, msg, null);
            }
        });
    }

    public void info(final String subTag, final String msg) {
        if (!isInfoEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.INFO, TagFormatter.format(subTag, msg), null);
            }
        });
    }

    public void info(final String subTag, final String format, final Object arg) {
        if (!isInfoEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.INFO, TagFormatter.format(subTag, format, arg), null);
            }
        });
    }

    public void info(final String subTag, final String format, final Object argA, final Object argB) {
        if (!isInfoEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.INFO, TagFormatter.format(subTag, format, argA, argB), null);
            }
        });
    }

    public void info(final String subTag, final String format, final Object... arguments) {
        if (!isInfoEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.INFO, TagFormatter.format(subTag, format, arguments), null);
            }
        });
    }

    public void info(final String subTag, final Throwable t) {
        if (!isInfoEnabled())
            return;
        log(Level.INFO, subTag, t);
    }

    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    public void warn(final String msg) {
        if (!isWarnEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.WARNING, msg, null);
            }
        });
    }

    public void warn(final String subTag, final String msg) {
        if (!isWarnEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.WARNING, TagFormatter.format(subTag, msg), null);
            }
        });
    }

    public void warn(final String subTag, final String format, final Object arg) {
        if (!isWarnEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.WARNING, TagFormatter.format(subTag, format, arg), null);
            }
        });
    }

    public void warn(final String subTag, final String format, final Object... arguments) {
        if (!isWarnEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.WARNING, TagFormatter.format(subTag, format, arguments), null);
            }
        });
    }

    public void warn(final String subTag, final String format, final Object argA, final Object argB) {
        if (!isWarnEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.WARNING, TagFormatter.format(subTag, format, argA, argB), null);
            }
        });
    }

    public void warn(String subTag, Throwable t) {
        if (!isWarnEnabled())
            return;
        log(Level.WARNING, subTag, t);
    }


    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    public void error(final String msg) {
        if (!isErrorEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.SEVERE, msg, null);
            }
        });
    }

    public void error(final String subTag, final String msg) {
        if (!isErrorEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.SEVERE, TagFormatter.format(subTag, msg), null);
            }
        });
    }

    public void error(final String subTag, final String format, final Object arg) {
        if (!isErrorEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.SEVERE, TagFormatter.format(subTag, format, arg), null);
            }
        });
    }

    public void error(final String subTag, final String format, final Object argA, final Object argB) {
        if (!isErrorEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.SEVERE, TagFormatter.format(subTag, format, argA, argB), null);
            }
        });
    }

    public void error(final String subTag, final String format, final Object... arguments) {
        if (!isErrorEnabled())
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                log(Level.SEVERE, TagFormatter.format(subTag, format, arguments), null);
            }
        });
    }

    public void error(String subTag, Throwable t) {
        if (!isErrorEnabled())
            return;
        log(Level.SEVERE, subTag, t);
    }

    public enum  LoggerLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR,
        OFF
    }

}
