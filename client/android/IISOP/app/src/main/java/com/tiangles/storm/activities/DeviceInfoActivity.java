package com.tiangles.storm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.request.GetSignalParameterRecordRequest;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceInfoActivity extends AppCompatActivity {
    @BindView(R.id.device_code) TextView mDeviceCodeTextView;
    @BindView(R.id.device_name) TextView mDeviceNameView;
    @BindView(R.id.device_model) TextView mDeviceModelView;
    @BindView(R.id.device_system) Button mDeviceSystemView;
    @BindView(R.id.device_parameters) TextView mDeviceParameterView;
    @BindView(R.id.device_distribution_cabinet) TextView mDeviceDistributionCabinetView;
    @BindView(R.id.device_local_control_panel) TextView mDeviceLocalControlPanelView;
    @BindView(R.id.device_dcs_cabinet) TextView mDeviceDcsCabinetView;

    private String mDeviceCode;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        mDeviceCode = intent.getStringExtra("code");
        showDevice(StormApp.getStormDB().getDevice(mDeviceCode));

        mTimer = new Timer();
        int refreshInterval = PreferenceEngine.getInstance().getSignalParameterRefreshInterval();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GetSignalParameterRecordRequest request = new GetSignalParameterRecordRequest(mDeviceCode, new GetSignalParameterRecordRequest.OnSignalParameterRecordListener() {
                    @Override
                    public void onRecord(String deviceCode, String value, String time) {
                        mDeviceParameterView.setText(value);
                    }
                });
                StormApp.getNetwork().sendRequest(request);
            }
        }, refreshInterval, refreshInterval);
    }

    private void showDevice(StormDevice device) {
        mDeviceModelView.setText(device.getModel());
        mDeviceNameView.setText(device.getName());
        mDeviceCodeTextView.setText(device.getCode());
        mDeviceSystemView.setText(device.getSystem());
        mDeviceParameterView.setText("--");
        mDeviceDistributionCabinetView.setText(device.getDistributionCabinet());
        mDeviceLocalControlPanelView.setText(device.getLocalControlPanel());
        mDeviceDcsCabinetView.setText(device.getDcsCabinet());
    }

    public void showSystemInfo(View view) {
        Intent intent = new Intent(DeviceInfoActivity.this, DeviceSystemInfoActivity.class);
        intent.putExtra("device_code", mDeviceCode);
        startActivity(intent);
    }
}
