package com.tiangles.storm.database;

import android.content.Context;

import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.UserEventDao;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.UserEvent;
import com.tiangles.storm.user.User;

import java.util.List;

public class UserDB {
    private String dbPath;
    private DaoSession daoSession;
    private UserEventDao userEventDao;

    public UserDB(Context context, String dbPath) {
        this.dbPath = dbPath;
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

    public void commitUserEventChanges(UserEvent event){
        if(event != null){
            if(getUserEvent(event.getId()) != null) {
                getUserEventDao().update(event);
            } else {
                getUserEventDao().insert(event);
            }
        }
    }

    public UserEvent getUserEvent(long id) {
        List<UserEvent> events = getUserEventDao().queryBuilder()
                .where(UserEventDao.Properties.Id.eq(id))
                .build()
                .list();
        if(events.size()>0) {
            return events.get(0);
        }
        return null;
    }

    public List<UserEvent> getUserEventByUser(User user) {
        if(user != null) {
            return getUserEventDao().queryBuilder()
                    .where(UserEventDao.Properties.User_id.eq(user.mID))
                    .build()
                    .list();
        }
        return null;
    }

    private UserEventDao getUserEventDao(){
        if(userEventDao==null) {
            userEventDao = getDaoSession().getUserEventDao();
        }
        return userEventDao;
    }

}
