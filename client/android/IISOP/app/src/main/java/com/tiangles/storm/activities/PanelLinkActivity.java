package com.tiangles.storm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.views.PanelLinkView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PanelLinkActivity extends AppCompatActivity {
    private String mDeviceCode;

    @BindView(R.id.panel_link_view) PanelLinkView mPanelLinkView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);
    }
}
