package com.tistory.kollhong.arduino_bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class vMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_main);


        mPrefMan mPrefMan = new mPrefMan(getApplicationContext());
        if (!mPrefMan.init) {
            /*
            TODO 첫 실행이면 웰컴 액티비티 실행
             */
            Intent firstlaunch_intent = new Intent(getApplicationContext(), vFirstLaunch.class);
            firstlaunch_intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(firstlaunch_intent);
        }

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    public void onSettingsBtn(View v) {
        startActivity(new Intent(getApplicationContext(), vFirstLaunch.class));
    }
}
