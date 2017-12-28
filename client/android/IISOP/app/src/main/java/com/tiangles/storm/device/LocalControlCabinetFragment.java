package com.tiangles.storm.device;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.panel.PanelActivity;
import com.tiangles.storm.views.TitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LocalControlCabinetFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.title) TitleView mTitleView;
    @BindView(R.id.cabinet_signal_list) ListView mSignalList;

    LocalControlCabinet mLocalControlCabinet;
    public LocalControlCabinetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_control_cabinet, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(mLocalControlCabinet == null) {
            String code = savedInstanceState.getString("device_code");
            mLocalControlCabinet = StormApp.getDBManager().getStormDB().getLocalControlCabinet(code);
        }
        showCabinet(mLocalControlCabinet);
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
    }

    public void setCabinet(LocalControlCabinet cabinet){
        mLocalControlCabinet = cabinet;
    }

    private void showCabinet(LocalControlCabinet cabinet){
        mTitleView.setTitle(cabinet.getCode(), cabinet.getName());

        mSignalList.addHeaderView(createListviewHeader());
        mSignalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListViewHolder holder = (ListViewHolder) view.getTag();
                Intent intent = new Intent(getActivity(), PanelActivity.class);
                intent.putExtra("connection_code", holder.codeView.getText());
                intent.putExtra("content_type", "local_control_connection");
                startActivity(intent);
            }
        });
        mSignalList.setAdapter(new ListViewAdaptor(cabinet));
    }

    private View createListviewHeader(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.list_item_three_columns, null);
        TextView idView = (TextView)view.findViewById(R.id.id);
        TextView codeView = (TextView)view.findViewById(R.id.code);
        TextView nameView = (TextView)view.findViewById(R.id.name);
        idView.setText("序号");
        codeView.setText("信号编码");
        nameView.setText("信号名称");
        return view;
    }

    private class ListViewHolder {
        TextView idView;
        TextView codeView;
        TextView nameView;
    }

    private class ListViewAdaptor extends BaseAdapter {
        List<LocalControlCabinetConnection> connections;
        ListViewAdaptor(LocalControlCabinet cabinet){
            this.connections = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnectionForCabinet(cabinet);
        }
        @Override
        public int getCount() {
            return connections.size();
        }

        @Override
        public Object getItem(int i) {
            return connections.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                view = inflater.inflate(R.layout.list_item_three_columns, null);
                ListViewHolder holder = new ListViewHolder();
                holder.idView = (TextView)view.findViewById(R.id.id);
                holder.codeView = (TextView)view.findViewById(R.id.code);
                holder.nameView = (TextView)view.findViewById(R.id.name);
                view.setTag(holder);
            }
            ListViewHolder holder = (ListViewHolder) view.getTag();
            holder.idView.setText("" + (i+1));
            holder.codeView.setText(connections.get(i).getCode());
            holder.nameView.setText(connections.get(i).getName());
            return view;
        }
    }

}
