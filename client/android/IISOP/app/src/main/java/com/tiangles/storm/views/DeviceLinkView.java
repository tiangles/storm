package com.tiangles.storm.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.legend.DeviceLink;
import com.tiangles.storm.legend.LegendFactory;
import com.tiangles.storm.legend.LinkDrawer;
import com.tiangles.storm.legend.PanelLink;

import java.util.Vector;

public class DeviceLinkView extends View implements View.OnTouchListener{
    private int viewWidth = 80;
    private int viewHeight = 80;
    private GestureDetector mDetector;

    private DeviceLink deviceLegend;
    private LinkDrawer linkDrawer = new LinkDrawer();
    private Paint paint = new Paint();
    private OnLinkedDeviceClickedListener mListener = null;

    public interface OnLinkedDeviceClickedListener {
        int LEFT_DEVICE = -1;
        int THIS_DEVICE = 0;
        int RIGHT_DEVICE = 1;

        boolean onLinkedDeviceClicked(String deviceCode, int linkDirection);
    }

    public DeviceLinkView(Context context) {
        super(context);
        init();
    }

    public DeviceLinkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeviceLinkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnDeviceClickedListener(OnLinkedDeviceClickedListener l){
        this.mListener = l;
    }

    public void setDevice(StormDevice device){
        if(device != null) {
            deviceLegend = LegendFactory.getInstance().createLegend(device, true);
            updateLegendRect(viewWidth, viewHeight);
            invalidate();
        }

    }

    private void init(){
        super.setOnTouchListener(this);
        mDetector = new GestureDetector(StormApp.getInstance().getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int x = (int)e.getX();
                int y = (int)e.getY();
                boolean handled = false;
                handled = handled?handled : handleClickEvent(deviceLegend, OnLinkedDeviceClickedListener.THIS_DEVICE, x, y);
                deviceLegend.setHighlight(false);

                for(DeviceLink legend: deviceLegend.getLeftDevices()) {
                    legend.setHighlight(false);
                    handled = handled?handled : handleClickEvent(legend, OnLinkedDeviceClickedListener.LEFT_DEVICE, x, y);
                }

                for(DeviceLink legend: deviceLegend.getRightDevices()) {
                    legend.setHighlight(false);
                    handled = handled?handled :handleClickEvent(legend, OnLinkedDeviceClickedListener.RIGHT_DEVICE, x, y);
                }
                DeviceLinkView.this.invalidate();
                if(!handled) {
                    mListener.onLinkedDeviceClicked("", OnLinkedDeviceClickedListener.THIS_DEVICE);
                }
                return handled;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(mListener != null) {
            return mDetector.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(deviceLegend == null) {
            return;
        }

        deviceLegend.draw(canvas, paint);

        Vector<DeviceLink> leftLegend = deviceLegend.getLeftDevices();
        for(int i=0; i<leftLegend.size(); ++i){
            DeviceLink leftDevice = leftLegend.get(i);
            leftDevice.draw(canvas, paint);
//            linkDrawer.draw(canvas, paint, deviceLegend.getLeftDockPoint(), leftDevice.getRightDockPoint());
        }

        Vector<DeviceLink> rightDevices = deviceLegend.getRightDevices();
        for(int i=0; i<rightDevices.size(); ++i){
            DeviceLink rightDevice = rightDevices.get(i);
            rightDevice.draw(canvas, paint);
//            linkDrawer.draw(canvas, paint, deviceLegend.getRightDockPoint(), rightDevice.getLeftDockPoint());
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth = w;
        viewHeight = h;
        updateLegendRect(w, h);
    }

    private void updateLegendRect(int w, int h) {
        if(deviceLegend == null) {
            return;
        }

        int cellWidth = w/3;
        int cellHeight = cellWidth;

        Point cellIndex = new Point(1, 0);
        Point cellPos = new Point(cellWidth*cellIndex.x, cellHeight*cellIndex.y);
        deviceLegend.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));

        Vector<DeviceLink> leftDevices = deviceLegend.getLeftDevices();
        for (int i = 0; i < leftDevices.size(); ++i) {
            cellIndex.x = 0;
            cellIndex.y = i;
            cellPos.x = cellWidth*cellIndex.x;
            cellPos.y = cellHeight*cellIndex.y;
            DeviceLink leftDevice = leftDevices.get(i);
            leftDevice.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));
        }

        Vector<DeviceLink> rightDevices = deviceLegend.getRightDevices();
        for (int i = 0; i < rightDevices.size(); ++i) {
            cellIndex.x = 2;
            cellIndex.y = i;
            cellPos.x = cellWidth*cellIndex.x;
            cellPos.y = cellHeight*cellIndex.y;
            DeviceLink rightDevice = rightDevices.get(i);
            rightDevice.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));
        }
    }

    private boolean handleClickEvent(DeviceLink legend, int linkDirection, int x, int y) {
        if(legend.getRect().contains(x, y)) {
            legend.setHighlight(true);
            mListener.onLinkedDeviceClicked(legend.getCode(), linkDirection);
            return true;
        }
        legend.setHighlight(false);
        return false;
    }
}
