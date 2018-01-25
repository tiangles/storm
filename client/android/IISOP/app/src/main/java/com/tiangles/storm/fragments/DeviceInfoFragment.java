package com.tiangles.storm.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.activities.ChartActivity;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.DeviceDioSignal;
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
import butterknife.Unbinder;


public class DeviceInfoFragment extends FragmentBase {
    private Unbinder unbinder;
    @BindView(R.id.title) TitleView mTitleView;
    @BindView(R.id.device_model) NamedLabelView mDeviceModelView;
    @BindView(R.id.device_system) NamedLabelView mDeviceSystemView;
    @BindView(R.id.device_status) NamedLayoutView mDeviceStatusView;
    @BindView(R.id.device_parameter) NamedLayoutView mDeviceParameterLayout;
    private StormDevice mDevice;
    private Map<String, DeviceDioSignal> mDioSignals;
    private Map<String, DeviceAioSignal> mAioSignals;
    private Map<String, DCSConnection> mDCSConnections;
    private Timer mTimer;
    private boolean mFragmentPaused;
    private Map<String, DeviceStatusViewHolder> mDioSignalViews;
    private Map<String, DeviceParameterViewHolder> mAioSignalViews;

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
        mTitleView = null;
        mDeviceModelView = null;
        mDeviceSystemView = null;
        mDeviceModelView = null;
        mDeviceStatusView = null;
        mDeviceParameterLayout = null;
        mDioSignals = null;
        mAioSignals = null;
        mDCSConnections = null;
        mTimer = null;
        mDioSignalViews = null;
        mAioSignalViews = null;
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
        this.registerForContextMenu(mTitleView);
        mDeviceModelView.setLabel(device.getModel());
        mDeviceSystemView.setLabel(device.getSystem());

        mDeviceSystemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StormApp.getMainActivity().showDeviceSystemInfoFragment(mDevice.getCode());
            }
        });
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

    @Override
    public void update() {
        showDevice(mDevice);
    }

    class DeviceStatusViewHolder {
        TextView statusView;
        TextView descriptionView;
    }
    void updateTextViewForDioSignal(final DeviceDioSignal dioSignal, double vaule){
        DeviceStatusViewHolder viewHolder;

        if(mDioSignalViews == null) {
            mDioSignalViews  = new HashMap<>();
        }
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
    class DeviceParameterViewHolder {
        TextView valueView;
        TextView unitView;
        TextView nameView;
    }
    void updateTextViewForAioSignal(final DeviceAioSignal aioSignal, double val){
        DeviceParameterViewHolder viewHolder;
        if(mAioSignalViews == null) {
            mAioSignalViews = new HashMap<>();
        }
        if(mAioSignalViews.containsKey(aioSignal.getCode())) {
            viewHolder = mAioSignalViews.get(aioSignal.getCode());
        } else {
            View view = getActivity().getLayoutInflater().inflate(R.layout.view_device_parameter, null);
            view.setTag(aioSignal.getCode());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(StormApp.getContext(), view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.device_signal_context_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch(menuItem.getItemId()) {
                                case R.id.tendency: {
                                    Intent intent = new Intent(getActivity(), ChartActivity.class);
                                    intent.putExtra("signal_code", aioSignal.getCode());
                                    startActivity(intent);
                                }
                                break;
                                case R.id.connection: {
                                    StormApp.getMainActivity().showDCSCabinetConnectionFragment(aioSignal.getCode());
                                }
                                break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
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
}
