package com.tiangles.storm.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.views.ThreeColumsListAdaptor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WorkshopListFragment extends FragmentBase {
    private Unbinder unbinder;
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;
    private View mListFooterView;
    private ThreeColumsListAdaptor mListAdaptor;
    private int mCurrentRecordOffset;
    private static int PAGE_SIZE = 20;
    boolean isLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WorkshopListFragment", "onDetach()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workshop_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCurrentRecordOffset = 0;
        initializeViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("WorkshopListFragment", "onAttach()");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("WorkshopListFragment", "onDetach()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mListAdaptor = null;
        mDeviceListView = null;
        mListFooterView = null;
        mCurrentRecordOffset = 0;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("WorkshopListFragment", "onResume()");

    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("WorkshopListFragment", "onPause()");
    }

    private void initializeViews(){
        mListFooterView =getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        mListFooterView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        mDeviceListView.addFooterView(mListFooterView);
        mDeviceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int lastVisibleItem;
            int totalItemCount;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (totalItemCount==lastVisibleItem&&scrollState==SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        mListFooterView.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
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
                ThreeColumsListAdaptor.Model model= (ThreeColumsListAdaptor.Model)mListAdaptor.getItem(position);
                StormApp.getMainActivity().switchToWorkshopDeviceListFragment(model.getCode());
            }
        });

        update();
    }

    @Override
    public void update(){
        isLoading = true;
        updateWorkshopListByKeyword(null);
        mListFooterView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        isLoading = false;
    }

    private void updateWorkshopListByKeyword(String keyword){
        List<StormWorkshop> workshops = StormApp.getDBManager().getStormDB().getWorkshopList(keyword, mCurrentRecordOffset,  PAGE_SIZE);
        mCurrentRecordOffset += workshops.size();
        if(mListAdaptor == null) {
            mListAdaptor = new ThreeColumsListAdaptor(getActivity());
            mListAdaptor.updateByWorkshop(workshops);
            View headerView = mListAdaptor.createHeaderView(R.string.index, R.string.device_code, R.string.device_name);

            mDeviceListView.addHeaderView(headerView);
            mDeviceListView.setAdapter(mListAdaptor);
        } else {
            mListAdaptor.appendWorkshop(workshops);
        }
        mListAdaptor.notifyDataSetChanged();
    }

}
