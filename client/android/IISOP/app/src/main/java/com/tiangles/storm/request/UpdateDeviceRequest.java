package com.tiangles.storm.request;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.device.DeviceUtils;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateDeviceRequest extends Request {
    StormDevice mStormDevice;
    public UpdateDeviceRequest(StormDevice device) {
        mStormDevice = device;
    }

    @Override
    public byte[] data() {
        try {
            JSONObject jObj = new JSONObject();
            jObj.put("cmd", "update_device");
            jObj.put("device", DeviceUtils.convertDeviceToJSON(mStormDevice));
            return jObj.toString().getBytes();
        } catch (JSONException e) {
            StormApp.getDeviceManager().onUpdateDeviceDone(mStormDevice.getCode(), -1);
        }
        return null;
    }

    @Override
    public boolean handleResponse(Response res) {
        StormApp.getDeviceManager().onUpdateDeviceDone(mStormDevice.getCode(), 0);
        return true;
    }

    @Override
    public void onError(Exception e) {
        StormApp.getDeviceManager().onUpdateDeviceDone(mStormDevice.getCode(), -1);
    }

}
