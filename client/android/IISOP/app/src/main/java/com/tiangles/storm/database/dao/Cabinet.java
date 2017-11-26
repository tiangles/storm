package com.tiangles.storm.database.dao;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.StormWorkshopDao;
import com.tiangles.greendao.gen.StormDeviceDao;

@Entity(
        nameInDb = "cabinets",
        createInDb = false
)
public class Cabinet {
    @Id
    String code;
    String usage;
    String specification;
    String maintenance_record;
    String workshop_id;
    String remark;
@Generated(hash = 1350906189)
public Cabinet(String code, String usage, String specification,
        String maintenance_record, String workshop_id, String remark) {
    this.code = code;
    this.usage = usage;
    this.specification = specification;
    this.maintenance_record = maintenance_record;
    this.workshop_id = workshop_id;
    this.remark = remark;
}
@Generated(hash = 456667810)
public Cabinet() {
}
public String getCode() {
    return this.code;
}
public void setCode(String code) {
    this.code = code;
}
public String getUsage() {
    return this.usage;
}
public void setUsage(String usage) {
    this.usage = usage;
}
public String getSpecification() {
    return this.specification;
}
public void setSpecification(String specification) {
    this.specification = specification;
}
public String getMaintenance_record() {
    return this.maintenance_record;
}
public void setMaintenance_record(String maintenance_record) {
    this.maintenance_record = maintenance_record;
}
public String getWorkshop_id() {
    return this.workshop_id;
}
public void setWorkshop_id(String workshop_id) {
    this.workshop_id = workshop_id;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
}
