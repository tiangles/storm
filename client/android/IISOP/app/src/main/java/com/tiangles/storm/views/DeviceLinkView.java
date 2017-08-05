package com.tiangles.storm.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.legend.LegendBase;
import com.tiangles.storm.legend.ElectricValve;
import com.tiangles.storm.legend.LinkDrawer;

import java.util.Vector;

public class DeviceLinkView extends View{
    private LegendBase deviceLegend = new ElectricValve();
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
        deviceLegend = new ElectricValve();
        deviceLegend.addLeftDevice(new ElectricValve());
        deviceLegend.addLeftDevice(new ElectricValve());
        deviceLegend.addRightDevice(new ElectricValve());
        deviceLegend.addRightDevice(new ElectricValve());
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setStrokeWidth(2);

        deviceLegend.draw(canvas, paint);

        Vector<LegendBase> leftLegend = deviceLegend.getLeftDevices();
        for(int i=0; i<leftLegend.size(); ++i){
            LegendBase leftDevice = leftLegend.get(i);
            leftDevice.draw(canvas, paint);
            linkDrawer.draw(canvas, paint, deviceLegend.getLeftDockPoint(), leftDevice.getRightDockPoint());
        }

        Vector<LegendBase> rightDevices = deviceLegend.getRightDevices();
        for(int i=0; i<rightDevices.size(); ++i){
            LegendBase rightDevice = rightDevices.get(i);
            rightDevice.draw(canvas, paint);
            linkDrawer.draw(canvas, paint, deviceLegend.getRightDockPoint(), rightDevice.getLeftDockPoint());
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {

        int cellWidth = w/3;
        int cellHeight = h/3;

        Point cellPos = new Point(cellWidth, cellHeight/2);
        deviceLegend.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));

        Vector<LegendBase> leftDevices = deviceLegend.getLeftDevices();
        for (int i = 0; i < leftDevices.size(); ++i) {
            cellPos.x = 0;
            cellPos.y = cellHeight - cellHeight/2;
            if(i == 1) {
                cellPos.y = cellHeight*2 - cellHeight/2;
            } else if(i == 2) {
                cellPos.y = 0;
            }
            LegendBase leftDevice = leftDevices.get(i);
            leftDevice.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));
        }

        Vector<LegendBase> rightDevices = deviceLegend.getRightDevices();
        for (int i = 0; i < rightDevices.size(); ++i) {
            cellPos.x = cellWidth*2;
            cellPos.y = cellHeight - cellHeight/2;
            if(i == 1) {
                cellPos.y = cellHeight*2 - cellHeight/2;
            } else if(i == 2) {
                cellPos.y = 0;
            }
            LegendBase rightDevice = rightDevices.get(i);
            rightDevice.setRect(new Rect(cellPos.x, cellPos.y, cellPos.x + cellWidth, cellPos.y+cellHeight));
        }
    }

}
