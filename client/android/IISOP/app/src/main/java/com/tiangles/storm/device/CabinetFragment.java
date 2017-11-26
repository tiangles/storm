package com.tiangles.storm.device;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.Cabinet;
import com.tiangles.storm.database.dao.DCSConnection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CabinetFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.cabinet_code) TextView mCabinetCodeTextView;
    @BindView(R.id.cabinet_name) TextView mCabinetNameView;
    @BindView(R.id.cabinet_controller) TextView mCabinetControllerView;
    @BindView(R.id.cabinet_clamp) TextView mCabinetClampView;

    Cabinet mCabinet;
    public CabinetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabinet, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(mCabinet == null) {

        }
        showCabinet(mCabinet);
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

    public void setCabinet(Cabinet cabinet){
        mCabinet = cabinet;
    }

    private void showCabinet(Cabinet cabinet){
        mCabinetCodeTextView.setText(cabinet.getCode());
        mCabinetNameView.setText(cabinet.getUsage());
        mCabinetControllerView.setText("DPU31");

        String fFace = createFaceText(cabinet, "F");
        String bFace = createFaceText(cabinet, "B");

        mCabinetClampView.setText(fFace + "\n" + bFace);
    }

    private String createFaceText(Cabinet cabinet, String face){
        List<DCSConnection> dcsConnections = StormApp.getDBManager().getDCSConnectionsFromCabinetFace(cabinet, face);
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
