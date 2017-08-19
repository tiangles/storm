package com.tiangles.storm.network;


import android.util.Log;

import com.tiangles.storm.network.connection.Connection;
import com.tiangles.storm.network.connection.WebSocketConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class Network {
    private Configuration config;
    private Connection connection;
    private LinkedBlockingQueue<Request> pendingRequests = new LinkedBlockingQueue<>(8);
    private Vector<Request> requests = new Vector<>();
    SendingThread sendingThread = null;

    public Network(Configuration config) {
        this.config = config;

        if(config.getConnectionType() == Configuration.WEB_SOCKET_CONNECTION) {
            connection = new WebSocketConnection.Builder(config)
                    .build();
            connection.setStatusListener(new ConnectionListener());
            connection.startConnect();
        }
    }

    public void sendRequest(Request req) {
        requests.add(req);
        try {
            JSONObject obj = req.data();
            boolean res = connection.sendMessage(obj.toString().getBytes());
            if(!res) {
                pendingRequests.put(req);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            req.onError(e);
        } catch ( InterruptedException e) {
            e.printStackTrace();
            req.onError(e);
        }
    }

    private void onResponse(Response res) {
        for(Request req: requests) {
            String reqCmd = req.command();
            if(reqCmd != null && reqCmd.equals(res.command())) {
                req.handleResponse(res);
                requests.remove(req);
                break;
            }
        }
    }

    private class ConnectionListener implements Connection.StatusListener {
        @Override
        public void onOpen(String msg) {
            config.getDelegate().onNetworkOpen(msg);
            sendingThread = new SendingThread();
            sendingThread.start();
        }

        @Override
        public void onMessage(String text) {
            Response res = config.getDelegate().createResponse(text);
            onResponse(res);
        }

        @Override
        public void onMessage(byte[] data) {
            Response res = config.getDelegate().createResponse(data);
            onResponse(res);
        }

        @Override
        public void onReconnect() {
        }

        @Override
        public void onClosing(int code, String reason) {
            sendingThread = null;
        }

        @Override
        public void onClosed(int code, String reason) {
            config.getDelegate().onNetworkClosed(code, reason);
        }

        @Override
        public void onFailure(Throwable t, String msg) {
            sendingThread = null;
            config.getDelegate().onFailure(t, msg);
            connection.stopConnect();
        }
    }

    class SendingThread extends Thread {
        public SendingThread(){
            super("NetworkSendingThread");
        }

        @Override
        public void run() {
            while (sendingThread != null) {
                try {
                    Request req = pendingRequests.take();
                    if( !connection.sendMessage(req.data().toString().getBytes()) ) {
                        Log.e("Network", "put request in sending thread!");
                        req.retry();
                        if(req.getRetryTimes() > config.getReconnectMaxTime()) {
                            req.onError(new IOException());
                        } else {
                            pendingRequests.put(req);
                        }
                        sleep(config.getReconnectInterval());
                    }
                    Log.e("Network", "take request!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
