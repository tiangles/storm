package com.tiangles.storm.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.DeviceAioSignal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DCSCabinetConnectionFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.cabinet_code) TextView mCabinetCodeView;
    @BindView(R.id.clamp_name) TextView mCabinetNameView;
    @BindView(R.id.channel) TextView mChannelView;
    @BindView(R.id.signal_code) TextView mSignalCodeView;
    @BindView(R.id.terminals) TextView mTerminalsView;
    @BindView(R.id.connection_method) TextView mConnectionMethodView;
    @BindView(R.id.cable_summary) TextView mCableSummaryView;
    @BindView(R.id.cabinet_direction) TextView mCableDirectionView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgent_dcs_cabinet_connection_panel, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        String connectionCode = bundle.getString("connection_code");

        showConnection(connectionCode);

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

    private void showConnection(String connectionCode){
        DCSConnection connection = StormApp.getDBManager().getStormDB().getDCSConnection(connectionCode);
        if(connection != null) {
            String dcsCabinetCode = connection.getDcs_cabinet_number();
            mCabinetCodeView.setText(dcsCabinetCode);
            DCSCabinet cabinet = StormApp.getDBManager().getStormDB().getDCSCabinet(dcsCabinetCode);
            mCabinetNameView.setText(cabinet.getUsage());
            mChannelView.setText(connection.getChannel());
            mSignalCodeView.setText(connection.getCode());
            mTerminalsView.setText(connection.getTerminal_a() + "  " + connection.getTerminal_b() + "  " + connection.getTerminal_c());

            DeviceAioSignal signal = StormApp.getDBManager().getStormDB().getDeviceAioSignal(connection.getCode());
            if(signal != null) {
                mConnectionMethodView.setText(DeviceAioSignal.createConnectionMethod(signal));
            } else {
                mConnectionMethodView.setText("--");
            }


            mCableSummaryView.setText(connection.getCable_code() + "  " + connection.getCable_model());
            mCableDirectionView.setText(connection.getCable_direction());
        }
    }
}
