package com.tiangles.storm.network;

import com.tiangles.storm.network.connection.Connection;
import com.tiangles.storm.network.connection.ConnectionConfig;
import com.tiangles.storm.network.connection.Response;
import com.tiangles.storm.network.connection.TCPConnection;

import java.io.IOException;
import java.util.Vector;


public class TCPNetwork implements Connection.Delegate{
    private TCPConnection connection;
    boolean connected;
    Vector<JRequest> requests = new Vector<>();

    public TCPNetwork(ConnectionConfig config) {
        connection = new TCPConnection(config, this);
    }

    public void sendRequest(JRequest req){
        synchronized (requests) {
            requests.add(req);
        }
        connection.send(req);

    }

    @Override
    public void onConnected() {
        connected = true;
    }

    @Override
    public void onDisconnected() {
        connected = false;
    }

    @Override
    public void onError(IOException e) {

    }

    @Override
    public void onResponse(Response res) {
        JResponse sres = (JResponse) res;
        synchronized (requests) {
            for (JRequest e: requests) {
                if(e.command().equals(sres.command())) {
                    e.handleResponse(sres);
                    requests.remove(e);
                    break;
                }
            }
        }
    }
}
