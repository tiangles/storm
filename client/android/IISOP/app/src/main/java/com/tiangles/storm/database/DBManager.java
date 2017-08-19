package com.tiangles.storm.database;

import android.util.Log;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.request.SyncDeviceRequest;
import com.tiangles.storm.request.UpdateDeviceRequest;

import java.util.Vector;

public class DBManager {
    private static String LOG_TAG = "DeviceManager";
    private Vector<DBManager.DBManagerObserver> mDeviceManagerObservers = new Vector<>();

    public interface DBManagerObserver{
        void onDeviceUpdated(StormDevice device);
        void onDeviceSynced(StormDevice device);
    }

    public DBManager(){

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
        SyncDeviceRequest request = new SyncDeviceRequest(code, null);
        StormApp.getNetwork().sendRequest(request);
    }

    public void syncDevice(StormDevice device) {

    }

    public StormDevice getDevice(String code) {
        return StormApp.getStormDB().getDevice(code);
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
        StormApp.getStormDB().commitDeviceChange(device);
        for(DBManager.DBManagerObserver observer: mDeviceManagerObservers){
            observer.onDeviceSynced(device);
        }
    }}
