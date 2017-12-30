package com.tiangles.storm.views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.fragments.DeviceSystemInfoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceLinkEditView extends LinearLayout {

    @BindView(R.id.device_code) EditText mDeviceCode;
    @BindView(R.id.device_name) TextView mDeviceName;
    @BindView(R.id.edit_linked_device) Button mEditLinkedDeviceBtn;
    @BindView(R.id.view_linked_device) Button mViewLinkedDeviceBtn;

    private  DeviceSystemInfoFragment mDeviceSystemInfoFragment;
    private StormDevice mStormDevice;
    private boolean mEditing;

    public DeviceLinkEditView(Context context) {
        super(context);
        init(context);
    }

    public DeviceLinkEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DeviceLinkEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_edit_device_link, this, true);
        ButterKnife.bind(this);
        mEditLinkedDeviceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditing = !mEditing;
                updateButtonState();
            }
        });

        mViewLinkedDeviceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditing) {
                    mEditing = false;
//                    mDeviceSystemInfoFragment.onLinkedDeviceChanged(mStormDevice.getCode(), mDeviceCode.getText().toString());
                } else {
//                    mDeviceSystemInfoFragment.switchToDevice(mStormDevice.getCode());
                }
                updateButtonState();
            }
        });
    }

    public void setDevice(String deviceCode, DeviceSystemInfoFragment fragment) {
        mStormDevice = StormApp.getDBManager().getStormDB().getDevice(deviceCode);
        mDeviceSystemInfoFragment = fragment;
        if(mStormDevice != null) {
            mDeviceCode.setText(mStormDevice.getCode());
            mDeviceName.setText(mStormDevice.getName());
        }
        mEditing = false;
    }

    private void updateButtonState(){
        if(!mEditing) {
            mEditLinkedDeviceBtn.setText(R.string.edit);
            mViewLinkedDeviceBtn.setText(R.string.view);
        } else {
            // switch to edit status
            mEditLinkedDeviceBtn.setText(R.string.cancel);
            mViewLinkedDeviceBtn.setText(R.string.ok);
        }
        mDeviceCode.setEnabled(mEditing);
    }

    public void tryToGo(){
        if(!mEditing) {
            setVisibility(GONE);
        }
    }

}
