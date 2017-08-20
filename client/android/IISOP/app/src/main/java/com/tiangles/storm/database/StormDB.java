package com.tiangles.storm.database;

import android.content.Context;
import android.os.Environment;

import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.greendao.gen.StormWorkshopDao;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.activities.DeviceInfoActivity;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;

public class StormDB {
    private DaoSession daoSession;
    private StormDeviceDao stormDeviceDao;
    private StormWorkshopDao stormWorkshopDao;
    private String dbPath;

    public StormDB(Context context) {
        dbPath = createDatabaseFile(context);
    }

    public String createDatabaseFile(Context context) {
//        File dir = context.getFilesDir();
        File dir =  Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath() + "/storm.db";
//        File f = new File(path);
////        f.delete();
//        if(!f.exists()) {
//            try {
//                InputStream in = context.getAssets().open("storm.db");
//
//                OutputStream out = new FileOutputStream(path);
//                byte[] buffer = new byte[1024];
//                int byteread = 0;
//                while ( (byteread = in.read(buffer)) != -1) {
//                    out.write(buffer, 0, byteread);
//                }
//                in.close();
//                out.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return path;
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

    public List<StormWorkshop> getWorkshopList(){
        List<StormWorkshop> workshops = getStormWorkshopDao().queryBuilder()
                .build()
                .list();
        return workshops;
    }

    public StormWorkshop getWorkshop(String code){
        if(code != null) {
            List<StormWorkshop> workshops = getStormWorkshopDao().queryBuilder()
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

    private DaoSession getDaoSession(){
        if(daoSession == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(StormApp.getContext(),
                    dbPath, null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            daoSession = daoMaster.newSession();

        }
        return daoSession;
    }
}
