package com.tiangles.storm.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.device.StormDevice;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceInfoActivity extends AppCompatActivity {
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

        Intent intent = this.getIntent();
        String qrCode = intent.getStringExtra("qrCode");
        showDevice(qrCode);
    }

    private void showDevice(String qrCode) {
        StormDeviceDao dao = StormApp.getStormDB().getStormDeviceDao();
        List<StormDevice> devices = dao.queryBuilder()
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
}
