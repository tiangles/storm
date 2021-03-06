package com.tiangles.storm;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.tiangles.storm.activities.MainActivity;
import com.tiangles.storm.database.DBManager;
import com.tiangles.storm.network.Configuration;
import com.tiangles.storm.network.Delegate;
import com.tiangles.storm.network.Network;
import com.tiangles.storm.network.Response;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.user.User;
import com.tiangles.storm.utilities.PermissionsHelper;
import com.uuzuche.lib_zxing.ZApplication;

public class StormApp extends ZApplication {

    private static StormApp instance;
    private static Handler handler;
    private static Network network;
    private static User user;
    private static DBManager dbManager;
    private static MainActivity mainActivity;
    private PermissionsHelper permissionsHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        permissionsHelper = new PermissionsHelper(this);

        user = new User();
        handler = new Handler();
    }

    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    public static StormApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static PermissionsHelper getPermissionsHelper(){
        return instance.permissionsHelper;
    }

    public static void runMainThreadTask(Runnable runnable) {
        handler.post(runnable);
    }

    public static Network getNetwork() {
        if(network == null) {
            network = createNetwork();
        }
        return network;
    }

    public static void closeNetwork() {
        if(network != null) {
            network.close();
        }
        network = null;
    }

    public static DBManager getDBManager(){
        if(dbManager == null) {
            dbManager = new DBManager(mainActivity);
        }
        return dbManager;
    }

    public static User getCurrentUser() {
        return user;
    }

    public static void setCurrentUser(User user) {
        StormApp.user = user;
    }


    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private static Network createNetwork(){
        return new Network(new Configuration() {
            @Override
            public Context getContext() {
                return StormApp.getInstance().getContext();
            }

            @Override
            public String getServerAddress() {
                return PreferenceEngine.getInstance().getServerAddress();
            }

            @Override
            public int getServerPort() {
                return PreferenceEngine.getInstance().getServerPort();
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
                    public void onNetworkOpen(String msg) {
                    }

                    @Override
                    public void onNetworkClosed(int code, String reason) {
                    }

                    @Override
                    public void onFailure(Throwable t, String msg) {
                        network = null;
                    }

                    @Override
                    public Response createResponse(byte[] data) {
                        return createResponse(new String(data));
                    }

                    @Override
                    public Response createResponse(String data) {
                        return new Response(data);
                    }
                };
            }
        });
    }
}
