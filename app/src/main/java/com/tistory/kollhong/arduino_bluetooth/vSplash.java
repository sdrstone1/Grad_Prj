package com.tistory.kollhong.arduino_bluetooth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class vSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_splash);
        String ROOT_DIR;
        if (Build.VERSION.SDK_INT >= 24) {
            ROOT_DIR = getApplicationContext().getDataDir().getAbsolutePath();
        } else {
            ROOT_DIR = getApplication().getFilesDir().getAbsolutePath();
        }

        //String ROOT_DIR = getApplicationContext().getDataDir().getAbsolutePath();
        String DATABASE_NAME = "data.db";

//        if(BuildConfig.isTEST)
//        {
//            //File file = new File(ROOT_DIR, "/database/data.db");
//            //boolean deleted = file.delete();
//        }
        try {
            Thread.sleep(200);      //스플래시 보여주기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(vSplash.this, vMainActivity.class));
        finish();
    }
}
