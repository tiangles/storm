package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;


public class SyncWorkshopListRequest extends Request {
    private static String COMMAND = "sync_workshop_list";
    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", COMMAND);
        return jsonObject;
    }

    @Override
    public boolean handleResponse(Response res) {
        Vector<StormWorkshop> workshops = new Vector<>();
        try {
            JSONObject jsonObject = res.object();
            JSONArray jWorkshops = jsonObject.getJSONArray("message");
            for(int i=0; i<jWorkshops.length(); ++i){
                JSONObject jWorkshop = (JSONObject)jWorkshops.get(i);
                StormWorkshop workshop = new StormWorkshop(jWorkshop.getString("code"),
                        jWorkshop.getInt("workshop_index"),
                        jWorkshop.getString("name"));
                workshops.add(workshop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StormApp.getDBManager().onSyncWorkshopListDone(workshops);

        return true;
    }

    @Override
    public void onError(Exception e) {

    }
}
