package com.tiangles.storm.database.dao;

import com.tiangles.storm.StormApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.security.PublicKey;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "user_event",
        createInDb = true
)
public class UserEvent {
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_UPLOADED = 1;
    public static final int STATUS_APPROVED = 2;
    public static final int STATUS_REJECTED = 3;
    @Id
    long id;
    Date date;
    String event;
    String device_code;
    int user_id;
    int status;
    public static long generateEventID(Date date){
        int rand = (int)(Math.random()*1000);
        String str=String.format("%10d%03d%03d", date.getTime()/1000, rand, StormApp.getCurrentUser().mID);
        return Long.parseLong(str);
    }

@Generated(hash = 1514076931)
public UserEvent(long id, Date date, String event, String device_code,
        int user_id, int status) {
    this.id = id;
    this.date = date;
    this.event = event;
    this.device_code = device_code;
    this.user_id = user_id;
    this.status = status;
}

@Generated(hash = 1510367177)
public UserEvent() {
}


public long getId() {
    return this.id;
}

public void setId(long id) {
    this.id = id;
}

public Date getDate() {
    return this.date;
}

public void setDate(Date date) {
    this.date = date;
}

public String getEvent() {
    return this.event;
}

public void setEvent(String event) {
    this.event = event;
}

public String getDevice_code() {
    return this.device_code;
}

public void setDevice_code(String device_code) {
    this.device_code = device_code;
}

public int getUser_id() {
    return this.user_id;
}

public void setUser_id(int user_id) {
    this.user_id = user_id;
}

public int getStatus() {
    return this.status;
}

public void setStatus(int status) {
    this.status = status;
}
}
