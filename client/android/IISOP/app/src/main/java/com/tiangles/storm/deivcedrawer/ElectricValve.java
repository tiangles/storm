package com.tiangles.storm.deivcedrawer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;


public class ElectricValve extends DeviceBase {
    private Point leftTop = new Point();
    private Point leftBottom = new Point();
    private Point rightTop = new Point();
    private Point rightBottom = new Point();
    private Point circleCenter = new Point();
    private Point center = new Point();
    private int c = 1;

    public ElectricValve() {
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(50);

        canvas.drawLine(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y, paint);
        canvas.drawLine(leftTop.x, leftTop.y, leftBottom.x, leftBottom.y, paint);
        canvas.drawLine(rightTop.x, rightTop.y, rightBottom.x, rightBottom.y, paint);
        canvas.drawLine(rightTop.x, rightTop.y, leftBottom.x, leftBottom.y, paint);

        canvas.drawLine(rect.left, (leftTop.y+leftBottom.y)/2, leftTop.x, (leftTop.y+leftBottom.y)/2, paint);
        canvas.drawLine(rightTop.x, (rightTop.y+rightBottom.y)/2, rect.right, (rightTop.y+rightBottom.y)/2, paint);

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(center.x, center.y, circleCenter.x, circleCenter.y+c, paint);
        canvas.drawCircle(circleCenter.x, circleCenter.y, c, paint);

        paint.setStyle(Paint.Style.FILL);
        Rect bounds = new Rect();
        paint.getTextBounds("M", 0, 1, bounds);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText("M",
                circleCenter.x - bounds.width() / 2 - bounds.left,
                circleCenter.y - bounds.height()/2 + baseline,
                paint);

        drawCode(canvas, paint);
        drawName(canvas, paint);
    }

    private void drawCode(Canvas canvas, Paint paint){
        paint.setTextSize(35);
        Rect bounds = new Rect();
        paint.getTextBounds(code, 0, code.length(), bounds);
        canvas.drawText(code,
                rect.left + (rect.width()-bounds.width()) / 2,
                leftBottom.y + bounds.height() + c/2,
                paint);
    }

    private void drawName(Canvas canvas, Paint paint) {
        paint.setTextSize(30);
        Rect bounds = new Rect();
        paint.getTextBounds(name, 0, name.length(), bounds);
        canvas.drawText(name,
                rect.left + (rect.width()-bounds.width()) / 2,
                leftBottom.y + bounds.height()*2 + c,
                paint);
    }
    @Override
    public Point getLeftDockPoint() {
        return new Point(leftTop.x, (leftTop.y+leftBottom.y)/2);
    }

    @Override
    public Point getRightDockPoint() {
        return new Point(rightTop.x, (rightTop.y+rightBottom.y)/2);
    }

    @Override
    public Point getUpDockPoint() {
        return new Point(center.x, center.y);
    }

    @Override
    public Point getDownDockPoint() {
        return new Point(center.x, center.y);
    }

    @Override
    protected void onRectChanged() {
//        int h = rect.width()* 1/4;
        int h = 60;
        c = h/2;

        center.set(rect.centerX(), rect.centerY());
        leftTop.set(center.x-h, center.y-h/2);
        leftBottom.set(center.x-h, center.y+h/2);
        rightTop.set(center.x+h, center.y-h/2);
        rightBottom.set(center.x+h, center.y+h/2);
        circleCenter.set(center.x, center.y-h);
    }
}
