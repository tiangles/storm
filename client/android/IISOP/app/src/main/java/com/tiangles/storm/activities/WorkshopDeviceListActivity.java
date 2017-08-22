package com.tiangles.storm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.DBManager;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkshopDeviceListActivity extends AppCompatActivity implements DBManager.DBManagerObserver{
    @BindView(R.id.workshop_device_list)
    ListView mDeviceListView;
    List<StormDevice> listItems = new ArrayList<>();
    String[] deviceCodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_device_list);
        ButterKnife.bind(this);

        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkshopDeviceListActivity.this, DeviceInfoActivity.class);
                intent.putExtra("code", listItems.get(position).getCode());
                startActivity(intent);
            }
        });

        StormApp.getDBManager().addObserver(this);

        Intent intent = this.getIntent();
        String workshopCode = intent.getStringExtra("workshop_code");
        init(workshopCode);
    }

    private void init(String workshopCode){
        StormWorkshop workshop = StormApp.getDBManager().getWorkshop(workshopCode);
        String deviceList = workshop.getDeviceList();
        deviceCodes = deviceList.split("\\|");

        createDeviceListAdaptor(deviceCodes);
    }

    private void createDeviceListAdaptor(String[] deviceCodes) {
        listItems.clear();
        for(String deviceCode: deviceCodes) {
            if(deviceCode.isEmpty()) {
                continue;
            }
            StormDevice device = StormApp.getDBManager().getDevice(deviceCode);
            if(device == null) {
                device = new StormDevice();
                device.setName("--");
                device.setCode(deviceCode);
                StormApp.getDBManager().syncDevice(deviceCode);
            }
            listItems.add(device);
        }

        mDeviceListView.setAdapter(new DeviceListAdaptor());
    }

    private void updateListView(int position){
    }

    @Override
    public void onSyncWorkshopListDone(List<StormWorkshop> workshops) {
    }

    @Override
    public void onDeviceUpdated(StormDevice device) {
        for(int i=0; i<deviceCodes.length; ++i){
            if(device.getCode().equals(deviceCodes[i])) {
                updateListView(i);
                break;
            }
        }
    }

    @Override
    public void onDeviceSynced(StormDevice device) {

    }

    private class DeviceListAdaptor extends BaseAdapter{

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View  view;
            if(convertView != null) {
                view = convertView;
            } else {
                view = getLayoutInflater().inflate(R.layout.list_item_workshop_device, null);
            }
            ((TextView)view.findViewById(R.id.device_code)).setText(listItems.get(position).getCode());
            ((TextView)view.findViewById(R.id.device_name)).setText(listItems.get(position).getName());
            return view;
        }
    }
}
