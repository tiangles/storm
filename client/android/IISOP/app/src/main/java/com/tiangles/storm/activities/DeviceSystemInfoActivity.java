package com.tiangles.storm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.views.DeviceLinkEditView;
import com.tiangles.storm.views.DeviceLinkView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceSystemInfoActivity extends AppCompatActivity implements DeviceLinkView.OnLinkedDeviceClickedListener{
    @BindView(R.id.device_name) TextView mDeviceNameView;
    @BindView(R.id.device_code) TextView mDeviceCodeView;
    @BindView(R.id.device_link_info) DeviceLinkView mDeviceLinkView;

    @BindView(R.id.device_link_edit_view) DeviceLinkEditView mEditView;

    private int mCurrentLinkedDevice;
    StormDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_system_info);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        String deviceCode = intent.getStringExtra("device_code");

        init(deviceCode);
    }

    private void init(String deviceCode) {
        mDevice = StormApp.getStormDB().getDevice(deviceCode);
        init();
    }

    private void init(){
        if(mDevice != null) {
            mDeviceCodeView.setText(mDevice.getCode());
            mDeviceNameView.setText(mDevice.getName());
            mDeviceLinkView.setDevice(mDevice);
            mDeviceLinkView.setOnDeviceClickedListener(this);
        }

        mEditView.tryToGo();
    }

    @Override
    public boolean onLinkedDeviceClicked(String deviceCode, int linkDirection) {
        mCurrentLinkedDevice = linkDirection;
        if(!deviceCode.isEmpty() && mCurrentLinkedDevice != THIS_DEVICE) {
            mEditView.setDevice(deviceCode, this);
            mEditView.setVisibility(View.VISIBLE);
            return true;
        } else {
            mEditView.tryToGo();
        }
        return false;
    }

    public void onLinkedDeviceChanged(String deviceCode, String newCode) {
        if(deviceCode != newCode) {
            if(newCode.isEmpty() || StormApp.getStormDB().getDevice(newCode) != null) {
                if(mCurrentLinkedDevice == LEFT_DEVICE) {
                    mDevice.setForwardDevice(newCode);
                } else if(mCurrentLinkedDevice == RIGHT_DEVICE) {
                    mDevice.setBackwardDevice(newCode);
                }
                mEditView.tryToGo();
                init();
                StormApp.getDBManager().updateDevice(mDevice);
            } else {
                Toast.makeText(this, R.string.can_not_find_device, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void switchToDevice(String deviceCode){
        init(deviceCode);
    }
}
