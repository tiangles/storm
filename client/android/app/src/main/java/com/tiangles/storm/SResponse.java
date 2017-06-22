package com.tiangles.storm;

import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by btian on 6/22/2017.
 */

public class SResponse implements Response {
    private String cmd;
    JSONObject jObj;

    public SResponse(String str) {
        try {
            jObj = new JSONObject(str);
            cmd = jObj.getString("cmd");
        } catch (JSONException e) {
            e.printStackTrace();
            cmd = "";
        }
    }

    public JSONObject getjObj() {
        return jObj;
    }
    public String getCmd() {
        return cmd;
    }
}
