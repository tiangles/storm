package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.UserEvent;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UploadUserEventRequest extends Request {
    private static String COMMAND = "upload_user_events";
    private List<UserEvent> events;

    public UploadUserEventRequest(List<UserEvent> events){
        this.events = events;
    }

    public UploadUserEventRequest(UserEvent event){
        this.events = new ArrayList<>();
        this.events.add(event);
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException {
        JSONObject jObj = new JSONObject();
        jObj.put("cmd", COMMAND);
        JSONArray jArr = new JSONArray();
        for(UserEvent event: events) {
            jArr.put(toJson(event));
        }
        jObj.put("events", jArr);
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        try {
            JSONObject object = res.object();
            int result = object.getInt("result");
            if(result == 0) {
                JSONArray array = object.getJSONArray("message");
                for(int i=0; i<array.length(); ++i){
                    JSONObject eventObj = (JSONObject)array.get(i);
                    long eventID = eventObj.getLong("event_id");
                    int status = eventObj.getInt("event_status");

                    UserEvent event = StormApp.getDBManager().getUserDB().getUserEvent(eventID);
                    if(event != null) {
                        event.setStatus(status);
                        StormApp.getDBManager().getUserDB().commitUserEventChanges(event);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onError(Exception e) {

    }

    private JSONObject toJson(UserEvent event) throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("event_id", event.getId());
        obj.put("date", event.getDate().getTime());
        obj.put("event", event.getEvent());
        obj.put("device_code", event.getDevice_code());
        obj.put("user_id", event.getUser_id());

        return obj;
    }
}
