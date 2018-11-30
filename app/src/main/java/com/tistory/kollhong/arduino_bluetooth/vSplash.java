/*
 * Copyright (c) 2018. KollHong. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        mApplicationVO applicationVO = (mApplicationVO) getApplicationContext();

        if (Build.VERSION.SDK_INT >= 24) {
            applicationVO.ROOT_DIR = getApplicationContext().getDataDir().getAbsolutePath();
        } else {
            applicationVO.ROOT_DIR = getApplication().getFilesDir().getAbsolutePath();
        }

        try {
            Thread.sleep(500);      //스플래시 보여주기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(vSplash.this, vMainActivity.class));
        finish();
    }
}
