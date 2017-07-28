package com.tiangles.storm.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.StormDB;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.views.DeviceSystemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceSystemInfoActivity extends AppCompatActivity {
    @BindView(R.id.device_link_forward) DeviceSystemView mForwardDeviceView;
    @BindView(R.id.device_link_this) DeviceSystemView mThisDeviceView;
    @BindView(R.id.device_link_backword) DeviceSystemView mBackwardDeviceView;
    @BindView(R.id.device_link_edit) Button mEditButtion;

    StormDevice mDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_system_info);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        String deviceCode = intent.getStringExtra("device_code");

        mDevice = StormApp.getStormDB().getDevice(deviceCode);
        init();
    }

    private void init() {
        if(mDevice != null) {
            StormDevice forward = StormApp.getStormDB().getDevice(mDevice.getForwardDevice());
            StormDevice backward = StormApp.getStormDB().getDevice(mDevice.getBackwardDevice());

            setupDeviceInfo(forward, mForwardDeviceView, "前相邻设备", R.drawable.power_switch);
            if(forward != null) {
                setupDeviceInfo(mDevice, mThisDeviceView, "本设备", R.drawable.this_device);
            } else {
                setupDeviceInfo(mDevice, mThisDeviceView, "本设备", R.drawable.power_switch);
            }
            setupDeviceInfo(backward, mBackwardDeviceView, "后相邻设备", R.drawable.power_switch);

        }
    }

    private void setupDeviceInfo(StormDevice device, DeviceSystemView view, String title, int img){
        view.setTitle(title);
        if(device != null) {
            view.setImageId(img);
            view.setDeviceCode(device.getCode());
            view.setDeviceName(device.getName());
        } else {
            view.setDeviceCode("");
            view.setDeviceName("");
            view.setImageId(R.drawable.null_device);
        }
    }

    public void onEditLinkInfo(View v){
        if(mDevice!=null) {
            mForwardDeviceView.setEditable(true);
            mBackwardDeviceView.setEditable(true);
            mEditButtion.setText(R.string.cancel);
        }
    }

    private boolean verifyNewDeviceCode(String newDev) {
        if(newDev.isEmpty()) {
            return  true;
        }

        StormDevice dev = StormApp.getStormDB().getDevice(newDev);
        if(dev != null) {
            return true;
        }

        return false;
    }

    private static int CHANGED = 1;
    private static int UNCHANGED = 0;
    private static int INVALID = -1;

    private int checkNewDevice(DeviceSystemView view, String currDev){
        String newDev = view.getDeviceCode().toString();
        if(newDev.equals(currDev)){
            return UNCHANGED;
        }

        if(newDev.isEmpty()) {
            return CHANGED;
        }

        if(StormApp.getStormDB().getDevice(newDev) == null) {
            return INVALID;
        }

        return CHANGED;
    }

    public void onEditLinkInfoDone(View v) {
        String forward = mForwardDeviceView.getDeviceCode().toString();
        String backward = mBackwardDeviceView.getDeviceCode().toString();

        int forwardRes = checkNewDevice(mForwardDeviceView, forward);
        int backwardRes = checkNewDevice(mBackwardDeviceView, backward);


        if(forwardRes == INVALID ) {
            showPromoteDialog("找不到可用的 前相邻设备！");
            mForwardDeviceView.requestFocus();
            return;
        }
        if(backwardRes == INVALID) {
            showPromoteDialog("找不到可用的 后相邻设备！");
            mBackwardDeviceView.requestFocus();
            return;
        }

        if(forwardRes == CHANGED || backwardRes == CHANGED) {
            mDevice.setForwardDevice(forward);
            mDevice.setBackwardDevice(backward);
            StormApp.getStormDB().commitDeviceChange(mDevice);
            init();
        }

        mForwardDeviceView.setEditable(false);
        mBackwardDeviceView.setEditable(false);
        mEditButtion.setText(R.string.edit);
    }

    private void showPromoteDialog(String message){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(DeviceSystemInfoActivity.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", null);
        normalDialog.show();
    }
}
