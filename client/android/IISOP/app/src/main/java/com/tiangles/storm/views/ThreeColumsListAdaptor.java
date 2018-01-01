package com.tiangles.storm.views;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.database.dao.DCSCabinet;
import com.tiangles.storm.database.dao.DCSConnection;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.database.dao.PowerDistributionCabinet;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.database.dao.StormWorkshop;
import com.tiangles.storm.database.dao.UserEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ThreeColumsListAdaptor extends BaseAdapter {
    private List<Model> mModels = new ArrayList<>();
    private Activity mActivity;
    private int mHeaderCount = 0;
    public class Model{
        Model(String index, String code, String name) {
            this.index = index;
            this.code = code;
            this.name = name;
        }
        public String getCode(){
            return code;
        }
        public String getName(){
            return name;
        }
        String index;
        String code;
        String name;
    }

    public ThreeColumsListAdaptor(Activity activity){
        mActivity = activity;
    }

    public  View createHeaderView(int colum0, int colum1, int colum2){
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_three_columns, null);
        TextView idView = (TextView)view.findViewById(R.id.id);
        TextView codeView = (TextView)view.findViewById(R.id.code);
        TextView nameView = (TextView)view.findViewById(R.id.name);
        idView.setText(mActivity.getString(colum0));
        codeView.setText(mActivity.getString(colum1));
        nameView.setText(mActivity.getString(colum2));

        ++mHeaderCount;
        return view;
    }

    public void updateByUserEvents(List<UserEvent> events){
        mModels.clear();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        for(UserEvent event: events){
            String dateString = formatter.format(event.getDate());
            mModels.add(new Model(""+(mModels.size()+1), dateString, event.getEvent()));
        }
    }

    public void updateByLocalControlCabinetConnection(List<LocalControlCabinetConnection> connections) {
        mModels.clear();
        for(LocalControlCabinetConnection connection: connections){
            mModels.add(new Model(""+(mModels.size()+1), connection.getCode(), connection.getName()));
        }
    }

    public void updateByDevice(List<StormDevice> devices, List<DCSCabinet> dcsCabinets, List<LocalControlCabinet> localControlCabinets, List<PowerDistributionCabinet> powerDistributionCabinets){
        mModels.clear();
        for(StormDevice device: devices) {
            mModels.add(new Model(""+(mModels.size()+1), device.getCode(), device.getName()));
        }

        for(DCSCabinet dcsCabinet : dcsCabinets) {
            mModels.add(new Model(""+(mModels.size()+1), dcsCabinet.getCode(), dcsCabinet.getUsage()));
        }

        for(LocalControlCabinet localControlCabinet: localControlCabinets){
            mModels.add(new Model(""+(mModels.size()+1), localControlCabinet.getCode(), localControlCabinet.getName()));
        }
        for(PowerDistributionCabinet cabinet: powerDistributionCabinets) {
            mModels.add(new Model(""+(mModels.size()+1), cabinet.getCode(), cabinet.getName()));
        }
    }

    public void updateByDCSConnection(List<DCSConnection> connections){
        mModels.clear();
        for(DCSConnection connection: connections) {
            mModels.add(new Model(connection.getChannel(), connection.getCode(), connection.getDescription()));
        }
    }

    public void updateByWorkshop(List<StormWorkshop> workshops){
        mModels.clear();
        appendWorkshop(workshops);
    }

    public void appendWorkshop(List<StormWorkshop> workshops){
        for(StormWorkshop workshop: workshops) {
            mModels.add(new Model(""+(mModels.size()+1), workshop.getCode(), workshop.getName()));
        }
    }

    public void clear(){
        mModels.clear();
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int position) {
        // Header (negative positions will throw an IndexOutOfBoundsException)
        if (position < mHeaderCount) {
            return null;
        }

        // Adapter
        int adjPosition = position - mHeaderCount;
        if (adjPosition < mModels.size()) {
            return mModels.get(adjPosition);
        }

        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View  view;
        ViewHolder viewHolder;
        if(convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        } else {
            view = mActivity.getLayoutInflater().inflate(R.layout.list_item_three_columns, null);
            viewHolder = new ViewHolder();
            viewHolder.idView = (TextView)view.findViewById(R.id.id);
            viewHolder.codeView = (TextView)view.findViewById(R.id.code);
            viewHolder.nameView = (TextView)view.findViewById(R.id.name);
            view.setTag(viewHolder);
        }
        Model model = mModels.get(i);
        viewHolder.idView.setText(model.index);
        viewHolder.codeView.setText(model.code);
        viewHolder.nameView.setText(model.name);
        return view;
    }
    class ViewHolder {
        TextView idView;
        TextView codeView;
        TextView nameView;
    }
}

