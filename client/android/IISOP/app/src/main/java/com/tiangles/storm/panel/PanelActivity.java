package com.tiangles.storm.panel;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;

public class PanelActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private PanelInfoFragment mPanelInfoFragment;
    private PanelLinkFragment mPanelLinkFragment;
    private DeviceSignalPanelFragment mDeviceSignalPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_main);

        Intent intent = this.getIntent();
        String connectionType = intent.getStringExtra("connection_type");
        String connectionCode = intent.getStringExtra("connection_code");
        fragmentManager = getFragmentManager();

        if(connectionType.equals("local_control_connection")) {
            showConnectionPanel(connectionCode);
        } else {
            showPanelLink();
        }
    }


    private void showPanelInfo(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mPanelInfoFragment == null) {
            mPanelInfoFragment = new PanelInfoFragment();
            transaction.add(R.id.content, mPanelInfoFragment);
        } else {
            transaction.show(mPanelInfoFragment);
        }
        transaction.commit();
    }

    private void showPanelLink(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mPanelLinkFragment == null) {
            mPanelLinkFragment = new PanelLinkFragment();
            transaction.add(R.id.content, mPanelLinkFragment);
        } else {
            transaction.show(mPanelLinkFragment);
        }
        transaction.commit();
    }

    private void showConnectionPanel(String code) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mDeviceSignalPanelFragment == null) {
            mDeviceSignalPanelFragment = new DeviceSignalPanelFragment();

            Bundle bundle = new Bundle();
            bundle.putString("connection_code", code);
            mDeviceSignalPanelFragment.setArguments(bundle);

            transaction.add(R.id.content, mDeviceSignalPanelFragment);
        } else {
            transaction.show(mDeviceSignalPanelFragment);
        }
        transaction.commit();
    }
}
