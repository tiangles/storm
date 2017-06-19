package com.tiangles.storm;

import android.content.Context;
import android.os.Handler;

import com.tiangles.storm.network.Response;
import com.tiangles.storm.network.ResponseFactory;
import com.tiangles.storm.network.TCPNetwork;
import com.tiangles.storm.network.connection.ConnectionConfig;
import com.tiangles.storm.user.User;
import com.uuzuche.lib_zxing.ZApplication;

public class StormApp extends ZApplication {

    private static StormApp instance;
    private static Handler handler;
    private static TCPNetwork network;
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();

        user = new User();
        handler = new Handler();
        network = createNetwork();

        instance = this;
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

    public static TCPNetwork getNetwork() {
        return network;
    }

    public static User getCurrentUser() {
        return user;
    }

    public static void setCurrentUser(User user) {
        StormApp.user = user;
    }

    private static TCPNetwork createNetwork(){
        return new TCPNetwork(new ConnectionConfig() {
            @Override
            public String getServerAddress() {
                return "192.168.84.29";
            }

            @Override
            public int getServerPort() {
                return 8080;
            }

            @Override
            public int getConnectionType() {
                return ConnectionConfig.TCP_CONNECTION;
            }

            @Override
            public int getSoTimeout() {
                return 3000;
            }

            @Override
            public ResponseFactory responseFactory() {
                return new ResponseFactory() {
                    @Override
                    public Response createResponse(byte[] data){
                        return new StormResponse(data);
                    }
                } ;
            }
        }, new StormResponseFactory());
    }
}
