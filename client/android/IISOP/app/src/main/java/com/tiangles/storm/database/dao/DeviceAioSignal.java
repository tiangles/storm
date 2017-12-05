package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "dcs_aio_signals",
        createInDb = false
)
public class DeviceAioSignal {
    private String code;
    private String figure_number;
    private String for_device_id;
    private String name;
    private String io_type;
    private String signal_type;
    private String isolation;
    private String unit;
    private float min_range;
    private float max_range;
    private String remark;
    private String power_supply;
    private String connect_to_system;
    private boolean lll;
    private boolean ll;
    private boolean l;
    private boolean h;
    private boolean hh;
    private boolean hhh;
    private boolean tendency;

    @Generated(hash = 2027673328)
    public DeviceAioSignal(String code, String figure_number, String for_device_id,
            String name, String io_type, String signal_type, String isolation,
            String unit, float min_range, float max_range, String remark,
            String power_supply, String connect_to_system, boolean lll, boolean ll,
            boolean l, boolean h, boolean hh, boolean hhh, boolean tendency) {
        this.code = code;
        this.figure_number = figure_number;
        this.for_device_id = for_device_id;
        this.name = name;
        this.io_type = io_type;
        this.signal_type = signal_type;
        this.isolation = isolation;
        this.unit = unit;
        this.min_range = min_range;
        this.max_range = max_range;
        this.remark = remark;
        this.power_supply = power_supply;
        this.connect_to_system = connect_to_system;
        this.lll = lll;
        this.ll = ll;
        this.l = l;
        this.h = h;
        this.hh = hh;
        this.hhh = hhh;
        this.tendency = tendency;
    }

    @Generated(hash = 1587963748)
    public DeviceAioSignal() {
    }

    public static String createConnectionMethod(DeviceAioSignal signal){
        String ioType = signal.getIo_type();
        String signalType = signal.getSignal_type();
        String isolation = signal.getIsolation();
        String result = null;
        if (ioType.equals("DI") || ioType.equals("DO")) {
            result = "-1 -2 S";
        } else if(ioType.equals("AO")) {
            result = "+ - S";
        } else if(ioType.equals("AI")) {
            if(signalType.equals("K")) {
                result = "+ - S";
            } else if(signalType.equals("RTD")) {
                result = "a b c S";
            } else if(signalType.equals("4~20mA")) {
                if(isolation.equals("Y")) {
                    result = "- + S";
                } else {
                    result = "+ - S";
                }
            } else {
                result = "+ -";
            }
        }
        return result;
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

    public String getIsolation() {
        return this.isolation;
    }

    public void setIsolation(String isolation) {
        this.isolation = isolation;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getMin_range() {
        return this.min_range;
    }

    public void setMin_range(float min_range) {
        this.min_range = min_range;
    }

    public float getMax_range() {
        return this.max_range;
    }

    public void setMax_range(float max_range) {
        this.max_range = max_range;
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

    public boolean getLll() {
        return this.lll;
    }

    public void setLll(boolean lll) {
        this.lll = lll;
    }

    public boolean getLl() {
        return this.ll;
    }

    public void setLl(boolean ll) {
        this.ll = ll;
    }

    public boolean getL() {
        return this.l;
    }

    public void setL(boolean l) {
        this.l = l;
    }

    public boolean getH() {
        return this.h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public boolean getHh() {
        return this.hh;
    }

    public void setHh(boolean hh) {
        this.hh = hh;
    }

    public boolean getHhh() {
        return this.hhh;
    }

    public void setHhh(boolean hhh) {
        this.hhh = hhh;
    }

    public boolean getTendency() {
        return this.tendency;
    }

    public void setTendency(boolean tendency) {
        this.tendency = tendency;
    }
    
}
