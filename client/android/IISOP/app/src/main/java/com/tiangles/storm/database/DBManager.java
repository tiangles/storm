package com.tiangles.storm.database;

import android.util.Log;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.DeviceDioSignal;
import com.tiangles.storm.database.dao.DeviceLinkInfo;
import com.tiangles.storm.database.dao.PowerDevice;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.request.SyncDatabaseVersionRequest;
import com.tiangles.storm.request.UpdateDeviceRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import okhttp3.OkHttpClient;

public class DBManager {
    private static String LOG_TAG = "DeviceManager";
    private Vector<DBManager.DBManagerObserver> mDeviceManagerObservers = new Vector<>();
    private StormDB stormDB;
    private String currentVersion = "1.0";

    public interface DBManagerObserver{
        void onSyncWorkshopListDone(List<StormWorkshop> workshops);
        void onSyncWorkshopDone(StormWorkshop workshop);
        void onDeviceUpdated(StormDevice device);
        void onDeviceSynced(StormDevice device);
    }

    public DBManager(){
        currentVersion = PreferenceEngine.getInstance().getCurrentDatabaseVersion();
        StormApp.getNetwork().sendRequest(new SyncDatabaseVersionRequest(currentVersion));
    }

    public StormDB getStormDB(){
        return stormDB;
    }

    public StormDevice getDevice(String code) {
        return getStormDB().getDevice(code);
    }

    public DCSCabinet getDCSCabinet(String code) {
        return getStormDB().getDCSCabinet(code);
    }

    public List<StormDevice> getLeftDevice(StormDevice device) {
        ArrayList<StormDevice> result = new ArrayList<>();
        if(device != null) {
            List<DeviceLinkInfo> leftLink = getStormDB().getLeftLinkInfoForDevice(device);
            if(leftLink != null) {
                for(DeviceLinkInfo info: leftLink) {
                    StormDevice leftDevice = getStormDB().getDevice(info.getLeft_device_id());
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
            List<DeviceLinkInfo> rightLink = getStormDB().getRightLinkInfoForDevice(device);
            if(rightLink != null) {
                for(DeviceLinkInfo info: rightLink) {
                    StormDevice rightDevice = getStormDB().getDevice(info.getRight_device_id());
                    if(rightDevice != null) {
                        result.add(rightDevice);
                    }
                }
            }
        }
        return result;
    }

    public List<StormWorkshop> getWorkshopList(String keyword, int offset, int limit){
        return getStormDB().getWorkshopList(keyword, offset, limit);
    }

    public List<DCSCabinet> getDCSCabinetsForWorkshop(StormWorkshop workshop, String keyword) {
        return getStormDB().getDCSCabinetsForWorkshop(workshop.getCode(), keyword);
    }

    public StormWorkshop getWorkshop(String code){
        return getStormDB().getWorkshop(code);
    }

    public List<StormDevice> getDeviceFromWorkshop(StormWorkshop workshop, String keyword){
        return getStormDB().getDeviceFromWorkshop(workshop.getCode(), keyword);
    }

    public PowerDevice getPowerDevice(String code){
        return getStormDB().getPowerDevice(code);
    }

    public List<DCSConnection> getDCSConnectionsFromSignals(List<DeviceDioSignal> dioSignals, List<DeviceAioSignal> aioSignals){
        List<DCSConnection> connections = new ArrayList<>();

        if(dioSignals != null){
            for(DeviceDioSignal signal: dioSignals) {
                connections.add(getDCSConnection(signal.getCode()));
            }
        }

        if(aioSignals != null){
            for(DeviceAioSignal signal: aioSignals) {
                connections.add(getDCSConnection(signal.getCode()));
            }
        }
        return  connections;
    }

    public List<DeviceDioSignal> getDioSignalsForDevice(String deviceCode){
        return getStormDB().getDioSignalsForDevice(deviceCode);
    }

    public List<DeviceAioSignal> getAioSignalsForDevice(String deviceCode){
        return getStormDB().getAioSignalsForDevice(deviceCode);
    }


    public DCSConnection getDCSConnection(String connectionCode){
        return getStormDB().getDCSConnection(connectionCode);
    }

    public List<DCSConnection> getDCSConnectionsFromCabinetFace(DCSCabinet dcsCabinet, String face) {
        return getStormDB().getDCSConnectionsFromCabinetFace(dcsCabinet, face);
    }

    public DeviceDioSignal getDeviceDioSignal(String code){
        return getStormDB().getDeviceDioSignal(code);
    }

    public DeviceAioSignal getDeviceAioSignal(String code){
        return getStormDB().getDeviceAioSignal(code);
    }


    public void addObserver(DBManager.DBManagerObserver observer) {
        mDeviceManagerObservers.add(observer);
    }

    public void updateDevice(StormDevice device) {
        UpdateDeviceRequest request = new UpdateDeviceRequest(device);
        StormApp.getNetwork().sendRequest(request);
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

    public void onSyncDatabaseDone(String version, String url){
        String dbPath = databaseFilePath();
        File dbFile = new File(dbPath);
        if(!version.equals(currentVersion) || !dbFile.exists()) {
            runHttpRequest(version, url);
        } else {
            createDababaseSession(dbPath);
        }
    }

    private String databaseFilePath( ) {
        File dir = StormApp.getContext().getFilesDir();
//        File dir =  Environment.getExternalStorageDirectory();
        return dir.getAbsolutePath() + "/storm.db";
    }

    private void runHttpRequest(final String version, final String url) {
        final Thread t = new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String dbFile = databaseFilePath();
                        Log.e("database", "store database file to "+ dbFile);
                        byte[] buf = new byte[2048];
                        int len = 0;

                        InputStream is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(dbFile);
                        if(file.exists()) {
                            file.delete();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        currentVersion = version;
                        PreferenceEngine.getInstance().setCurrentDatabaseVersion(currentVersion);
                        createDababaseSession(dbFile);
                    }
                } catch (IOException e) {
                    ///TODO: handle network error
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void createDababaseSession(String path){
        stormDB = new StormDB(StormApp.getContext(), path);
    }
}
