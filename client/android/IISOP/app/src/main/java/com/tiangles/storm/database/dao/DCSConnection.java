package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "device_dcsconnection",
        createInDb = false
)
public class DCSConnection {
    @Id
    private String code;
    private String belong_to_system;
    private String description;
    private String dcs_cabinet_number;
    private String id_type;
    private String signal_type;
    private String face_name;
    private String clamp;
    private String channel;
    private String terminal_a;
    private String terminal_b;
    private String terminal_c;
    private String cable_number_1;
    private String cable_number_2;
    private String cable_number_3;
    private String cable_code;
    private String cable_model;
    private String cabel_backup_core;
    private String cable_direction;
    private String remarks;
@Generated(hash = 1376396547)
public DCSConnection(String code, String belong_to_system, String description,
        String dcs_cabinet_number, String id_type, String signal_type,
        String face_name, String clamp, String channel, String terminal_a,
        String terminal_b, String terminal_c, String cable_number_1,
        String cable_number_2, String cable_number_3, String cable_code,
        String cable_model, String cabel_backup_core, String cable_direction,
        String remarks) {
    this.code = code;
    this.belong_to_system = belong_to_system;
    this.description = description;
    this.dcs_cabinet_number = dcs_cabinet_number;
    this.id_type = id_type;
    this.signal_type = signal_type;
    this.face_name = face_name;
    this.clamp = clamp;
    this.channel = channel;
    this.terminal_a = terminal_a;
    this.terminal_b = terminal_b;
    this.terminal_c = terminal_c;
    this.cable_number_1 = cable_number_1;
    this.cable_number_2 = cable_number_2;
    this.cable_number_3 = cable_number_3;
    this.cable_code = cable_code;
    this.cable_model = cable_model;
    this.cabel_backup_core = cabel_backup_core;
    this.cable_direction = cable_direction;
    this.remarks = remarks;
}
@Generated(hash = 1684381285)
public DCSConnection() {
}
public String getCode() {
    return this.code;
}
public void setCode(String code) {
    this.code = code;
}
public String getBelong_to_system() {
    return this.belong_to_system;
}
public void setBelong_to_system(String belong_to_system) {
    this.belong_to_system = belong_to_system;
}
public String getDescription() {
    return this.description;
}
public void setDescription(String description) {
    this.description = description;
}
public String getDcs_cabinet_number() {
    return this.dcs_cabinet_number;
}
public void setDcs_cabinet_number(String dcs_cabinet_number) {
    this.dcs_cabinet_number = dcs_cabinet_number;
}
public String getId_type() {
    return this.id_type;
}
public void setId_type(String id_type) {
    this.id_type = id_type;
}
public String getSignal_type() {
    return this.signal_type;
}
public void setSignal_type(String signal_type) {
    this.signal_type = signal_type;
}
public String getFace_name() {
    return this.face_name;
}
public void setFace_name(String face_name) {
    this.face_name = face_name;
}
public String getClamp() {
    return this.clamp;
}
public void setClamp(String clamp) {
    this.clamp = clamp;
}
public String getChannel() {
    return this.channel;
}
public void setChannel(String channel) {
    this.channel = channel;
}
public String getTerminal_a() {
    return this.terminal_a;
}
public void setTerminal_a(String terminal_a) {
    this.terminal_a = terminal_a;
}
public String getTerminal_b() {
    return this.terminal_b;
}
public void setTerminal_b(String terminal_b) {
    this.terminal_b = terminal_b;
}
public String getTerminal_c() {
    return this.terminal_c;
}
public void setTerminal_c(String terminal_c) {
    this.terminal_c = terminal_c;
}
public String getCable_number_1() {
    return this.cable_number_1;
}
public void setCable_number_1(String cable_number_1) {
    this.cable_number_1 = cable_number_1;
}
public String getCable_number_2() {
    return this.cable_number_2;
}
public void setCable_number_2(String cable_number_2) {
    this.cable_number_2 = cable_number_2;
}
public String getCable_number_3() {
    return this.cable_number_3;
}
public void setCable_number_3(String cable_number_3) {
    this.cable_number_3 = cable_number_3;
}
public String getCable_code() {
    return this.cable_code;
}
public void setCable_code(String cable_code) {
    this.cable_code = cable_code;
}
public String getCable_model() {
    return this.cable_model;
}
public void setCable_model(String cable_model) {
    this.cable_model = cable_model;
}
public String getCabel_backup_core() {
    return this.cabel_backup_core;
}
public void setCabel_backup_core(String cabel_backup_core) {
    this.cabel_backup_core = cabel_backup_core;
}
public String getCable_direction() {
    return this.cable_direction;
}
public void setCable_direction(String cable_direction) {
    this.cable_direction = cable_direction;
}
public String getRemarks() {
    return this.remarks;
}
public void setRemarks(String remarks) {
    this.remarks = remarks;
}
}
