package com.tiangles.storm.database.dao;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "local_control_cabinet_terminals",
        createInDb = false
)
public class LocalControlCabinetTerminal {
    @Id
    int id;
    String cabinet_id;
    String cabinet_terminal;
    String cabinet_cable_number;
    String instrument_terminal;
    String instrument_cable_number;
    String for_connection_id;
@Generated(hash = 665869288)
public LocalControlCabinetTerminal(int id, String cabinet_id,
        String cabinet_terminal, String cabinet_cable_number,
        String instrument_terminal, String instrument_cable_number,
        String for_connection_id) {
    this.id = id;
    this.cabinet_id = cabinet_id;
    this.cabinet_terminal = cabinet_terminal;
    this.cabinet_cable_number = cabinet_cable_number;
    this.instrument_terminal = instrument_terminal;
    this.instrument_cable_number = instrument_cable_number;
    this.for_connection_id = for_connection_id;
}
@Generated(hash = 895890503)
public LocalControlCabinetTerminal() {
}
public int getId() {
    return this.id;
}
public void setId(int id) {
    this.id = id;
}
public String getCabinet_id() {
    return this.cabinet_id;
}
public void setCabinet_id(String cabinet_id) {
    this.cabinet_id = cabinet_id;
}
public String getCabinet_terminal() {
    return this.cabinet_terminal;
}
public void setCabinet_terminal(String cabinet_terminal) {
    this.cabinet_terminal = cabinet_terminal;
}
public String getCabinet_cable_number() {
    return this.cabinet_cable_number;
}
public void setCabinet_cable_number(String cabinet_cable_number) {
    this.cabinet_cable_number = cabinet_cable_number;
}
public String getInstrument_terminal() {
    return this.instrument_terminal;
}
public void setInstrument_terminal(String instrument_terminal) {
    this.instrument_terminal = instrument_terminal;
}
public String getInstrument_cable_number() {
    return this.instrument_cable_number;
}
public void setInstrument_cable_number(String instrument_cable_number) {
    this.instrument_cable_number = instrument_cable_number;
}
public String getFor_connection_id() {
    return this.for_connection_id;
}
public void setFor_connection_id(String for_connection_id) {
    this.for_connection_id = for_connection_id;
}
}
