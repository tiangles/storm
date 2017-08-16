package com.tiangles.storm.request;

import com.tiangles.storm.R;
import com.tiangles.storm.database.dao.StormDevice;
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
            JSONObject jDev = convertDeviceToJson(mStormDevice);
            jObj.put("device", jDev);
            return jObj.toString().getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean handleResponse(Response res) {
        return false;
    }

    @Override
    public void onError(Exception e) {

    }

    private JSONObject convertDeviceToJson(StormDevice device) throws JSONException{
        JSONObject jObj = new JSONObject();
        jObj.put("code", device.getCode());
        jObj.put("name", device.getName());
        jObj.put("model", device.getModel());
        jObj.put("system", device.getSystem());
        jObj.put("distribution_cabinet", device.getDistributionCabinet());
        jObj.put("local_control_panel", device.getLocalControlPanel());
        jObj.put("dcs_cabinet", device.getDcsCabinet());
        jObj.put("forward_device", device.getForwardDevice());
        jObj.put("backward_device", device.getBackwardDevice());
        jObj.put("legend", device.getLegend());

        return jObj;
    }
}
