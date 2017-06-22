package com.tiangles.storm.network.connection;

import java.io.DataInputStream;
import java.io.IOException;

public interface Response {
    void read(DataInputStream input) throws IOException;
}
