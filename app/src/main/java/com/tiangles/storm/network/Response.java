package com.tiangles.storm.network;

import java.io.DataInputStream;
import java.io.IOException;

public interface Response {
    public byte[] data();
    public void read(DataInputStream input) throws IOException;
    public boolean finished();
}
