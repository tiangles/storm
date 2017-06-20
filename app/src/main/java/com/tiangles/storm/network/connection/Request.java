package com.tiangles.storm.network.connection;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Request {
    void onStartTransfer();
    void onEndTransfer();
    void onError(IOException e);

    void write(DataOutputStream output) throws IOException;
}
