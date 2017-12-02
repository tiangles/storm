package com.tiangles.storm.legend.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Text extends Model{
    public Text(String c, Point center) {
        this.c = c;
        this.center = center;
    }
    public Text(String c, int x, int y) {
        this.c = c;
        this.center = new Point(x, y);
    }
    public String c;
    public Point center;

    @Override
    public void draw(Canvas canvas, Paint paint) {

    }
}
