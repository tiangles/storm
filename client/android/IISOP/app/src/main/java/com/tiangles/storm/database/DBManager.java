package com.tiangles.storm.database;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.activities.MainActivity;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.request.SyncDatabaseVersionRequest;
import com.tiangles.storm.utilities.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import okhttp3.OkHttpClient;

public class DBManager {
    private static String LOG_TAG = "DeviceManager";
    private StormDB stormDB;
    private String currentVersion = "1.0";
    private Dialog waitingDialog;
    private MainActivity activity;
    private boolean ready = false;
    public DBManager(MainActivity activity){
        this.activity = activity;
        currentVersion = PreferenceEngine.getInstance().getCurrentDatabaseVersion();
        waitingDialog = DialogUtils.createLoadingDialog(activity, "正在更新数据库，请稍候...");
        StormApp.getNetwork().sendRequest(new SyncDatabaseVersionRequest(currentVersion));
    }

    public StormDB getStormDB(){
        return stormDB;
    }

    public void onSyncDatabaseDone(String version, String url){
        String dbPath = databaseFilePath();
        File dbFile = new File(dbPath);
        if(!version.equals(currentVersion) || !dbFile.exists()) {
            runHttpRequest(version, url);
        } else {
            createDababaseSession(dbPath);
        }
    }

    public boolean ready(){
        return ready;
    }

    private String databaseFilePath( ) {
        File dir = StormApp.getContext().getFilesDir();
//        File dir =  Environment.getExternalStorageDirectory();
        return dir.getAbsolutePath() + "/storm.db";
    }

    private void runHttpRequest(final String version, final String url) {
        final Thread t = new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String dbFile = databaseFilePath();
                        Log.e("database", "store database file to "+ dbFile);
                        byte[] buf = new byte[2048];
                        int len = 0;

                        InputStream is = response.body().byteStream();
                        File file = new File(dbFile);
                        if(file.exists()) {
                            file.delete();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        currentVersion = version;
                        PreferenceEngine.getInstance().setCurrentDatabaseVersion(currentVersion);
                        createDababaseSession(dbFile);
                    }
                } catch (IOException e) {
                    ///TODO: handle network error
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void createDababaseSession(String path){
        stormDB = new StormDB(StormApp.getContext(), path);
        DialogUtils.closeDialog(waitingDialog);
        ready = true;
        activity.onDababaseReady();
    }


}
