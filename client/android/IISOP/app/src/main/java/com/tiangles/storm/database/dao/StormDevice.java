package com.tiangles.storm.database.dao;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StormDevice {
    @Id
    Long id;

    private String code;    //设备编码
    private String name;    //设备名称
    private String model;   //设备型号
    private String system;  //所在系统
    private String distributionCabinet; //配电柜
    private String localControlPanel;   //就地控制柜
    private String dcsCabinet;      //DCS控制柜

    private String forwardDevice;
    private String backwardDevice;
    private String legend;
    @Generated(hash = 130495297)
    public StormDevice(Long id, String code, String name, String model,
            String system, String distributionCabinet, String localControlPanel,
            String dcsCabinet, String forwardDevice, String backwardDevice,
            String legend) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.model = model;
        this.system = system;
        this.distributionCabinet = distributionCabinet;
        this.localControlPanel = localControlPanel;
        this.dcsCabinet = dcsCabinet;
        this.forwardDevice = forwardDevice;
        this.backwardDevice = backwardDevice;
        this.legend = legend;
    }
    @Generated(hash = 898982401)
    public StormDevice() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getDistributionCabinet() {
        return this.distributionCabinet;
    }
    public void setDistributionCabinet(String distributionCabinet) {
        this.distributionCabinet = distributionCabinet;
    }
    public String getLocalControlPanel() {
        return this.localControlPanel;
    }
    public void setLocalControlPanel(String localControlPanel) {
        this.localControlPanel = localControlPanel;
    }
    public String getDcsCabinet() {
        return this.dcsCabinet;
    }
    public void setDcsCabinet(String dcsCabinet) {
        this.dcsCabinet = dcsCabinet;
    }
    public String getForwardDevice() {
        return this.forwardDevice;
    }
    public void setForwardDevice(String forwardDevice) {
        this.forwardDevice = forwardDevice;
    }
    public String getBackwardDevice() {
        return this.backwardDevice;
    }
    public void setBackwardDevice(String backwardDevice) {
        this.backwardDevice = backwardDevice;
    }
    public String getLegend() {
        return this.legend;
    }
    public void setLegend(String legend) {
        this.legend = legend;
    }
}
