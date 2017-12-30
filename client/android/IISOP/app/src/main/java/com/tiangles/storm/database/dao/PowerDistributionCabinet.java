package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "power_distribution_cabinet",
        createInDb = false
)
public class PowerDistributionCabinet {
    @Id
    String code;
    String model;
    String name;
    String cable_mode;
    String circuit_index;
    String circuit_name;
    String circuit_electric_current;
    String vacuum_breaker;
    String fc_circuit;
    String voltage_transformer;
    String current_transformer;
    String earthing_switch;
    String arrester;
    String zero_sequence_current_transformer;
    String cable_code;
    String workshop_id;
    String remark;
    @Generated(hash = 1314130877)
    public PowerDistributionCabinet(String code, String model, String name,
            String cable_mode, String circuit_index, String circuit_name,
            String circuit_electric_current, String vacuum_breaker,
            String fc_circuit, String voltage_transformer,
            String current_transformer, String earthing_switch, String arrester,
            String zero_sequence_current_transformer, String cable_code,
            String workshop_id, String remark) {
        this.code = code;
        this.model = model;
        this.name = name;
        this.cable_mode = cable_mode;
        this.circuit_index = circuit_index;
        this.circuit_name = circuit_name;
        this.circuit_electric_current = circuit_electric_current;
        this.vacuum_breaker = vacuum_breaker;
        this.fc_circuit = fc_circuit;
        this.voltage_transformer = voltage_transformer;
        this.current_transformer = current_transformer;
        this.earthing_switch = earthing_switch;
        this.arrester = arrester;
        this.zero_sequence_current_transformer = zero_sequence_current_transformer;
        this.cable_code = cable_code;
        this.workshop_id = workshop_id;
        this.remark = remark;
    }
    @Generated(hash = 1423752132)
    public PowerDistributionCabinet() {
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getModel() {
        return this.model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCable_mode() {
        return this.cable_mode;
    }
    public void setCable_mode(String cable_mode) {
        this.cable_mode = cable_mode;
    }
    public String getCircuit_index() {
        return this.circuit_index;
    }
    public void setCircuit_index(String circuit_index) {
        this.circuit_index = circuit_index;
    }
    public String getCircuit_name() {
        return this.circuit_name;
    }
    public void setCircuit_name(String circuit_name) {
        this.circuit_name = circuit_name;
    }
    public String getCircuit_electric_current() {
        return this.circuit_electric_current;
    }
    public void setCircuit_electric_current(String circuit_electric_current) {
        this.circuit_electric_current = circuit_electric_current;
    }
    public String getVacuum_breaker() {
        return this.vacuum_breaker;
    }
    public void setVacuum_breaker(String vacuum_breaker) {
        this.vacuum_breaker = vacuum_breaker;
    }
    public String getFc_circuit() {
        return this.fc_circuit;
    }
    public void setFc_circuit(String fc_circuit) {
        this.fc_circuit = fc_circuit;
    }
    public String getVoltage_transformer() {
        return this.voltage_transformer;
    }
    public void setVoltage_transformer(String voltage_transformer) {
        this.voltage_transformer = voltage_transformer;
    }
    public String getCurrent_transformer() {
        return this.current_transformer;
    }
    public void setCurrent_transformer(String current_transformer) {
        this.current_transformer = current_transformer;
    }
    public String getEarthing_switch() {
        return this.earthing_switch;
    }
    public void setEarthing_switch(String earthing_switch) {
        this.earthing_switch = earthing_switch;
    }
    public String getArrester() {
        return this.arrester;
    }
    public void setArrester(String arrester) {
        this.arrester = arrester;
    }
    public String getZero_sequence_current_transformer() {
        return this.zero_sequence_current_transformer;
    }
    public void setZero_sequence_current_transformer(
            String zero_sequence_current_transformer) {
        this.zero_sequence_current_transformer = zero_sequence_current_transformer;
    }
    public String getCable_code() {
        return this.cable_code;
    }
    public void setCable_code(String cable_code) {
        this.cable_code = cable_code;
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
