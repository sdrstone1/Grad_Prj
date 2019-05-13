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
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityCalendar extends AppCompatActivity {
    //String session;
    private mRecords records;
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


        //심지어 이 싴밬새끼 달력보기 xml은 만들지도 않음;;
        /*
        //double record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        //float recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?

        month.set(Calendar.MONTH, Calendar.JANUARY);
        double record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        float recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        BarData data = new BarData("JANUARY", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");


        month.set(Calendar.MONTH, Calendar.FEBRUARY);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("FEBRUARY", recordf, recordf + "mL");
        MonthList.add(data);
        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.MARCH);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("MARCH", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.APRIL);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("APRIL", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.MAY);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("MAY", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.JUNE);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("JUNE", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.JULY);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("JULY", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.AUGUST);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("AUGUST", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.SEPTEMBER);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("SEPTEMBER", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.OCTOBER);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("OCTOBER", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.NOVEMBER);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("NOVEMBER", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        month.set(Calendar.MONTH, Calendar.DECEMBER);
        record = records.getMonthRecord(month.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf = (float) record;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data = new BarData("DECEMBER", recordf, recordf + "mL");
        MonthList.add(data);

        Log.d("time : ", month.getTimeInMillis()+"");
        Log.d("record : ", record+"");

        mChart = findViewById(R.id.ChartProgressBar);
        mChart.setDataList(MonthList);
        mChart.build();

*/
        ArrayList<BarData> WeekList = new ArrayList<>();

        Calendar week = Calendar.getInstance();

        //TODO for DEBUG
        week.set(2019, Calendar.MAY, 4, 11, 34, 0);
        week.set(Calendar.DAY_OF_MONTH, 12);
        week.set(Calendar.MILLISECOND, 0);
        //records.getWeekRecord(week.getTime());
        //TODO for DEBUG

        double record1;
        float recordf1;

        week.set(Calendar.DAY_OF_WEEK, week.getFirstDayOfWeek());
        int day = 32;
        while (week.get(Calendar.DAY_OF_MONTH) < day) {
            day = week.get(Calendar.DAY_OF_MONTH);
            week.add(Calendar.DAY_OF_MONTH, -7);
        }


        record1 = records.getWeekRecord(week.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf1 = (float) record1;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        BarData data1 = new BarData("1주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        Log.d("time : ", week.getTimeInMillis() + "");
        Log.d("record : ", record1 + "");

        week.add(Calendar.WEEK_OF_YEAR, 1);
        record1 = records.getWeekRecord(week.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf1 = (float) record1;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data1 = new BarData("2주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        Log.d("time : ", week.getTimeInMillis() + "");
        Log.d("record : ", record1 + "");

        week.add(Calendar.WEEK_OF_YEAR, 1);
        record1 = records.getWeekRecord(week.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf1 = (float) record1;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data1 = new BarData("3주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        Log.d("time : ", week.getTimeInMillis() + "");
        Log.d("record : ", record1 + "");

        week.add(Calendar.WEEK_OF_YEAR, 1);
        record1 = records.getWeekRecord(week.getTime());       //아니 시발 이거 진짜 개멍청이 아냐?
        recordf1 = (float) record1;         //기록 가져올 날짜를 먼저 정하고 기록을 가져와야지ㅡㅡ; 이걸 내가 알려줘야 아는건가?
        data1 = new BarData("4주", recordf1, recordf1 + "mL");
        WeekList.add(data1);

        Log.d("time : ", week.getTimeInMillis() + "");
        Log.d("record : ", record1 + "");

        mChart = findViewById(R.id.ChartProgressBar);
        mChart.setDataList(WeekList);
        mChart.build();
    }
}
