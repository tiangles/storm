package com.tiangles.storm.legend;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.tiangles.storm.legend.model.Circle;
import com.tiangles.storm.legend.model.Line;
import com.tiangles.storm.legend.model.Text;

import java.util.Vector;

public class LegendBase {
    protected String name = "#1引风机出口电动门";
    protected String code = "HNA50AA001";
    protected boolean highlight;

    protected Vector<LegendBase> leftDevices = new Vector<>();
    protected Vector<LegendBase> rightDevices = new Vector<>();
    protected Rect rect;
    protected Vector<Line> lines = new Vector<>();
    protected Vector<Circle> circles = new Vector<>();
    protected Vector<Text> texts = new Vector<>();
    protected int nameOffset = 0;
    protected int codeOffset = 0;
    protected Point leftLinkPoint = new Point();
    protected Point rightLinkPoint = new Point();
    protected int textFontSize = 50;
    protected int codeFontSize = 35;
    protected int nameFontSize = 25;
    protected float baseLength = 80.f;

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
    public void setRect(Rect rect) {
        this.rect = rect;
    }
    public Rect getRect() {
        return this.rect;
    }

    public String name(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addLeftDevice(LegendBase d) {
        if(d != null) {
            leftDevices.add(d);
        }
    }

    public void addRightDevice(LegendBase d) {
        if(d != null) {
            rightDevices.add(d);
        }
    }

    public Vector<LegendBase> getLeftDevices(){
        return leftDevices;
    }

    public Vector<LegendBase> getRightDevices(){
        return rightDevices;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Point getLeftDockPoint() {
        return new Point(leftLinkPoint.x+rect.centerX(), leftLinkPoint.y+rect.centerY());
    }

    public Point getRightDockPoint() {
        return new Point(rightLinkPoint.x+rect.centerX(), rightLinkPoint.y+rect.centerY());
    }

    public void draw(Canvas canvas, Paint paint) {
        if(highlight) {
            paint.setColor(Color.rgb(0, 0, 255));
        } else {
            paint.setColor(Color.rgb(0, 0, 0));
        }
        paint.setStrokeWidth(3);


        float radio = baseLength /(float) rect.width();
        canvas.translate(rect.centerX(), rect.centerY());
        canvas.scale(radio, radio);

        paint.setTextSize(textFontSize);
        paint.setStyle(Paint.Style.STROKE);
        for(Line line: lines) {
            canvas.drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y, paint);
        }
        for(Circle circle: circles) {
            canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, paint);
        }
        paint.setStyle(Paint.Style.FILL);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (fontMetrics.bottom - fontMetrics.top) / 2 ;
        for(Text text: texts) {
            Rect bounds = new Rect();
            paint.getTextBounds(text.c, 0, 1, bounds);
            canvas.drawText(text.c,
                    text.center.x - bounds.width()/2 - bounds.left/2,
                    text.center.y - bounds.height()/2 + baseline,
                    paint);
        }
        drawCode(canvas, paint);
        drawName(canvas, paint);

        canvas.scale(1/radio, 1/radio);
        canvas.translate(-rect.centerX(), -rect.centerY());
    }

    private void drawCode(Canvas canvas, Paint paint){
        paint.setTextSize(codeFontSize);
        Rect bounds = new Rect();
        paint.getTextBounds(code, 0, code.length(), bounds);
        canvas.drawText(code,
                -bounds.width()/2,
                nameOffset,
                paint);
    }

    private void drawName(Canvas canvas, Paint paint) {
        paint.setTextSize(nameFontSize);
        Rect bounds = new Rect();
        paint.getTextBounds(name, 0, name.length(), bounds);
        canvas.drawText(name,
                -bounds.width()/2,
                codeOffset,
                paint);
    }

}
