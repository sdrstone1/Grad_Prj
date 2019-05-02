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
//Login, Join, Manage Activity
//Get ID, PW. and encrypt pw
//Access to DB for Add or Remove records through cDB.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class mAccounts {

    Context context;
    String session;
    SQLiteDatabase mDb;

    mAccounts(Context context, boolean RW) {
        this.context = context;
        mDb = mDbMan.DBinit(context, "accounts", RW);
    }

    public String Login(String id, String pw) {
        //TODO session ID를 저장하여 자동 로그인이 가능하도록
        //getDRecord(SQLiteDatabase db, String table, String[] col, String where)
        Cursor cursor = mDbMan.getARecord(mDb, "user", new String[]{"loginid", "pw"}, "loginid='" + id + "'");
        if (cursor.getCount() == 1) {
            cursor.moveToNext();

            if (pw.equals(cursor.getString(1))) {
                cursor.close();
                return id;
            }
        }
        return "false";
    }

    public int Join(String id, String pw, String name, int age, float weight, float height, String email) {
        Cursor cursor = mDbMan.getARecord(mDb, "user", new String[]{"loginid", "pw"}, "loginid='" + id + "'");
        //check id redundancy
        if (cursor.getCount() != 0) {
            Log.e("Join Error", "ID is redundant");
            cursor.close();
            return 1;
            //
        }
        cursor.close();
        //check pw condition
        if (pw.length() > 15 || pw.length() < 8) {
            Log.e("Join Error", "pw condition not satisfied");
            return 2;
        }

        //pw = encrypt(pw);
        ContentValues values = new ContentValues();
        values.put("loginid", id);
        values.put("password", pw);
        values.put("name", name);
        values.put("age", age);
        values.put("weight", weight);
        values.put("height", height);
        values.put("email", email);

        if (!mDbMan.putRecord(mDb, "user", values)) {
            Log.e("Join Error", "unexpected error");
            return 3;  //3 -> fail, 1 -> id redundant, 2-> pw condition not satisfied, 0->success
        }

        SQLiteDatabase mDbJoin = mDbMan.DBinit(context, id, true);
        if (!mDbMan.Cretable(mDbJoin, "record", " date INTEGER NOT NULL, measurement REAL NOT NULL")) {
            mDbMan.delRecord(mDb, "user", "loginid = '" + id + "'");
            return 3;
        }
        return 0;
    }


    public String encrypt(String pw) {
        //TODO crypto
        return pw;
    }
}
