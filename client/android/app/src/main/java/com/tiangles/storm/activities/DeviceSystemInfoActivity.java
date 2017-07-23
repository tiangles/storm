package com.tiangles.storm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.views.DeviceSystemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceSystemInfoActivity extends AppCompatActivity {
    @BindView(R.id.device_link_forward) DeviceSystemView mForwardDevice;
    @BindView(R.id.device_link_this) DeviceSystemView mThisDevice;
    @BindView(R.id.device_link_backword) DeviceSystemView mBackwordDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_system_info);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        String deviceCode = intent.getStringExtra("device_code");

        init(StormApp.getStormDB().getDevice(deviceCode));
    }

    private void init(StormDevice device) {
        if(device != null) {
            StormDevice forward = StormApp.getStormDB().getDevice(device.getForwardDevice());
            StormDevice backward = StormApp.getStormDB().getDevice(device.getBackwardDevice());

            setupDeviceInfo(forward, mForwardDevice, "前相邻设备", R.drawable.power_switch);
            if(forward != null) {
                setupDeviceInfo(device, mThisDevice, "本设备", R.drawable.this_device);
            } else {
                setupDeviceInfo(device, mThisDevice, "本设备", R.drawable.power_switch);
            }
            setupDeviceInfo(backward, mBackwordDevice, "后相邻设备", R.drawable.power_switch);
        }
    }

    private void setupDeviceInfo(StormDevice device, DeviceSystemView view, String title, int img){
        view.setTitle(title);
        if(device != null) {
            view.setImageId(img);
            view.setDeviceCode(device.getCode());
            view.setDeviceName(device.getName());
        } else {
            view.setImageId(R.drawable.null_device);
        }
    }
}
