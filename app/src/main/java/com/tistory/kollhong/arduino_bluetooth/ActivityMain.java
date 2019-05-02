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
import android.view.View;
import android.widget.ImageButton;

public class ActivityMain extends AppCompatActivity {
    String session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {





        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        session = intent.getStringExtra("session");


        super.onCreate(savedInstanceState);

        ImageButton calendarbutton = findViewById(R.id.calendarbutton);
        calendarbutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivityCalendar.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                });

        ImageButton mypagebutton = findViewById(R.id.mypagebutton);
        mypagebutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivityMypage.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                });
        ImageButton LEDbutton = findViewById(R.id.LEDbutton);
        LEDbutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivityLEDsettings.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                });
        ImageButton Controlbutton = findViewById(R.id.Controlbutton);
        Controlbutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivitySetting.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}