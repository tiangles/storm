package com.tiangles.storm.legend;

import com.tiangles.storm.legend.model.Circle;
import com.tiangles.storm.legend.model.Line;
import com.tiangles.storm.legend.model.Text;


public class ElectricValve extends LegendBase {
    private int h;
    public ElectricValve() {
        type = "type";
        h = 80;
        nameOffset = 90;
        codeOffset = 130;
        baseLenght = 4.25f*h;

        lines.add(new  Line(-h, -h /2, -h,  h /2));
        lines.add(new  Line(h, -h /2, h,  h /2));
        lines.add(new  Line(-h, -h /2, h, h /2));
        lines.add(new  Line(-h,  h /2, h, -h /2));

        lines.add(new  Line(0,  0,  0, -h /2));

        lines.add(new  Line(-2*h,  0,  -h, 0));
        lines.add(new  Line( 2*h,  0,   h, 0));

        circles.add(new Circle(0, -h, h /2));

        texts.add(new Text("M", 0, -h));

        leftLinkPoint.set(-2* h, 0);
        rightLinkPoint.set(2* h, 0);
    }
}
