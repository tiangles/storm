package com.tiangles.storm.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.PowerDistributionCabinet;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.database.dao.UserEvent;
import com.tiangles.storm.fragments.DCSCabinetClampFragment;
import com.tiangles.storm.fragments.ConnectionDetailFragment;
import com.tiangles.storm.fragments.DCSCabinetFragment;
import com.tiangles.storm.fragments.DeviceInfoFragment;
import com.tiangles.storm.fragments.DeviceSystemInfoFragment;
import com.tiangles.storm.fragments.FragmentBase;
import com.tiangles.storm.fragments.LocalControlCabinetFragment;
import com.tiangles.storm.fragments.PowerDistributionCabinetFragment;
import com.tiangles.storm.fragments.WorkshopDeviceListFragment;
import com.tiangles.storm.fragments.WorkshopListFragment;
import com.tiangles.storm.fragments.LocalControlCabinetSignalPanelFragment;
import com.tiangles.storm.request.UploadUserEventRequest;
import com.tiangles.storm.views.TitleBar;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final static int SCAN_REQUEST_CODE = 1;
    private final static int FIND_DEVICE_BY_KEYWORD = 2;

    private FragmentManager fragmentManager;
    private FragmentBase currentFragment;
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

    @BindView(R.id.event_content) EditText mEventContentView;
    @BindView(R.id.content) FrameLayout mContentLayout;

    private String mCurrentDeviceCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StormApp.setMainActivity(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragmentManager = getFragmentManager();

        if(StormApp.getDBManager().ready()) {
            switchToWorkshopListFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, DeviceSelectorActivity.class);
                startActivityForResult(intent, FIND_DEVICE_BY_KEYWORD);
                return true;
            default:
                break;
        }

        return false;
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
            currentFragment = (FragmentBase) fragmentManager.findFragmentById(R.id.content);
        }
    }

    @OnClick(R.id.commit_event)
    void onCommitEvent(){
        String event = mEventContentView.getText().toString();
        if(!event.isEmpty()) {
            Date date = new Date();
            long eventId = UserEvent.generateEventID(date);

            UserEvent userEvent = new UserEvent(eventId, date, event, mCurrentDeviceCode, StormApp.getCurrentUser().mID, UserEvent.STATUS_PENDING);
            StormApp.getDBManager().getUserDB().commitUserEventChanges(userEvent);
            StormApp.getNetwork().sendRequest(new UploadUserEventRequest(userEvent));
            mEventContentView.setText("");
        }
    }

    @OnClick(R.id.show_user_event_list)
    void onShowUserEventList() {
        Intent intent = new Intent(this, UserEventActivity.class);
        startActivity(intent);
    }

    public void onDatabaseFailed(){
        TextView textView = new TextView(this);
        textView.setText(R.string.sync_database_failed);
        mContentLayout.addView(textView);
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
        mCurrentDeviceCode = code;
    }
    public void showDCSCabinetConnectionFragment(String code){
        if(mConnectionDetailFragment == null) {
            mConnectionDetailFragment = new ConnectionDetailFragment();
        }
        mConnectionDetailFragment.setConnectionCode(code);
        showFragment(mConnectionDetailFragment);
        mCurrentDeviceCode = code;
    }

    public void showConnectionPanel(String code) {
        if (mLocalControlCabinetSignalPanelFragment == null) {
            mLocalControlCabinetSignalPanelFragment = new LocalControlCabinetSignalPanelFragment();

        }
        mLocalControlCabinetSignalPanelFragment.setConnectionCode(code);
        showFragment(mLocalControlCabinetSignalPanelFragment);
        mCurrentDeviceCode = code;
    }

    public void showDcsClampFragment(String dcsCabinetCode, String face, int clamp){
        if(mDCSCabinetClampFragment == null) {
            mDCSCabinetClampFragment = new DCSCabinetClampFragment();
        }
        mDCSCabinetClampFragment.setClamp(dcsCabinetCode, face, clamp);
        showFragment(mDCSCabinetClampFragment);
        mCurrentDeviceCode = dcsCabinetCode;
    }

    public void switchToWorkshopListFragment(){
        if(mWorkshopListFragment == null) {
            mWorkshopListFragment = new WorkshopListFragment();
        }
        showFragment(mWorkshopListFragment);
        mCurrentDeviceCode = null;
    }

    public void switchToWorkshopDeviceListFragment(String workshopCode){
        if(mWorkshopDeviceListFragment == null) {
            mWorkshopDeviceListFragment = new WorkshopDeviceListFragment();
        }
        mWorkshopDeviceListFragment.setWorkshopCode(workshopCode);
        showFragment(mWorkshopDeviceListFragment);
        mCurrentDeviceCode = workshopCode;
    }

    public void switchToDeviceDetailFragment(String deviceCode){
        mCurrentDeviceCode = deviceCode;
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

        StormWorkshop workshop = StormApp.getDBManager().getStormDB().getWorkshop(deviceCode);
        if(workshop != null) {
            switchToWorkshopDeviceListFragment(workshop.getCode());
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
            case FIND_DEVICE_BY_KEYWORD:
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

    private void showFragment(FragmentBase fragment){
        if(fragment == null || (fragment == currentFragment && mWorkshopListFragment == currentFragment)) {
            return;
        }
        if(fragment == currentFragment) {
            fragment.update();
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
