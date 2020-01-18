/*
 * Copyright (c) 2019. KollHong. All Rights Reserved.
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static com.tistory.kollhong.arduino_bluetooth.ActivityWelcome.FIRST_LAUNCH;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            Thread.sleep(300);      //스플래시 보여주기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mPreferences mPreferences = new mPreferences(getApplicationContext());
        if (!mPreferences.getBoolValue(com.tistory.kollhong.arduino_bluetooth.mPreferences.APP_INIT)) {

            startActivityForResult(
                    new Intent(getApplicationContext(), ActivityWelcome.class).
                            setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME),
                    FIRST_LAUNCH);
        } else {
            startActivity(new Intent(ActivitySplash.this, ActivityLogin.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FIRST_LAUNCH) {
            mPreferences mPref = new mPreferences(getApplicationContext());
            mPref.setValue(mPreferences.APP_INIT, true);
            startActivity(new Intent(ActivitySplash.this, ActivityLogin.class));
            finish();
        }
    }
}
