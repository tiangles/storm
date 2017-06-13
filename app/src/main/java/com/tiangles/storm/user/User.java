package com.tiangles.storm.user;

import com.tiangles.storm.App;
import com.tiangles.storm.preference.PreferenceEngine;

public class User {
    public String mUserName;
    public String mPassword;
    public boolean mRememberPassowrd;
    public boolean mAutoLogin;

    private User(String userName,
                 String password,
                 boolean rememberPassword,
                 boolean autoLogin){
        mUserName = userName;
        mPassword = password;
        mRememberPassowrd = rememberPassword;
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

    public static User loadUserInfo(){
        PreferenceEngine engine = PreferenceEngine.getInstance();
        return new User(engine.getUserName(),
                engine.getUserPassword(),
                engine.getRememberPassword(),
                engine.getAutoLogin());
    }


}
