package com.tiangles.storm.network;

import android.os.Handler;

import com.tiangles.storm.network.connection.ConnectionConfig;
import com.tiangles.storm.network.connection.TCPConnection;


public class TCPNetwork{
    private TCPConnection connection;
    Handler handler = new Handler();
    Request currentRequest;
    ResponseFactory responseFactory;

    public TCPNetwork(ConnectionConfig config, ResponseFactory responseFactory) {
        connection = new TCPConnection(config, handler);
        this.responseFactory = responseFactory;
    }

    public void sendRequest(Request req){
        connection.send(req);
        currentRequest = req;
    }
}
