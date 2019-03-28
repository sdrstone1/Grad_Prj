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

import android.content.Context;
import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.Date;

public class mRecords {
    Context context;
    cDb mDb;
    String session; //지금은 로그인 아이디


    mRecords(Context context) {
        this.context = context;
    }

    public ArrayList<Double> getRecord(String session, Date date) {
        //TODO 세션 , id로 바꾸기
        //Date startdate = new Date(date), enddate = new Date(date);

        Long startdate = date.getTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        cal.set(2019, Calendar.JANUARY, 1, 0, 0, 0);
        cal.getTimeInMillis();
        cal.set(2019, Calendar.FEBRUARY, 1);
        cal.getTimeInMillis();


        cal.add(Calendar.DATE, 2);
        cal.add(Calendar.MONTH, 2);

        Long enddate = cal.getTimeInMillis() - 1;

        mDb = new cDb(context, session, false);
        ArrayList<Double> record = mDb.getRecord(session, startdate, enddate);
        return record;
    }
    //TODO 월별 통계, 주간 통계


    //view returns String

    //add record

    //edit record


}
