package com.tiangles.storm.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.activities.ChartActivity;
import com.tiangles.storm.activities.DeviceSystemInfoActivity;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.DeviceDioSignal;
import com.tiangles.storm.database.dao.PowerDevice;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.request.GetSignalParameterRecordRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class DeviceInfoFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.device_code) TextView mDeviceCodeTextView;
    @BindView(R.id.device_name) TextView mDeviceNameView;
    @BindView(R.id.device_model) TextView mDeviceModelView;
    @BindView(R.id.device_system) Button mDeviceSystemView;
    @BindView(R.id.device_status) TextView mDeviceStatusView;
    @BindView(R.id.device_distribution_cabinet) TextView mDeviceDistributionCabinetView;
    @BindView(R.id.device_inspection_records) TextView mDeviceInspectionRecordsView;
    @BindView(R.id.device_parameters_layout) LinearLayout mDeviceParameterLayout;
    StormDevice mDevice;
    Map<String, DeviceDioSignal> mDioSignals;
    Map<String, DeviceAioSignal> mAioSignals;
    Map<String, DCSConnection> mDCSConnections;
    private Timer mTimer;
    boolean mFragmentPaused;

    public DeviceInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);

        unbinder = ButterKnife.bind(this, view);

        if(mDevice == null) {
            Bundle bundle = this.getArguments();
            mDevice = StormApp.getDBManager().getDevice(bundle.getString("device_code"));
        }
        showDevice(mDevice);
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

    @Override
    public void onResume(){
        super.onResume();
        mFragmentPaused = false;
        mTimer = new Timer();
        int refreshInterval = PreferenceEngine.getInstance().getSignalParameterRefreshInterval();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GetSignalParameterRecordRequest request = new GetSignalParameterRecordRequest(mDevice.getCode(), mDCSConnections, new GetSignalParameterRecordRequest.OnSignalParameterRecordListener() {
                    @Override
                    public void onRecord(String deviceCode, Map<String, Double> parameters, String time) {
                        if(!mFragmentPaused) {
                            updateParameters(parameters);
                        }
                    }
                });
                StormApp.getNetwork().sendRequest(request);
            }
        }, 0, refreshInterval);
    }

    @Override
    public void onPause(){
        super.onPause();
        mFragmentPaused = true;
        mTimer.cancel();
    }

    public void setDevice(StormDevice device){
        mDevice = device;
    }


    private void showDevice(StormDevice device) {
        List<DeviceDioSignal> dioSignals = StormApp.getDBManager().getStormDB().getDioSignalsForDevice(device.getCode());
        mDioSignals = new HashMap<>(dioSignals.size());
        for(DeviceDioSignal signal: dioSignals){
            mDioSignals.put(signal.getCode(), signal);
        }

        List<DeviceAioSignal> aioSignals = StormApp.getDBManager().getStormDB().getAioSignalsForDevice(device.getCode());
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

        PowerDevice powerDevice = StormApp.getDBManager().getPowerDevice(device.getPower_device_id());
        if(powerDevice != null){
            mDeviceDistributionCabinetView.setText(powerDevice.getName().replace('\n', ' '));
        } else {
            mDeviceDistributionCabinetView.setText("--");
        }
        mDeviceInspectionRecordsView.setText(device.getMaintenance_record());
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
                    updateTextViewForAioSignal(aioSignal, val);
                }
            }
        }
        mDeviceStatusView.setText(dioSB.toString());
    }

    Map<String, TextView> mAioSignalViews = new HashMap<>();
    void updateTextViewForAioSignal(final DeviceAioSignal aioSignal, double val){
        TextView view = null;
        if(mAioSignalViews.containsKey(aioSignal.getCode())) {
            view = mAioSignalViews.get(aioSignal.getCode());
        } else {
            view = new TextView(getActivity());
            view.setTextSize(getResources().getDimensionPixelSize(R.dimen.signal_parameter_text_size));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChartActivity.class);
                    intent.putExtra("signal_code", aioSignal.getCode());
                    startActivity(intent);
                }
            });
            mAioSignalViews.put(aioSignal.getCode(), view);
            mDeviceParameterLayout.addView(view);
        }
        float formatedVal = (float)(Math.round(val*100))/100;
        view.setText(formatedVal + "  " + aioSignal.getUnit() + "      " + aioSignal.getName());
    }

    @OnClick(R.id.device_system)
    void showSystemInfo(){
        Intent intent = new Intent(this.getActivity(), DeviceSystemInfoActivity.class);
        intent.putExtra("device_code", mDevice.getCode());
        startActivity(intent);
    }

}
