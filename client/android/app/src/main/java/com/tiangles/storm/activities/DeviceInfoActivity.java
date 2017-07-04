package com.tiangles.storm.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.DemoDeviceDao;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.device.StormDevice;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceInfoActivity extends AppCompatActivity {
    private StormDeviceDao stormDeviceDao;
    @BindView(R.id.device_code) TextView mDeviceCodeTextView;
    @BindView(R.id.device_name) TextView mDeviceNameView;
    @BindView(R.id.device_model) TextView mDeviceModelView;
    @BindView(R.id.device_system) TextView mDeviceSystemView;
    @BindView(R.id.device_parameters) TextView mDeviceParameterView;
    @BindView(R.id.device_distribution_cabinet) TextView mDeviceDistributionCabinetView;
    @BindView(R.id.device_local_control_panel) TextView mDeviceLocalControlPanelView;
    @BindView(R.id.device_dcs_cabinet) TextView mDeviceDcsCabinetView;

    @BindView(R.id.device_qrcode_image) ImageView mDeviceQRCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);

        initializeDao();
        Intent intent = this.getIntent();
        String qrCode = intent.getStringExtra("qrCode");
        showDevice(qrCode);
    }

    private void showDevice(String qrCode) {
        List<StormDevice> devices = stormDeviceDao.queryBuilder()
                .where(StormDeviceDao.Properties.Code.eq(qrCode))
                .build()
                .list();
        if(!devices.isEmpty()) {
            showDevice(devices.get(0));
        }
    }

    private void showDevice(StormDevice device) {
        mDeviceModelView.setText(device.getModel());
        mDeviceNameView.setText(device.getName());
        mDeviceCodeTextView.setText(device.getCode());
        mDeviceSystemView.setText(device.getSystem());
        mDeviceParameterView.setText(device.getParameter());
        mDeviceDistributionCabinetView.setText(device.getDistributionCabinet());
        mDeviceLocalControlPanelView.setText(device.getLocalControlPanel());
        mDeviceDcsCabinetView.setText(device.getDcsCabinet());

        try {
            Bitmap bmp = EncodingHandler.createQRCode(device.getCode(), 512);
            mDeviceQRCodeImageView.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    private void initializeDao(){
        if(stormDeviceDao == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(StormApp.getContext(),
                    createDatabaseFile(), null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            DaoSession daoSession = daoMaster.newSession();
            stormDeviceDao = daoSession.getStormDeviceDao();
        }
    }

    public String createDatabaseFile() {
        File dir = getApplicationContext().getFilesDir();
        String path = dir.getAbsolutePath() + "/storm.db";
        File f = new File(path);
        if(!f.exists()) {
            try {
                InputStream in = getResources().getAssets().open("storm.db");

                OutputStream out = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int byteread = 0;
                while ( (byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
