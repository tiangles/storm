package com.tiangles.storm.analytics.event;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.user.User;

import java.util.Date;

public class LoginEvent implements Event{
    Date mDate;
    User user;
    public LoginEvent(User user, Date date){
        this.user = user;
        mDate = date;
    }

    @Override
    public String tag() {
        return "login";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(user.mUserName)
                .append(",")
                .append(mDate.getTime())
                .append(",")
                .append(user.mRememberPassword)
                .append(",")
                .append(user.mAutoLogin)
                .append(",")
                .append(user.mAuthSucceeded);
        return sb.toString();
    }
}
