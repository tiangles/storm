package com.tiangles.storm.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.PowerDistributionCabinet;
import com.tiangles.storm.views.NamedLabelView;
import com.tiangles.storm.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PowerDistributionCabinetFragment extends Fragment{
    private Unbinder unbinder;
    private String mCabinetCode;
    private PowerDistributionCabinet mCabinet;
    @BindView(R.id.title)TitleView mTitleView;
    @BindView(R.id.circuit) NamedLabelView mcircuitView;
    @BindView(R.id.circuit_electric_current) NamedLabelView mcircuit_electric_currentView;
    @BindView(R.id.vacuum_breaker_fc_circuit) NamedLabelView mvacuum_breaker_fc_circuitView;
    @BindView(R.id.current_transformer) NamedLabelView mcurrent_transformerView;
    @BindView(R.id.voltage_transformer) NamedLabelView mvoltage_transformerView;
    @BindView(R.id.earthing_switch) NamedLabelView mearthing_switchView;
    @BindView(R.id.earrester) NamedLabelView mearresterView;
    @BindView(R.id.zero_sequence_current_transformer) NamedLabelView mzero_sequence_current_transformerView;
    @BindView(R.id.cable) NamedLabelView mcableView;
    @BindView(R.id.connection_manner) ImageView mconnection_manner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_power_distribution_cabinet_layout, container, false);
        unbinder = ButterKnife.bind(this, view);

        initializeView();
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

    public void setCabinet(PowerDistributionCabinet cabinet){
        mCabinet = cabinet;
    }

    private void initializeView(){
        if(mCabinet != null) {
            mTitleView.setTitle(mCabinet.getCode(), mCabinet.getName());
            mcircuitView.setLabel(mCabinet.getCircuit_name());
            mcircuit_electric_currentView.setLabel(mCabinet.getCircuit_electric_current());
            mvacuum_breaker_fc_circuitView.setLabel(mCabinet.getVacuum_breaker() + "/" + mCabinet.getFc_circuit());
            mcurrent_transformerView.setLabel(mCabinet.getCurrent_transformer());
            mvoltage_transformerView.setLabel(mCabinet.getVoltage_transformer());
            mearthing_switchView.setLabel(mCabinet.getEarthing_switch());
            mearresterView.setLabel(mCabinet.getEarthing_switch());
            mzero_sequence_current_transformerView.setLabel(mCabinet.getZero_sequence_current_transformer());
            mcableView.setLabel("电缆编号: " + mCabinet.getCable_code() + "\n电缆型号: " + mCabinet.getCable_mode());
        }
    }
}
