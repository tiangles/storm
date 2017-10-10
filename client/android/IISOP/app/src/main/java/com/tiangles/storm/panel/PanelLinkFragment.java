package com.tiangles.storm.panel;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiangles.storm.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PanelLinkFragment extends Fragment {
    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_link, container, false);
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

