package com.tiangles.storm.panel;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tiangles.storm.R;

public class PanelActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private PanelInfoFragment mPanelInfoFragment;
    private PanelLinkFragment mPanelLinkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_main);

        fragmentManager = getFragmentManager();

        showPanelLink();
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
}
