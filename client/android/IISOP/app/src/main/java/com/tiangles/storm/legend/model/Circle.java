package com.tiangles.storm.legend.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Circle extends Model{
    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle(int cx, int cy, int radius) {
        this.center = new Point(cx, cy);
        this.radius = radius;
    }
    public Point center;
    public int radius;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(center.x, center.y, radius, paint);
    }
}
