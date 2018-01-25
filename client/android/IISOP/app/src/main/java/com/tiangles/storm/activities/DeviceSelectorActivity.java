package com.tiangles.storm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.tiangles.storm.database.dao.PowerDistributionCabinet;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tiangles.storm.StormApp.getContext;

public class DeviceSelectorActivity extends Activity {
    @BindView(R.id.keyword_input) EditText mKeywordInputView;
    @BindView(R.id.content_view) ListView mListView;

    private DataAdaptor mDataAdaptor = new DataAdaptor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_selector);
        ButterKnife.bind(this);
        mListView.setAdapter(mDataAdaptor);
        mKeywordInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                Object obj = mDataAdaptor.getItem(position);
                if(obj instanceof StormWorkshop) {
                    bundle.putString(CodeUtils.RESULT_STRING, ((StormWorkshop)obj).getCode());
                } else if(obj instanceof  StormDevice) {
                    bundle.putString(CodeUtils.RESULT_STRING, ((StormDevice)obj).getCode());
                } else if(obj instanceof  DCSCabinet) {
                    bundle.putString(CodeUtils.RESULT_STRING, ((DCSCabinet)obj).getCode());
                } else if(obj instanceof  LocalControlCabinet) {
                    bundle.putString(CodeUtils.RESULT_STRING, ((LocalControlCabinet)obj).getCode());
                } else if(obj instanceof  PowerDistributionCabinet) {
                    bundle.putString(CodeUtils.RESULT_STRING, ((PowerDistributionCabinet)obj).getCode());
                }

                resultIntent.putExtras(bundle);
                DeviceSelectorActivity.this.setResult(RESULT_OK, resultIntent);
                DeviceSelectorActivity.this.finish();
            }
        });
    }

    private void updateData(String keyword){
        List<StormWorkshop> workshops = null;
        List<StormDevice> devices = null;
        List<DCSCabinet> dcsCabinets = null;
        List<LocalControlCabinet> localControlCabinets = null;
        List<PowerDistributionCabinet> powerDistributionCabinets = null;
        if(keyword != null && !keyword.isEmpty()) {
            workshops = StormApp.getDBManager().getStormDB().findWorkshop(keyword);
            devices = StormApp.getDBManager().getStormDB().findDevice(keyword);
            dcsCabinets =  StormApp.getDBManager().getStormDB().findDCSCabinets(keyword);
            localControlCabinets = StormApp.getDBManager().getStormDB().findLocalControlCabinet(keyword);
            powerDistributionCabinets = StormApp.getDBManager().getStormDB().findPowerDistributionCabinet(keyword);
            }
        if(mDataAdaptor == null) {
            mDataAdaptor = new DataAdaptor();
        }
        mDataAdaptor.setData(workshops, devices, dcsCabinets, localControlCabinets, powerDistributionCabinets);
        mDataAdaptor.notifyDataSetChanged();
    }

    class DataAdaptor extends BaseAdapter {
        private List<Object> data = new ArrayList<>();
        public void setData(List<StormWorkshop> workshops, List<StormDevice> devices, List<DCSCabinet> dcsCabinets, List<LocalControlCabinet> localControlCabinets, List<PowerDistributionCabinet> powerDistributionCabinets){
            data.clear();
            if(workshops != null) {
                for(StormWorkshop workshop: workshops) {
                    data.add(workshop);
                }
            }
            if(devices != null) {
                for(StormDevice device: devices) {
                    data.add(device);
                }
            }
            if(dcsCabinets != null) {
                for(DCSCabinet cabinet: dcsCabinets) {
                    data.add(cabinet);
                }
            }
            if(localControlCabinets != null) {
                for(LocalControlCabinet cabinet: localControlCabinets) {
                    data.add(cabinet);
                }
            }
            if(powerDistributionCabinets != null) {
                for(PowerDistributionCabinet cabinet: powerDistributionCabinets) {
                    data.add(cabinet);
                }
            }
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                ViewHoder viewHoder = new ViewHoder();
                view = inflater.inflate(R.layout.list_item_device_selector, null);
                viewHoder.codeView = (TextView)view.findViewById(R.id.code);
                viewHoder.nameView = (TextView)view.findViewById(R.id.name);
                view.setTag(viewHoder);
            }
            ViewHoder viewHoder = (ViewHoder)view.getTag();
            Object obj = data.get(i);
            if(obj instanceof StormWorkshop) {
                StormWorkshop workshop = (StormWorkshop) obj;
                viewHoder.codeView.setText(workshop.getCode());
                viewHoder.nameView.setText(workshop.getName());
            } else if(obj instanceof StormDevice) {
                StormDevice device = (StormDevice)obj;
                viewHoder.codeView.setText(device.getCode());
                viewHoder.nameView.setText(device.getName());
            } else if(obj instanceof DCSCabinet) {
                DCSCabinet cabinet = (DCSCabinet)obj;
                viewHoder.codeView.setText(cabinet.getCode());
                viewHoder.nameView.setText(cabinet.getUsage());
            } else if(obj instanceof LocalControlCabinet) {
                LocalControlCabinet cabinet = (LocalControlCabinet)obj;
                viewHoder.codeView.setText(cabinet.getCode());
                viewHoder.nameView.setText(cabinet.getName());
            } else if(obj instanceof PowerDistributionCabinet) {
                PowerDistributionCabinet cabinet = (PowerDistributionCabinet)obj;
                viewHoder.codeView.setText(cabinet.getCode());
                viewHoder.nameView.setText(cabinet.getName());
            }
            return view;
        }

        private class ViewHoder{
            TextView codeView;
            TextView nameView;
        }
    }
}
