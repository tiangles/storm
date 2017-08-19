package com.tiangles.storm.database.dao;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.StormWorkshopDao;
import com.tiangles.greendao.gen.StormDeviceDao;

@Entity
public class StormDevice {
    @Id private String code;    //设备编码
    private String name;    //设备名称
    private String model;   //设备型号
    private String system;  //所在系统
    private String distributionCabinet; //配电柜
    private String localControlPanel;   //就地控制柜
    private String dcsCabinet;      //DCS控制柜
    private String forwardDevice;
    private String backwardDevice;
    private String legend;
    private String belongToWorkshop;
    @ToOne(joinProperty = "belongToWorkshop")  private StormWorkshop workShop;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 262992284)
    private transient StormDeviceDao myDao;
    @Generated(hash = 788849806)
    public StormDevice(String code, String name, String model, String system,
            String distributionCabinet, String localControlPanel, String dcsCabinet,
            String forwardDevice, String backwardDevice, String legend,
            String belongToWorkshop) {
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
        this.belongToWorkshop = belongToWorkshop;
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
    public String getBelongToWorkshop() {
        return this.belongToWorkshop;
    }
    public void setBelongToWorkshop(String belongToWorkshop) {
        this.belongToWorkshop = belongToWorkshop;
    }
    @Generated(hash = 1736800678)
    private transient String workShop__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1513489648)
    public StormWorkshop getWorkShop() {
        String __key = this.belongToWorkshop;
        if (workShop__resolvedKey == null || workShop__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StormWorkshopDao targetDao = daoSession.getStormWorkshopDao();
            StormWorkshop workShopNew = targetDao.load(__key);
            synchronized (this) {
                workShop = workShopNew;
                workShop__resolvedKey = __key;
            }
        }
        return workShop;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1249880137)
    public void setWorkShop(StormWorkshop workShop) {
        synchronized (this) {
            this.workShop = workShop;
            belongToWorkshop = workShop == null ? null : workShop.getCode();
            workShop__resolvedKey = belongToWorkshop;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 947959612)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStormDeviceDao() : null;
    }
}
