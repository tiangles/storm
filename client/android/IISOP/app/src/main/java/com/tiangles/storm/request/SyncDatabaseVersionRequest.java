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

public class SyncDatabaseVersionRequest extends Request {
    private static String COMMAND = "sync_database";
    String currentVersion;

    public SyncDatabaseVersionRequest(String currentVersion){
        this.currentVersion = currentVersion;
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
        jObj.put("db_version", currentVersion);
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        try{
            JSONObject jObj = res.object().getJSONObject("message");
            String url = jObj.getString("url");
            String version = jObj.getString("db_version");
            StormApp.getDBManager().onSyncDatabaseDone(version, url);
        } catch (JSONException e) {
            e.printStackTrace();
            StormApp.getDBManager().onSyncDatabaseDone(null, null);
        }
        return true;
    }

    @Override
    public void onError(Exception e) {

    }


}
