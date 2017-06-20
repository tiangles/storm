package com.tiangles.storm.network;

import com.tiangles.storm.network.connection.Request;

public abstract  class JRequest implements Request {
    public abstract String command();
    public abstract void handleResponse(JResponse response);
}
