package com.tiangles.storm.request;

import com.tiangles.storm.SResponse;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncDeviceRequest extends Request{
    private static String COMMAND = "sync_device";

    String deviceCode;
    public SyncDeviceRequest(String deviceCode){
        this.deviceCode = deviceCode;
    }

    @Override
    public byte[] data() {
        try {
            JSONObject jObj = new JSONObject();
            jObj.put("cmd", COMMAND);
            jObj.put("device_code", deviceCode);
            return jObj.toString().getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
            StormApp.getDeviceManager().onSyncDeviceDone(null, deviceCode, -1);
        }
        return null;
    }

    @Override
    public boolean handleResponse(Response res) {
        SResponse sRes = (SResponse)res;
        if(sRes.getCmd().equals(COMMAND)) {
            handleSyncResult(sRes.getjObj());
            return true;
        }
        return false;
    }

    @Override
    public void onError(Exception e) {
        StormApp.getDeviceManager().onSyncDeviceDone(null, deviceCode, -1);
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

        StormApp.getDeviceManager().onSyncDeviceDone(device, deviceCode, resCode);
    }

}
