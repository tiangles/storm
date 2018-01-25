package com.tiangles.storm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.PowerDistributionCabinet;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.views.ThreeColumsListAdaptor;
import com.tiangles.storm.views.TitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WorkshopDeviceListFragment extends FragmentBase {
    private Unbinder unbinder;
    private String mWorkshopCode;
    @BindView(R.id.workshop_device_list) ListView mDeviceListView;
    @BindView(R.id.title) TitleView mTitleView;

    private ThreeColumsListAdaptor mListAdaptor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workshop_device_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(mWorkshopCode == null || mWorkshopCode.isEmpty()) {
            Bundle bundle = this.getArguments();
            mWorkshopCode = bundle.getString("workshop_code");
        }

        update();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mDeviceListView = null;
        mTitleView = null;
        mListAdaptor = null;
    }

    public void setWorkshopCode(String code){
        mWorkshopCode = code;
    }

    @Override
    public void update() {
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ThreeColumsListAdaptor.Model model = (ThreeColumsListAdaptor.Model)mListAdaptor.getItem(position);
                StormApp.getMainActivity().switchToDeviceDetailFragment(model.getCode());
            }
        });

        StormWorkshop workshop = StormApp.getDBManager().getStormDB().getWorkshop(mWorkshopCode);
        List<StormDevice> devices = StormApp.getDBManager().getStormDB().findDeviceInWorkshop(workshop.getCode(), null);
        List<DCSCabinet> dcsCabinets = StormApp.getDBManager().getStormDB().findDCSCabinetsInWorkshop(workshop.getCode(), null);
        List<LocalControlCabinet> localControlCabinets = StormApp.getDBManager().getStormDB().findLocalControlCabinetInWorkshop(workshop, null);
        List<PowerDistributionCabinet> powerDistributionCabinets = StormApp.getDBManager().getStormDB().findPowerDistributionCabinetInWorkshop(workshop, null);

        mTitleView.setTitle(workshop.getCode(), workshop.getName());

        mListAdaptor = new ThreeColumsListAdaptor(getActivity());
        mListAdaptor.updateByDevice(devices, dcsCabinets, localControlCabinets, powerDistributionCabinets);
        if(mDeviceListView.getHeaderViewsCount() == 0) {
            View headerView = mListAdaptor.createHeaderView(R.string.index, R.string.device_code, R.string.device_name);
            mDeviceListView.addHeaderView(headerView);
        }
        mDeviceListView.setAdapter(mListAdaptor);
    }

}
