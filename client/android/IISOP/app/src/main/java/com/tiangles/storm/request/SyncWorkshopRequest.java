package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncWorkshopRequest extends Request {
    private static  String COMMAND = "sync_workshop";

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException{
        JSONObject jObj = new JSONObject();
        jObj.put("cmd", COMMAND);
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        return false;
    }

    @Override
    public void onError(Exception e) {

    }
}
