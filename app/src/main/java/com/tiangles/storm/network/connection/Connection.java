package com.tiangles.storm.network.connection;

import android.os.Handler;
import android.util.Log;

import com.tiangles.storm.debug.Logger;
import com.tiangles.storm.network.JResponseFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Connection {
    protected abstract void connect() throws IOException;
    protected abstract void close();
    protected abstract DataInputStream getInputStream() throws IOException;
    protected abstract DataOutputStream getOutputStream() throws IOException;

    private SendThread sendThread;
    private ReceiveThread receiveThread;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private Handler handler;
    private Delegate delegate;

    private JResponseFactory responseFactory;

    private LinkedBlockingQueue<Request> pendingRequests = new LinkedBlockingQueue<>(8);

    public interface Delegate {
        void onConnected();
        void onDisconnected();
        void onError(IOException e);
        void onResponse(Response res);
    }

    protected Connection(ConnectionConfig config, Delegate delegate){
        handler = config.getMessageHandler();
        responseFactory = config.getResponseFactory();
        this.delegate = delegate;

        sendThread = new SendThread();
        sendThread.start();
    }

    public void send(Request request){
        Log.e("Network", "Main: Get a request from main");
        try {
            pendingRequests.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("Network", "Main: Report request to read thread");
    }

    private void reportError(final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                delegate.onError(e);
            }
        });
    }

    private void reportResponse(final Response res) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                delegate.onResponse(res);
            }
        });
    }

    private void reportStartTransfer(final Request req) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                req.onStartTransfer();
            }
        });
    }

    private void reportEndTransfer(final Request req) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                req.onEndTransfer();
            }
        });
    }

    private class SendThread extends Thread {
        private SendThread() {
            super("Network-SendThread");
        }
        @Override
        public void run() {
            Log.e("Network", "SendThread: start");
            try {
                connect();
                Log.e("Network", "SendThread: connected to server");
                outputStream = getOutputStream();
            } catch (IOException e) {
                reportError(e);
                return;
            }
            receiveThread = new ReceiveThread();
            receiveThread.start();
            while (sendThread != null) {
                try {
                    runBody();
                } catch (IOException e) {
                    e.printStackTrace();
                    reportError(e);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void runBody() throws InterruptedException, IOException{
            Request req = pendingRequests.take();
            if(req != null ){
                reportStartTransfer(req);
                req.write(outputStream);
                reportEndTransfer(req);
            } else {
                Logger.log("Network: invalid request!");
            }

        }
    }

    private class ReceiveThread extends Thread {
        private ReceiveThread() {
            super("Network-ReceiveThread");
        }
        @Override
        public void run() {
            try {
                inputStream = getInputStream();
            } catch (IOException e) {
                reportError(e);
                return;
            }
            while (receiveThread != null) {
                try {
                    runBody();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void runBody() throws IOException, InterruptedException{
            Response res = responseFactory.createResponse();
            res.read(inputStream);
            reportResponse(res);
        }
    }
}
