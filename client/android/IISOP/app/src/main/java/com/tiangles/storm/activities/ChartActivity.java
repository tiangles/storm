package com.tiangles.storm.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tiangles.storm.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {
    private LineChartView lineChart;
    private Spinner spinner;

    String[] date = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};
    int[] score= {50,42,90,33,10,74,22,18,79,20};
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setupChart();
        setupSpinner();
    }

    private void setupSpinner(){
        spinner = (Spinner) findViewById(R.id.chart_date_choose);
        List<String> dataList = new ArrayList<>();
        dataList.add("2015");
        dataList.add("2016");
        dataList.add("2017");
        dataList.add("2017, Q1");

        ArrayAdapter<String> arrAdapter= new ArrayAdapter<>(this,
                R.layout.spinner_item,
                dataList);
        arrAdapter.setDropDownViewResource(R.layout.spinner_dropdown_stytle);
        spinner.setAdapter(arrAdapter);
    }

    private void setupChart(){
        lineChart = (LineChartView)findViewById(R.id.line_chart);
        /**
         * 设置X 轴的显示
         */
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
        /**
         * 图表的每个点的显示
         */
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }

        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE); //ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND
        line.setCubic(false); //smooth?
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true); //show lines or not
        line.setHasPoints(true); //show points or not
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.WHITE);
        axisX.setName("Date");
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(8);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);

        Axis axisY = new Axis();
        axisY.setName("Score");
        axisY.setTextSize(10);
        data.setAxisYLeft(axisY);

        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom(2.0f);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
    }
}
