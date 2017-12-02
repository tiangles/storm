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
    String code;
    String name;
    String type;
    String driver_type;
    String power_circuit_voltage;
    String control_circuit_voltage;
    String model;
    String maintenance_record;
    String workshop_id;
    String system;
    String power_device_id;

    public String getLegend(){
        return driver_type + type;
    }
@Generated(hash = 429532647)
public StormDevice(String code, String name, String type, String driver_type,
        String power_circuit_voltage, String control_circuit_voltage,
        String model, String maintenance_record, String workshop_id,
        String system, String power_device_id) {
    this.code = code;
    this.name = name;
    this.type = type;
    this.driver_type = driver_type;
    this.power_circuit_voltage = power_circuit_voltage;
    this.control_circuit_voltage = control_circuit_voltage;
    this.model = model;
    this.maintenance_record = maintenance_record;
    this.workshop_id = workshop_id;
    this.system = system;
    this.power_device_id = power_device_id;
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
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public String getDriver_type() {
    return this.driver_type;
}
public void setDriver_type(String driver_type) {
    this.driver_type = driver_type;
}
public String getPower_circuit_voltage() {
    return this.power_circuit_voltage;
}
public void setPower_circuit_voltage(String power_circuit_voltage) {
    this.power_circuit_voltage = power_circuit_voltage;
}
public String getControl_circuit_voltage() {
    return this.control_circuit_voltage;
}
public void setControl_circuit_voltage(String control_circuit_voltage) {
    this.control_circuit_voltage = control_circuit_voltage;
}
public String getModel() {
    return this.model;
}
public void setModel(String model) {
    this.model = model;
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
public String getSystem() {
    return this.system;
}
public void setSystem(String system) {
    this.system = system;
}
public String getPower_device_id() {
    return this.power_device_id;
}
public void setPower_device_id(String power_device_id) {
    this.power_device_id = power_device_id;
}
}
