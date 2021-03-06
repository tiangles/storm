package com.tiangles.storm.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.views.NamedLabelView;
import com.tiangles.storm.views.NamedLayoutView;
import com.tiangles.storm.views.TitleView;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DCSCabinetFragment extends FragmentBase {
    private Unbinder unbinder;
    @BindView(R.id.title)TitleView mTitleView;
    @BindView(R.id.cabinet_name) NamedLabelView mCabinetNameView;
    @BindView(R.id.cabinet_controller) NamedLabelView mCabinetControllerView;
    @BindView(R.id.location) NamedLabelView mLocationView;
    @BindView(R.id.cabinet_clamps) NamedLayoutView mCabinetClamps;
    private DCSCabinet mDCSCabinet;

    public DCSCabinetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dcs_cabinet, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(mDCSCabinet != null) {
            showCabinet(mDCSCabinet);
        }
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
        mCabinetNameView = null;
        mCabinetControllerView = null;
        mLocationView = null;
        mCabinetClamps = null;
    }

    public void setCabinet(DCSCabinet dcsCabinet){
        mDCSCabinet = dcsCabinet;
    }

    private void showCabinet(DCSCabinet dcsCabinet){
        mTitleView.setTitle(dcsCabinet.getCode(), dcsCabinet.getUsage());
        mCabinetNameView.setLabel(dcsCabinet.getUsage());
        mCabinetControllerView.setLabel("DPU31");

        StormWorkshop workshop = StormApp.getDBManager().getStormDB().getWorkshop(mDCSCabinet.getWorkshop_id());
        if(workshop != null) {
            mLocationView.setLabel(workshop.getCode() + "\n" + workshop.getName());
        }

        mCabinetClamps.addView(createFaceText(dcsCabinet, "F"));
        mCabinetClamps.addView(createFaceText(dcsCabinet, "B"));
    }

    private LinearLayout createFaceText(final DCSCabinet dcsCabinet, final String face){
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        List<DCSConnection> dcsConnections = StormApp.getDBManager().getStormDB().getDCSConnectionsFromCabinetFace(dcsCabinet, face);
        SortedSet<Integer> clamps = new TreeSet<>();
        for(DCSConnection connection: dcsConnections){
            clamps.add(connection.getClamp());
        }

        layout.addView(newTextView(face + "面:   "), params);
        for(final int clamp: clamps){
            TextView view = newTextView("" + clamp + ",   ");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                StormApp.getMainActivity().showDcsClampFragment(dcsCabinet.getCode(), face, clamp);
                }
            });
            layout.addView(view, params);
        }
        return layout;
    }

    private TextView newTextView(String str){
        TextView view = new TextView(getActivity());
        view.setText(str);
        return view;
    }

    @Override
    public void update() {
        showCabinet(mDCSCabinet);
    }
}
