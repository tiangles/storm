package com.tiangles.storm.analytics.event;

import com.tiangles.storm.user.User;

import java.util.Date;

public class LoginEvent implements Event{
    Date mDate;

    public LoginEvent(Date date){
        mDate = date;
    }

    @Override
    public String tag() {
        return LoginEvent.class.getName();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        User user = User.getInstance();
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
