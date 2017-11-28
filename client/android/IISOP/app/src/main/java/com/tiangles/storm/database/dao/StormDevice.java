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
        nameInDb = "storm_devices",
        createInDb = false
)
public class StormDevice {
    @Id
    private String code;    //设备编码
    private String name;    //设备名称
    private String model;   //设备型号
    private String system;  //所在系统
    private String distribution_cabinet; //配电柜
    private String local_control_panel;   //就地控制柜
    private String dcs_cabinet;      //DCS控制柜
    private String legend;
    private String workshop_id;
    private String power_device_id;
    private String inspection_records;
@Generated(hash = 102823395)
public StormDevice(String code, String name, String model, String system,
        String distribution_cabinet, String local_control_panel,
        String dcs_cabinet, String legend, String workshop_id,
        String power_device_id, String inspection_records) {
    this.code = code;
    this.name = name;
    this.model = model;
    this.system = system;
    this.distribution_cabinet = distribution_cabinet;
    this.local_control_panel = local_control_panel;
    this.dcs_cabinet = dcs_cabinet;
    this.legend = legend;
    this.workshop_id = workshop_id;
    this.power_device_id = power_device_id;
    this.inspection_records = inspection_records;
}
@Generated(hash = 898982401)
public StormDevice() {
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
public String getModel() {
    return this.model;
}
public void setModel(String model) {
    this.model = model;
}
public String getSystem() {
    return this.system;
}
public void setSystem(String system) {
    this.system = system;
}
public String getDistribution_cabinet() {
    return this.distribution_cabinet;
}
public void setDistribution_cabinet(String distribution_cabinet) {
    this.distribution_cabinet = distribution_cabinet;
}
public String getLocal_control_panel() {
    return this.local_control_panel;
}
public void setLocal_control_panel(String local_control_panel) {
    this.local_control_panel = local_control_panel;
}
public String getDcs_cabinet() {
    return this.dcs_cabinet;
}
public void setDcs_cabinet(String dcs_cabinet) {
    this.dcs_cabinet = dcs_cabinet;
}
public String getLegend() {
    return this.legend;
}
public void setLegend(String legend) {
    this.legend = legend;
}
public String getWorkshop_id() {
    return this.workshop_id;
}
public void setWorkshop_id(String workshop_id) {
    this.workshop_id = workshop_id;
}
public String getPower_device_id() {
    return this.power_device_id;
}
public void setPower_device_id(String power_device_id) {
    this.power_device_id = power_device_id;
}
public String getInspection_records() {
    return this.inspection_records;
}
public void setInspection_records(String inspection_records) {
    this.inspection_records = inspection_records;
}
}
