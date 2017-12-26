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

public class NamedLabelView extends LinearLayout {
    @BindView(R.id.label) TextView mLabelView;
    @BindView(R.id.name) TextView mNameView;

    public NamedLabelView(Context context) {
        super(context);
        init(context, null);
    }

    public NamedLabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NamedLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.view_named_label, this, true);
        ButterKnife.bind(this);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NamedLabelText);
        if(attributes!=null) {
            String name = attributes.getString(R.styleable.NamedLabelText_name);
            mNameView.setText(name);

            String label = attributes.getString(R.styleable.NamedLabelText_label);
            mLabelView.setText(label);
        }
    }

    public void setLabel(String str){
        mLabelView.setText(str);
    }
}
