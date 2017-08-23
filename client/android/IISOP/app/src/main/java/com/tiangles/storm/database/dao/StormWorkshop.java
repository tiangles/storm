package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StormWorkshop {
    @Id
    private String code;
    private String name;
    private String deviceList;
    @Generated(hash = 1327145321)
    public StormWorkshop(String code, String name, String deviceList) {
        this.code = code;
        this.name = name;
        this.deviceList = deviceList;
    }
    @Generated(hash = 37746842)
    public StormWorkshop() {
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDeviceList() {

        return this.deviceList;
    }
    public void setDeviceList(String deviceList) {
        this.deviceList = deviceList;
    }
}
