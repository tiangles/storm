package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "local_control_cabinets",
        createInDb = false
)
public class LocalControlCabinet {
    @Id
    String code;
    String name;
    String specification;
    String deployed_to;
    int terminal_count;
    String remark;
@Generated(hash = 1079570527)
public LocalControlCabinet(String code, String name, String specification,
        String deployed_to, int terminal_count, String remark) {
    this.code = code;
    this.name = name;
    this.specification = specification;
    this.deployed_to = deployed_to;
    this.terminal_count = terminal_count;
    this.remark = remark;
}
@Generated(hash = 2076969631)
public LocalControlCabinet() {
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
public String getSpecification() {
    return this.specification;
}
public void setSpecification(String specification) {
    this.specification = specification;
}
public String getDeployed_to() {
    return this.deployed_to;
}
public void setDeployed_to(String deployed_to) {
    this.deployed_to = deployed_to;
}
public int getTerminal_count() {
    return this.terminal_count;
}
public void setTerminal_count(int terminal_count) {
    this.terminal_count = terminal_count;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
}
