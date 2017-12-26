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
import com.tiangles.storm.views.NamedLabelView;
import com.tiangles.storm.views.NamedLayoutView;
import com.tiangles.storm.views.TitleView;

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
    @BindView(R.id.title) TitleView mTitleView;
    @BindView(R.id.device_model) NamedLabelView mDeviceModelView;
    @BindView(R.id.device_system) NamedLabelView mDeviceSystemView;
    @BindView(R.id.device_status) NamedLayoutView mDeviceStatusView;
    @BindView(R.id.device_parameter) NamedLayoutView mDeviceParameterLayout;
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
            mDevice = StormApp.getDBManager().getStormDB().getDevice(bundle.getString("device_code"));
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

        List<DCSConnection> connections = StormApp.getDBManager().getStormDB().getDCSConnectionsFromSignals(dioSignals, aioSignals);
        mDCSConnections = new HashMap<>(connections.size());
        for(DCSConnection dcsConnection: connections){
            mDCSConnections.put(dcsConnection.getCode(), dcsConnection);
        }

        mTitleView.setTitle(device.getCode(), device.getName());
        mDeviceModelView.setLabel(device.getModel());
        mDeviceSystemView.setLabel(device.getSystem());
    }

    private void updateParameters(Map<String, Double> parameters){
        for(String code: parameters.keySet()){
            DCSConnection connection = mDCSConnections.get(code);
            if(connection != null) {
                double val = parameters.get(code);
                DeviceDioSignal dioSignal = mDioSignals.get(code);
                if(dioSignal != null) {
                    updateTextViewForDioSignal(dioSignal, val);
                    continue;
                }
                DeviceAioSignal aioSignal = mAioSignals.get(code);
                if(aioSignal!=null) {
                    updateTextViewForAioSignal(aioSignal, val);
                }
            }
        }
    }

    Map<String, DeviceStatusViewHolder> mDioSignalViews = new HashMap<>();
    class DeviceStatusViewHolder {
        TextView statusView;
        TextView descriptionView;
    }
    void updateTextViewForDioSignal(final DeviceDioSignal dioSignal, double vaule){
        DeviceStatusViewHolder viewHolder;
        if(mDioSignalViews.containsKey(dioSignal.getCode())) {
            viewHolder = mDioSignalViews.get(dioSignal.getCode());
        } else {
            View view = getActivity().getLayoutInflater().inflate(R.layout.view_device_status, null);
            viewHolder = new DeviceStatusViewHolder();
            viewHolder.statusView = (TextView) view.findViewById(R.id.status);
            viewHolder.descriptionView = (TextView) view.findViewById(R.id.description);
            viewHolder.descriptionView.setText(dioSignal.getName());
            mDeviceStatusView.addView(view);
            mDioSignalViews.put(dioSignal.getCode(), viewHolder);
        }
        if(vaule>-12) {
            viewHolder.statusView.setText(dioSignal.getStatus_when_io_is_1());
        } else {
            viewHolder.statusView.setText("--");
        }

    }
    Map<String, DeviceParameterViewHolder> mAioSignalViews = new HashMap<>();
    class DeviceParameterViewHolder {
        TextView valueView;
        TextView unitView;
        TextView nameView;
    }
    void updateTextViewForAioSignal(final DeviceAioSignal aioSignal, double val){
        DeviceParameterViewHolder viewHolder;
        if(mAioSignalViews.containsKey(aioSignal.getCode())) {
            viewHolder = mAioSignalViews.get(aioSignal.getCode());
        } else {
            View view = getActivity().getLayoutInflater().inflate(R.layout.view_device_parameter, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChartActivity.class);
                    intent.putExtra("signal_code", aioSignal.getCode());
                    startActivity(intent);
                }
            });
            viewHolder = new DeviceParameterViewHolder();
            viewHolder.valueView = (TextView) view.findViewById(R.id.value);
            viewHolder.unitView = (TextView) view.findViewById(R.id.unit);
            viewHolder.nameView = (TextView) view.findViewById(R.id.name);

            mAioSignalViews.put(aioSignal.getCode(), viewHolder);
            mDeviceParameterLayout.addView(view);
        }
        float formatedVal = (float)(Math.round(val*100))/100;
        viewHolder.valueView.setText(""+formatedVal);
        viewHolder.unitView.setText(aioSignal.getUnit());
        viewHolder.nameView.setText(aioSignal.getName());
    }

    @OnClick(R.id.device_system)
    void showSystemInfo(){
        Intent intent = new Intent(this.getActivity(), DeviceSystemInfoActivity.class);
        intent.putExtra("device_code", mDevice.getCode());
        startActivity(intent);
    }

}
