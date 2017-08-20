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

public class WorkshopActivity extends AppCompatActivity implements DBManager.DBManagerObserver{
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;
    List<Map<String, Object>> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);
        ButterKnife.bind(this);

        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkshopActivity.this, WorkshopDeviceListActivity.class);
                intent.putExtra("workshop_code", (String)listItems.get(position).get("code"));
                startActivity(intent);
            }
        });


        List<StormWorkshop> workshops = StormApp.getDBManager().getWorkshopList();
        mDeviceListView.setAdapter(createDeviceListAdaptor(workshops));

        StormApp.getDBManager().addObserver(this);
        StormApp.getDBManager().syncWorkshopList();
    }

    private SimpleAdapter createDeviceListAdaptor(List<StormWorkshop> workshops) {
        listItems.clear();
        for(StormWorkshop workshop: workshops) {
            Map<String, Object> item = new HashMap<>();
            item.put("code", workshop.getCode());
            item.put("name", workshop.getName());
            listItems.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems,
                R.layout.list_item_workshop_device, new String[] { "code", "name" },
                new int[] {R.id.device_code, R.id.device_name});

        return adapter;
    }

    @Override
    public void onSyncWorkshopListDone(List<StormWorkshop> workshops) {
        mDeviceListView.setAdapter(createDeviceListAdaptor(workshops));
    }

    @Override
    public void onDeviceUpdated(StormDevice device) {

    }

    @Override
    public void onDeviceSynced(StormDevice device) {

    }
}
