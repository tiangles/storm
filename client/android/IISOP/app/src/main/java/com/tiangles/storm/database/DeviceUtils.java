package com.tiangles.storm.database;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceUtils {

    public static JSONObject convertDeviceToJSON(StormDevice device) throws JSONException{
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
        jObj.put("workshop", device.getWorkShop().getCode());
        return jObj;
    }

    public static StormDevice converJSONToDevice(JSONObject object) throws JSONException{
        StormDevice device = new StormDevice();
        device.setCode(object.getString("code"));
        device.setName(object.getString("name"));
        device.setModel(object.getString("model"));
        device.setSystem(object.getString("system"));
        device.setDistributionCabinet(object.getString("distribution_cabinet"));
        device.setLocalControlPanel(object.getString("local_control_panel"));
        device.setDcsCabinet(object.getString("dcs_cabinet"));
        device.setForwardDevice(object.getString("forward_device"));
        device.setBackwardDevice(object.getString("backward_device"));
        device.setLegend(object.getString("legend"));
        device.setWorkShop(StormApp.getDBManager().getWorkshop(object.getString("workshop")));
        return device;
    }
}
