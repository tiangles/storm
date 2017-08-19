package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncDeviceRequest extends Request{
    private static String COMMAND = "sync_device";

    private String deviceCode;
    private String workshop;
    public SyncDeviceRequest(String deviceCode, String workshop){
        this.deviceCode = deviceCode;
        this.workshop = workshop;
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
        jObj.put("workshop", workshop);
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
        StormDevice device = new StormDevice();
        int resCode = -1;
        try {
            device.setCode(jObj.getString("code"));
            device.setName(jObj.getString("name"));
            device.setModel(jObj.getString("model"));
            device.setSystem(jObj.getString("system"));


            resCode = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StormApp.getDBManager().onSyncDeviceDone(device, deviceCode, resCode);
    }

}
