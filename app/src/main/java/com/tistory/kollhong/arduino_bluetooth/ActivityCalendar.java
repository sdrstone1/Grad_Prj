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
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityCalendar extends AppCompatActivity {
    //String session;
    mRecords records;
    private ChartProgressBar mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        Intent intent = getIntent();
        String session = intent.getStringExtra("session");

        //TODO session을 복호화 해서 id찾기
        records = new mRecords(this, session, true);

        ArrayList<BarData> MonthList = new ArrayList<>();

        Calendar month = Calendar.getInstance();

        Double record = records.getMonthRecord(month.getTime());
        Float recordf = record.floatValue();

        month.set(Calendar.MONTH, Calendar.JANUARY);
        BarData data = new BarData("JANUARY", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.FEBRUARY);
        data = new BarData("FEBRUARY", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.MARCH);
        data = new BarData("MARCH", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.APRIL);
        data = new BarData("APRIL", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.MAY);
        data = new BarData("MAY", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.JUNE);
        data = new BarData("JUNE", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.JULY);
        data = new BarData("JULY", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.AUGUST);
        data = new BarData("AUGUST", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.SEPTEMBER);
        data = new BarData("SEPTEMBER", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.OCTOBER);
        data = new BarData("OCTOBER", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.NOVEMBER);
        data = new BarData("NOVEMBER", recordf, recordf + "mL");
        MonthList.add(data);

        month.set(Calendar.MONTH, Calendar.DECEMBER);
        data = new BarData("DECEMBER", recordf, recordf + "mL");
        MonthList.add(data);

        mChart = findViewById(R.id.ChartProgressBar);
        mChart.setDataList(MonthList);
        mChart.build();


        ArrayList<BarData> WeekList = new ArrayList<>();

        Calendar week = Calendar.getInstance();

        Double record1 = records.getWeekRecord(week.getTime());
        Float recordf1 = record1.floatValue();

        week.set(Calendar.WEEK_OF_MONTH, 1);
        BarData data1 = new BarData("1주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        week.set(Calendar.WEEK_OF_MONTH, 2);
        data1 = new BarData("2주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        week.set(Calendar.WEEK_OF_MONTH, 3);
        data1 = new BarData("3주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        week.set(Calendar.WEEK_OF_MONTH, 4);
        data1 = new BarData("4주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        mChart = findViewById(R.id.ChartProgressBar);
        mChart.setDataList(WeekList);
        mChart.build();
    }
}
