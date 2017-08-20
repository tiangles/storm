package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.DeviceUtils;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncDeviceRequest extends Request{
    private static String COMMAND = "sync_device";

    private String deviceCode;
    public SyncDeviceRequest(String deviceCode){
        this.deviceCode = deviceCode;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException{
        JSONObject jObj = new JSONObject();
        jObj.put("cmd", COMMAND);
        jObj.put("device_code", deviceCode);
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        if(res.command().equals(COMMAND)) {
            handleSyncResult(res.object());
            return true;
        }
        return false;
    }

    @Override
    public void onError(Exception e) {
        StormApp.getDBManager().onSyncDeviceDone(null, deviceCode, -1);
    }

    private void handleSyncResult(JSONObject jObj){
        StormDevice device = null;
        int resCode = -1;
        try {
            device = DeviceUtils.converJSONToDevice(jObj.getJSONObject("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StormApp.getDBManager().onSyncDeviceDone(device, deviceCode, resCode);
    }

}
