package com.tiangles.storm.user;

import com.tiangles.storm.StormResponse;
import com.tiangles.storm.debug.Logger;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginRequest implements Request {
    private User user;
    private User.LoginListener listener;
    public LoginRequest(User user, User.LoginListener listener) {
        this.user = user;
        this.listener = listener;
    }

    @Override
    public void write(DataOutputStream output) {
        StringBuilder sb = new StringBuilder();
        sb.append("A55AAA55{")
                .append("\"user_name\":").append(user.mUserName).append(",")
                .append("\"password\":").append(user.mPassword)
                .append("}");
        byte[] data = sb.toString().getBytes();
        try {
            output.write(data);
        } catch (IOException e) {
            Logger.log(e.toString());
            user.mAuthSucceeded = false;
            listener.onLoginDone();
        }
    }

    @Override
    public Response createResponse() {
        return new StormResponse();
    }

    @Override
    public boolean handleResponse(Response response) {
        String str = new String(response.data());
        Logger.log(str);
        if(str.length()>0) {
            user.mAuthSucceeded = true;
            listener.onLoginDone();
            return true;
        }

        return false;
    }

    @Override
    public void onStartTransfer() {
        listener.onBeginLogin();
    }

    @Override
    public void onError(IOException e) {

    }

    private void handleLoginResult(String str) {
        if(str.length()>0) {
            user.mAuthSucceeded = true;
        }
        listener.onLoginDone();
    }
}
