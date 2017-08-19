package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.DeviceUtils;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateDeviceRequest extends Request {
    private static String COMMAND = "update_device";
    private StormDevice mStormDevice;

    public UpdateDeviceRequest(StormDevice device) {
        mStormDevice = device;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException {
        JSONObject jObj = new JSONObject();
        jObj.put("cmd", COMMAND);
        jObj.put("device", DeviceUtils.convertDeviceToJSON(mStormDevice));
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        StormApp.getDBManager().onUpdateDeviceDone(mStormDevice.getCode(), 0);
        return true;
    }

    @Override
    public void onError(Exception e) {
        StormApp.getDBManager().onUpdateDeviceDone(mStormDevice.getCode(), -1);
    }

}
