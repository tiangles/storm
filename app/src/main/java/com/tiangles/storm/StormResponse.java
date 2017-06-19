package com.tiangles.storm;

import com.tiangles.storm.debug.Logger;
import com.tiangles.storm.network.Response;

import java.io.DataInputStream;
import java.io.IOException;

public class StormResponse implements Response {
    private byte[] data;

    public StormResponse() {
    }

    @Override
    public void read(DataInputStream input)  throws IOException{
        if(readHeader(input)){
           readBody(input, readBodyLen(input));
        }
    }

    @Override
    public boolean finished() {
        return true;
    }

    @Override
    public byte[] data() {
        return data;
    }

    private boolean readHeader(DataInputStream input) throws IOException{
        int h = input.readInt();
        return h == 0XA55AAA55;
    }

    private int readBodyLen(DataInputStream input) throws IOException {
        int len = input.readInt();
        return len;
    }

    private void readBody(DataInputStream input, int len) throws IOException {
        if(len>0) {
            data = new byte[len];
            input.readFully(data);
        }
    }
}
