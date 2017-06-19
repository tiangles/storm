package com.tiangles.storm.network.connection;

import com.tiangles.storm.network.ResponseFactory;

public interface ConnectionConfig {
    public static int HTTP_CONNECTION = 0;
    public static int HTTPS_CONNECTION = 1;
    public static int TCP_CONNECTION = 2;
    public static int TLS_CONNECTION = 3;

    public String getServerAddress();
    public int getServerPort();
    public int getConnectionType();
    public int getSoTimeout();
}
