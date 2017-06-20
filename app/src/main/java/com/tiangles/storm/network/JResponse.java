package com.tiangles.storm.network;

import com.tiangles.storm.network.connection.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;

public class JResponse implements Response {
    private JSONObject data;
    private int serverCode = -1;
    private String cmd = "";

    public JResponse() {
    }

    public JSONObject data() {
        return data;
    }

    public int serverCode() {
        return serverCode;
    }

    public String command() {
        return cmd;
    }

    @Override
    public void read(DataInputStream input)  throws IOException{
        if(readHeader(input)){
           readBody(input, readBodyLen(input));
        }
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
            byte[] buff = new byte[len];
            input.readFully(buff);

            try {
                data = new JSONObject(new String(buff));
                serverCode = data.getInt("code");
                cmd = data.getString("cmd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
