package com.tiangles.storm.network;

public interface Request {
    byte[] data();
    boolean handleResponse(Response res);
}
