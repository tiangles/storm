package com.tiangles.storm.legend;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.tiangles.storm.legend.model.Circle;
import com.tiangles.storm.legend.model.Line;
import com.tiangles.storm.legend.model.Model;
import com.tiangles.storm.legend.model.Text;

import java.util.Vector;

public class DeviceLink {
    protected String name;
    protected String code;
    protected boolean highlight;

    protected DeviceLinkLegend legend;

    protected Vector<DeviceLink> leftDevices = new Vector<>();
    protected Vector<DeviceLink> rightDevices = new Vector<>();
    protected Rect rect;

    public DeviceLink(DeviceLinkLegend legend){
        this.legend = legend;
    }

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

    public void addLeftDevice(DeviceLink d) {
        if(d != null) {
            leftDevices.add(d);
        }
    }

    public void addRightDevice(DeviceLink d) {
        if(d != null) {
            rightDevices.add(d);
        }
    }

    public Vector<DeviceLink> getLeftDevices(){
        return leftDevices;
    }

    public Vector<DeviceLink> getRightDevices(){
        return rightDevices;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Point getLeftDockPoint() {
        return new Point(legend.leftLinkPoint.x+rect.centerX(), legend.leftLinkPoint.y+rect.centerY());
    }

    public Point getRightDockPoint() {
        return new Point(legend.rightLinkPoint.x+rect.centerX(), legend.rightLinkPoint.y+rect.centerY());
    }

    public void draw(Canvas canvas, Paint paint) {
        if(highlight) {
            paint.setColor(Color.rgb(0, 0, 255));
        } else {
            paint.setColor(Color.rgb(0, 0, 0));
        }
        paint.setStrokeWidth(3);


        canvas.translate(rect.centerX(), rect.centerY());
        float xRadio = legend.width/(float)rect.width();
        float yRadio = legend.height/(float)rect.height();
        canvas.scale(xRadio, yRadio);

        paint.setTextSize(legend.textFontSize);
        paint.setStyle(Paint.Style.STROKE);
        for(Model model: legend.models) {
            model.draw(canvas, paint);
        }
        paint.setStyle(Paint.Style.FILL);

//        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//        int baseline = (fontMetrics.bottom - fontMetrics.top) / 2 ;
//        for(Text text: legend.texts) {
//            Rect bounds = new Rect();
//            paint.getTextBounds(text.c, 0, 1, bounds);
//            canvas.drawText(text.c,
//                    text.center.x - bounds.width()/2 - bounds.left/2,
//                    text.center.y - bounds.height()/2 + baseline,
//                    paint);
//        }
        drawCode(canvas, paint);
        drawName(canvas, paint);

        canvas.scale(1.0f/xRadio, 1.0f/yRadio);
        canvas.translate(-rect.centerX(), -rect.centerY());
    }

    private void drawCode(Canvas canvas, Paint paint){
        paint.setTextSize(legend.codeFontSize);
        Rect bounds = new Rect();
        paint.getTextBounds(code, 0, code.length(), bounds);
        canvas.drawText(code,
                -bounds.width()/2,
                legend.nameOffset,
                paint);
    }

    private void drawName(Canvas canvas, Paint paint) {
        paint.setTextSize(legend.nameFontSize);
        Rect bounds = new Rect();
        paint.getTextBounds(name, 0, name.length(), bounds);
        canvas.drawText(name,
                -bounds.width()/2,
                legend.codeOffset,
                paint);
    }

}
