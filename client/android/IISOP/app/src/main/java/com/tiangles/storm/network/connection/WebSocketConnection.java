package com.tiangles.storm.network.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.tiangles.storm.network.Configuration;

import java.net.ConnectException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketConnection implements Connection {
    private Context context;
    private String url;
    private WebSocket socket;
    private OkHttpClient httpClient;
    private int currentStatus = Status.DISCONNECTED;
    private boolean isNeedReconnect = true;
    private StatusListener statusListener;
    private Lock globalLock;
    private Handler handler;
    private int reconnectCount = 0;
    private int reconnectInterval = 0;
    private int reconnectMaxTime = 0;


    private WebSocketListener listener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            socket = webSocket;
            setCurrentStatus(Status.CONNECTED);
            connected();
            statusListener.onOpen(response.toString());
        }

        @Override
        public void onMessage(WebSocket webSocket, final ByteString bytes) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    statusListener.onMessage(bytes.toByteArray());
                }
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    statusListener.onMessage(text);
                }
            });
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            statusListener.onClosing(code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            statusListener.onClosed(code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            if(!tryReconnect()){
                statusListener.onFailure(t, response!=null?response.toString():"");
            }
        }
    };

    private WebSocketConnection(Builder builder) {
        context = builder.mContext;
        url = builder.url;
        handler = builder.handler;
        reconnectInterval = builder.mReconnectInterval;
        reconnectMaxTime = builder.mReconnectMaxTime;
        globalLock = new ReentrantLock();
    }

    private void initWebSocket() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.dispatcher().cancelAll();
        try {
            globalLock.lockInterruptibly();
            try {
                httpClient.newWebSocket(request, listener);
            } finally {
                globalLock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            statusListener.onFailure(e, "Fail to initialize websocket");
        }
    }

    @Override
    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    @Override
    public synchronized boolean isConnected() {
        return currentStatus == Status.CONNECTED;
    }

    @Override
    public synchronized int getCurrentStatus() {
        return currentStatus;
    }

    @Override
    public synchronized void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public void startConnect() {
        isNeedReconnect = true;
        buildConnect();
    }

    @Override
    public void stopConnect() {
        isNeedReconnect = false;
        disconnect();
    }


    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (statusListener != null) {
                statusListener.onReconnect();
            }
            buildConnect();
        }
    };


    private boolean tryReconnect() {
        if (!isNeedReconnect || reconnectCount> reconnectMaxTime) {
            return false;
        }

        if (!isNetworkAvailable(context)) {
            setCurrentStatus(Status.DISCONNECTED);
            return false;
        }

        setCurrentStatus(Status.RECONNECT);

        handler.postDelayed(reconnectRunnable, reconnectInterval);
        reconnectCount++;
        return true;
    }

    private void cancelReconnect() {
        handler.removeCallbacks(reconnectRunnable);
        reconnectCount = 0;
    }

    private void connected() {
        cancelReconnect();
    }

    private void disconnect() {
        if (currentStatus == Status.DISCONNECTED) {
            return;
        }
        cancelReconnect();
        if (httpClient != null) {
            httpClient.dispatcher().cancelAll();
        }
        if (socket != null) {
            boolean closed = socket.close(Status.CODE.NORMAL_CLOSE, Status.TIP.NORMAL_CLOSE);
            if (!closed) {
                if (statusListener != null) {
                    statusListener.onClosed(Status.CODE.ABNORMAL_CLOSE, Status.TIP.ABNORMAL_CLOSE);
                }
            }
        }
        setCurrentStatus(Status.DISCONNECTED);
    }

    private synchronized void buildConnect() {
        if (!isNetworkAvailable(context)) {
            setCurrentStatus(Status.DISCONNECTED);
            statusListener.onFailure(new ConnectException(), "Network available");
            return;
        }
        switch (getCurrentStatus()) {
            case Status.CONNECTED:
            case Status.CONNECTING:
                break;
            default:
                setCurrentStatus(Status.CONNECTING);
                initWebSocket();
        }
    }

    //发送消息
    @Override
    public boolean sendMessage(String msg) {
        return send(msg);
    }

    @Override
    public boolean sendMessage(byte[] data) {
        return send(ByteString.of(data));
    }

    private boolean send(Object msg) {
        boolean sent = false;
        if (socket != null && currentStatus == Status.CONNECTED) {
            if (msg instanceof String) {
                sent = socket.send((String) msg);
            } else if (msg instanceof ByteString) {
                sent = socket.send((ByteString) msg);
            }
            if (!sent) {
                statusListener.onClosing(-1, "");
                tryReconnect();
            }
        }
        return sent;
    }

    private boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static final class Builder {

        private Context mContext;
        private String url;
        private Handler handler;
        private int mReconnectInterval = 0;
        private int mReconnectMaxTime = 0;

        public Builder(Configuration configuration) {
            url = "ws://" + configuration.getServerAddress() + ":" + configuration.getServerPort();
            handler = configuration.getMessageHandler();
            mReconnectInterval = configuration.getReconnectInterval();
            mReconnectMaxTime = configuration.getReconnectMaxTime();
            mContext = configuration.getContext();
        }

        public WebSocketConnection build() {
            return new WebSocketConnection(this);
        }
    }
}
