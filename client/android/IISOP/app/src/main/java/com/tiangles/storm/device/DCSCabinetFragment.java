package com.tiangles.storm.device;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;

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
    @BindView(R.id.cabinet_clamp) TextView mCabinetClampView;

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

        String fFace = createFaceText(dcsCabinet, "F");
        String bFace = createFaceText(dcsCabinet, "B");

        mCabinetClampView.setText(fFace + "\n" + bFace);
    }

    private String createFaceText(DCSCabinet dcsCabinet, String face){
        List<DCSConnection> dcsConnections = StormApp.getDBManager().getDCSConnectionsFromCabinetFace(dcsCabinet, face);
        SortedSet<Integer> clamps = new TreeSet<>();
        for(DCSConnection connection: dcsConnections){
            clamps.add(connection.getClamp());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(face);
        sb.append("面: ");
        for(int clamp: clamps){
            sb.append(clamp);
            sb.append(", ");
        }
        return sb.toString();
    }
}
