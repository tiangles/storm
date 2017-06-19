package com.tiangles.storm.debug;

import android.util.*;

public class Logger {
    public static final int VERBOSE = 2;
    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    public static void log(String message){
        log(message, "Storm", DEBUG);
    }
    public static void log(String message, String component) {
        log(message, component, DEBUG);
    }
    public static void log(String message, int severity) {
        log(message, "Storm", severity);
    }
    public static void log(String message, String component, int severity){
        switch (severity){
            case DEBUG:
                Log.d(component, message);
                break;
            case INFO:
                Log.i(component, message);
                break;
            case WARN:
                Log.w(component, message);
                break;
            case ERROR:
            case ASSERT:
                Log.e(component, message);
                break;
            case VERBOSE:
            default:
                Log.v(component, message);
                break;
        }
        Log.e(component, message);
    }
}
