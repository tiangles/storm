package com.tiangles.storm.legend.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Text extends Model{
    public Text(String c, Point center) {
        this.text = c;
        this.center = center;
    }
    public Text(String c, int size, int x, int y) {
        this.text = c;
        this.size = size;
        this.center = new Point(x, y);
    }
    public String text;
    public Point center;
    public int size;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        paint.setTextSize(size);

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        canvas.drawText(text,
                center.x - paint.measureText(text)/2,
                center.y - fm.ascent/2,
                paint);
    }
}
