package com.tistory.kollhong.arduino_bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class vSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_settings);
    }

    public void onSensorConnectBtn(View v) {
        startActivity(new Intent(getApplicationContext(), vFirstLaunch.class));
    }
}
