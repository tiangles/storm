package com.tiangles.storm.database;

import android.content.Context;

import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.storm.StormApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StormDB {
    private StormDeviceDao stormDeviceDao;
    private String dbPath;

    public StormDB(Context context) {
        dbPath = createDatabaseFile(context);
    }

    public String createDatabaseFile(Context context) {
        File dir = context.getFilesDir();
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
}
