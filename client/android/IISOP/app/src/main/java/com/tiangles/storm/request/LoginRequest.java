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
        int userID = -1;
        int result = -1;
        String error = null;
        try {
            result = jObj.getInt("result");
            JSONObject message = jObj.getJSONObject("message");
            if(result == 0) {
                userID = message.getInt("user_id");
                error = "Succeed";
            } else {
                error = message.getString("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(result == 0) {
            user.mAuthSucceeded = true;
            user.mID = userID;
        }
        listener.onLoginDone(result, error);
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
        handleLoginResult(res.object());
        return true;
    }

    @Override
    public void onError(Exception e) {
        user.mAuthSucceeded = false;
        listener.onLoginDone(-1, String.valueOf(R.string.network_error));
    }
}
