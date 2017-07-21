package com.tiangles.storm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.device.StormDevice;

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
        mDeviceModelView.setText("SAF26-17-2\n双级动叶可调轴流式风机\n6kV电动机\n轴功率3280kW\n转速990r/min\n风机全压升10311Pa\n静压升10311Pa\n入口质量流量244.58kg/s");
        mDeviceNameView.setText(device.getName());
        mDeviceCodeTextView.setText(device.getCode());
        mDeviceSystemView.setText(device.getSystem());
        mDeviceParameterView.setText(device.getParameter());
        mDeviceDistributionCabinetView.setText(device.getDistributionCabinet());
        mDeviceLocalControlPanelView.setText(device.getLocalControlPanel());
        mDeviceDcsCabinetView.setText(device.getDcsCabinet());
    }

    public void showSystemInfo(View view) {

    }
}
