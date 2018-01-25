package com.tiangles.storm.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.database.dao.LocalControlCabinetTerminal;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.views.NamedLabelView;
import com.tiangles.storm.views.TitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConnectionDetailFragment extends FragmentBase {
    private Unbinder unbinder;
    @BindView(R.id.title) TitleView mTitleView;
    @BindView(R.id.device_model) NamedLabelView mDeviceModelView;
    @BindView(R.id.local_control_cabinet) NamedLabelView mLocalControlCabinetView;
    @BindView(R.id.dcs_connection) NamedLabelView mDcsConnectionView;
    @BindView(R.id.cable) NamedLabelView mCableView;
    @BindView(R.id.cable_connection_method) NamedLabelView mCableConnectionMethod;

    private String mConnectionCode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgent_connection_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        showConnection(mConnectionCode);

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

    public void setConnectionCode(String code){
        mConnectionCode = code;
    }

    private void showConnection(String connectionCode){
        StormDevice device = StormApp.getDBManager().getStormDB().getDevice(connectionCode);
        if(device != null) {
            mDeviceModelView.setLabel(device.getType());
        } else {
            mDeviceModelView.setLabel("--");
        }

        DCSConnection dcsConnection = StormApp.getDBManager().getStormDB().getDCSConnection(connectionCode);

        if(dcsConnection != null) {
            mTitleView.setTitle(dcsConnection.getCode(), dcsConnection.getDescription());

            String dcsCabinetCode = dcsConnection.getDcs_cabinet_number();
            DCSCabinet dcsCabinet = StormApp.getDBManager().getStormDB().getDCSCabinet(dcsCabinetCode);
            if(dcsCabinet != null) {
                mDcsConnectionView.setLabel(dcsCabinet.getCode()
                        + "\n" + dcsCabinet.getUsage()
                        + "\n" + dcsConnection.getFace_name() + "面 " + dcsConnection.getClamp() + "号卡 " + dcsConnection.getChannel() + "通道"
                        + "\n端子: " + dcsConnection.getTerminal_a() + "  " + dcsConnection.getTerminal_b() + "  " + dcsConnection.getTerminal_c());
            }

            DeviceAioSignal signal = StormApp.getDBManager().getStormDB().getDeviceAioSignal(dcsConnection.getCode());
            if(signal != null) {
                mCableConnectionMethod.setLabel(DeviceAioSignal.createConnectionMethod(signal));
            } else {
                mCableConnectionMethod.setLabel("--");
            }

            mCableView.setLabel("电缆编号: " + dcsConnection.getCable_code() + "\n电缆型号: " + dcsConnection.getCable_model());
        }

        LocalControlCabinetConnection localControlCabinetConnection = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnection(connectionCode);
        if(localControlCabinetConnection != null) {
            String cabinetCode = localControlCabinetConnection.getCabinet_id();
            LocalControlCabinet cabinet = StormApp.getDBManager().getStormDB().getLocalControlCabinet(cabinetCode);
            if(cabinet != null) {
                List<LocalControlCabinetTerminal> terminals = StormApp.getDBManager().getStormDB().getLocalControlCabinetTerminalForConnection(localControlCabinetConnection);
                StringBuilder sb = new StringBuilder();
                sb.append(cabinet.getCode());
                sb.append("\n");
                sb.append(cabinet.getName());
                sb.append("\n");
                sb.append("端子: ");
                for(LocalControlCabinetTerminal terminal: terminals) {
                    sb.append(terminal.getCabinet_terminal());
                    sb.append(" ");
                }
                mLocalControlCabinetView.setLabel(sb.toString());
            }
        }
    }

    @Override
    public void update() {
        showConnection(mConnectionCode);
    }
}
