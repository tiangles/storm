package com.tiangles.storm.network;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Request {
    void write(DataOutputStream output) throws IOException;
    boolean handleResponse(Response response);
}
