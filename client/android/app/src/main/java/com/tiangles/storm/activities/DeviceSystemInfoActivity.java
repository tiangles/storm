package com.tiangles.storm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;
import com.tiangles.storm.database.device.StormDevice;
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

        init(null);
    }

    private void init(StormDevice device) {
        mForwardDevice.setImageId(R.drawable.power_switch);
        mForwardDevice.setTitle("前相邻设备");
        mForwardDevice.setDeviceCode("10HNA30AA001");
        mForwardDevice.setDeviceName("#1机组#1引风机入口电动门");

        mThisDevice.setImageId(R.drawable.this_device);
        mThisDevice.setTitle("本设备");
        mThisDevice.setDeviceCode("10HNA30AA001");
        mThisDevice.setDeviceName("#1机组#1引风机");


        mBackwordDevice.setImageId(R.drawable.power_switch);
        mBackwordDevice.setTitle("后相邻设备");
        mBackwordDevice.setDeviceCode("10HNA30AA001");
        mBackwordDevice.setDeviceName("#1机组#1引风机出口电动门");
    }

}
