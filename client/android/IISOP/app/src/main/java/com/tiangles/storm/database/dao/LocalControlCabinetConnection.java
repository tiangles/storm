package com.tiangles.storm.database.dao;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "local_control_cabinet_connections",
        createInDb = false
)
public class LocalControlCabinetConnection {
    @Id
    String code;
    String figure_number;
    String name;
    String instrument_type;
    String cable_wire_model;
    String cable_pipe_model;
    String cable_index;
    String cable_model;
    String cable_backup_core;
    String cable_direction;
    String remark;
        String cabinet_id;
@Generated(hash = 185593685)
public LocalControlCabinetConnection(String code, String figure_number,
        String name, String instrument_type, String cable_wire_model,
        String cable_pipe_model, String cable_index, String cable_model,
        String cable_backup_core, String cable_direction, String remark,
        String cabinet_id) {
    this.code = code;
    this.figure_number = figure_number;
    this.name = name;
    this.instrument_type = instrument_type;
    this.cable_wire_model = cable_wire_model;
    this.cable_pipe_model = cable_pipe_model;
    this.cable_index = cable_index;
    this.cable_model = cable_model;
    this.cable_backup_core = cable_backup_core;
    this.cable_direction = cable_direction;
    this.remark = remark;
    this.cabinet_id = cabinet_id;
}
@Generated(hash = 1010443610)
public LocalControlCabinetConnection() {
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
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getInstrument_type() {
    return this.instrument_type;
}
public void setInstrument_type(String instrument_type) {
    this.instrument_type = instrument_type;
}
public String getCable_wire_model() {
    return this.cable_wire_model;
}
public void setCable_wire_model(String cable_wire_model) {
    this.cable_wire_model = cable_wire_model;
}
public String getCable_pipe_model() {
    return this.cable_pipe_model;
}
public void setCable_pipe_model(String cable_pipe_model) {
    this.cable_pipe_model = cable_pipe_model;
}
public String getCable_index() {
    return this.cable_index;
}
public void setCable_index(String cable_index) {
    this.cable_index = cable_index;
}
public String getCable_model() {
    return this.cable_model;
}
public void setCable_model(String cable_model) {
    this.cable_model = cable_model;
}
public String getCable_backup_core() {
    return this.cable_backup_core;
}
public void setCable_backup_core(String cable_backup_core) {
    this.cable_backup_core = cable_backup_core;
}
public String getCable_direction() {
    return this.cable_direction;
}
public void setCable_direction(String cable_direction) {
    this.cable_direction = cable_direction;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
public String getCabinet_id() {
    return this.cabinet_id;
}
public void setCabinet_id(String cabinet_id) {
    this.cabinet_id = cabinet_id;
}
}
