package com.tiangles.storm.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.tiangles.greendao.gen.DaoMaster;
import com.tiangles.greendao.gen.DaoSession;
import com.tiangles.greendao.gen.DemoDeviceDao;
import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.device.DemoDevice;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceInfoActivity extends AppCompatActivity {
    private DemoDeviceDao demoDeviceDao;
    @BindView(R.id.device_model) TextView mDeviceModelView;
    @BindView(R.id.device_name) TextView mDeviceNameView;
    @BindView(R.id.device_qrcode_text) TextView mDeviceQRCodeTextView;
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
        List<DemoDevice> devices = demoDeviceDao.queryBuilder()
                .where(DemoDeviceDao.Properties.QrCode.eq(qrCode))
                .build()
                .list();
        if(!devices.isEmpty()) {
            showDevice(devices.get(0));
        }
    }

    private void showDevice(DemoDevice device) {
        mDeviceModelView.setText(device.getModel());
        mDeviceNameView.setText(device.getName());
        mDeviceQRCodeTextView.setText(device.getQrCode());
        try {
            Bitmap bmp = EncodingHandler.createQRCode(device.getQrCode(), 512);
            mDeviceQRCodeImageView.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    private void initializeDao(){
        if(demoDeviceDao == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(StormApp.getContext(),
                    "devices-db", null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            DaoSession daoSession = daoMaster.newSession();
            demoDeviceDao = daoSession.getDemoDeviceDao();
        }
    }
}
