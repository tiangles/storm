package com.tiangles.storm.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;

public class DeviceSystemView extends LinearLayout {
    private ImageView deviceImage;
    private TextView  deviceTitle;
    private TextView  deviceCode;
    private TextView  deviceName;
    private OnClickListener onClickListener;
    public DeviceSystemView(Context context) {
        this(context, null);
    }

    public DeviceSystemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 导入布局
        LayoutInflater.from(context).inflate(R.layout.view_device_link, this, true);
        init();


        deviceImage.setClickable(false);
        deviceImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(DeviceSystemView.this);
                }
            }
        });
    }

    private void init(){
        deviceImage = (ImageView) findViewById(R.id.device_link_image);
        deviceCode = (EditText) findViewById(R.id.device_code);
        deviceName = (TextView) findViewById(R.id.device_name);
        deviceTitle = (TextView) findViewById(R.id.view_title);

        deviceCode.setEnabled(false);
    }

    public void setOnClickListener(OnClickListener l){
        onClickListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setEditable(boolean enabled) {
        deviceCode.setEnabled(enabled);
    }

    public CharSequence getDeviceCode(){
        return deviceCode.getText();
    }

    public void setTitle(String title) {
        deviceTitle.setText(title);
    }

    public void setImageId(int id) {
        deviceImage.setBackgroundResource(id);
    }

    public void setDeviceCode(String code) {
        deviceCode.setText(code);
    }

    public void setDeviceName(String name) {
        deviceName.setText(name);
    }

}
