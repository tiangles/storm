package com.tiangles.storm.database;

import android.content.Context;
import android.os.Environment;

import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.activities.DeviceInfoActivity;
import com.tiangles.storm.database.dao.StormDevice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class StormDB {
    private StormDeviceDao stormDeviceDao;
    private String dbPath;

    public StormDB(Context context) {
        dbPath = createDatabaseFile(context);
    }

    public String createDatabaseFile(Context context) {
        File dir = context.getFilesDir();
//        File dir =  Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath() + "/storm.db";
        File f = new File(path);
        if(!f.exists()) {
            try {
                InputStream in = context.getAssets().open("storm.db");

                OutputStream out = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int byteread = 0;
                while ( (byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public StormDeviceDao getStormDeviceDao() {
        if(stormDeviceDao == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(StormApp.getContext(),
                    dbPath, null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            DaoSession daoSession = daoMaster.newSession();
            stormDeviceDao = daoSession.getStormDeviceDao();
        }

        return stormDeviceDao;
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
            getStormDeviceDao().update(device);
        }
    }
}
