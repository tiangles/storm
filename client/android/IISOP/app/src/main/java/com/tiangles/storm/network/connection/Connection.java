package com.tiangles.storm.network.connection;

public interface Connection {
    interface StatusListener {
        void onOpen(String msg);
        void onMessage(String text);
        void onMessage(byte[] data);
        void onReconnect();
        void onClosing(int code, String reason);
        void onClosed(int code, String reason);
        void onFailure(Throwable t, String msg);
    }

    class Status {
        public final static int DISCONNECTED = -1;
        public final static int CONNECTING = 0;
        public final static int CONNECTED = 1;
        public final static int RECONNECT = 2;

        class CODE {
            public final static int NORMAL_CLOSE = 1000;
            public final static int ABNORMAL_CLOSE = 1001;
        }

        class TIP {
            public final static String NORMAL_CLOSE = "normal close";
            public final static String ABNORMAL_CLOSE = "abnormal close";
        }
    }

    void startConnect();
    void stopConnect();
    boolean isConnected();
    int getCurrentStatus();
    void setCurrentStatus(int currentStatus);
    boolean sendMessage(String msg);
    boolean sendMessage(byte[] data);
    void setStatusListener(StatusListener statusListener);
}
