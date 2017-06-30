package com.tiangles.storm.device;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DemoDevice {
    @Id private Long id;
    private String name;
    private String qrCode;
    private String model;
    @Generated(hash = 1620849609)
    public DemoDevice(Long id, String name, String qrCode, String model) {
        this.id = id;
        this.name = name;
        this.qrCode = qrCode;
        this.model = model;
    }
    @Generated(hash = 628281377)
    public DemoDevice() {
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
    public String getQrCode() {
        return this.qrCode;
    }
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    public String getModel() {
        return this.model;
    }
    public void setModel(String model) {
        this.model = model;
    }
}
