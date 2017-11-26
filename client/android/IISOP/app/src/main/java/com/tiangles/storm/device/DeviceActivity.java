package com.tiangles.storm.device;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.Cabinet;
import com.tiangles.storm.database.dao.StormDevice;

public class DeviceActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private String mDeviceCode;
    private DeviceInfoFragment mDeviceFragment;
    private CabinetFragment mCabinetFragment;

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

        Cabinet cabinet = StormApp.getDBManager().getCabinet(mDeviceCode);
        if(cabinet != null) {
            showCabinet(cabinet);
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

    private void showCabinet(Cabinet cabinet){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCabinetFragment == null) {
            mCabinetFragment = new CabinetFragment();
            Bundle bundle = new Bundle();
            bundle.putString("device_code", cabinet.getCode());
            mCabinetFragment.setArguments(bundle);
            mCabinetFragment.setCabinet(cabinet);
            transaction.add(R.id.content, mCabinetFragment);
        } else {
            transaction.show(mCabinetFragment);
        }
        transaction.commit();
    }
}
