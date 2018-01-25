package com.tiangles.storm.database;

import android.content.Context;

import com.tiangles.greendao.gen.DCSCabinetDao;
import com.tiangles.greendao.gen.DCSConnectionDao;
import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.DeviceAioSignalDao;
import com.tiangles.greendao.gen.DeviceDioSignalDao;
import com.tiangles.greendao.gen.DeviceLinkInfoDao;
import com.tiangles.greendao.gen.LocalControlCabinetConnectionDao;
import com.tiangles.greendao.gen.LocalControlCabinetDao;
import com.tiangles.greendao.gen.LocalControlCabinetTerminalDao;
import com.tiangles.greendao.gen.PowerDeviceDao;
import com.tiangles.greendao.gen.PowerDistributionCabinetDao;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.greendao.gen.StormWorkshopDao;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.DeviceDioSignal;
import com.tiangles.storm.database.dao.DeviceLinkInfo;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.database.dao.LocalControlCabinetTerminal;
import com.tiangles.storm.database.dao.PowerDevice;
import com.tiangles.storm.database.dao.PowerDistributionCabinet;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
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
    private DCSCabinetDao dcsCabinetDao;
    private LocalControlCabinetDao localControlCabinetDao;
    private LocalControlCabinetConnectionDao localControlCabinetConnectionDao;
    private LocalControlCabinetTerminalDao localControlCabinetTerminalDao;
    private PowerDistributionCabinetDao powerDistributionCabinetDao;
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

    public List<StormDevice> findDevice(String keyword){
        List<StormDevice> devices = null;
        if(keyword != null && !keyword.isEmpty()){
            String query = "%" + keyword+"%";
            QueryBuilder qb = getStormDeviceDao().queryBuilder();
            qb = qb.where(StormDeviceDao.Properties.Code.like(query), StormDeviceDao.Properties.Name.like(query));
            devices = qb.build().list();
        }
        return devices;
    }

    public List<StormDevice> findDeviceInWorkshop(String workshopCode, String keyword){
        List<StormDevice> devices = null;
        if(workshopCode != null) {
            if(keyword == null || keyword.isEmpty()){
                devices = getStormDeviceDao().queryBuilder()
                        .where(StormDeviceDao.Properties.Workshop_id.eq(workshopCode))
                        .build()
                        .list();
            } else {
                QueryBuilder qb = getStormDeviceDao().queryBuilder();
                qb = qb.where(StormDeviceDao.Properties.Workshop_id.eq(workshopCode),
                        qb.or(StormDeviceDao.Properties.Code.like(keyword), StormDeviceDao.Properties.Name.like(keyword)));
                devices = qb.build().list();
            }
        }
        return devices;
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

    public List<DCSConnection> getDCSConnectionsFromCabinetFace(DCSCabinet dcsCabinet, String face) {
        return getDcsConnectionDao().queryBuilder()
                .where(DCSConnectionDao.Properties.Dcs_cabinet_number.eq(dcsCabinet.getCode()),
                       DCSConnectionDao.Properties.Face_name.eq(face))
                .build()
                .list();
    }

    public List<DCSConnection> getDCSConnectionFromClamp(String cabinetCode, String face, int clamp) {
        return getDcsConnectionDao().queryBuilder()
                .where(DCSConnectionDao.Properties.Dcs_cabinet_number.eq(cabinetCode),
                        DCSConnectionDao.Properties.Face_name.eq(face),
                        DCSConnectionDao.Properties.Clamp.eq(clamp))
                .build()
                .list();
    }

    public List<DCSCabinet> getDCSCabinetsForWorkshop(String workshopCode, String keyword) {
        List<DCSCabinet> result = null;
        if(workshopCode != null && !workshopCode.isEmpty()) {
            if(keyword == null || keyword.isEmpty()){
                result = getDCSCabinetDao().queryBuilder()
                        .where(DCSCabinetDao.Properties.Workshop_id.eq(workshopCode))
                        .build()
                        .list();
            } else {
                QueryBuilder qb = getDCSCabinetDao().queryBuilder();
                qb = qb.where(DCSCabinetDao.Properties.Workshop_id.eq(workshopCode), qb.or(DCSCabinetDao.Properties.Code.like(keyword), DCSCabinetDao.Properties.Usage.like(keyword)));
                result = qb.build().list();
            }
        }
        return result;
    }

    public DCSCabinet getDCSCabinet(String code) {
        if(code != null && !code.isEmpty()) {
            List<DCSCabinet> dcsCabinets = getDCSCabinetDao().queryBuilder()
                    .where(DCSCabinetDao.Properties.Code.eq(code))
                    .build()
                    .list();
            if(dcsCabinets.size()>0) {
                return dcsCabinets.get(0);
            }
        }
        return null;
    }


    public LocalControlCabinet getLocalControlCabinet(String code){
        if(code != null && !code.isEmpty()) {
            List<LocalControlCabinet> localControlCabinets = getLocalControlCabinetDao().queryBuilder()
                    .where(LocalControlCabinetDao.Properties.Code.eq(code))
                    .build()
                    .list();
            if(localControlCabinets.size()>0) {
                return localControlCabinets.get(0);
            }
        }
        return null;
    }

    public List<LocalControlCabinet> getLocalControlCabinetForWorkshop(StormWorkshop workshop, String keyword){
        List<LocalControlCabinet> result = null;
        if(workshop!=null){
            if(keyword == null || keyword.isEmpty()){
                result = getLocalControlCabinetDao().queryBuilder()
                        .where(LocalControlCabinetDao.Properties.Workshop_id.eq(workshop.getCode()))
                        .build()
                        .list();
            } else {
                QueryBuilder qb = getLocalControlCabinetDao().queryBuilder();
                qb = qb.where(LocalControlCabinetDao.Properties.Workshop_id.eq(workshop.getCode()),
                        qb.or(LocalControlCabinetDao.Properties.Code.like(keyword), LocalControlCabinetDao.Properties.Name.like(keyword)));
                result = qb.build().list();
            }
        }
        return result;
    }

    public List<PowerDistributionCabinet> getPowerDistributionCabinetForWorkshop(StormWorkshop workshop, String keyword){
        List<PowerDistributionCabinet> result = null;
        if(workshop!=null){
            if(keyword == null || keyword.isEmpty()){
                result = getPowerDistributionCabinetDao().queryBuilder()
                        .where(PowerDistributionCabinetDao.Properties.Workshop_id.eq(workshop.getCode()))
                        .build()
                        .list();
            } else {
                QueryBuilder qb = getPowerDistributionCabinetDao().queryBuilder();
                qb = qb.where(PowerDistributionCabinetDao.Properties.Workshop_id.eq(workshop.getCode()),
                        qb.or(PowerDistributionCabinetDao.Properties.Code.like(keyword), PowerDistributionCabinetDao.Properties.Name.like(keyword)));
                result = qb.build().list();
            }
        }
        return result;
    }

    public LocalControlCabinetConnection getLocalControlCabinetConnection(String code){
        List<LocalControlCabinetConnection> connections = getLocalControlCabinetConnectionDao().queryBuilder()
                .where(LocalControlCabinetConnectionDao.Properties.Code.eq(code))
                .build()
                .list();
        if(connections.size()>0) {
            return connections.get(0);
        }
        return null;
    }

    public List<LocalControlCabinetConnection> getLocalControlCabinetConnectionForCabinet(LocalControlCabinet cabinet){
        List<LocalControlCabinetConnection> connections = null;
        if(cabinet != null) {
            QueryBuilder qb = getLocalControlCabinetConnectionDao().queryBuilder();
            qb.where(LocalControlCabinetConnectionDao.Properties.Cabinet_id.eq(cabinet.getCode()));
            connections = qb.build().list();
        }

        return connections;
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


    public List<StormWorkshop> findWorkshop(String keyword) {
        if(keyword != null && !keyword.isEmpty()) {
            String query = "%" + keyword + "%";
            QueryBuilder qb = getStormWorkshopDao().queryBuilder();
            qb = qb.where(qb.or(StormWorkshopDao.Properties.Code.like(query),
                    StormWorkshopDao.Properties.Name.like(query)));
            return qb.build().list();
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

    public List<LocalControlCabinetTerminal> getLocalControlCabinetTerminalForConnection(LocalControlCabinetConnection connection) {
        List<LocalControlCabinetTerminal> terminals = null;
        if(connection != null) {
            terminals = getLocalControlCabinetTerminalDao().queryBuilder()
                    .where(LocalControlCabinetTerminalDao.Properties.For_connection_id.eq(connection.getCode()))
                    .build()
                    .list();
        }
        return terminals;
    }


    public List<StormDevice> getLeftDevice(StormDevice device) {
        ArrayList<StormDevice> result = new ArrayList<>();
        if(device != null) {
            List<DeviceLinkInfo> leftLink = getLeftLinkInfoForDevice(device);
            if(leftLink != null) {
                for(DeviceLinkInfo info: leftLink) {
                    StormDevice leftDevice = getDevice(info.getLeft_device_id());
                    if(leftDevice != null) {
                        result.add(leftDevice);
                    }
                }
            }
        }
        return result;
    }

    public List<StormDevice> getRightDevice(StormDevice device) {
        ArrayList<StormDevice> result = new ArrayList<>();
        if(device != null) {
            List<DeviceLinkInfo> rightLink = getRightLinkInfoForDevice(device);
            if(rightLink != null) {
                for(DeviceLinkInfo info: rightLink) {
                    StormDevice rightDevice = getDevice(info.getRight_device_id());
                    if(rightDevice != null) {
                        result.add(rightDevice);
                    }
                }
            }
        }
        return result;
    }

    public List<DCSConnection> getDCSConnectionsFromSignals(List<DeviceDioSignal> dioSignals, List<DeviceAioSignal> aioSignals){
        List<DCSConnection> connections = new ArrayList<>();

        if(dioSignals != null){
            for(DeviceDioSignal signal: dioSignals) {
                DCSConnection connection = getDCSConnection(signal.getCode());
                if(connection != null) {
                    connections.add(connection);
                }
            }
        }

        if(aioSignals != null){
            for(DeviceAioSignal signal: aioSignals) {
                DCSConnection connection = getDCSConnection(signal.getCode());
                if(connection != null) {
                    connections.add(connection);
                }
            }
        }
        return  connections;
    }

    public PowerDistributionCabinet getPowerDistributionCabinet(String code){
        if(code != null) {
            List<PowerDistributionCabinet> cabinets = getPowerDistributionCabinetDao().queryBuilder()
                    .where(PowerDistributionCabinetDao.Properties.Code.eq(code))
                    .build()
                    .list();
            assert (cabinets.size()<2);
            if(cabinets.size()>0) {
                return cabinets.get(0);
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

    private DCSCabinetDao getDCSCabinetDao(){
        if(dcsCabinetDao == null) {
            dcsCabinetDao = getDaoSession().getDCSCabinetDao();
        }
        return dcsCabinetDao;
    }

    private LocalControlCabinetDao getLocalControlCabinetDao(){
        if(localControlCabinetDao == null) {
            localControlCabinetDao = getDaoSession().getLocalControlCabinetDao();
        }
        return localControlCabinetDao;
    }

    private LocalControlCabinetConnectionDao getLocalControlCabinetConnectionDao(){
        if(localControlCabinetConnectionDao == null) {
            localControlCabinetConnectionDao = getDaoSession().getLocalControlCabinetConnectionDao();
        }
        return localControlCabinetConnectionDao;
    }

    private LocalControlCabinetTerminalDao getLocalControlCabinetTerminalDao(){
        if(localControlCabinetTerminalDao == null) {
            localControlCabinetTerminalDao = getDaoSession().getLocalControlCabinetTerminalDao();
        }
        return localControlCabinetTerminalDao;
    }

    private PowerDistributionCabinetDao getPowerDistributionCabinetDao(){
        if(powerDistributionCabinetDao == null) {
            powerDistributionCabinetDao = getDaoSession().getPowerDistributionCabinetDao();
        }
        return powerDistributionCabinetDao;
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
