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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;

import java.util.Date;

public class mRecords {
    Context context;
    SQLiteDatabase db;
    String session; //지금은 로그인 아이디


    mRecords(Context context, String session, boolean RW) {
        this.context = context;
        db = mDbMan.DBinit(context, session, RW);
    }

    Double getDrinkRecord(String session, Calendar cal, int loop) {
        Double record = 0d;

        cal.set(Calendar.HOUR_OF_DAY, 12);
/*
        int Sunday = cal.getFirstDayOfWeek();
        cal.set(Calendar.DAY_OF_MONTH, Sunday);
        cal.set(Calendar.HOUR_OF_DAY, 12);      //낮 12시부터 다음 날 낮 12시까지
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
*/

        Long startdate = cal.getTimeInMillis();
        cal.add(Calendar.DATE, 1);
        Long enddate = cal.getTimeInMillis() - 1l;

        for (int i = 0; i < loop; i++) {
            record = record + getRecord(startdate, enddate);
            startdate = enddate + 1l;
            cal.add(Calendar.DATE, 1);
            enddate = cal.getTimeInMillis() - 1l;

        }
        return record;
    }

    //TODO 월별 통계, 주간 통계
    public Double getWeekRecord(Date date) {        //date => 일요일, 낮 12시 00분 00초 수신
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Double record = 0d;
        record = record + getDrinkRecord(session, cal, 7);
        return record;
    }

    public Double getMonthRecord(Date date) {       //연,월 수신, 1일 12시 00분 00초
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Double record = 0d;
        record = record + getDrinkRecord(session, cal, lastDay);
        return record;
    }

    public double getRecord(long startdate, long enddate) {
        return mDbMan.getDRecord(db, "record", new String[]{"date", "measurement"}, "date BETWEEN '" + startdate + "' AND '" + enddate + "'");
        //SQLiteDatabase db, String table, String[] col, String where
    }
    //add record
    public boolean putRecord(Date date, Double measurement) {
        ContentValues values = new ContentValues();
        values.put("date", date.getTime());
        values.put("measurement", measurement);
        return mDbMan.putRecord(db, "record", values);
    }

}
