package com.tiangles.storm.legend;

import android.graphics.Point;
import android.util.Size;

import com.tiangles.storm.legend.model.Circle;
import com.tiangles.storm.legend.model.Ellipse;
import com.tiangles.storm.legend.model.Line;
import com.tiangles.storm.legend.model.Model;
import com.tiangles.storm.legend.model.Text;

import java.util.Vector;

public class DeviceLinkLegend {
    protected Vector<Model> models = new Vector<>();
    protected int nameOffset = 0;
    protected int codeOffset = 0;
    protected Point leftLinkPoint = new Point();
    protected Point rightLinkPoint = new Point();
    protected int textFontSize = 50;
    protected int codeFontSize = 35;
    protected int nameFontSize = 25;
    protected int height;
    protected int width;
}
