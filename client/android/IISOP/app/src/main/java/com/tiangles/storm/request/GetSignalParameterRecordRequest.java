package com.tiangles.storm.request;

import android.util.Log;

import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetSignalParameterRecordRequest extends Request{
    private static String COMMAND = "get_signal_parameter_record";
    String deviceCode;
    List<DCSConnection> connections;
    private OnSignalParameterRecordListener listener;

    public interface OnSignalParameterRecordListener{
        void onRecord(String deviceCode, Map<String, Double> values, String time);
    }

    public GetSignalParameterRecordRequest(String deviceCode, List<DCSConnection> connections, OnSignalParameterRecordListener listener){
        this.deviceCode = deviceCode;
        this.connections = connections;
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

        JSONArray jConnArray = new JSONArray();
        for(DCSConnection conn: connections){
            jConnArray.put(conn.getCode());
        }
        jObject.put("connections", jConnArray);
        jObject.put("time", "now");
        jObject.put("cmd", COMMAND);
        return jObject;
    }

    @Override
    public boolean handleResponse(Response res) {
        try {
            JSONObject jObj = res.object().getJSONObject("message");
            String code = jObj.getString("device_code");
            if(code.equals(deviceCode) && listener != null) {
                String time = jObj.getString("time");
                JSONObject jValues = jObj.getJSONObject("values");
                Iterator it = jValues.keys();
                Map<String, Double> values = new HashMap<>();
                while (it.hasNext()){
                    String key = (String) it.next();
                    double value = jValues.getDouble(key);
                    values.put(key, value);
                }
                listener.onRecord(code, values, time);
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
