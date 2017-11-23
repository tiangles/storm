package com.tiangles.storm.database.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.greendao.gen.StormSignalParameterRecordDao;

@Entity(
        nameInDb = "device_signal_parameter_records",
        // Flag if the DAO should create the database table (default is true).
        // Set this to false, if you have multiple entities mapping to one table,
        // or the table creation is done outside of greenDAO.
        createInDb = false
)
public class StormSignalParameterRecord {
    @Id
    private int id;
    private String parameterRecordForDevice;
    @ToOne(joinProperty = "parameterRecordForDevice")
    private StormDevice device;
    private Date date;
    private float value;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 1408278937)
private transient StormSignalParameterRecordDao myDao;
@Generated(hash = 466716293)
public StormSignalParameterRecord(int id, String parameterRecordForDevice,
        Date date, float value) {
    this.id = id;
    this.parameterRecordForDevice = parameterRecordForDevice;
    this.date = date;
    this.value = value;
}
@Generated(hash = 2060906498)
public StormSignalParameterRecord() {
}
public int getId() {
    return this.id;
}
public void setId(int id) {
    this.id = id;
}
public String getParameterRecordForDevice() {
    return this.parameterRecordForDevice;
}
public void setParameterRecordForDevice(String parameterRecordForDevice) {
    this.parameterRecordForDevice = parameterRecordForDevice;
}
public Date getDate() {
    return this.date;
}
public void setDate(Date date) {
    this.date = date;
}
public float getValue() {
    return this.value;
}
public void setValue(float value) {
    this.value = value;
}
@Generated(hash = 1600407698)
private transient String device__resolvedKey;
/** To-one relationship, resolved on first access. */
@Generated(hash = 989909095)
public StormDevice getDevice() {
    String __key = this.parameterRecordForDevice;
    if (device__resolvedKey == null || device__resolvedKey != __key) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        StormDeviceDao targetDao = daoSession.getStormDeviceDao();
        StormDevice deviceNew = targetDao.load(__key);
        synchronized (this) {
            device = deviceNew;
            device__resolvedKey = __key;
        }
    }
    return device;
}
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1691985139)
public void setDevice(StormDevice device) {
    synchronized (this) {
        this.device = device;
        parameterRecordForDevice = device == null ? null : device.getCode();
        device__resolvedKey = parameterRecordForDevice;
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
@Generated(hash = 1461739187)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getStormSignalParameterRecordDao()
            : null;
}
}
