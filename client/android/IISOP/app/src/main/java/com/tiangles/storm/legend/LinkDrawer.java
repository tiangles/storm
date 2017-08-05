package com.tiangles.storm.legend;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class LinkDrawer {
    public LinkDrawer(){

    }
    public void draw(Canvas canvas, Paint paint, Point p1, Point p2){
        canvas.drawLine(p1.x, p1.y, (p1.x + p2.x)/2, p1.y, paint);
        canvas.drawLine((p1.x + p2.x)/2, p1.y, (p1.x + p2.x)/2, p2.y, paint);
        canvas.drawLine((p1.x + p2.x)/2, p2.y, p2.x, p2.y, paint);
////        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
//        float[] coords = new float[]{p1.x, p1.y,
//                (p1.x + p2.x)/2, p1.y,
//                (p1.x + p2.x)/2, p2.y,
//                p2.x, p2.y};
//
//        canvas.drawLines(coords, 0, 3, paint);
    }

}
