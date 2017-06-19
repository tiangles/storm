package com.tiangles.storm.network.connection;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.tiangles.storm.debug.Logger;
import com.tiangles.storm.network.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public abstract class Connection {
    public interface Listener {
        void onError(IOException e);
        void onData(byte[] data);
    }

    protected abstract void connect() throws IOException;
    protected abstract void close();
    protected abstract DataInputStream getInputStream() throws IOException;
    protected abstract DataOutputStream getOutputStream() throws IOException;

    private Listener listener;
    private SendThread sendThread;
    private ReceiveThread receiveThread;
    private Request currentRequest;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Handler handler;
    Semaphore semaphore = new Semaphore(1);

    public Connection(Handler handler, Listener listener){
        this.handler = handler;
        this.listener = listener;
        try {
            semaphore.acquire();
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
        semaphore.release();
        Log.e("Network", "Main: Report request to read thread");
    }

    private void reportListener(final byte[] data, final IOException e) {
        if(data != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onData(data);
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onError(e);
                }
            });
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
                reportListener(null, e);
                return;
            }
            receiveThread = new ReceiveThread();
            receiveThread.start();
            while (sendThread != null) {
                try {
                    runBody();
                } catch (IOException e) {
                    e.printStackTrace();
                    reportListener(null, e);
                    currentRequest = null;
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        private void runBody() throws InterruptedException, IOException{
            Log.e("Network", "SendThread: Begin to wait request");
            semaphore.acquire();
            Log.e("Network", "SendThread: Get a request");
            currentRequest.write(outputStream);
            Log.e("Network", "SendThread: Write request to socket: done");
        }
    }

    private class ReceiveThread extends Thread {
        public ReceiveThread() {
            super("Network-ReceiveThread");
        }
        @Override
        public void run() {
            Log.e("Network", "ReceiveThread: start");
            try {
                inputStream = getInputStream();
            } catch (IOException e) {
                reportListener(null, e);
                return;
            }
            while (receiveThread != null) {
                try {
                    runBody();
                } catch (IOException e) {
                    Log.e("Network", e.toString());
                    e.printStackTrace();
                }
            }
        }

        private void runBody() throws IOException{
            Log.e("Network", "ReceiveThread: Begin to read socket");
            int tag = inputStream.readInt();
            Log.e("Network", "ReceiveThread: Read socket tag: " + tag);
            if(tag != 0xA55AAA55) {
                throw new IOException();
            }
            int len = inputStream.readInt();
            Log.e("Network", "ReceiveThread: Read socket len: " + len);
            byte[] data = new byte[len];
            inputStream.readFully(data);
            Log.e("Network", "ReceiveThread: Read socket data: " + data.toString());
            reportListener(data, null);
        }
    }
}
