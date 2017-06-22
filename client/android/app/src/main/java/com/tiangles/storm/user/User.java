package com.tiangles.storm.user;

import com.tiangles.storm.StormApp;

public class User {
    private static User mInstance;

    public String mUserName;
    public String mPassword;
    public boolean mRememberPassword;
    public boolean mAutoLogin;
    public boolean mAuthSucceeded;

    public interface LoginListener {
        public void onBeginLogin();
        public void onLoginDone();
    }

    public User(){

    }

    public void set(String userName,
                 String password,
                 boolean rememberPassword,
                 boolean autoLogin){
        mUserName = userName;
        mPassword = password;
        mRememberPassword = rememberPassword;
        mAutoLogin = autoLogin;
    }

    public void login(LoginListener listener) {
        StormApp.getNetwork().sendRequest(new LoginRequest(this, listener));
    }
}
