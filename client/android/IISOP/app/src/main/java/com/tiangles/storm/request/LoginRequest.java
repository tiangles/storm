package com.tiangles.storm.request;

import com.tiangles.storm.R;
import com.tiangles.storm.network.Request;
import com.tiangles.storm.network.Response;
import com.tiangles.storm.user.User;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginRequest extends Request {
    private static String COMMAND = "login";
    private User user;
    private User.LoginListener listener;
    public LoginRequest(User user, User.LoginListener listener) {
        this.user = user;
        this.listener = listener;
    }

    private void handleLoginResult(JSONObject jObj) {
        String message = "";
        int result = -1;
        try {
             result = jObj.getInt("result");
            message = jObj.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.mAuthSucceeded = (result == 0);
        listener.onLoginDone(result, message);
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public JSONObject data()  throws JSONException{
        JSONObject jObj = new JSONObject();
        jObj.put("user_name", user.mUserName);
        jObj.put("password", user.mPassword);
        jObj.put("cmd", "login");
        return jObj;
    }

    @Override
    public boolean handleResponse(Response res) {
        if( res.command().equals(COMMAND) ) {
            handleLoginResult(res.object());
            return true;
        }
        return false;
    }

    @Override
    public void onError(Exception e) {
        user.mAuthSucceeded = false;
        listener.onLoginDone(-1, String.valueOf(R.string.network_error));
    }
}
