package com.tiangles.storm.legend;

import android.graphics.Point;

import com.tiangles.storm.legend.model.Circle;
import com.tiangles.storm.legend.model.Ellipse;
import com.tiangles.storm.legend.model.Line;
import com.tiangles.storm.legend.model.Text;

import java.util.Vector;

/**
 * Created by btian on 10/14/17.
 */

public class DeviceLinkLegend {
    protected Vector<Line> lines = new Vector<>();
    protected Vector<Circle> circles = new Vector<>();
    protected Vector<Ellipse> ellipses = new Vector<>();
    protected Vector<Text> texts = new Vector<>();
    protected int nameOffset = 0;
    protected int codeOffset = 0;
    protected Point leftLinkPoint = new Point();
    protected Point rightLinkPoint = new Point();
    protected int textFontSize = 50;
    protected int codeFontSize = 35;
    protected int nameFontSize = 25;
    protected float baseLength = 80.f;
}
