package com.tiangles.storm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkshopDeviceListActivity extends AppCompatActivity {
    @BindView(R.id.workshop_device_list)
    ListView mDeviceListView;
    private List<StormDevice> listItems = new ArrayList<>();
    private DeviceListAdaptor listAdaptor;
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

        Intent intent = this.getIntent();
        String workshopCode = intent.getStringExtra("workshop_code");
        StormWorkshop workshop = StormApp.getDBManager().getWorkshop(workshopCode);
        init(workshop);
    }

    private void init(StormWorkshop workshop){
        List<StormDevice> devices = StormApp.getDBManager().getDeviceFromWorkshop(workshop);
        createDeviceListAdaptor(devices);
    }

    private void createDeviceListAdaptor(List<StormDevice> devices) {
        listItems.clear();
        for(StormDevice device: devices) {
            if(device != null) {
                listItems.add(device);
            }
        }

        listAdaptor = new DeviceListAdaptor();
        mDeviceListView.setAdapter(listAdaptor);
    }

    private class DeviceListAdaptor extends BaseAdapter{
        class ViewHolder {
            TextView codeView;
            TextView nameView;
        }
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
            ViewHolder viewHolder = null;
            if(convertView != null) {
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            } else {
                view = getLayoutInflater().inflate(R.layout.list_item_workshop_device, null);
                viewHolder = new ViewHolder();
                viewHolder.codeView = (TextView)view.findViewById(R.id.device_code);
                viewHolder.nameView = (TextView)view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            }
            viewHolder.codeView.setText(listItems.get(position).getCode());
            viewHolder.nameView.setText(listItems.get(position).getName());
            return view;
        }
    }
}
