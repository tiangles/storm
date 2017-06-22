package com.tiangles.storm.user;

import com.tiangles.storm.network.JRequest;
import com.tiangles.storm.network.JResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginRequest extends JRequest {
    private User user;
    private User.LoginListener listener;
    public LoginRequest(User user, User.LoginListener listener) {
        this.user = user;
        this.listener = listener;
    }

    @Override
    public void write(DataOutputStream output) throws IOException{
        try {
            JSONObject jObj = new JSONObject();
            jObj.put("user_name", user.mUserName);
            jObj.put("password", user.mPassword);
            byte[] data = jObj.toString().getBytes();
            output.write(data);
        } catch (JSONException e) {
            user.mAuthSucceeded = false;
            listener.onLoginDone();
            e.printStackTrace();
        }
    }

    @Override
    public String command() {
        return "login";
    }

    @Override
    public void handleResponse(JResponse response) {
        try {
            String result = response.data().getString("result");
            handleLoginResult(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartTransfer() {
        listener.onBeginLogin();
    }

    @Override
    public void onEndTransfer() {
        //do nothing
    }

    @Override
    public void onError(IOException e) {
        handleLoginResult(null);
    }

    private void handleLoginResult(String str) {
        if(str != null && str.equals("succeed")) {
            user.mAuthSucceeded = true;
        } else {
            user.mAuthSucceeded = false;
        }
        listener.onLoginDone();
    }
}
