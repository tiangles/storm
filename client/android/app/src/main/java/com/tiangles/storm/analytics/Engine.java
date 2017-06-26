package com.tiangles.storm.analytics;

import android.os.Environment;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.analytics.event.Event;
import com.tiangles.storm.analytics.logger.FileLogger;
import com.tiangles.storm.analytics.logger.LoggerFormatter;

import java.util.logging.Formatter;

public class Engine {
    private static Engine mInstance;
    private Session mCurrentSession;
    private FileLogger mFileLogger;
    static final class Configuration {
        public static FileLogger.LoggerLevel loggerLevel = FileLogger.LoggerLevel.DEBUG;
        public static String fileDirectory = Environment.getExternalStorageDirectory().getPath() + "/storm_logs/";
        public static Formatter fileFormatter = new LoggerFormatter();
        public static int expiredPeriod = 1;
    }

    public static Engine getInstance(){
        if(mInstance == null){
            mInstance = new Engine();
            mInstance.newSession();
        }
        return mInstance;
    }
    private Engine(){

    }

    public boolean newSession() {
        endSession();
        mCurrentSession = new Session();
        buildLogger();
        return false;
    }

    public boolean addEvent(Event e) {
        mFileLogger.error(e.tag(), e.toString());
        StormApp.getNetwork().sendRequest(new UploadRequest(e));
        return false;
    }

    private void endSession(){
        // zip the log file and upload to server
    }

    private void buildLogger(){
        mFileLogger = new FileLogger(mCurrentSession.name());
        mFileLogger.setLevel(Configuration.loggerLevel);
        mFileLogger.setFilePathAndFormatter(Configuration.fileDirectory,
                Configuration.fileFormatter,
                Configuration.expiredPeriod);
    }
}
