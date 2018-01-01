package com.tiangles.storm.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.UserEvent;
import com.tiangles.storm.views.ThreeColumsListAdaptor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserEventActivity extends Activity {
    @BindView(R.id.event_list)    ListView mEventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event);
        ButterKnife.bind(this);
        initializeView();
    }


    private void initializeView(){
        List<UserEvent> events = StormApp.getDBManager().getUserDB().getUserEventByUser(StormApp.getCurrentUser());
        ThreeColumsListAdaptor adaptor = new ThreeColumsListAdaptor(this);
        adaptor.updateByUserEvents(events);

        mEventListView.setAdapter(adaptor);
    }
}
