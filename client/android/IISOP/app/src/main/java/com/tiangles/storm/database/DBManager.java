package com.tiangles.storm.database;

import android.app.Dialog;
import android.util.Log;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.activities.MainActivity;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.request.SyncDatabaseVersionRequest;
import com.tiangles.storm.utilities.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;

public class DBManager {
    private static String LOG_TAG = "DeviceManager";
    private StormDB stormDB;
    private UserDB userDB;
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

    public UserDB getUserDB(){
        return userDB;
    }

    public void onSyncDatabaseDone(String version, String url){
        if(version != null && url != null) {
            String dbPath = stormDBFilePath();
            File dbFile = new File(dbPath);
            if(!version.equals(currentVersion) || !dbFile.exists()) {
                runHttpRequest(version, url);
            } else {
                createDababaseSession(dbPath, userDBFilePath());
            }
        } else {
            activity.onDatabaseFailed();
        }
        DialogUtils.closeDialog(waitingDialog);
    }

    public boolean ready(){
        return ready;
    }

    private String stormDBFilePath( ) {
        File dir = StormApp.getContext().getFilesDir();
//        File dir =  Environment.getExternalStorageDirectory();
        return dir.getAbsolutePath() + "/storm.db";
    }

    private String userDBFilePath() {
        File dir = StormApp.getContext().getFilesDir();
//        File dir =  Environment.getExternalStorageDirectory();
        return dir.getAbsolutePath() + "/user.db";
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
                        String dbFile = stormDBFilePath();
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
                        createDababaseSession(dbFile, userDBFilePath());
                    }
                } catch (IOException e) {
                    ///TODO: handle network error
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void createDababaseSession(String stormPath, String userPath){
        stormDB = new StormDB(StormApp.getContext(), stormPath);
        userDB = new UserDB(StormApp.getContext(), userPath);
        ready = true;
        activity.onDababaseReady();
    }


}
