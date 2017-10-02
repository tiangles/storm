package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "storm_workshop",
        // Flag if the DAO should create the database table (default is true).
        // Set this to false, if you have multiple entities mapping to one table,
        // or the table creation is done outside of greenDAO.
        createInDb = false
)
public class StormWorkshop {
    @Id
    private String code;
    private int workshop_index;
    private String name;
@Generated(hash = 1013761996)
public StormWorkshop(String code, int workshop_index, String name) {
    this.code = code;
    this.workshop_index = workshop_index;
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
public int getWorkshop_index() {
    return this.workshop_index;
}
public void setWorkshop_index(int workshop_index) {
    this.workshop_index = workshop_index;
}
}
