package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(
        nameInDb = "storm_power_devices",
        createInDb = false
)
public class PowerDevice {
    @Id
    private String code;
    private String name;
@Generated(hash = 1840683132)
public PowerDevice(String code, String name) {
    this.code = code;
    this.name = name;
}
@Generated(hash = 1030580101)
public PowerDevice() {
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
