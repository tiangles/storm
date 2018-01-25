package com.tiangles.storm.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.views.DeviceLinkEditView;
import com.tiangles.storm.views.DeviceLinkView;
import com.tiangles.storm.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DeviceSystemInfoFragment extends FragmentBase implements DeviceLinkView.OnLinkedDeviceClickedListener{
    private Unbinder unbinder;
    @BindView(R.id.title) TitleView mTitleView;
    @BindView(R.id.device_link_info) DeviceLinkView mDeviceLinkView;
//    @BindView(R.id.device_link_edit_view) DeviceLinkEditView mEditView;

    private String mDeviceCode;
    private int mCurrentLinkedDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_system_info, container, false);
        unbinder = ButterKnife.bind(this, view);

        update();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setDeviceCode(String code){
        mDeviceCode = code;
    }

    @Override
    public void update() {
        StormDevice device = StormApp.getDBManager().getStormDB().getDevice(mDeviceCode);
        if(device != null) {
            mTitleView.setTitle(device.getCode(), device.getName());
            mDeviceLinkView.setDevice(device);
            mDeviceLinkView.setOnDeviceClickedListener(this);
        }
    }


    @Override
    public boolean onLinkedDeviceClicked(String deviceCode, int linkDirection) {
        mCurrentLinkedDevice = linkDirection;
        showDevice(deviceCode);
        return false;
    }

    private void showDevice(String deviceCode){
        setDeviceCode(deviceCode);
    }

}
