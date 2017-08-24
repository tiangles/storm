package com.tiangles.storm.request;

import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class GetSignalParameterRecordRequest extends Request{
    private static String COMMAND = "get_signal_parameter_record";
    private String deviceCode;
    private OnSignalParameterRecordListener listener;

    public interface OnSignalParameterRecordListener{
        void onRecord(String deviceCode, String value, String time);
    }

    public GetSignalParameterRecordRequest(String deviceCode, OnSignalParameterRecordListener listener){
        this.deviceCode = deviceCode;
        this.listener = listener;
    }
    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data() throws JSONException {
        JSONObject jObject = new JSONObject();
        jObject.put("device_code", deviceCode);
        jObject.put("time", "now");
        jObject.put("cmd", COMMAND);
        return jObject;
    }

    @Override
    public boolean handleResponse(Response res) {
        try {
            JSONObject jObj = res.object().getJSONObject("message");
            String code = jObj.getString("device_code");
            if(code.equals(deviceCode)) {
                String value = jObj.getString("value");
                String time = jObj.getString("time");
                if(listener != null) {
                    listener.onRecord(code, value, time);
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onError(Exception e) {

    }
}
