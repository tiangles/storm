package com.tiangles.storm.database;

import android.content.Context;

import com.tiangles.greendao.gen.CabinetDao;
import com.tiangles.greendao.gen.DCSConnectionDao;
import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.DeviceAioSignalDao;
import com.tiangles.greendao.gen.DeviceDioSignalDao;
import com.tiangles.greendao.gen.DeviceLinkInfoDao;
import com.tiangles.greendao.gen.PowerDeviceDao;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.greendao.gen.StormWorkshopDao;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.Cabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.DeviceDioSignal;
import com.tiangles.storm.database.dao.DeviceLinkInfo;
import com.tiangles.storm.database.dao.PowerDevice;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class StormDB {
    private DaoSession daoSession;
    private StormDeviceDao stormDeviceDao;
    private StormWorkshopDao stormWorkshopDao;
    private DeviceLinkInfoDao deviceLinkInfoDao;
    private DeviceDioSignalDao deviceDioSignalDao;
    private DeviceAioSignalDao deviceAioSignalDao;
    private DCSConnectionDao dcsConnectionDao;
    private PowerDeviceDao powerDeviceDao;
    CabinetDao cabinetDao;
    private String dbPath;

    public StormDB(Context context, String dbPath) {
        this.dbPath = dbPath;
    }

    public StormDevice getDevice(String deviceCode) {
        if(deviceCode != null) {
            List<StormDevice> devices = getStormDeviceDao().queryBuilder()
                    .where(StormDeviceDao.Properties.Code.eq(deviceCode))
                    .build()
                    .list();
            assert (devices.size()<2);
            if(devices.size()>0) {
                return devices.get(0);
            }
        }
        return null;
    }

    public List<DeviceLinkInfo> getLeftLinkInfoForDevice(StormDevice device){
        return getDeviceLinkInfoDao().queryBuilder()
                .where(DeviceLinkInfoDao.Properties.Right_device_id.eq(device.getCode()))
                .build()
                .list();

    }

    public List<DeviceLinkInfo> getRightLinkInfoForDevice(StormDevice device){
        return getDeviceLinkInfoDao().queryBuilder()
                .where(DeviceLinkInfoDao.Properties.Left_device_id.eq(device.getCode()))
                .build()
                .list();

    }

    public List<StormDevice> getDeviceFromWorkshop(String workshopCode){
        if(workshopCode != null) {
            List<StormDevice> devices = getStormDeviceDao().queryBuilder()
                    .where(StormDeviceDao.Properties.Workshop_id.eq(workshopCode))
                    .build()
                    .list();
            return devices;
        }
        return null;
    }

    public List<DeviceDioSignal> getDioSignalsForDevice(String deviceCode){
        List<DeviceDioSignal> signals = getDeviceDioSignalDao().queryBuilder()
                .where(DeviceDioSignalDao.Properties.For_device_id.eq(deviceCode))
                .build()
                .list();
        return signals;
    }

    public List<DeviceAioSignal> getAioSignalsForDevice(String deviceCode){
        List<DeviceAioSignal> signals = getDeviceAioSignalDao().queryBuilder()
                .where(DeviceAioSignalDao.Properties.For_device_id.eq(deviceCode))
                .build()
                .list();
        return signals;
    }

    public DCSConnection getDCSConnection(String connectionCode){
        List<DCSConnection> connections = getDcsConnectionDao().queryBuilder()
                .where(DCSConnectionDao.Properties.Code.eq(connectionCode))
                .build()
                .list();
        if(connections.size()>0) {
            return connections.get(0);
        }
        return null;
    }

    public DeviceDioSignal getDeviceDioSignal(String code) {
        List<DeviceDioSignal> signals = getDeviceDioSignalDao().queryBuilder()
                .where(DeviceDioSignalDao.Properties.Code.eq(code))
                .build()
                .list();
        if(signals.size()>0){
            return signals.get(0);
        }
        return null;
    }

    public DeviceAioSignal getDeviceAioSignal(String code) {
        List<DeviceAioSignal> signals = getDeviceAioSignalDao().queryBuilder()
                .where(DeviceAioSignalDao.Properties.Code.eq(code))
                .build()
                .list();
        if(signals.size()>0){
            return signals.get(0);
        }
        return null;
    }

    public List<DCSConnection> getDCSConnectionsFromCabinetFace(Cabinet cabinet, String face) {
        return getDcsConnectionDao().queryBuilder()
                .where(DCSConnectionDao.Properties.Dcs_cabinet_number.eq(cabinet.getCode()),
                       DCSConnectionDao.Properties.Face_name.eq(face))
                .build()
                .list();
    }

    public List<Cabinet> getCabinetsForWorkshop(String workshopCode) {
        return getCabinetDao().queryBuilder()
                .where(CabinetDao.Properties.Workshop_id.eq(workshopCode))
                .build()
                .list();

    }

    public Cabinet getCabinet(String code) {
        List<Cabinet>  cabinets = getCabinetDao().queryBuilder()
                .where(CabinetDao.Properties.Code.eq(code))
                .build()
                .list();
        if(cabinets.size()>0) {
            return cabinets.get(0);
        }
        return null;
    }


    public void commitDeviceChange(StormDevice device){
        if(device != null){
            if(getDevice(device.getCode()) != null) {
                getStormDeviceDao().update(device);
            } else {
                getStormDeviceDao().insert(device);
            }
        }
    }

    public void commitWorkshopChange(StormWorkshop workshop) {
        if(workshop != null) {
            if(getWorkshop(workshop.getCode()) != null) {
                getStormWorkshopDao().update(workshop);
            } else {
                getStormWorkshopDao().insert(workshop);
            }
        }
    }

    public PowerDevice getPowerDevice(String code){
        if(code !=null) {
            List<PowerDevice> powerDevices = getPowerDeviceDao().queryBuilder()
                    .where(PowerDeviceDao.Properties.Code.eq(code))
                    .build()
                    .list();
            if(powerDevices.size()>0){
                return powerDevices.get(0);
            }
        }
        return null;
    }


    public List<StormWorkshop> getWorkshopList(String keyword, int offset, int limit){
        List<StormWorkshop> workshops;
        if(keyword == null || keyword.isEmpty()){
            workshops = getStormWorkshopDao().queryBuilder()
                    .offset(offset)
                    .limit(limit)
                    .build()
                    .list();
        } else {
            QueryBuilder qb = getStormWorkshopDao().queryBuilder();
            qb = qb.where(qb.or(StormWorkshopDao.Properties.Code.like(keyword),
                            StormWorkshopDao.Properties.Name.like(keyword)));
            workshops = qb.build().list();
        }
        return workshops;
    }

    public StormWorkshop getWorkshop(String code){
        if(code != null) {
            List<StormWorkshop> workshops = getStormWorkshopDao().queryBuilder()
                    .where(StormWorkshopDao.Properties.Code.eq(code))
                    .build()
                    .list();
            assert (workshops.size()<2);
            if(workshops.size()>0) {
                return workshops.get(0);
            }
        }
        return null;
    }

    private StormDeviceDao getStormDeviceDao() {
        if(stormDeviceDao == null) {
            stormDeviceDao = getDaoSession().getStormDeviceDao();
        }

        return stormDeviceDao;
    }

    private StormWorkshopDao getStormWorkshopDao(){
        if(stormWorkshopDao == null) {
            stormWorkshopDao = getDaoSession().getStormWorkshopDao();
        }

        return stormWorkshopDao;
    }

    private DeviceLinkInfoDao getDeviceLinkInfoDao(){
        if(deviceLinkInfoDao == null){
            deviceLinkInfoDao = getDaoSession().getDeviceLinkInfoDao();
        }
        return deviceLinkInfoDao;
    }

    private DeviceDioSignalDao getDeviceDioSignalDao(){
        if(deviceDioSignalDao == null) {
            deviceDioSignalDao = getDaoSession().getDeviceDioSignalDao();
        }
        return deviceDioSignalDao;
    }

    private DeviceAioSignalDao getDeviceAioSignalDao(){
            if(deviceAioSignalDao == null) {
                deviceAioSignalDao = getDaoSession().getDeviceAioSignalDao();
            }
            return deviceAioSignalDao;
        }

    private DCSConnectionDao getDcsConnectionDao(){
        if(dcsConnectionDao == null){
            dcsConnectionDao = getDaoSession().getDCSConnectionDao();
        }
        return dcsConnectionDao;
    }

    private PowerDeviceDao getPowerDeviceDao(){
        if(powerDeviceDao == null){
            powerDeviceDao = getDaoSession().getPowerDeviceDao();
        }
        return powerDeviceDao;
    }

    private CabinetDao getCabinetDao(){
        if(cabinetDao == null) {
            cabinetDao = getDaoSession().getCabinetDao();
        }
        return cabinetDao;
    }
    private DaoSession getDaoSession(){
        if(daoSession == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(StormApp.getContext(),
                    dbPath, null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            daoSession = daoMaster.newSession();
            daoSession.clear();
        }
        return daoSession;
    }
}
