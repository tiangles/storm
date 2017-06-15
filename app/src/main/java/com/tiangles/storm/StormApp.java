package com.tiangles.storm;

import android.content.Context;
import android.os.Handler;

import com.uuzuche.lib_zxing.ZApplication;

public class StormApp extends ZApplication {

    private static StormApp instance;
    private static Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static StormApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
