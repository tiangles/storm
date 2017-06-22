package com.tiangles.storm.network;

import android.content.Context;
import android.os.Handler;

public interface Configuration {
    int TCP_CONNECTION = 0;
    int WEB_SOCKET_CONNECTION = 1;

    Context getContext();
    String getServerAddress();
    int getServerPort();
    int getConnectionType();
    int getReconnectInterval();
    int getReconnectMaxTime();
    Handler getMessageHandler();
    Delegate getDelegate();
}