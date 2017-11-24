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
import com.tiangles.storm.database.dao.PowerDevice;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.panel.PanelActivity;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.request.GetSignalParameterRecordRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    @BindView(R.id.device_inspection_records) TextView mDeviceInspectionRecordsView;

    private String mDeviceCode;
    Map<String, DeviceDioSignal> mDioSignals;
    Map<String, DeviceAioSignal> mAioSignals;
    Map<String, DCSConnection> mDCSConnections;
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
        List<DeviceDioSignal> dioSignals = StormApp.getDBManager().getDioSignalsForDevice(device.getCode());
        mDioSignals = new HashMap<>(dioSignals.size());
        for(DeviceDioSignal signal: dioSignals){
            mDioSignals.put(signal.getCode(), signal);
        }

        List<DeviceAioSignal> aioSignals = StormApp.getDBManager().getAioSignalsForDevice(device.getCode());
        mAioSignals = new HashMap<>(aioSignals.size());
        for(DeviceAioSignal signal: aioSignals){
            mAioSignals.put(signal.getCode(), signal);
        }

        List<DCSConnection> connections = StormApp.getDBManager().getDCSConnectionsFromSignals(dioSignals, aioSignals);
        mDCSConnections = new HashMap<>(connections.size());
        for(DCSConnection dcsConnection: connections){
            mDCSConnections.put(dcsConnection.getCode(), dcsConnection);
        }

        mDeviceModelView.setText(device.getModel());
        mDeviceNameView.setText(device.getName());
        mDeviceCodeTextView.setText(device.getCode());
        mDeviceSystemView.setText(device.getSystem());
        mDeviceParameterView.setText("--");

        PowerDevice powerDevice = StormApp.getDBManager().getPowerDevice(device.getPower_device_id());
        if(powerDevice != null){
            mDeviceDistributionCabinetView.setText(powerDevice.getName().replace('\n', ' '));
        } else {
            mDeviceDistributionCabinetView.setText("--");
        }
        mDeviceInspectionRecordsView.setText(device.getInspection_records());
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
        }, 0, refreshInterval);
    }


    @Override
    public void onPause(){
        super.onPause();
        mTimer.cancel();
    }

    private void updateParameters(Map<String, Double> parameters){
        StringBuilder aioSB = new StringBuilder();
        StringBuilder dioSB = new StringBuilder();
        for(String code: parameters.keySet()){
            DCSConnection connection = mDCSConnections.get(code);
            if(connection != null) {
                DeviceDioSignal dioSignal = mDioSignals.get(code);
                if(dioSignal != null) {
                    if(dioSignal != null ){
                        if(parameters.get(code)>-12) {
                            dioSB.append(dioSignal.getStatus_when_io_is_1());
                        } else {
                            dioSB.append("--");
                        }
                        dioSB.append("  ");
                        dioSB.append(dioSignal.getName());
                        dioSB.append("\n");
                    }
                    continue;
                }
                DeviceAioSignal aioSignal = mAioSignals.get(code);
                if(aioSignal!=null) {
                    double val = parameters.get(code);
                    float formatedVal = (float)(Math.round(val*100))/100;
                    aioSB.append(formatedVal);
                    aioSB.append("  ");
                    aioSB.append(aioSignal.getUnit());
                    aioSB.append("  ");
                    aioSB.append(aioSignal.getName());
                    aioSB.append("\n");
                }
            }
        }
        mDeviceStatusView.setText(dioSB.toString());
        mDeviceParameterView.setText(aioSB.toString());
    }
}
