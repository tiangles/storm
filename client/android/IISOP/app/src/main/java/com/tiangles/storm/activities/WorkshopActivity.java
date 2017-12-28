package com.tiangles.storm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.views.ThreeColumsListAdaptor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkshopActivity extends AppCompatActivity{
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;
    @BindView(R.id.find_keyword) EditText mInputBox;
    @BindView(R.id.root_view) View mRootView;
    View mFooterView;
    private ThreeColumsListAdaptor mListAdaptor;
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
                        update();
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
                ThreeColumsListAdaptor.Model model= (ThreeColumsListAdaptor.Model)mListAdaptor.getItem(position);
                intent.putExtra("workshop_code", model.getCode());
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
                if(mListAdaptor != null) {
                    mListAdaptor.clear();
                }
                update();
            }
        });

        updateWorkshopListByKeyword(null);
    }

    public void updateWorkshopListByKeyword(String keyword){
        List<StormWorkshop> workshops = StormApp.getDBManager().getStormDB().getWorkshopList(keyword, mCurrentRecordOffset,  PAGE_SIZE);
        if(mListAdaptor == null) {
            mListAdaptor = new ThreeColumsListAdaptor(this);
            mListAdaptor.updateByWorkshop(workshops);
            View headerView = mListAdaptor.createHeaderView(R.string.index, R.string.device_code, R.string.device_name);

            mDeviceListView.addHeaderView(headerView);
            mDeviceListView.setAdapter(mListAdaptor);
        } else {
            mListAdaptor.appendWorkshop(workshops);
        }
        mListAdaptor.notifyDataSetChanged();
    }

    private void update(){
        isLoading = true;
        mCurrentRecordOffset += PAGE_SIZE;

        String keyword = mInputBox.getText().toString();
        if(keyword.isEmpty()){
            mCurrentRecordOffset = 0;
            updateWorkshopListByKeyword(null);
        } else {
            updateWorkshopListByKeyword("%"+keyword+"%");
        }
        mFooterView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        isLoading = false;
    }

    @OnClick(R.id.root_view)
    public void onHideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
    }

}
