package com.tiangles.storm.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.DeviceDioSignal;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.database.dao.LocalControlCabinetTerminal;
import com.tiangles.storm.database.dao.StormDevice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocalControlCabinetSignalPanelFragment extends FragmentBase {
    private Unbinder unbinder;
    @BindView(R.id.signal_code)TextView mCodeView;
    @BindView(R.id.signal_name) TextView mNameView;
    @BindView(R.id.device_summary) TextView mDeviceSummaryView;
    @BindView(R.id.cabinet_summary) TextView mCabinetSummaryView;
    @BindView(R.id.dcs_cabinet_summary) TextView mDCSCabinetSummaryView;
    @BindView(R.id.cable_summary) TextView mCableView;
    @BindView(R.id.cable_connection_method) TextView mCableConnectionMethodView;

    private DeviceAioSignal mAioSignal;
    private DeviceDioSignal mDioSignal;
    private StormDevice mStormDevice;
    private String mConnectionCode;

    public LocalControlCabinetSignalPanelFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgent_local_control_cabinet_connection_panel, container, false);
        unbinder = ButterKnife.bind(this, view);

        update();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        this.getActivity().setTitle("信号");
    }

    public void setConnectionCode(String code){
        mConnectionCode = code;
    }


    private void getSignal(String code){
        mAioSignal = StormApp.getDBManager().getStormDB().getDeviceAioSignal(code);
        if(mAioSignal == null) {
            mDioSignal = StormApp.getDBManager().getStormDB().getDeviceDioSignal(code);
        }
    }

    private void showAsDioPanal(){
        mStormDevice = StormApp.getDBManager().getStormDB().getDevice(mDioSignal.getFor_device_id());
        mCodeView.setText(mDioSignal.getCode());
        mNameView.setText(mDioSignal.getName());
        if(mStormDevice != null) {
            mDeviceSummaryView.setText(mStormDevice.getCode() + " " + mStormDevice.getName());
        } else {
            mDeviceSummaryView.setText("--");
        }

        mCabinetSummaryView.setText("CabinetSummary");
        mDCSCabinetSummaryView.setText("mDCSCabinetSummaryView");
        mCableView.setText("mCableView");
        mCableConnectionMethodView.setText("mCableConnectionMethodView");
    }

    private void showAsAioPanal(){
        mStormDevice = StormApp.getDBManager().getStormDB().getDevice(mAioSignal.getFor_device_id());
        mCodeView.setText(mAioSignal.getCode());
        mNameView.setText(mAioSignal.getName());

        if(mStormDevice != null) {
            mDeviceSummaryView.setText(mStormDevice.getType());
        } else {
            mDeviceSummaryView.setText("--");
        }

        LocalControlCabinetConnection localControlCabinetConnection = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnection(mAioSignal.getCode());
        LocalControlCabinet  cabinet = StormApp.getDBManager().getStormDB().getLocalControlCabinet(localControlCabinetConnection.getCabinet_id());
        List<LocalControlCabinetTerminal> terminals = StormApp.getDBManager().getStormDB().getLocalControlCabinetTerminalForConnection(localControlCabinetConnection);
        StringBuilder sb = new StringBuilder();
        if(cabinet != null) {
            sb.append(cabinet.getCode());
            sb.append(" ");
            sb.append("端子: ");
            for(LocalControlCabinetTerminal terminal: terminals) {
                sb.append(terminal.getCabinet_terminal());
                sb.append(" ");
            }
        } else {
            sb.append("--");
        }
        mCabinetSummaryView.setText(sb.toString());

        DCSConnection dcsConnection = StormApp.getDBManager().getStormDB().getDCSConnection(mAioSignal.getCode());

        sb = new StringBuilder();
        //CBB05柜 F面 0号卡 8通道,端子29、31
        sb.append(dcsConnection.getDcs_cabinet_number());
        sb.append("柜 ");
        sb.append(dcsConnection.getFace_name());
        sb.append("面 ");
        sb.append(dcsConnection.getChannel());
        sb.append("通道, ");
        sb.append("端子: ");
        sb.append(dcsConnection.getTerminal_a());
        sb.append(", ");
        sb.append(dcsConnection.getTerminal_b());
        sb.append(", ");
        sb.append(dcsConnection.getTerminal_c());
        mDCSCabinetSummaryView.setText(sb.toString());


        mCableView.setText(dcsConnection.getCable_model());


        String connectionMethod = DeviceAioSignal.createConnectionMethod(mAioSignal);
        mCableConnectionMethodView.setText(connectionMethod);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void update() {
        getSignal(mConnectionCode);
        if(mAioSignal != null) {
            showAsAioPanal();
        } else if(mDioSignal != null) {
            showAsDioPanal();
        }
    }
}
