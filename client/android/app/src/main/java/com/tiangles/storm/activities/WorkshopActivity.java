package com.tiangles.storm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tiangles.greendao.gen.StormDeviceDao;
import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.device.StormDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkshopActivity extends AppCompatActivity {
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);
        ButterKnife.bind(this);

        mDeviceListView.setAdapter(createDeviceListAdaptor());
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkshopActivity.this, DeviceSystemInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private SimpleAdapter createDeviceListAdaptor() {
        List<Map<String, Object>> listItems = new ArrayList<>();
        StormDeviceDao dao = StormApp.getStormDB().getStormDeviceDao();
        List<StormDevice> devices = dao.queryBuilder()
                .build()
                .list();
        for(StormDevice device: devices) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", R.drawable.fan);
            item.put("name", device.getName());
            item.put("model", device.getModel());
            listItems.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems,
                R.layout.list_item_workshop_device, new String[] { "image", "name", "model" },
                new int[] {R.id.device_qrcode_image, R.id.device_name, R.id.device_model});

        return adapter;
    }

}
