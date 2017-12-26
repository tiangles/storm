package com.tiangles.storm.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiangles.storm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleView extends LinearLayout {
    @BindView(R.id.code) TextView mCodeView;
    @BindView(R.id.name) TextView mNameView;

    public TitleView(Context context) {
        super(context);
        init(context, null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.title_view, this, true);
        ButterKnife.bind(this);

    }

    public void setTitle(String code, String name){
        mCodeView.setText(code);
        mNameView.setText(name);
    }
}
