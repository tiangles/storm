package com.tiangles.storm.analytics;

import com.tiangles.storm.R;
import com.tiangles.storm.SResponse;
import com.tiangles.storm.analytics.event.Event;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadRequest extends Request {
    private String COMMAND = "upload_event";
    private Event mEvent;
    @Override
    public byte[] data() {
        try {
            JSONObject jObj = new JSONObject();
            jObj.put("cmd", "upload_event");
            jObj.put("type", mEvent.tag());
            jObj.put("event", mEvent.toString());
            return jObj.toString().getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean handleResponse(Response res) {
        SResponse sRes = (SResponse)res;
        return sRes.getCmd().equals(COMMAND);
    }

    @Override
    public void onError(Exception e) {
        ///TODO
    }

    public UploadRequest(Event e) {
        mEvent = e;
    }
}
