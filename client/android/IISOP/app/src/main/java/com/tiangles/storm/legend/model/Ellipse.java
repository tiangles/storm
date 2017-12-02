package com.tiangles.storm.legend.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Ellipse extends Model{
    public Ellipse(int cx, int cy, int rx, int ry){
        this.cx = cx;
        this.cy = cy;
        this.rx = rx;
        this.ry = ry;
    }
    public int cx;
    public int cy;
    public int rx;
    public int ry;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawOval(cx-rx/2, cy-ry/2, cx+ry/2, cy+ry/2, paint);
    }
}
