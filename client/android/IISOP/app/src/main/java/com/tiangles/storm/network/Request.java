package com.tiangles.storm.network;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Request {
    private int retryTimes = 0;
    public abstract String command();
    public abstract JSONObject data() throws JSONException;
    public abstract boolean handleResponse(Response res);
    public abstract void onError(Exception e);

    void retry() {
        ++retryTimes;
    }

    int getRetryTimes () {
        return retryTimes;
    }

}
