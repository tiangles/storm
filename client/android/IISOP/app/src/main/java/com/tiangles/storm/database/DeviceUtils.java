package com.tiangles.storm.database;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceUtils {

    public static JSONObject convertDeviceToJSON(StormDevice device) throws JSONException{
        JSONObject jObj = new JSONObject();
        return jObj;
    }

    public static StormDevice converJSONToDevice(JSONObject object) throws JSONException{
        StormDevice device = new StormDevice();
        return device;
    }
}
