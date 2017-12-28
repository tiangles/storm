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
import com.tiangles.storm.views.ThreeColumsListAdaptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkshopDeviceListActivity extends AppCompatActivity {
    @BindView(R.id.find_keyword) EditText mInputBox;
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;
    private StormWorkshop workshop;
    private ThreeColumsListAdaptor mListAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_device_list);
        ButterKnife.bind(this);

        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkshopDeviceListActivity.this, DeviceActivity.class);
                ThreeColumsListAdaptor.Model model = (ThreeColumsListAdaptor.Model)mListAdaptor.getItem(position);
                intent.putExtra("code", model.getCode());
                startActivity(intent);
            }
        });

        Intent intent = this.getIntent();
        String workshopCode = intent.getStringExtra("workshop_code");
        workshop = StormApp.getDBManager().getStormDB().getWorkshop(workshopCode);
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
        List<StormDevice> devices = StormApp.getDBManager().getStormDB().getDeviceFromWorkshop(workshop.getCode(), keyword);
        List<DCSCabinet> dcsCabinets = StormApp.getDBManager().getStormDB().getDCSCabinetsForWorkshop(workshop.getCode(), keyword);
        List<LocalControlCabinet> localControlCabinets = StormApp.getDBManager().getStormDB().getLocalControlCabinetForWorkshop(workshop, keyword);

        mListAdaptor = new ThreeColumsListAdaptor(this);
        mListAdaptor.updateByDevice(devices, dcsCabinets, localControlCabinets);
        View headerView = mListAdaptor.createHeaderView(R.string.index, R.string.device_code, R.string.device_name);
        mDeviceListView.addHeaderView(headerView);
        mDeviceListView.setAdapter(mListAdaptor);
    }
}
