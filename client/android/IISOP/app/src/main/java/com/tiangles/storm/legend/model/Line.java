package com.tiangles.storm.legend.model;

import android.graphics.Point;

public class Line {
    public Line(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }
    public Line(int x1, int y1, int x2, int y2) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
    }
    public Point p1;
    public Point p2;
}
