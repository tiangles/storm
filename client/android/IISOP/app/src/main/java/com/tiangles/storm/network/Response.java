package com.tiangles.storm.network;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {
    private String cmd;
    JSONObject jObj;

    public Response(String str) {
        try {
            jObj = new JSONObject(str);
            cmd = jObj.getString("cmd");
        } catch (JSONException e) {
            e.printStackTrace();
            cmd = "";
        }
    }

    public JSONObject object() {
        return jObj;
    }
    public String command() {
        return cmd;
    }

}
