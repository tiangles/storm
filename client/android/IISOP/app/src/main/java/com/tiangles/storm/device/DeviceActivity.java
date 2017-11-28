package com.tiangles.storm.device;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.StormDevice;

public class DeviceActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private String mDeviceCode;
    private DeviceInfoFragment mDeviceFragment;
    private DCSCabinetFragment mDCSCabinetFragment;
    private LocalControlCabinetFragment mLocalControlCabinetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_main);
        mFragmentManager = getFragmentManager();

        Intent intent = this.getIntent();
        mDeviceCode = intent.getStringExtra("code");

        StormDevice device = StormApp.getDBManager().getDevice(mDeviceCode);
        if(device != null) {
            showStormDevice(device);
            return;
        }

        DCSCabinet dcsCabinet = StormApp.getDBManager().getDCSCabinet(mDeviceCode);
        if(dcsCabinet != null) {
            showDCSCabinet(dcsCabinet);
            return;
        }

        LocalControlCabinet localControlCabinet = StormApp.getDBManager().getStormDB().getLocalControlCabinet(mDeviceCode);
        if(localControlCabinet != null) {
            showLocalControlCabinet(localControlCabinet);
            return;
        }
    }

    private void showStormDevice(StormDevice device){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mDeviceFragment == null) {
            mDeviceFragment = new DeviceInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("device_code", device.getCode());
            mDeviceFragment.setArguments(bundle);
            mDeviceFragment.setDevice(device);
            transaction.add(R.id.content, mDeviceFragment);
        } else {
            transaction.show(mDeviceFragment);
        }
        transaction.commit();
    }

    private void showDCSCabinet(DCSCabinet dcsCabinet){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mDCSCabinetFragment == null) {
            mDCSCabinetFragment = new DCSCabinetFragment();
            Bundle bundle = new Bundle();
            bundle.putString("device_code", dcsCabinet.getCode());
            mDCSCabinetFragment.setArguments(bundle);
            mDCSCabinetFragment.setCabinet(dcsCabinet);
            transaction.add(R.id.content, mDCSCabinetFragment);
        } else {
            transaction.show(mDCSCabinetFragment);
        }
        transaction.commit();
    }

    private void showLocalControlCabinet(LocalControlCabinet cabinet){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mLocalControlCabinetFragment == null) {
            mLocalControlCabinetFragment = new LocalControlCabinetFragment();
            Bundle bundle = new Bundle();
            bundle.putString("device_code", cabinet.getCode());
            mLocalControlCabinetFragment.setArguments(bundle);
            mLocalControlCabinetFragment.setCabinet(cabinet);
            transaction.add(R.id.content, mLocalControlCabinetFragment);
        } else {
            transaction.show(mLocalControlCabinetFragment);
        }
        transaction.commit();
    }
}
