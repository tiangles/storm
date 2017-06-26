package com.tiangles.storm.network;

public interface Delegate {
    void onNetworkOpen(String msg);
    void onNetworkClosed(int code, String reason);
    void onFailure(Throwable t, String msg);

    Response createResponse(byte[] data);
    Response createResponse(String data);
}
