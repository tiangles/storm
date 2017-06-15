package com.tiangles.storm.user;

import com.tiangles.storm.preference.PreferenceEngine;

public class User {
    private static User mInstance;

    public String mUserName;
    public String mPassword;
    public boolean mRememberPassword;
    public boolean mAutoLogin;
    public boolean mAuthSucceeded;

    public static User getInstance(){
        if(mInstance == null){
            mInstance = new User();
        }
        return mInstance;
    }

    private User(){

    }

    private void set(String userName,
                 String password,
                 boolean rememberPassword,
                 boolean autoLogin){
        mUserName = userName;
        mPassword = password;
        mRememberPassword = rememberPassword;
        mAutoLogin = autoLogin;
    }

    public static void saveUserInfo(String userName,
                                    String password,
                                    boolean rememberPassword,
                                    boolean autoLogin){
        PreferenceEngine engine = PreferenceEngine.getInstance();
        engine.setAutoLogin(autoLogin);
        engine.setRememberPassword(rememberPassword);
        if(rememberPassword){
            engine.setUserPassword(password);
        } else {
            engine.setUserPassword("");
        }
        engine.setUserName(userName);
    }

    public void loadUserInfo(){
        PreferenceEngine engine = PreferenceEngine.getInstance();
        mInstance.set(engine.getUserName(),
                engine.getUserPassword(),
                engine.getRememberPassword(),
                engine.getAutoLogin());
    }
}
