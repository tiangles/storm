package com.tiangles.storm.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tiangles.storm.legend.PanelLink;

public class PanelLinkView extends View {
    private PanelLink mPanelLink = new PanelLink();
    private Paint paint = new Paint();

    public PanelLinkView(Context context) {
        super(context);
        init();
    }

    public PanelLinkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PanelLinkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        mPanelLink.draw(canvas, paint);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mPanelLink.setRect(new Rect(0, 0, w, h));
    }

}
