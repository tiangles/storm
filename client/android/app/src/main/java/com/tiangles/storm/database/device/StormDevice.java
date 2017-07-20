package com.tiangles.storm.database.device;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StormDevice {
    @Id private Long id;
    private String name;
    private String code;
    private String model;
    private String system;
    private String parameter;
    private String distributionCabinet;
    private String localControlPanel;
    private String dcsCabinet;
    @Generated(hash = 462611757)
    public StormDevice(Long id, String name, String code, String model,
            String system, String parameter, String distributionCabinet,
            String localControlPanel, String dcsCabinet) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.model = model;
        this.system = system;
        this.parameter = parameter;
        this.distributionCabinet = distributionCabinet;
        this.localControlPanel = localControlPanel;
        this.dcsCabinet = dcsCabinet;
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getSystem() {
        return this.system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
    public String getParameter() {
        return this.parameter;
    }
    public void setParameter(String parameter) {
        this.parameter = parameter;
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
}
