package com.tiangles.storm.database;

import android.util.Log;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.request.SyncDatabaseRequest;
import com.tiangles.storm.request.SyncDeviceRequest;
import com.tiangles.storm.request.SyncWorkshopListRequest;
import com.tiangles.storm.request.SyncWorkshopRequest;
import com.tiangles.storm.request.UpdateDeviceRequest;

import java.util.List;
import java.util.Vector;

public class DBManager {
    private static String LOG_TAG = "DeviceManager";
    private Vector<DBManager.DBManagerObserver> mDeviceManagerObservers = new Vector<>();
    StormDB stormDB;// = new StormDB(getApplicationContext());

    public interface DBManagerObserver{
        void onSyncWorkshopListDone(List<StormWorkshop> workshops);
        void onSyncWorkshopDone(StormWorkshop workshop);
        void onDeviceUpdated(StormDevice device);
        void onDeviceSynced(StormDevice device);
    }

    public DBManager(){
        StormApp.getNetwork().sendRequest(new SyncDatabaseRequest());
    }

    public StormDB getStormDB(){
        return stormDB;
    }

    public StormDevice getDevice(String code) {
        return getStormDB().getDevice(code);
    }

    public List<StormWorkshop> getWorkshopList(){
        return getStormDB().getWorkshopList();
    }

    public StormWorkshop getWorkshop(String code){
        return getStormDB().getWorkshop(code);
    }


    public void addObserver(DBManager.DBManagerObserver observer) {
        mDeviceManagerObservers.add(observer);
    }

    public void updateDevice(String code){

    }

    public void updateDevice(StormDevice device) {
        UpdateDeviceRequest request = new UpdateDeviceRequest(device);
        StormApp.getNetwork().sendRequest(request);
    }

    public void syncDevice(String code){
        SyncDeviceRequest request = new SyncDeviceRequest(code);
        StormApp.getNetwork().sendRequest(request);
    }

    public void syncDevice(StormDevice device) {
        syncDevice(device.getCode());
    }

    public void syncWorkshopList(){
        SyncWorkshopListRequest request = new SyncWorkshopListRequest();
        StormApp.getNetwork().sendRequest(request);
    }

    public void syncWorkshop(String workshopCode){
        SyncWorkshopRequest request = new SyncWorkshopRequest(workshopCode);
        StormApp.getNetwork().sendRequest(request);
    }

    public void syncWorkshop(StormWorkshop workshop) {
        syncWorkshop(workshop.getCode());
    }

    public void onUpdateDeviceDone(String deviceCode, int result){
        if(result != 0){
            Log.e(LOG_TAG, "Update device failed, device code: "+ deviceCode);
        }
        for(DBManager.DBManagerObserver observer: mDeviceManagerObservers){
            observer.onDeviceUpdated(null);
        }
    }

    public void onSyncDeviceDone(StormDevice device, String deviceCode, int result){
        if(result != 0){
            Log.e(LOG_TAG, "Sync device failed, device code: "+ deviceCode);
        }
        getStormDB().commitDeviceChange(device);
        for(DBManager.DBManagerObserver observer: mDeviceManagerObservers){
            observer.onDeviceSynced(device);
        }
    }

    public void onSyncWorkshopListDone(Vector<StormWorkshop> workshops){
        for(StormWorkshop workshop: workshops){
            getStormDB().commitWorkshopChange(workshop);
        }

        for(DBManager.DBManagerObserver observer: mDeviceManagerObservers){
            observer.onSyncWorkshopListDone(workshops);
        }

    }

    public void onSyncWorkshopDone(StormWorkshop workshop){
        getStormDB().commitWorkshopChange(workshop);
        for(DBManager.DBManagerObserver observer: mDeviceManagerObservers){
            observer.onSyncWorkshopDone(workshop);
        }
    }

    public void onSyncDatabaseDone(String dbFile){
        stormDB = new StormDB(StormApp.getContext(), dbFile);
    }

    public void syncWorkshopDevices(String workshopCode){

    }
}
