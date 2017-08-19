package com.tiangles.storm.analytics.event;

import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadRequest extends Request {
    private static String COMMAND = "upload_event";
    private Event mEvent;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data()  throws JSONException{
        JSONObject jObj = new JSONObject();
        jObj.put("cmd", "upload_event");
        jObj.put("type", mEvent.tag());
        jObj.put("event", mEvent.toString());
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        return res.command().equals(COMMAND);
    }

    @Override
    public void onError(Exception e) {
        ///TODO
    }

    public UploadRequest(Event e) {
        mEvent = e;
    }
}
