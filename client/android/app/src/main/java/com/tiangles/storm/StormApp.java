package com.tiangles.storm;

import android.content.Context;
import android.os.Handler;

import com.tiangles.storm.network.Configuration;
import com.tiangles.storm.network.Delegate;
import com.tiangles.storm.network.Network;
import com.tiangles.storm.network.Response;
import com.tiangles.storm.user.User;
import com.uuzuche.lib_zxing.ZApplication;

public class StormApp extends ZApplication {

    private static StormApp instance;
    private static Handler handler;
    private static Network network;
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        user = new User();
        handler = new Handler();
        network = createNetwork();
    }

    public static StormApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static void runMainThreadTask(Runnable runnable) {
        handler.post(runnable);
    }

    public static Network getNetwork() {
        return network;
    }

    public static User getCurrentUser() {
        return user;
    }

    public static void setCurrentUser(User user) {
        StormApp.user = user;
    }

    private static Network createNetwork(){
        return new Network(new Configuration() {
            @Override
            public Context getContext() {
                return StormApp.getInstance().getContext();
            }

            @Override
            public String getServerAddress() {
                return "192.168.3.11";
            }

            @Override
            public int getServerPort() {
                return 8080;
            }

            @Override
            public int getConnectionType() {
                return Configuration.WEB_SOCKET_CONNECTION;
            }

            @Override
            public int getReconnectInterval() {
                return 1000*3;  // 3 seconds
            }

            @Override
            public int getReconnectMaxTime() {
                return 4;       // try to connect to server 5 times (1 connect + 4 retry)
            }

            @Override
            public Handler getMessageHandler() {
                return handler;
            }

            @Override
            public Delegate getDelegate() {
                return new Delegate() {
                    @Override
                    public void onOpen(String msg) {
                    }

                    @Override
                    public void onClosed(int code, String reason) {
                    }

                    @Override
                    public void onFailure(Throwable t, String msg) {
                    }

                    @Override
                    public Response createResponse(byte[] data) {
                        return createResponse(new String(data));
                    }

                    @Override
                    public Response createResponse(String data) {
                        return new SResponse(data);
                    }
                };
            }
        });
    }
}
