package com.tiangles.storm;

import com.tiangles.storm.debug.Logger;
import com.tiangles.storm.network.Response;

import java.io.DataInputStream;
import java.io.IOException;

public class StormResponse implements Response {
    private String data;
//
//    public void read(DataInputStream input) {
//        try {
//            while(true) {
//                if(!readHeader(input)){
//                    break;
//                }
//                int len = readBodyLen(input);
//                if(len <= 0) {
//                    break;
//                }
//                data = readBody(input, len);
//            }
//        } catch (IOException e) {
//            Logger.log(e.toString());
//        }
//    }
//
//    private boolean readHeader(DataInputStream input) throws IOException{
//        int h = input.readInt();
//        return h == 0XA55AAA55;
//    }
//
//    private int readBodyLen(DataInputStream input) throws IOException {
//        int len = input.readInt();
//        return len;
//    }
//
//    private String readBody(DataInputStream input, int len) throws IOException {
//        byte[] b = new byte[len];
//        input.readFully(b);
//        return b.toString();
//    }

    public StormResponse(byte[] data) {
        this.data = new String(data);
    }
    @Override
    public String data() {
        return data;
    }
}
