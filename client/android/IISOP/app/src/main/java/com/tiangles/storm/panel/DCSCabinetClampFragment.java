package com.tiangles.storm.panel;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DCSCabinetClampFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.cabinet_code) TextView mCabinetCodeTextView;
    @BindView(R.id.cabinet_name) TextView mCabinetNameView;
    @BindView(R.id.content_layout) LinearLayout mContentLayout;

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

        Bundle bundle = this.getArguments();
        String face = bundle.getString("face");
        int clamp = bundle.getInt("clamp", 0);
        String cabinet = bundle.getString("cabinet");

        setClamp(cabinet, face, clamp);

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

    private void setClamp(String cabinetCode, String face, int clamp){
        DCSCabinet cabinet = StormApp.getDBManager().getStormDB().getDCSCabinet(cabinetCode);
        mCabinetCodeTextView.setText(cabinet.getCode());

        StringBuilder sb = new StringBuilder();
        sb.append(cabinet.getUsage());
        sb.append("\n");
        sb.append(face);
        sb.append("面");
        sb.append(clamp);
        sb.append("号卡件");
        mCabinetNameView.setText(sb.toString());

        List<DCSConnection> connections = StormApp.getDBManager().getStormDB().getDCSConnectionFromClamp(cabinetCode, face, clamp);
        for(DCSConnection connection: connections) {
            mContentLayout.addView(newTextView(connection));
        }
    }

    private TextView newTextView(final DCSConnection connection) {
        TextView view = new TextView(getActivity());

        StringBuilder sb = new StringBuilder();
        sb.append(connection.getCode());
        sb.append("  ");
        sb.append(connection.getDescription());
        view.setText(sb.toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PanelActivity activity = (PanelActivity)getActivity();
                activity.showDCSCabinetConnectionFragment(connection.getCode());
            }
        });
        return view;
    }
}
