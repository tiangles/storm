package com.tiangles.storm.analytics.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggerFormatter extends Formatter {
    private static final String LINE_SEPARATOR = "\n";


    public LoggerFormatter() {
        //unused
    }

    /**
     * Converts a object into a human readable string
     * representation.
     *
     */
    @Override
    public String format(LogRecord r) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatMessage(r)).append(LINE_SEPARATOR);
        return sb.toString();
    }
}