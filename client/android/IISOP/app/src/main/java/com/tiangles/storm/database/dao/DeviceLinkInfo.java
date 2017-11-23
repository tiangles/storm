package com.tiangles.storm.database.dao;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(
        nameInDb = "device_link_information",
        createInDb = false
)
public class DeviceLinkInfo {
    @Id
    private int id;
    private String left_device_id;
    private String right_device_id;
@Generated(hash = 2102717349)
public DeviceLinkInfo(int id, String left_device_id, String right_device_id) {
    this.id = id;
    this.left_device_id = left_device_id;
    this.right_device_id = right_device_id;
}
@Generated(hash = 1267855628)
public DeviceLinkInfo() {
}
public int getId() {
    return this.id;
}
public void setId(int id) {
    this.id = id;
}
public String getLeft_device_id() {
    return this.left_device_id;
}
public void setLeft_device_id(String left_device_id) {
    this.left_device_id = left_device_id;
}
public String getRight_device_id() {
    return this.right_device_id;
}
public void setRight_device_id(String right_device_id) {
    this.right_device_id = right_device_id;
}
}
