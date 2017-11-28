package com.tiangles.storm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.device.DeviceActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkshopDeviceListActivity extends AppCompatActivity {
    @BindView(R.id.find_keyword) EditText mInputBox;
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;
    private List<ListItemContent> listItems = new ArrayList<>();
    private DeviceListAdaptor listAdaptor;
    StormWorkshop workshop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_device_list);
        ButterKnife.bind(this);

        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkshopDeviceListActivity.this, DeviceActivity.class);
                intent.putExtra("code", listItems.get(position).code);
                startActivity(intent);
            }
        });

        Intent intent = this.getIntent();
        String workshopCode = intent.getStringExtra("workshop_code");
        workshop = StormApp.getDBManager().getWorkshop(workshopCode);
        init(null);

        mInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if(keyword.isEmpty()){
                    init(null);
                } else {
                    init("%"+keyword+"%");
                }
            }
        });

    }

    private void init(String keyword){
        List<StormDevice> devices = StormApp.getDBManager().getDeviceFromWorkshop(workshop, keyword);
        List<DCSCabinet> dcsCabinets = StormApp.getDBManager().getDCSCabinetsForWorkshop(workshop, keyword);
        List<LocalControlCabinet> localControlCabinets = StormApp.getDBManager().getStormDB().getLocalControlCabinetForWorkshop(workshop, keyword);

        createDeviceListAdaptor(devices, dcsCabinets, localControlCabinets);
    }

    private void createDeviceListAdaptor(List<StormDevice> devices, List<DCSCabinet> dcsCabinets, List<LocalControlCabinet> localControlCabinets) {
        listItems.clear();
        for(StormDevice device: devices) {
            if(device != null) {
                listItems.add(new ListItemContent(device.getCode(), device.getName()));
            }
        }

        for(DCSCabinet dcsCabinet : dcsCabinets) {
            listItems.add(new ListItemContent(dcsCabinet.getCode(), dcsCabinet.getUsage()));
        }

        for(LocalControlCabinet localControlCabinet: localControlCabinets){
            listItems.add(new ListItemContent(localControlCabinet.getCode(), localControlCabinet.getName()));
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
            viewHolder.codeView.setText(listItems.get(position).code);
            viewHolder.nameView.setText(listItems.get(position).name);
            return view;
        }
    }
    private class ListItemContent {
        ListItemContent(String code, String name) {
            this.code = code;
            this.name = name;
        }
        String code;
        String name;
    }}
