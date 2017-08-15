package com.tiangles.storm.legend.model;

import android.graphics.Point;

public class Text {
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
}
