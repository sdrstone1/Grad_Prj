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

import static com.tistory.kollhong.arduino_bluetooth.mDbMan.*;

public class mAccounts {

    Context context;
    String session;
    private SQLiteDatabase mDb;

    mAccounts(Context context, boolean RW) {
        this.context = context;
        mDb = mDbMan.DBinit(context, "accounts", RW);
    }

    String Login(String id, String pw) {
        //TODO session ID를 저장하여 자동 로그인이 가능하도록
        //getDRecord(SQLiteDatabase db, String table, String[] col, String where)
        Cursor cursor = mDbMan.getRecordCursor(mDb, userTable, new String[]{userTableVar[0], userTableVar[1]}, userTableVar[0] + " ='" + id + "'");
        if (cursor.getCount() == 1) {
            cursor.moveToNext();

            if (pw.equals(cursor.getString(1))) {
                cursor.close();
                return id;
            }
        }
        return "false";
    }

    public int Join(String id, String pw, String name, int age, float weight, float height, String email, int gender) {
        Cursor cursor = mDbMan.getRecordCursor(mDb, userTable, new String[]{userTableVar[0], userTableVar[1]}, userTableVar[0] + " = '" + id + "'");
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
        values.put(userTableVar[0], id);
        values.put(userTableVar[1], pw);
        values.put(userTableVar[2], name);
        values.put(userTableVar[3], age);
        values.put(userTableVar[4], weight);
        values.put(userTableVar[5], height);
        values.put(userTableVar[6], email);
        values.put(userTableVar[7], gender);

        if (!mDbMan.putRecord(mDb, userTable, values)) {
            Log.e("Join Error", "unexpected error");
            return 3;  //3 -> fail, 1 -> id redundant, 2-> pw condition not satisfied, 0->success
        }

        SQLiteDatabase mDbJoin = mDbMan.DBinit(context, id, true);
        if (!mDbMan.Cretable(mDbJoin, recordTable, " '" + recordTableVar[0] + "' INTEGER NOT NULL, '" + recordTableVar[1] + "' REAL NOT NULL")) {
            mDbMan.delRecord(mDb, userTable, userTableVar[0] + " = '" + id + "'");
            return 3;
        }
        return 0;
    }

    ContentValues getAccountInfo() {

        String[] tables = recordTableVar;
        Cursor cursor = mDbMan.getRecordCursor(mDb, userTable, tables, " '" + userTableVar[0] + "' is '" + session + "' ");
        ContentValues values = new ContentValues();
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            values.put(userTableVar[2], cursor.getString(2));
            values.put(userTableVar[3], cursor.getInt(3));
            values.put(userTableVar[4], cursor.getFloat(4));
            values.put(userTableVar[5], cursor.getFloat(5));
            values.put(userTableVar[6], cursor.getString(6));
            values.put(userTableVar[7], cursor.getInt(7));
        }
        cursor.close();
        return values;
    }

    int getInt() {
        Cursor cursor = mDbMan.getRecordCursor(mDb, userTable, new String[]{userTableVar[7]}, " '" + userTableVar[0] + "' is '" + session + "' ");
        int gender = 0;
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            gender = cursor.getInt(0);
        }
        cursor.close();
        return gender;
    }

    public String encrypt(String pw) {
        //TODO crypto
        return pw;
    }
}
