package com.tiangles.storm.network;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Request {
    Response createResponse();
    boolean handleResponse(Response response);

    void onStartTransfer();
    void onError(IOException e);

    void write(DataOutputStream output) throws IOException;
}
