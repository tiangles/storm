package com.tiangles.storm.preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.user.User;

public class PreferenceEngine {
    private final String KEY_CURRENT_USER_NAME = "key_current_user_name";
    private final String KEY_SERVER_ADDRESS = "key_server_address";
    private final String KEY_SERVER_PORT = "key_server_port";
    private final String KEY_CURRENT_USER_PASSWORD = "key_current_user_password";
    private final String KEY_AUTO_LOGIN = "key_auto_login";
    private final String KEY_REMEMBER_PASSWORD = "key_remember_password";
    private final String KEY_SIGNAL_PARAMETER_REFRESH_INTERVAL = "key_signal_parameter_refresh_interval";

    private static PreferenceEngine mInstance;
    private SharedPreferences mDefaultSharedPreference;

    public static PreferenceEngine getInstance() {
        if(mInstance == null) {
            mInstance = new PreferenceEngine();
        }
        return mInstance;
    }

    private PreferenceEngine() {
    }

    private SharedPreferences getDefaultPreference() {
        if(mDefaultSharedPreference == null) {
            mDefaultSharedPreference = PreferenceManager.getDefaultSharedPreferences(StormApp.getContext());
        }
        return mDefaultSharedPreference;
    }

    public void clear() {
        getDefaultPreference().edit().clear().commit();
    }

    public String getServerAddress(){
        return getDefaultPreference().getString(KEY_SERVER_ADDRESS, "127.0.0.1");
    }

    public int getServerPort(){
        String port =  getDefaultPreference().getString(KEY_SERVER_PORT, "8129");
        return Integer.parseInt(port);
    }

    public void setUserName(String userName) {
        getDefaultPreference().edit().putString(KEY_CURRENT_USER_NAME, userName).commit();
    }

    public String getUserName() {
        return getDefaultPreference().getString(KEY_CURRENT_USER_NAME, "");
    }

    public void setUserPassword(String password) {
        getDefaultPreference().edit().putString(KEY_CURRENT_USER_PASSWORD, password).commit();
    }

    public String getUserPassword() {
        return getDefaultPreference().getString(KEY_CURRENT_USER_PASSWORD, "");
    }

    public void setAutoLogin(boolean autoLogin) {
        getDefaultPreference().edit().putBoolean(KEY_AUTO_LOGIN, autoLogin).commit();
    }

    public boolean getAutoLogin() {
        return getDefaultPreference().getBoolean(KEY_AUTO_LOGIN, false);
    }

    public void setRememberPassword(boolean autoLogin) {
        getDefaultPreference().edit().putBoolean(KEY_REMEMBER_PASSWORD, autoLogin).commit();
    }

    public boolean getRememberPassword() {
        return getDefaultPreference().getBoolean(KEY_REMEMBER_PASSWORD, false);
    }

    public int getSignalParameterRefreshInterval(){
        return getDefaultPreference().getInt(KEY_SIGNAL_PARAMETER_REFRESH_INTERVAL, 5*1000);
    }


    public void setSignalParameterRefreshInterval(int interval){
        getDefaultPreference().edit().putInt(KEY_SIGNAL_PARAMETER_REFRESH_INTERVAL, interval).commit();
    }


    public User loadUserInfo(User user) {
        user.set(getUserName(),
                getUserPassword(),
                getRememberPassword(),
                getAutoLogin());

        return user;
    }

    public void saveUserInfo(User user) {
        setAutoLogin(user.mAutoLogin);
        setRememberPassword(user.mRememberPassword);
        if(user.mRememberPassword){
            setUserPassword(user.mPassword);
        } else {
            setUserPassword("");
        }
        setUserName(user.mUserName);
    }
}
