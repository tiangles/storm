package com.tiangles.storm.device;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.panel.PanelActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LocalControlCabinetFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.cabinet_code) TextView mCabinetCodeTextView;
    @BindView(R.id.cabinet_name) TextView mCabinetNameView;
    @BindView(R.id.cabinet_signals) LinearLayout mSignalsLayout;

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
        for(final LocalControlCabinetConnection connection: connections) {
            TextView view = new TextView(this.getActivity());
            view.setText(connection.getCode() + " " + connection.getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PanelActivity.class);
                    intent.putExtra("connection_code", connection.getCode());
                    intent.putExtra("content_type", "local_control_connection");
                    startActivity(intent);
                }
            });
            mSignalsLayout.addView(view);
        }
    }
}
