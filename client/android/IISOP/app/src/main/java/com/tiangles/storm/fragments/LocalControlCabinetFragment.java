package com.tiangles.storm.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.views.ThreeColumsListAdaptor;
import com.tiangles.storm.views.TitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LocalControlCabinetFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.title) TitleView mTitleView;
    @BindView(R.id.cabinet_signal_list) ListView mSignalList;
    private LocalControlCabinet mLocalControlCabinet;
    private ThreeColumsListAdaptor mListAdaptor;

    public LocalControlCabinetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_control_cabinet, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(mLocalControlCabinet == null) {
            String code = savedInstanceState.getString("device_code");
            mLocalControlCabinet = StormApp.getDBManager().getStormDB().getLocalControlCabinet(code);
        }
        showCabinet(mLocalControlCabinet);
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
        mSignalList = null;
        mListAdaptor = null;
        mLocalControlCabinet = null;
    }

    public void setCabinet(LocalControlCabinet cabinet){
        mLocalControlCabinet = cabinet;
    }

    private void showCabinet(LocalControlCabinet cabinet){
        mTitleView.setTitle(cabinet.getCode(), cabinet.getName());

        mSignalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ThreeColumsListAdaptor.Model model = (ThreeColumsListAdaptor.Model)mListAdaptor.getItem(i);
            StormApp.getMainActivity().showConnectionPanel(model.getCode());
            }
        });

        mListAdaptor = new ThreeColumsListAdaptor(getActivity());
        mSignalList.addHeaderView(mListAdaptor.createHeaderView(R.string.index, R.string.signal_code, R.string.signal_name));

        List<LocalControlCabinetConnection> connections = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnectionForCabinet(cabinet);
        mListAdaptor.updateByLocalControlCabinetConnection(connections);
        mSignalList.setAdapter(mListAdaptor);
    }
}
