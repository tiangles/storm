package com.tiangles.storm.network;

public interface Delegate {
    void onOpen(String msg);
    void onClosed(int code, String reason);
    void onFailure(Throwable t, String msg);

    Response createResponse(byte[] data);
    Response createResponse(String data);
}
