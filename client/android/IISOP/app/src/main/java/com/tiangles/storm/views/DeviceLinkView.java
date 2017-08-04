package com.tiangles.storm.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;

import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.deivcedrawer.DeviceBase;
import com.tiangles.storm.deivcedrawer.ElectricValve;
import com.tiangles.storm.deivcedrawer.LinkDrawer;

import java.util.Vector;

public class DeviceLinkView extends View{
    private DeviceBase deviceDrawer = new ElectricValve();
    private LinkDrawer linkDrawer = new LinkDrawer();
    private Paint paint = new Paint();

    public DeviceLinkView(Context context) {
        super(context);
        setDevice(null);
    }

    public DeviceLinkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDevice(null);
    }

    public DeviceLinkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDevice(null);
    }

    public void setDevice(StormDevice device){
        deviceDrawer = new ElectricValve();
        deviceDrawer.addLeftDevice(new ElectricValve());
        deviceDrawer.addLeftDevice(new ElectricValve());
        deviceDrawer.addRightDevice(new ElectricValve());
        deviceDrawer.addRightDevice(new ElectricValve());
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setStrokeWidth(2);

        deviceDrawer.draw(canvas, paint);

        Vector<DeviceBase> leftDevices = deviceDrawer.getLeftDevices();
        for(int i=0; i<leftDevices.size(); ++i){
            DeviceBase leftDevice = leftDevices.get(i);
            leftDevice.draw(canvas, paint);
            linkDrawer.draw(canvas, paint, deviceDrawer.getLeftDockPoint(), leftDevice.getRightDockPoint());
        }

        Vector<DeviceBase> rightDevices = deviceDrawer.getRightDevices();
        for(int i=0; i<rightDevices.size(); ++i){
            DeviceBase rightDevice = rightDevices.get(i);
            rightDevice.draw(canvas, paint);
            linkDrawer.draw(canvas, paint, deviceDrawer.getRightDockPoint(), rightDevice.getLeftDockPoint());
        }

    }

    private int width;
    private int height;
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;

        int cellWidth = w/3;
        int cellHeight = h/3;

        Point cellPos = new Point(cellWidth, cellHeight/2);
        deviceDrawer.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));

        Vector<DeviceBase> leftDevices = deviceDrawer.getLeftDevices();
        for (int i = 0; i < leftDevices.size(); ++i) {
            cellPos.x = 0;
            cellPos.y = cellHeight - cellHeight/2;
            if(i == 1) {
                cellPos.y = cellHeight*2 - cellHeight/2;
            } else if(i == 2) {
                cellPos.y = 0;
            }
            DeviceBase leftDevice = leftDevices.get(i);
            leftDevice.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));
        }

        Vector<DeviceBase> rightDevices = deviceDrawer.getRightDevices();
        for (int i = 0; i < rightDevices.size(); ++i) {
            cellPos.x = cellWidth*2;
            cellPos.y = cellHeight - cellHeight/2;
            if(i == 1) {
                cellPos.y = cellHeight*2 - cellHeight/2;
            } else if(i == 2) {
                cellPos.y = 0;
            }
            DeviceBase rightDevice = rightDevices.get(i);
            rightDevice.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));
        }
    }

}
