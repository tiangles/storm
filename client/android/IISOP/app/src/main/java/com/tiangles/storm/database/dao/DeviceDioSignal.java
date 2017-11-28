package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "dcs_dio_signals",
        createInDb = false
)
public class DeviceDioSignal {
    @Id
    private String code;
    private String figure_number;
    private String for_device_id;
    private String name;
    private String io_type;
    private String signal_type;
    private String remark;
    private String power_supply;
    private String connect_to_system;
    private String status_when_io_is_1;
    private String status_when_io_is_0;
    private String interface_type;
    private String control_signal_type;
    private String incident_record;
@Generated(hash = 431769224)
public DeviceDioSignal(String code, String figure_number, String for_device_id,
        String name, String io_type, String signal_type, String remark,
        String power_supply, String connect_to_system,
        String status_when_io_is_1, String status_when_io_is_0,
        String interface_type, String control_signal_type,
        String incident_record) {
    this.code = code;
    this.figure_number = figure_number;
    this.for_device_id = for_device_id;
    this.name = name;
    this.io_type = io_type;
    this.signal_type = signal_type;
    this.remark = remark;
    this.power_supply = power_supply;
    this.connect_to_system = connect_to_system;
    this.status_when_io_is_1 = status_when_io_is_1;
    this.status_when_io_is_0 = status_when_io_is_0;
    this.interface_type = interface_type;
    this.control_signal_type = control_signal_type;
    this.incident_record = incident_record;
}
@Generated(hash = 642258940)
public DeviceDioSignal() {
}
public String getCode() {
    return this.code;
}
public void setCode(String code) {
    this.code = code;
}
public String getFigure_number() {
    return this.figure_number;
}
public void setFigure_number(String figure_number) {
    this.figure_number = figure_number;
}
public String getFor_device_id() {
    return this.for_device_id;
}
public void setFor_device_id(String for_device_id) {
    this.for_device_id = for_device_id;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getIo_type() {
    return this.io_type;
}
public void setIo_type(String io_type) {
    this.io_type = io_type;
}
public String getSignal_type() {
    return this.signal_type;
}
public void setSignal_type(String signal_type) {
    this.signal_type = signal_type;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
public String getPower_supply() {
    return this.power_supply;
}
public void setPower_supply(String power_supply) {
    this.power_supply = power_supply;
}
public String getConnect_to_system() {
    return this.connect_to_system;
}
public void setConnect_to_system(String connect_to_system) {
    this.connect_to_system = connect_to_system;
}
public String getStatus_when_io_is_1() {
    return this.status_when_io_is_1;
}
public void setStatus_when_io_is_1(String status_when_io_is_1) {
    this.status_when_io_is_1 = status_when_io_is_1;
}
public String getStatus_when_io_is_0() {
    return this.status_when_io_is_0;
}
public void setStatus_when_io_is_0(String status_when_io_is_0) {
    this.status_when_io_is_0 = status_when_io_is_0;
}
public String getInterface_type() {
    return this.interface_type;
}
public void setInterface_type(String interface_type) {
    this.interface_type = interface_type;
}
public String getControl_signal_type() {
    return this.control_signal_type;
}
public void setControl_signal_type(String control_signal_type) {
    this.control_signal_type = control_signal_type;
}
public String getIncident_record() {
    return this.incident_record;
}
public void setIncident_record(String incident_record) {
    this.incident_record = incident_record;
}
}
