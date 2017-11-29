package com.tiangles.storm.device;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LocalControlCabinetFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.cabinet_code) TextView mCabinetCodeTextView;
    @BindView(R.id.cabinet_name) TextView mCabinetNameView;
    @BindView(R.id.cabinet_signals) TextView mCabinetSignalsView;

    LocalControlCabinet mLocalControlCabinet;
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
    }

    public void setCabinet(LocalControlCabinet cabinet){
        mLocalControlCabinet = cabinet;
    }

    private void showCabinet(LocalControlCabinet cabinet){
        mCabinetCodeTextView.setText(cabinet.getCode());
        mCabinetNameView.setText(cabinet.getName());
        List<LocalControlCabinetConnection> connections = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnectionForCabinet(cabinet);
        StringBuilder sb = new StringBuilder();
        for(LocalControlCabinetConnection connection: connections) {
            sb.append(connection.getCode());
            sb.append(" ");
            sb.append(connection.getName());
            sb.append("\n");
        }
        mCabinetSignalsView.setText(sb.toString());
    }
}
