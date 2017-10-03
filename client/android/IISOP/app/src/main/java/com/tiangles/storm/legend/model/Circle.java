package com.tiangles.storm.legend.model;

import android.graphics.Point;

public class Circle {
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
}
