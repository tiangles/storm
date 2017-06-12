package com.tiangles.storm.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tiangles.storm.R;
import com.tiangles.storm.utilities.DES3Utils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

public class QRCodeActivity extends Activity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private final static String PROTOCAL_HEADER = "storm://";
    private EditText mResultTextView;
    private ImageView mQRImage;
    private String mScanResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        mResultTextView = (EditText) this.findViewById(R.id.scan_result);
        mQRImage = (ImageView) this.findViewById(R.id.iv_qr_image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    mScanResult = bundle.getString(CodeUtils.RESULT_STRING);
                    mResultTextView.setText(mScanResult);
                    showQRCode();
                }
                break;
            default:
                break;
        }
    }

    public void scanQRCode(View v){
        Intent openCameraIntent = new Intent(QRCodeActivity.this,
                CaptureActivity.class);
        startActivityForResult(openCameraIntent, SCANNIN_GREQUEST_CODE);
    }

    public void createQRCode(View v){
        mScanResult = mResultTextView.getText().toString();
        showQRCode();
    }

    private void showQRCode() {
        if(!mScanResult.isEmpty()){
            try {
                String encoded = DES3Utils.encryptMode(PROTOCAL_HEADER + mScanResult);
                Bitmap bmp = EncodingHandler.createQRCode(encoded, 512);
                mQRImage.setImageBitmap(bmp);
                Log.e("Scan", mScanResult);
                Log.e("Encoded", encoded);
                String decoded = DES3Utils.decryptMode(encoded);
                Log.e("Decoded", decoded);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
