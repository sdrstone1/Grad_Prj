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
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

class mDbMan {
    static final String userTable = "user";
    static final String recordTable = "record";
    static final String[] userTableVar = {"loginid", "pw", "name", "age", "weight", "height", "email", "gender"};
    static final String[] recordTableVar = {"date", "measurement"};

    static SQLiteDatabase DBinit(Context context, String database, boolean RW) {
        String ROOT_DIR;
        if (Build.VERSION.SDK_INT >= 24) {
            ROOT_DIR = context.getDataDir().getAbsolutePath();
        } else {
            ROOT_DIR = context.getFilesDir().getAbsolutePath();
        }


        if (RW) {
            return SQLiteDatabase.openDatabase(ROOT_DIR + "/database/" + database + ".db", null, SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } else {
            Log.d("Opening DB", ROOT_DIR + "/database/" + database + ".db");
            return SQLiteDatabase.openDatabase(ROOT_DIR + "/database/" + database + ".db", null, SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        }
    }

    static boolean Cretable(SQLiteDatabase db, String table, String tablearg) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + table + " ( " + tablearg + " ); ");
        } catch (SQLException e) {
            e.getLocalizedMessage();
            return false;
        }
        return true;
    }

    static boolean putRecord(SQLiteDatabase db, String table, ContentValues values) {
        try {
            db.insertOrThrow(table, null, values);
        } catch (SQLiteConstraintException e) {
            Log.e("PUT record error", e.getMessage());
            return false;
        }
        return true;
    }

    static double getDRecord(SQLiteDatabase db, String table, String[] col, String where) {
        Cursor cursor = db.query(table, col, where, null, null, null, null);
        double record = 0d;
        int count = cursor.getCount();
        if (count != 0) {
            for (int i = 0; i < count; i++) {

                record = record + cursor.getDouble(0);
            }
        }
        cursor.close();
        return record;
    }

    static Cursor getARecord(SQLiteDatabase db, String table, String[] col, String where) {
        return db.query(table, col, where, null, null, null, null);
    }

    static boolean delRecord(SQLiteDatabase db, String table, String where) {
        db.delete(table, where, null);
        return true;
    }
}