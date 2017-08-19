package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StormWorkshop {
    @Id
    private String code;
    private String name;
    @Generated(hash = 782120252)
    public StormWorkshop(String code, String name) {
        this.code = code;
        this.name = name;
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
}
