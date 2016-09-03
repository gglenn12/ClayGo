package com.himolabs.claygo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class GetPoints extends AppCompatActivity {
    QREader qrEader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_points);



        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.camera_view);

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);

        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override public void onDetected(final String data) {
                // Do something with the string data

                show();
            }
        }).build();

        qrEader.init();
        qrEader.start();
    }

    private void show() {
        setResult(RESULT_OK);
        finish();
        qrEader.stop();
        qrEader.releaseAndCleanup();
    }


}
