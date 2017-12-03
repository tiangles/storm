package com.tiangles.storm.device;

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
import com.tiangles.storm.panel.PanelActivity;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DCSCabinetFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.cabinet_code) TextView mCabinetCodeTextView;
    @BindView(R.id.cabinet_name) TextView mCabinetNameView;
    @BindView(R.id.cabinet_controller) TextView mCabinetControllerView;
//    @BindView(R.id.cabinet_clamp) TextView mCabinetClampView;
    @BindView(R.id.cabinet_f_clamp) LinearLayout mCabinetFClampLayout;
    @BindView(R.id.cabinet_b_clamp) LinearLayout mCabinetBClampLayout;

    DCSCabinet mDCSCabinet;
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
        if(mDCSCabinet == null) {
            String code = savedInstanceState.getString("device_code");
            mDCSCabinet = StormApp.getDBManager().getDCSCabinet(code);
        }
        showCabinet(mDCSCabinet);
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

    public void setCabinet(DCSCabinet dcsCabinet){
        mDCSCabinet = dcsCabinet;
    }

    private void showCabinet(DCSCabinet dcsCabinet){
        mCabinetCodeTextView.setText(dcsCabinet.getCode());
        mCabinetNameView.setText(dcsCabinet.getUsage());
        mCabinetControllerView.setText("DPU31");

        createFaceText(mCabinetFClampLayout, dcsCabinet, "F");
        createFaceText(mCabinetBClampLayout, dcsCabinet, "B");
    }

    private void createFaceText(LinearLayout layout, final DCSCabinet dcsCabinet, final String face){
        List<DCSConnection> dcsConnections = StormApp.getDBManager().getDCSConnectionsFromCabinetFace(dcsCabinet, face);
        SortedSet<Integer> clamps = new TreeSet<>();
        for(DCSConnection connection: dcsConnections){
            clamps.add(connection.getClamp());
        }

        layout.addView(newTextView(face + "面:   "));
        for(final int clamp: clamps){
            TextView view = newTextView("" + clamp + ",   ");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PanelActivity.class);
                    intent.putExtra("face", face);
                    intent.putExtra("clamp", clamp);
                    intent.putExtra("cabinet", dcsCabinet.getCode());
                    intent.putExtra("content_type", "dcs_clamp");
                    startActivity(intent);

                }
            });
            layout.addView(view);
        }
    }

    private TextView newTextView(String str){
        TextView view = new TextView(getActivity());
        view.setText(str);
        return view;
    }
}
