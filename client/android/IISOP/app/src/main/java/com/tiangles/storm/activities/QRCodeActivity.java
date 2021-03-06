package com.tiangles.storm.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.tiangles.storm.R;
import com.tiangles.storm.utilities.DES3Utils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRCodeActivity extends AppCompatActivity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private final static String PROTOCAL_HEADER = "storm://";

    @BindView(R.id.scan_result) EditText mResultTextView;
    @BindView(R.id.input_message)  EditText mInputMessageTextView;
    @BindView(R.id.iv_qr_image) ImageView mQRImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
    }


    public void scanQRCode(View v){
        Intent openCameraIntent = new Intent(QRCodeActivity.this,
                CaptureActivity.class);
        startActivityForResult(openCameraIntent, SCANNIN_GREQUEST_CODE);
    }

    public void createQRCode(View v){
        String message = mInputMessageTextView.getText().toString();
        String encoded = DES3Utils.encryptMode(message);
        showQRCode(PROTOCAL_HEADER + encoded);
    }

    public void showDevice(View v) {
//        if(!mScanResult.isEmpty()) {
//            Intent intent = new Intent(this, DeviceActivity.class);
//            intent.putExtra("code", mScanResult);
//            startActivity(intent);
//        }
    }

    private void showQRCode(String message) {
        if(message!= null && !message.isEmpty()){
            try {
                Bitmap bmp = EncodingHandler.createQRCode(message, 512);
                mQRImage.setImageBitmap(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
