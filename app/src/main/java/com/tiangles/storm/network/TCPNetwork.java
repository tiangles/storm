package com.tiangles.storm.network;

import android.os.Handler;

import com.tiangles.storm.debug.Logger;
import com.tiangles.storm.network.connection.Connection;
import com.tiangles.storm.network.connection.ConnectionConfig;
import com.tiangles.storm.network.connection.TCPConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class TCPNetwork implements Connection.Listener{
    private TCPConnection connection;
    Handler handler = new Handler();
    Request currentRequest;
    ResponseFactory responseFactory;

    public TCPNetwork(ConnectionConfig config, ResponseFactory responseFactory) {
        connection = new TCPConnection(config, handler, this);
        this.responseFactory = responseFactory;
    }

    public void sendRequest(Request req){
        connection.send(req);
        currentRequest = req;
    }

    @Override
    public void onError(IOException e) {

    }

    @Override
    public void onData(byte[] data) {
        Response res = responseFactory.createResponse(data);
        currentRequest.handleResponse(res);
    }
}
