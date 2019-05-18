/*
 * Copyright (c) 2019. Team 김우준. All Rights Reserved.
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
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityCalendar extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";
    //String session;
    private mRecords records;
    float weekmax = 0;
    private ChartProgressBar mChart;
    float weeksum = 0;
    private mAccounts accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        Intent intent = getIntent();
        String session = intent.getStringExtra("session");

        //TODO session을 복호화 해서 id찾기
        records = new mRecords(this, session, true);
        accounts = new mAccounts(this, true);

        //ArrayList<BarData> MonthList = new ArrayList<>();

        //Calendar month = Calendar.getInstance();


        buildWeekGraph();


        //370ml
        //221ml
        ProgressBar PB = findViewById(R.id.alcRecomBar);
        PB.setMax(alcoholLimit() * 7);
        PB.setProgress((int) weeksum);
        TextView alcohol = findViewById(R.id.alcRecom);
        alcohol.setText(getString(R.string.alcohol_limit) + " : " + alcoholLimit());
    }


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

    void buildWeekGraph() {


        ArrayList<BarData> WeekList = new ArrayList<>();

        Calendar week = Calendar.getInstance();

        if (BuildConfig.DEBUG) {
            week.set(2019, Calendar.MAY, 4, 11, 34, 0);
        }

        week.set(Calendar.DAY_OF_MONTH, 12);
        week.set(Calendar.MINUTE, 0);
        week.set(Calendar.SECOND, 0);
        week.set(Calendar.MILLISECOND, 0);
        week.set(Calendar.DAY_OF_WEEK, week.getFirstDayOfWeek());

        int day = 32;
        while (week.get(Calendar.DAY_OF_MONTH) < day) {
            day = week.get(Calendar.DAY_OF_MONTH);
            week.add(Calendar.DAY_OF_MONTH, -7);
        }


        float recordf1;
        BarData data1;
        for (int i = 1; i < 5; i++) {
            recordf1 = buildWeekData(week);

            weeksum += recordf1;
            if (weekmax < recordf1) weekmax = recordf1;

            data1 = new BarData(i + getString(R.string.week), recordf1, recordf1 + "mL");
            WeekList.add(data1);

            week.add(Calendar.WEEK_OF_YEAR, 1);

            if (BuildConfig.DEBUG) {
                Log.i(TAG, "time : " + week.getTimeInMillis());
                Log.i(TAG, "amount : " + recordf1);
            }
        }


        mChart = findViewById(R.id.ChartProgressBar);
        mChart.setMaxValue(weekmax);
        mChart.setDataList(WeekList);
        mChart.build();
    }

    float buildWeekData(Calendar week) {
        return (float) records.getWeekRecord(week.getTime());
    }

    int alcoholLimit() {
        int gender = accounts.getInt();
        if (gender == 0) {
            return 370;
        } else return 221;
    }
}
