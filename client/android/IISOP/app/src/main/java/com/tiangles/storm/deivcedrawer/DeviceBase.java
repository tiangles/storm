package com.tiangles.storm.deivcedrawer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Vector;

public abstract class DeviceBase {
    private String type;

    protected String name = "#1引风机出口电动门";
    protected String code = "HNA50AA001";
    protected Vector<DeviceBase> leftDevices = new Vector<>();
    protected Vector<DeviceBase> rightDevices = new Vector<>();
    protected Rect rect;

    public Rect getRect() {
        return rect;
    }
    public void setRect(Rect rect) {
        this.rect = rect;
        onRectChanged();
    }
    public String name(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void addLeftDevice(DeviceBase d) {
        leftDevices.add(d);
    }
    public void addRightDevice(DeviceBase d) {
        rightDevices.add(d);
    }
    public Vector<DeviceBase> getLeftDevices(){
        return leftDevices;
    }
    public Vector<DeviceBase> getRightDevices(){
        return rightDevices;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract void draw(Canvas canvas, Paint paint);
    public abstract Point getLeftDockPoint();
    public abstract Point getRightDockPoint();
    public abstract Point getUpDockPoint();
    public abstract Point getDownDockPoint();

    protected abstract void onRectChanged();
}
