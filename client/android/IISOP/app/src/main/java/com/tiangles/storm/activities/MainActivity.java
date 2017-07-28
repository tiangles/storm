package com.tiangles.storm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tiangles.storm.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends AppCompatActivity {
    private final static int SCAN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showQRCodeActivity(View v){
        Intent openCameraIntent = new Intent(MainActivity.this,
                CaptureActivity.class);
        startActivityForResult(openCameraIntent, SCAN_REQUEST_CODE);
    }

    public void showChartActivity(View v){
        Intent intent = new Intent(MainActivity.this, ChartActivity.class);
        startActivity(intent);
    }

    public void showWorkshopActivity(View v){
        Intent intent = new Intent(MainActivity.this, WorkshopActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("ScanResult", result);
                    showDeviceDetailView(result);
                }
                break;
            default:
                break;
        }
    }

    private void showDeviceDetailView(String scanResult) {
        Intent intent = new Intent(this, DeviceInfoActivity.class);
        intent.putExtra("qrCode", scanResult);
        startActivity(intent);
    }
}
