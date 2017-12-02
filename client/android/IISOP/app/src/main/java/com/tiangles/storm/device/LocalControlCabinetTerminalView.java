package com.tiangles.storm.device;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.LocalControlCabinet;
import com.tiangles.storm.database.dao.LocalControlCabinetConnection;
import com.tiangles.storm.panel.PanelActivity;

import java.util.List;

public class LocalControlCabinetTerminalView extends View implements View.OnTouchListener{
    private LocalControlCabinet mCabinet;
    public LocalControlCabinetTerminalView(Context context) {
        super(context);
    }

    public LocalControlCabinetTerminalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LocalControlCabinetTerminalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.setOnTouchListener(this);
    }

    public void setCabinet(LocalControlCabinet cabinet) {
        mCabinet = cabinet;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        if(mListener != null) {
//            List<LocalControlCabinetConnection> connections = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnectionForCabinet(mCabinet);
//            mListener.onSignalClicked(connections.get(0));
//        }

        List<LocalControlCabinetConnection> connections = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnectionForCabinet(mCabinet);
        Intent intent = new Intent(StormApp.getContext(), PanelActivity.class);
        intent.putExtra("connection_code", connections.get(0).getCode());
        intent.putExtra("connection_type", "local_control_connection");
        intent.putExtra("cabinet_code", mCabinet.getCode());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        StormApp.getContext().startActivity(intent);

        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLUE);
        paint.setTextSize(getResources().getDimension(R.dimen.local_control_cabinet_terminals_font_size));


        int padding = getResources().getDimensionPixelSize(R.dimen.padding_local_control_cabinet_terminals);
        int cellHeight = getResources().getDimensionPixelSize(R.dimen.local_control_cabinet_terminals_cell_height);
        int totalWidth = getWidth() - 2*padding;
        int startX = (int)getX() + padding;
        int startY = (int)getY() + padding;
        int col0 = startX;
        int col1 = startX + totalWidth/6;
        int col2 = startX + totalWidth/2;
        int col3 = startX + totalWidth;
        int textOffset = 10;

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textBaseline = - fontMetrics.descent+(fontMetrics.bottom-fontMetrics.top)/2;


        canvas.drawLine(startX, startY, getWidth()-padding, startY, paint);


        int offset = 0;
        int offsetY = startY + cellHeight*offset;
        int textX0 = col0 + textOffset;
        int textX1 = col1 + textOffset;
        int textX2 = col2 + textOffset;

        float textY = offsetY + cellHeight/2 + textBaseline;
        canvas.drawText("序号",      textX0, textY , paint);
        canvas.drawText("信号",      textX1, textY, paint);
        canvas.drawText("信号名称",   textX2, textY, paint);
        canvas.drawLine(startX, offsetY+cellHeight, getWidth()-padding, offsetY+cellHeight, paint);

        if(mCabinet != null){
            List<LocalControlCabinetConnection> connections = StormApp.getDBManager().getStormDB().getLocalControlCabinetConnectionForCabinet(mCabinet);
            for(LocalControlCabinetConnection connection: connections) {
                ++offset;
                offsetY = startY + cellHeight*offset;
                textY = offsetY + cellHeight/2 + textBaseline;
                canvas.drawText(String.valueOf(offset), col0 + textOffset, textY , paint);
                canvas.drawText(connection.getCode(),   col1 + textOffset, textY, paint);
                canvas.drawText(connection.getName(),   col2 + textOffset, textY, paint);
                canvas.drawLine(startX, offsetY+cellHeight, getWidth()-padding, offsetY+cellHeight, paint);
            }
        }

        ++offset;
        offsetY = startY + cellHeight*offset;
        canvas.drawLine(col0, startY, col0, offsetY, paint);
        canvas.drawLine(col1, startY, col1, offsetY, paint);
        canvas.drawLine(col2, startY, col2, offsetY, paint);
        canvas.drawLine(col3, startY, col3, offsetY, paint);

    }
}
