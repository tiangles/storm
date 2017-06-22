package com.tiangles.storm.network.connection;

import android.os.Handler;

import com.tiangles.storm.network.JResponseFactory;

public interface ConnectionConfig {
    int HTTP_CONNECTION = 0;
    int HTTPS_CONNECTION = 1;
    int TCP_CONNECTION = 2;
    int TLS_CONNECTION = 3;

    String getServerAddress();
    int getServerPort();
    int getConnectionType();
    int getSoTimeout();
    Handler getMessageHandler();
    JResponseFactory getResponseFactory();
}
