package com.tiangles.storm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.DeviceDioSignal;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.panel.PanelActivity;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.request.GetSignalParameterRecordRequest;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceInfoActivity extends AppCompatActivity {
    @BindView(R.id.device_code) TextView mDeviceCodeTextView;
    @BindView(R.id.device_name) TextView mDeviceNameView;
    @BindView(R.id.device_model) TextView mDeviceModelView;
    @BindView(R.id.device_system) Button mDeviceSystemView;
    @BindView(R.id.device_status) TextView mDeviceStatusView;
    @BindView(R.id.device_parameters) TextView mDeviceParameterView;
    @BindView(R.id.device_distribution_cabinet) TextView mDeviceDistributionCabinetView;
    @BindView(R.id.device_local_control_panel) TextView mDeviceLocalControlPanelView;
    @BindView(R.id.device_dcs_cabinet) TextView mDeviceDcsCabinetView;

    private String mDeviceCode;
    List<DeviceDioSignal> mDioSignals;
    List<DeviceAioSignal> mAioSignals;
    List<DCSConnection> mDCSConnections;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        mDeviceCode = intent.getStringExtra("code");
        showDevice(StormApp.getDBManager().getDevice(mDeviceCode));
    }

    private void showDevice(StormDevice device) {
        mDeviceModelView.setText(device.getModel());
        mDeviceNameView.setText(device.getName());
        mDeviceCodeTextView.setText(device.getCode());
        mDeviceSystemView.setText(device.getSystem());
        mDeviceParameterView.setText("--");
        mDeviceDistributionCabinetView.setText(device.getDistribution_cabinet());
        mDeviceLocalControlPanelView.setText(device.getLocal_control_panel());
        mDeviceDcsCabinetView.setText(device.getDcs_cabinet());

        mDioSignals = StormApp.getDBManager().getDioSignalsForDevice(device.getCode());
        mAioSignals = StormApp.getDBManager().getAioSignalsForDevice(device.getCode());
        mDCSConnections = StormApp.getDBManager().getDCSConnectionsFromSignals(mDioSignals, mAioSignals);
    }

    public void showSystemInfo(View view) {
        Intent intent = new Intent(DeviceInfoActivity.this, DeviceSystemInfoActivity.class);
        intent.putExtra("device_code", mDeviceCode);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        mTimer = new Timer();
        int refreshInterval = PreferenceEngine.getInstance().getSignalParameterRefreshInterval();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GetSignalParameterRecordRequest request = new GetSignalParameterRecordRequest(mDeviceCode, mDCSConnections, new GetSignalParameterRecordRequest.OnSignalParameterRecordListener() {
                    @Override
                    public void onRecord(String deviceCode, Map<String, Double> parameters, String time) {
                        updateParameters(parameters);
                    }
                });
                StormApp.getNetwork().sendRequest(request);
            }
        }, refreshInterval, refreshInterval);
    }

    @OnClick(R.id.device_dcs_cabinet)
    public void showPanelInfo(){
        Intent intent = new Intent(DeviceInfoActivity.this, PanelActivity.class);
        intent.putExtra("device_code", mDeviceCode);
        startActivity(intent);

    }

    @Override
    public void onPause(){
        super.onPause();
        mTimer.cancel();
    }

    private void updateParameters(Map<String, Double> parameters){
        StringBuilder sb = new StringBuilder();
        for(String code: parameters.keySet()){
            DCSConnection connection = StormApp.getDBManager().getDCSConnection(code);
            DeviceDioSignal dioSignal = StormApp.getDBManager().getDeviceDioSignal(code);
            if(dioSignal != null && connection != null && parameters.get(code)>-12){
                mDeviceStatusView.setText(dioSignal.getStatus_when_io_is_1());
            }
            DeviceAioSignal aioSignal = StormApp.getDBManager().getDeviceAioSignal(code);
            if(aioSignal!=null && connection!=null) {
                sb.append(parameters.get(code));
                sb.append("\t");
                sb.append(aioSignal.getUnit());
                sb.append("\t");
                sb.append(aioSignal.getName());
                sb.append("\n");
            }
        }
        mDeviceParameterView.setText(sb.toString());
    }
}
