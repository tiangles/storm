package com.tiangles.storm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormWorkshop;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkshopActivity extends AppCompatActivity{
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;
    @BindView(R.id.find_keyword) EditText mInputBox;
    @BindView(R.id.root_view) View mRootView;
    View mFooterView;
    private MyAdaptor mListAdaptor;
    private int mCurrentRecordOffset;
    private static int PAGE_SIZE = 20;
    boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mFooterView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_footer, null);
        mFooterView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        mDeviceListView.addFooterView(mFooterView);
        mDeviceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int lastVisibleItem;
            int totalItemCount;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (totalItemCount==lastVisibleItem&&scrollState==SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        mFooterView.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                        loadMore();
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.lastVisibleItem=firstVisibleItem+visibleItemCount;
                this.totalItemCount=totalItemCount;
            }
        });

        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkshopActivity.this, WorkshopDeviceListActivity.class);
                StormWorkshop workshop = (StormWorkshop)mListAdaptor.getItem(position);
                intent.putExtra("workshop_code", workshop.getCode());
                startActivity(intent);
            }
        });

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
                    mListAdaptor.workshops.clear();
                    mCurrentRecordOffset = 0;
                    updateWorkshopListByKeyword(null);
                } else {
                    updateWorkshopListByKeyword("%"+keyword+"%");
                }
            }
        });

        updateWorkshopListByKeyword(null);
    }

    public void updateWorkshopListByKeyword(String keyword){
        List<StormWorkshop> workshops = StormApp.getDBManager().getStormDB().getWorkshopList(keyword, mCurrentRecordOffset,  PAGE_SIZE);
        if(mListAdaptor == null) {
            mListAdaptor = new MyAdaptor(workshops);
            mDeviceListView.setAdapter(mListAdaptor);
        } else {
            if(keyword != null) {
                mListAdaptor.workshops = workshops;
            } else {
                mListAdaptor.workshops.addAll(workshops);
            }
            mListAdaptor.notifyDataSetChanged();
        }
    }

    private void loadMore(){
        isLoading = true;
        mCurrentRecordOffset += PAGE_SIZE;
        updateWorkshopListByKeyword(null);
        mFooterView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        isLoading = false;
    }

    private class MyAdaptor extends BaseAdapter{
        List<StormWorkshop> workshops;
        MyAdaptor(List<StormWorkshop> workshops){
            this.workshops = workshops;
        }
        @Override
        public int getCount() {
            return workshops.size();
        }

        @Override
        public Object getItem(int position) {
            return workshops.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View  view;
            if(convertView != null) {
                view = convertView;
            } else {
                view = getLayoutInflater().inflate(R.layout.list_item_workshop_device, null);
            }
            ((TextView)view.findViewById(R.id.device_code)).setText(workshops.get(position).getCode());
            ((TextView)view.findViewById(R.id.device_name)).setText(""+position+". "+workshops.get(position).getName());
            return view;
        }
    }

    @OnClick(R.id.root_view)
    public void onHideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
    }

}
