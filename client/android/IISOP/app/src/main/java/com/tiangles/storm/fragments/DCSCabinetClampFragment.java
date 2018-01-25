package com.tiangles.storm.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.views.ThreeColumsListAdaptor;
import com.tiangles.storm.views.TitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DCSCabinetClampFragment extends FragmentBase {
    private Unbinder unbinder;
    @BindView(R.id.title) TitleView mTitleView;
    @BindView(R.id.signal_list) ListView mSignalListView;
    private String cabinetCode;
    private String face;
    private int clamp;

    public DCSCabinetClampFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dcs_cabinet_clamp, container, false);
        unbinder = ButterKnife.bind(this, view);

        showClamp(cabinetCode, face, clamp);

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
        mSignalListView = null;
    }

    public void setClamp(String cabinetCode, String face, int clamp){
        this.cabinetCode = cabinetCode;
        this.face = face;
        this.clamp = clamp;
    }

    private void showClamp(String cabinetCode, String face, int clamp){
        DCSCabinet cabinet = StormApp.getDBManager().getStormDB().getDCSCabinet(cabinetCode);
        StringBuilder sb = new StringBuilder();
        sb.append(cabinet.getUsage());
        sb.append(face);
        sb.append("面");
        sb.append(clamp);
        sb.append("号卡件");
        mTitleView.setTitle(sb.toString(), cabinetCode);

        final List<DCSConnection> connections = StormApp.getDBManager().getStormDB().getDCSConnectionFromClamp(cabinetCode, face, clamp);
        final ThreeColumsListAdaptor adaptor = new ThreeColumsListAdaptor(getActivity());
        adaptor.updateByDCSConnection(connections);
        if(mSignalListView.getHeaderViewsCount() == 0) {
            View header = adaptor.createHeaderView(R.string.channel, R.string.signal_code, R.string.signal_name);
            mSignalListView.addHeaderView(header);
        }
        mSignalListView.setAdapter(adaptor);
    }

    @Override
    public void update() {
        showClamp(cabinetCode, face, clamp);
    }
}
