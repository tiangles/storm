package com.tiangles.storm.user;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.request.LoginRequest;

public class User {
    private static User mInstance;

    public int mID;
    public String mUserName;
    public String mPassword;
    public boolean mRememberPassword;
    public boolean mAutoLogin;
    public boolean mAuthSucceeded;

    public interface LoginListener {
        public void onLoginDone(int error, String msg);
    }

    public User(){

    }

    public void set(int id,
                    String userName,
                    String password,
                    boolean rememberPassword,
                    boolean autoLogin){
        mID = id;
        mUserName = userName;
        mPassword = password;
        mRememberPassword = rememberPassword;
        mAutoLogin = autoLogin;
    }

    public void login(LoginListener listener) {
//        if(mUserName.equals("foo") && mPassword.equals("foo")) {
//            mAuthSucceeded = true;
//            listener.onLoginDone(0, "OK");
//        } else {
            StormApp.getNetwork().sendRequest(new LoginRequest(this, listener));
//        }
    }
}
