package com.tiangles.storm.network;

public abstract class Request {
    private int retryTimes = 0;
    public abstract byte[] data();
    public abstract boolean handleResponse(Response res);
    public abstract void onError(Exception e);

    void retry() {
        ++retryTimes;
    }

    int getRetryTimes () {
        return retryTimes;
    }

}
