package com.tiangles.storm.panel;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tiangles.storm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PanelInfoFragment extends Fragment{
    private Unbinder unbinder;
    @BindView(R.id.device_code) TextView mCodeTextView;
    @BindView(R.id.device_name) TextView mNameView;
    @BindView(R.id.device_model) TextView mControllerView;
    @BindView(R.id.fsurface) TextView mFSurfaceView;
    @BindView(R.id.bsurface) TextView mBSurfaceView;

    public PanelInfoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupViewContent();
        return view;
    }

    private void setupViewContent(){
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
