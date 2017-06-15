package com.tiangles.storm.preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tiangles.storm.StormApp;

public class PreferenceEngine {
    private final String KEY_CURRENT_USER_NAME = "key_current_user_name";
    private final String KEY_CURRENT_USER_PASSWORD = "key_current_user_password";
    private final String KEY_AUTO_LOGIN = "key_auto_login";
    private final String KEY_REMEMBER_PASSWORD = "key_remember_password";

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
}
