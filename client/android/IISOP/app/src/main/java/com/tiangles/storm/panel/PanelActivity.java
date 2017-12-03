package com.tiangles.storm.panel;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;

public class PanelActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    private LocalControlCabinetSignalPanelFragment mLocalControlCabinetSignalPanelFragment;
    private DCSCabinetClampFragment mDCSCabinetClampFragment;
    private DCSCabinetConnectionFragment mDCSCabinetConnectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_main);

        Intent intent = this.getIntent();
        String contentType = intent.getStringExtra("content_type");
        fragmentManager = getFragmentManager();

        if(contentType.equals("local_control_connection")) {
            String connectionCode = intent.getStringExtra("connection_code");
            showConnectionPanel(connectionCode);
        } else if(contentType.equals("dcs_clamp")) {
            String face = intent.getStringExtra("face");
            int clamp = intent.getIntExtra("clamp", 0);
            String cabinet = intent.getStringExtra("cabinet");

            showDCSCabinetClamp(cabinet, face, clamp);
        }
    }

    private void showDCSCabinetClamp(String cabinet, String face, int clamp){
        if(mDCSCabinetClampFragment == null) {
            mDCSCabinetClampFragment = new DCSCabinetClampFragment();
            Bundle bundle = new Bundle();
            bundle.putString("cabinet", cabinet);
            bundle.putString("face", face);
            bundle.putInt("clamp", clamp);
            mDCSCabinetClampFragment.setArguments(bundle);
        }
        showFragment(mDCSCabinetClampFragment);
    }

    private void showConnectionPanel(String code) {
        if (mLocalControlCabinetSignalPanelFragment == null) {
            mLocalControlCabinetSignalPanelFragment = new LocalControlCabinetSignalPanelFragment();

            Bundle bundle = new Bundle();
            bundle.putString("connection_code", code);
            mLocalControlCabinetSignalPanelFragment.setArguments(bundle);
        }
        showFragment(mLocalControlCabinetSignalPanelFragment);
    }

    public void showDCSCabinetConnectionFragment(String code){
        if (mDCSCabinetConnectionFragment == null) {
            mDCSCabinetConnectionFragment = new DCSCabinetConnectionFragment();

            Bundle bundle = new Bundle();
            bundle.putString("connection_code", code);
            mDCSCabinetConnectionFragment.setArguments(bundle);

        }
        showFragment(mDCSCabinetConnectionFragment);
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if(!fragment.isAdded()) {
            transaction.add(R.id.content, fragment);
        } else {
            transaction.show(fragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
        currentFragment = fragment;
    }
}
