package com.tiangles.storm.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.PowerDistributionCabinet;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.fragments.DCSCabinetClampFragment;
import com.tiangles.storm.fragments.ConnectionDetailFragment;
import com.tiangles.storm.fragments.DCSCabinetFragment;
import com.tiangles.storm.fragments.DeviceInfoFragment;
import com.tiangles.storm.fragments.DeviceSystemInfoFragment;
import com.tiangles.storm.fragments.LocalControlCabinetFragment;
import com.tiangles.storm.fragments.PowerDistributionCabinetFragment;
import com.tiangles.storm.fragments.WorkshopDeviceListFragment;
import com.tiangles.storm.fragments.WorkshopListFragment;
import com.tiangles.storm.fragments.LocalControlCabinetSignalPanelFragment;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends AppCompatActivity {
    private final static int SCAN_REQUEST_CODE = 1;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private WorkshopListFragment mWorkshopListFragment;
    private WorkshopDeviceListFragment mWorkshopDeviceListFragment;
    private DeviceInfoFragment mDeviceFragment;
    private DCSCabinetFragment mDCSCabinetFragment;
    private LocalControlCabinetFragment mLocalControlCabinetFragment;
    private LocalControlCabinetSignalPanelFragment mLocalControlCabinetSignalPanelFragment;
    private DCSCabinetClampFragment mDCSCabinetClampFragment;
    private ConnectionDetailFragment mConnectionDetailFragment;
    private DeviceSystemInfoFragment mDeviceSystemInfoFragment;
    private PowerDistributionCabinetFragment mPowerDistributionCabinetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StormApp.setMainActivity(this);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();

        if(StormApp.getDBManager().ready()) {
            switchToWorkshopListFragment();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        showFragment(currentFragment);
        Log.e("MainActivity", "onResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("MainActivity", "onPause()");
    }

    @Override
    public void onBackPressed() {
        Log.e("MainActivity", "fragmentManager.getBackStackEntryCount: " + fragmentManager.getBackStackEntryCount());
        if(fragmentManager.getBackStackEntryCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void onDababaseReady(){
        switchToWorkshopListFragment();
    }

    public void showWorkshopListFragment(View v){
        switchToWorkshopListFragment();
    }

    public void showDeviceSystemInfoFragment(String code){
        if(mDeviceSystemInfoFragment == null) {
            mDeviceSystemInfoFragment = new DeviceSystemInfoFragment();
        }
        mDeviceSystemInfoFragment.setDeviceCode(code);
        showFragment(mDeviceSystemInfoFragment);

    }
    public void showDCSCabinetConnectionFragment(String code){
        if(mConnectionDetailFragment == null) {
            mConnectionDetailFragment = new ConnectionDetailFragment();
        }
        mConnectionDetailFragment.setConnectionCode(code);
        showFragment(mConnectionDetailFragment);
    }

    public void showConnectionPanel(String code) {
        if (mLocalControlCabinetSignalPanelFragment == null) {
            mLocalControlCabinetSignalPanelFragment = new LocalControlCabinetSignalPanelFragment();

        }
        mLocalControlCabinetSignalPanelFragment.setConnectionCode(code);
        showFragment(mLocalControlCabinetSignalPanelFragment);
    }

    public void showDcsClampFragment(String dcsCabinetCode, String face, int clamp){
        if(mDCSCabinetClampFragment == null) {
            mDCSCabinetClampFragment = new DCSCabinetClampFragment();
        }
        mDCSCabinetClampFragment.setClamp(dcsCabinetCode, face, clamp);
        showFragment(mDCSCabinetClampFragment);
    }

    public void switchToWorkshopListFragment(){
        if(mWorkshopListFragment == null) {
            mWorkshopListFragment = new WorkshopListFragment();
        }
        showFragment(mWorkshopListFragment);
    }

    public void switchToWorkshopDeviceListFragment(String workshopCode){
        if(mWorkshopDeviceListFragment == null) {
            mWorkshopDeviceListFragment = new WorkshopDeviceListFragment();
        }
        mWorkshopDeviceListFragment.setWorkshopCode(workshopCode);
        showFragment(mWorkshopDeviceListFragment);
    }

    public void switchToDeviceDetailFragment(String deviceCode){
        StormDevice device = StormApp.getDBManager().getStormDB().getDevice(deviceCode);
        if(device != null) {
            showStormDevice(device);
            return;
        }

        DCSCabinet dcsCabinet = StormApp.getDBManager().getStormDB().getDCSCabinet(deviceCode);
        if(dcsCabinet != null) {
            showDCSCabinet(dcsCabinet);
            return;
        }

        LocalControlCabinet localControlCabinet = StormApp.getDBManager().getStormDB().getLocalControlCabinet(deviceCode);
        if(localControlCabinet != null) {
            showLocalControlCabinet(localControlCabinet);
            return;
        }

        PowerDistributionCabinet powerDistributionCabinet = StormApp.getDBManager().getStormDB().getPowerDistributionCabinet(deviceCode);
        if(powerDistributionCabinet != null) {
            showPowerDistributionCabinet(powerDistributionCabinet);
            return;
        }
    }

    private void showStormDevice(StormDevice device){
        if (mDeviceFragment == null) {
            mDeviceFragment = new DeviceInfoFragment();
        }
        mDeviceFragment.setDevice(device);
        showFragment(mDeviceFragment);
    }

    private void showDCSCabinet(DCSCabinet dcsCabinet){
        if (mDCSCabinetFragment == null) {
            mDCSCabinetFragment = new DCSCabinetFragment();
        }
        mDCSCabinetFragment.setCabinet(dcsCabinet);
        showFragment(mDCSCabinetFragment);
    }

    private void showLocalControlCabinet(LocalControlCabinet cabinet){
        if (mLocalControlCabinetFragment == null) {
            mLocalControlCabinetFragment = new LocalControlCabinetFragment();
        }
        mLocalControlCabinetFragment.setCabinet(cabinet);

        showFragment(mLocalControlCabinetFragment);
    }

    private void showPowerDistributionCabinet(PowerDistributionCabinet cabinet){
        if(mPowerDistributionCabinetFragment == null) {
            mPowerDistributionCabinetFragment = new PowerDistributionCabinetFragment();
        }
        mPowerDistributionCabinetFragment.setCabinet(cabinet);
        showFragment(mPowerDistributionCabinetFragment);
    }

    public void showQRCodeActivity(View v){
        Intent openCameraIntent = new Intent(MainActivity.this,
                CaptureActivity.class);
        startActivityForResult(openCameraIntent, SCAN_REQUEST_CODE);
    }

    public void showChartActivity(View v){
        Intent intent = new Intent(MainActivity.this, ChartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    switchToDeviceDetailFragment(result);
                }
                break;
            default:
                break;
        }
    }

    private void showFragment(Fragment fragment){
        if(fragment == null || (fragment == currentFragment && mWorkshopListFragment == currentFragment)) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();

//        if(currentFragment != null) {
//            transaction.hide(currentFragment);
//        }
//        if(!fragment.isAdded()) {
            transaction.replace(R.id.content, fragment);
//        } else {
//            transaction.show(fragment);
            transaction.addToBackStack(null);
//        }
        transaction.commit();
        currentFragment = fragment;
    }

}
