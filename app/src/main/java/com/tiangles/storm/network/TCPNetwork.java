package com.tiangles.storm.network;

import android.os.Handler;

import com.tiangles.storm.network.connection.ConnectionConfig;
import com.tiangles.storm.network.connection.TCPConnection;


public class TCPNetwork{
    private TCPConnection connection;
    Handler handler = new Handler();
    Request currentRequest;

    public TCPNetwork(ConnectionConfig config) {
        connection = new TCPConnection(config, handler);
    }

    public void sendRequest(Request req){
        connection.send(req);
        currentRequest = req;
    }
}
