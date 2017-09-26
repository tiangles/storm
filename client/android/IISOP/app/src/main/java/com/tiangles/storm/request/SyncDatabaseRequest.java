package com.tiangles.storm.request;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.DBManager;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;

public class SyncDatabaseRequest extends Request {
    private static String COMMAND = "sync_database";

    public SyncDatabaseRequest(){
    }
    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException {
        JSONObject jObj = new JSONObject();
        jObj.put("cmd", COMMAND);
        jObj.put("build_version", "1.0");
        jObj.put("database_version", "20070719");
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        try{
            JSONObject jObj = res.object().getJSONObject("message");
            String url = jObj.getString("url");
            runHttpRequest(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onError(Exception e) {

    }

    void runHttpRequest(final String url) {
        final Thread t = new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String dbFile = createDatabaseFile();
                        Log.e("database", "store database file to "+ dbFile);
                        byte[] buf = new byte[2048];
                        int len = 0;

                        InputStream is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(dbFile);
                        if(file.exists()) {
                            file.delete();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        StormApp.getDBManager().onSyncDatabaseDone(dbFile);
                    }
                } catch (IOException e) {
                    ///TODO: handle network error
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public String createDatabaseFile( ) {
//        File dir = StormApp.getContext().getFilesDir();
        File dir =  Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath() + "/storm.db";
        return path;
    }

}
