package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncWorkshopRequest extends Request {
    private static  String COMMAND = "sync_workshop";
    private String code;

    public SyncWorkshopRequest(String code){
        this.code = code;
    }
    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException{
        JSONObject jObj = new JSONObject();
        jObj.put("cmd", COMMAND);
        jObj.put("workshop_code", code);
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        try {
            JSONObject jWorkshop = res.object().getJSONObject("message");
            StormWorkshop workshop = new StormWorkshop(jWorkshop.getString("code"),
                    jWorkshop.getInt("workshop_index"),
                    jWorkshop.getString("name"));
            StormApp.getDBManager().onSyncWorkshopDone(workshop);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onError(Exception e) {

    }
}
