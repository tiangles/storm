package com.tiangles.storm.network.connection;

import android.os.Handler;
import android.util.Log;

import com.tiangles.storm.debug.Logger;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public abstract class Connection {
    protected abstract void connect() throws IOException;
    protected abstract void close();
    protected abstract DataInputStream getInputStream() throws IOException;
    protected abstract DataOutputStream getOutputStream() throws IOException;

    private SendThread sendThread;
    private ReceiveThread receiveThread;
    private Request currentRequest;
    private Response currentResponse;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Handler handler;
    Semaphore writeSemaphore = new Semaphore(1);
    Semaphore readSemaphore = new Semaphore(1);

    public Connection(Handler handler){
        this.handler = handler;
        try {
            writeSemaphore.acquire();
            readSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendThread = new SendThread();
        sendThread.start();
    }

    public void send(Request request){
        Log.e("Network", "Main: Get a request from main");
        if(currentRequest == null) {
            currentRequest = request;
        } else {
            Logger.log("Busy!");
        }
        writeSemaphore.release();
        Log.e("Network", "Main: Report request to read thread");
    }

    private void reportError(final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentRequest.onError(e);
            }
        });
    }

    private void reportResponse(final Response res) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentRequest.handleResponse(res);
            }
        });
    }

    private void reportStartTransfer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentRequest.onStartTransfer();
            }
        });
    }

    private void finishRequest(boolean closeSocket) {
        currentResponse = null;
        currentResponse = null;
        if(closeSocket) {
            close();
        }
    }

    private class SendThread extends Thread {
        public SendThread() {
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
                    finishRequest(true);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void runBody() throws InterruptedException, IOException{
            writeSemaphore.acquire();
            if(currentResponse != null) {
                throw new IOException();
            }
            reportStartTransfer();
            currentResponse = currentRequest.createResponse();
            currentRequest.write(outputStream);
            readSemaphore.release();
        }
    }

    private class ReceiveThread extends Thread {
        public ReceiveThread() {
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
                    finishRequest(true);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void runBody() throws IOException, InterruptedException{
            if(currentResponse == null) {
                readSemaphore.acquire();
            }
            currentResponse.read(inputStream);
            reportResponse(currentResponse);
            if(currentResponse.finished()) {
                finishRequest(false);
            }
        }
    }
}
