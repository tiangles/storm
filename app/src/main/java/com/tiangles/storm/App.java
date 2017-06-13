package com.tiangles.storm;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.uuzuche.lib_zxing.ZApplication;

public class App extends ZApplication {

    private static App instance;
    private static Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

}
