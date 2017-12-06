package com.tiangles.storm.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.DeviceAioSignal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private final static int MAX_RECORD_COUNT = 60;
    @BindView(R.id.title_label) TextView mLabelView;
    @BindView(R.id.line_chart) LineChartView lineChart;
    DeviceAioSignal mSignal;
    List<Float> mValues = new ArrayList<>(MAX_RECORD_COUNT+1);
    Handler mHandler = new Handler();
    Runnable mRunnable  = new Runnable() {
        @Override
        public void run() {
            float val = generateData();
            mValues.add(val);
            if(mValues.size()>MAX_RECORD_COUNT){
                mValues.remove(0);
            }
            updateChart();
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        String signalCode = intent.getStringExtra("signal_code");
        if(signalCode == null || signalCode.isEmpty()) {
            signalCode = "HNA60CP101";
        }

        mSignal = StormApp.getDBManager().getStormDB().getDeviceAioSignal(signalCode);
        mLabelView.setText(mSignal.getName());

        setupChart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mHandler.post(mRunnable);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }


    LineChartData data;
    List<Line> lines;
    Axis axisX;
    Axis axisY;
    private void setupChart(){
        lineChart.setInteractive(false);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setZoomEnabled(false);
        lineChart.setMaxZoom(1.0f);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        lines = createLines();
        data = new LineChartData();
        data.setAxisYLeft(createYAxis());
        if(mSignal.getMin_range()<0) {
            data.setAxisXTop(createXAxis());
        } else {
            data.setAxisXBottom(createXAxis());
        }
    }

    private Axis createXAxis(){
        if(axisX == null) {
            axisX = Axis.generateAxisFromRange(0, MAX_RECORD_COUNT, 1);
            axisX.setHasTiltedLabels(false);
            axisX.setTextColor(Color.BLUE);
            axisX.setMaxLabelChars(1);//max label length, for example 60
            axisX.setTextSize(10);
            List<AxisValue> axisValues = axisX.getValues();
            for(AxisValue value: axisValues){
                value.setLabel(""+value.getValue());
            }
            axisX.setValues(axisValues);
        }
        return axisX;
    }

    private Axis createYAxis(){
        if(axisY == null) {
            float max = mSignal.getMax_range();
            float min = mSignal.getMin_range();
            Log.e("Chart", "range: " + max + ", " + min);
            axisY = Axis.generateAxisFromRange(min, max, (max-min)/10);
            axisY.setHasTiltedLabels(false);
            axisY.setTextColor(Color.BLUE);
            axisY.setMaxLabelChars(1);
            axisY.setTextSize(10);
            axisY.setHasLines(true);
            axisY.setName(mSignal.getUnit());

            List<AxisValue> axisValues = axisY.getValues();
            for(AxisValue value: axisValues){
                String str = ""+value.getValue();
                value.setLabel(str);
                Log.e("Chart", "YAxis label: " + str);
            }
            axisY.setValues(axisValues);
        }

        return axisY;
    }

    private List<PointValue> createPointValues(){
        List<PointValue> values = new ArrayList<>();
        for(int i=0; i<mValues.size(); ++i) {
            values.add(new PointValue(i, mValues.get(i)));
        }
        return values;
    }

    private List<Line> createLines(){
        if(lines == null) {
            lines = new ArrayList<>();
            Line line = new Line();
            line.setColor(Color.parseColor("#FFCD41"));
            line.setShape(ValueShape.CIRCLE); //ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND
            line.setCubic(true); //smooth?
            line.setFilled(false);
            line.setHasLabels(false);
            line.setHasLines(true); //show lines or not
            line.setHasPoints(true); //show points or not
            line.setPointRadius(2);
            line.setStrokeWidth(1);
            lines.add(line);
        }

        return lines;
    }

    private void updateChart(){
        lines.get(0).setValues(createPointValues());

        data.setLines(lines);
        lineChart.setLineChartData(data);

        float max = mSignal.getMax_range();
        float min = mSignal.getMin_range();
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = MAX_RECORD_COUNT;
        v.top =  max;
        v.bottom = min;
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
    }

    private float generateData(){
        float max = mSignal.getMax_range();
        float min = mSignal.getMin_range();

        float val = (max+min)/2 + ((float)Math.random() - 0.5f)*(max-min)*0.1f;
        return val;
    }

}
