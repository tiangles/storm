package com.tiangles.storm.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.views.DeviceSystemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceSystemInfoActivity extends AppCompatActivity {
    @BindView(R.id.device_link_forward) DeviceSystemView mForwardDeviceView;
    @BindView(R.id.device_link_this) DeviceSystemView mThisDeviceView;
    @BindView(R.id.device_link_backword) DeviceSystemView mBackwardDeviceView;
    @BindView(R.id.device_link_edit) Button mEditButton;
    @BindView(R.id.device_name) TextView mDeviceNameView;
    @BindView(R.id.device_code) TextView mDeviceCodeView;

    StormDevice mDevice;
    boolean mEditing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_system_info);
        ButterKnife.bind(this);
        mForwardDeviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToDevice(mDevice.getForwardDevice());
            }
        });
        mBackwardDeviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToDevice(mDevice.getBackwardDevice());
            }
        });

        Intent intent = this.getIntent();
        String deviceCode = intent.getStringExtra("device_code");

        mDevice = StormApp.getStormDB().getDevice(deviceCode);
        init();
    }

    private void init() {
        if(mDevice != null) {
            StormDevice forward = StormApp.getStormDB().getDevice(mDevice.getForwardDevice());
            StormDevice backward = StormApp.getStormDB().getDevice(mDevice.getBackwardDevice());

            setupDeviceInfo(forward, mForwardDeviceView, "前相邻设备");
            setupDeviceInfo(mDevice, mThisDeviceView, "本设备");
            setupDeviceInfo(backward, mBackwardDeviceView, "后相邻设备");

            mDeviceCodeView.setText(mDevice.getCode());
            mDeviceNameView.setText(mDevice.getName());
        }
    }

    private int getImage(String legend) {
        if(legend.equals("power_switch")) {
            return R.drawable.power_switch;
        } else if(legend.equals("this_device")) {
            return R.drawable.this_device;
        }
        return R.drawable.null_device;
    }

    private void setupDeviceInfo(StormDevice device, DeviceSystemView view, String title){
        view.setTitle(title);
        if(device != null) {
            view.setImageId(getImage(device.getLegend()));
            view.setDeviceCode(device.getCode());
            view.setDeviceName(device.getName());
        } else {
            view.setImageId(R.drawable.null_device);
            view.setDeviceCode("");
            view.setDeviceName("");
        }
    }

    public void onEditLinkInfo(View v){
        if(mDevice!=null && !mEditing) {
            mForwardDeviceView.setEditable(true);
            mBackwardDeviceView.setEditable(true);
            mEditButton.setText(R.string.cancel);
            mEditing = true;
        } else {
            onEditLinkInfoCanceled();
        }
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
        String forward = mDevice.getForwardDevice();
        String backward = mDevice.getBackwardDevice();

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
        mEditButton.setText(R.string.edit);
        mEditing = false;
    }

    public void onEditLinkInfoCanceled() {
        String forward = mDevice.getForwardDevice();
        String backward = mDevice.getBackwardDevice();

        if(!forward.equals(mForwardDeviceView.getDeviceCode().toString()) ||
                !backward.equals(mBackwardDeviceView.getDeviceCode().toString()) ) {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(DeviceSystemInfoActivity.this);
            normalDialog.setTitle("提示");
            normalDialog.setMessage("放弃未保存的修改？");
            normalDialog.setPositiveButton("放弃", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    doCancelEdit();
                }
            });
            normalDialog.setNegativeButton("不放弃", null);
            normalDialog.show();
        } else {
            doCancelEdit();
        }
    }

    private void doCancelEdit(){
        mForwardDeviceView.setEditable(false);
        mBackwardDeviceView.setEditable(false);
        mEditButton.setText(R.string.edit);
        init();
        mEditing = false;
    }

    private void showPromoteDialog(String message){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(DeviceSystemInfoActivity.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", null);
        normalDialog.show();
    }

    private void changeToDevice(String deviceCode){
        if(deviceCode==null || deviceCode.isEmpty()) {
            return;
        }
        if(!mEditing) {
            mDevice = StormApp.getStormDB().getDevice(deviceCode);
            init();
        } else {

        }
    }
}
