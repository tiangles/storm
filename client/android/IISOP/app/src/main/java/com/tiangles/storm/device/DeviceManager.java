package com.tiangles.storm.device;

import android.util.Log;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.request.SyncDeviceRequest;
import com.tiangles.storm.request.UpdateDeviceRequest;

import java.util.Vector;

public class DeviceManager {
    private static String LOG_TAG = "DeviceManager";
    private Vector<DeviceManagerObserver> mDeviceManagerObservers = new Vector<>();

    public interface DeviceManagerObserver{
        void onDeviceUpdated(StormDevice device);
        void onDeviceSynced(StormDevice device);
    }

    public DeviceManager(){

    }

    public void addObserver(DeviceManagerObserver observer) {
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

    }

    public StormDevice getDevice(String code) {
        return StormApp.getStormDB().getDevice(code);
    }

    public void onUpdateDeviceDone(String deviceCode, int result){
        if(result != 0){
            Log.e(LOG_TAG, "Update device failed, device code: "+ deviceCode);
        }
        for(DeviceManagerObserver observer: mDeviceManagerObservers){
            observer.onDeviceUpdated(null);
        }
    }

    public void onSyncDeviceDone(StormDevice device, String deviceCode, int result){
        if(result != 0){
            Log.e(LOG_TAG, "Sync device failed, device code: "+ deviceCode);
        }
        StormApp.getStormDB().commitDeviceChange(device);
        for(DeviceManagerObserver observer: mDeviceManagerObservers){
            observer.onDeviceSynced(device);
        }
    }
}
