package com.tiangles.storm.network.connection;

import android.os.Handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.tiangles.storm.debug.Logger;

public class TCPConnection extends Connection{
    private Socket socket;
    private ConnectionConfig config;

    public TCPConnection(ConnectionConfig config, Delegate delegate){
        super(config, delegate);
        this.config = config;
    }

    @Override
    public void connect() throws IOException{
        socket = new Socket(config.getServerAddress(), config.getServerPort());
        int soTimeout = config.getSoTimeout();
        if(soTimeout > 0 && soTimeout != socket.getSoTimeout()){
            socket.setSoTimeout(config.getSoTimeout());
        }
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            Logger.log(e.toString());
        }
    }

    public DataInputStream getInputStream() throws IOException{
        return new DataInputStream(socket.getInputStream());
    }

    public DataOutputStream getOutputStream() throws IOException{
        return new DataOutputStream(socket.getOutputStream());
    }
}
