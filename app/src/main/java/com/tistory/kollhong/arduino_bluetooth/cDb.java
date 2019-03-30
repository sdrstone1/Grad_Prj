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
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

class cDb {
    SQLiteDatabase db;
    Cursor cursor;
    Context context;

    //여기서 읽기 쓰기 작업 모두 진행
    //여기서 디비 객체 선언
    cDb(Context context, String database, boolean RW) {
        this.context = context;
        String ROOT_DIR;
        if (Build.VERSION.SDK_INT >= 24) {
            ROOT_DIR = context.getDataDir().getAbsolutePath();
        } else {
            ROOT_DIR = context.getFilesDir().getAbsolutePath();
        }


        if (RW) {
            db = SQLiteDatabase.openDatabase(ROOT_DIR + "/database/" + database + ".db", null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } else {
            Log.d("Opening DB", ROOT_DIR + "/database/" + database + ".db");
            db = SQLiteDatabase.openDatabase(ROOT_DIR + "/database/" + database + ".db", null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        }

        //todo db writing
    }

    /* recommended to do not close SQL on android
    void closeDB(){
        db.close();
    }
    */
    String Login(String id, String pw) { //기본 admin, admin
        cursor = db.query(true, "user", new String[]{"loginid", "pw"}, "loginid='" + id + "'", null, null, null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            String dbpw = cursor.getString(1);
            if (pw.equals(dbpw)) {
                //TODO generate session id & return session id
                return id;
            } else return "false";
        } else return "false";
    }

    boolean Join(String id, String pw, String name, int age, float weight, float height, String email) {
        ContentValues values = new ContentValues();
        values.put("loginid", id);
        values.put("password", pw);
        values.put("name", name);
        values.put("age", age);
        values.put("weight", weight);
        values.put("height", height);
        values.put("email", email);


        try {
            db.insertOrThrow("user", null, values);
        } catch (SQLiteConstraintException e) {
            Log.e("Join Error", e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    boolean IDredun(String id) {
        cursor = db.query(true, "user", new String[]{"loginid"}, "loginid='" + id + "'", null, null, null, null, null);
        return cursor.getCount() != 0;
    }


    ArrayList<Double> getRecord(String session, long startdate, long enddate) {
        cursor = db.query(true, session, new String[]{"measurement"}, "date BETWEEN '" + startdate + "' AND '" + enddate + "'", null, null, null, null, null);
        ArrayList<Double> record = new ArrayList<>();
        int count = cursor.getCount();
        if (count != 0) {
            for (int i = 0; i < count; i++) {

                record.add(cursor.getDouble(0));
            }
        }
        return record;

    }

/* table : record , col : userid, measurement, date
    /*
    public Cursor getTransbyID(long id) {
        cursor = db.rawQuery("SELECT _id, time, categoryid, amount, accountid, recipient, notes, rewardrecipientid, budgetexception, rewardamount, perfexception, rewardtype FROM trans WHERE _id = '" + id + "' ", null);
        return cursor;
    }

    public String getCategoryName(long id) {
        cursor = db.rawQuery("select c.name " +
                "from category c " +
                "where c._id = '" + id +
                "'", null);
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            return cursor.getString(0);
        }
        return "";
    }

    public int getCatLevel(long id) {
        cursor = db.rawQuery("select level " +
                "from category  " +
                "where _id = '" + id +
                "'", null);
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            return cursor.getInt(0);
        }

        return 0;
    }

    public Cursor getCategoryList(String name) {
        cursor = db.rawQuery("select _id from category where name = '" + name + "' ", null);
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            int id = cursor.getInt(0);
            cursor = db.rawQuery("select _id, name " +
                    "from category " +
                    "where parent == '" + id +
                    "' ", null);
            return cursor;
        } else return null;
    }


    public Cursor getAccList() {
        cursor = db.rawQuery("select _id, type, name, balance, withdrawalaccount, withdrawalday, cardid " +
                "from accounts ", null);
        return cursor;
    }

    public Cursor getLearnData(String name) {        //이름과 계좌가 같을 경우
        cursor = db.rawQuery("select _id, categoryid, accid, recipientid, budgetexception, perfexception, rewardtype, rewardamount " +
                "from learning where recipient = '" + name + "' ", null);
        return cursor;
    }


    public void updateAccBalance(long acc_id, float amount) {
        Cursor tmp = getAccInfo(data.acc_id);
        Float tmp_amount;
        if (tmp.getCount() != 0) {
            tmp.moveToNext();
            tmp_amount = tmp.getFloat(4);
            amount = tmp_amount + amount;
        }
        db.execSQL("update accounts set balance = balance - '" + amount + "' where _id = '" + acc_id + "' ");
    }

    public void updateTransaction(SaveData data) {
        ContentValues values = new ContentValues();
        values.put("time", data.timeinmillis);
        values.put("categoryid", data.category_id);
        values.put("amount", data.amount);
        values.put("accountid", data.acc_id);
        values.put("recipient", data.recipname);
        values.put("notes", data.note);
        values.put("rewardrecipientid", data.recipid);
        values.put("budgetexception", data.budgetexception);
        values.put("rewardamount", data.rew_amount_calculated);
        values.put("perfexception", data.perfexception);
        values.put("rewardtype", data.rew_type);
        Float tmp_amount = 0f;
        Cursor tmp = getTransbyID(data.trans_id);
        if (tmp.getCount() != 0) {
            tmp.moveToNext();
            tmp_amount = tmp.getFloat(3);
            tmp_amount = tmp_amount - data.amount;
        }
        if (data.acc_type == 1) {       //체크카드 출금계좌에서 잔액 보정.
            updateAccBalance(data.withdrawlaccount, tmp_amount);
        } else {
            updateAccBalance(data.acc_id, tmp_amount);
        }

        db.update("trans", values, "_id = " + data.trans_id, null);

    }

    public void updateCat(long id, String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);

        db.update("category", values, "_id = " + id, null);
    }



    public void addTransaction(SaveData data) {
        ContentValues values = new ContentValues();
        values.put("time", data.timeinmillis);
        values.put("categoryid", data.category_id);
        values.put("amount", data.amount);
        values.put("accountid", data.acc_id);
        values.put("recipient", data.recipname);
        values.put("notes", data.note);
        values.put("rewardrecipientid", data.recipid);
        values.put("budgetexception", data.budgetexception);
        values.put("rewardamount", data.rew_amount_calculated);
        values.put("perfexception", data.perfexception);
        values.put("rewardtype", data.rew_type);


        db.insert("trans", null, values);
    }


    public void addTransactiononSave(SaveData data) {     //잔액 조절, 카드 확인, 기록 추가
        int tmp = getCatLevel(data.cardid);
        switch (tmp) {
            case 0:
            case 1:
            case 2: {       //수입 -> data.amount만큼 잔액 더함.

                if (data.acc_type == 1) {       //체크카드 출금계좌에서 잔액 보정.
                    updateAccBalance(data.withdrawlaccount, data.amount);
                } else {
                    updateAccBalance(data.acc_id, data.amount);
                }
                break;
            }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                if (data.acc_type == 1) {       //체크카드 출금계좌에서 잔액 보정.
                    updateAccBalance(data.withdrawlaccount, -data.amount);
                } else {
                    updateAccBalance(data.acc_id, -data.amount);
                }
                break;
            }
        }

        if (data.isUpdate) {
            updateTransaction(data);
        } else {
            addTransaction(data);
        }
        if (data.learn) {
            updateLearn(data);
        }
    }

    public void updateLearn(SaveData data) {
        ContentValues values = new ContentValues();
        values.put("recipient", data.recipname);
        values.put("categoryid", data.category_id);
        values.put("accid", data.acc_id);
        values.put("recipientid", data.recipid);
        values.put("budgetexception", data.budgetexception);
        values.put("perfexception", data.perfexception);
        values.put("rewardtype", data.rew_type);
        values.put("rewardamount", data.rew_amount);


        try {
            db.insertOrThrow("learning", null, values);
        } catch (SQLiteConstraintException e) {
            db.update("learning", values, "recipient = '" + data.recipname + "' ", null);
        }
    }


    public void deleteTrans(long id) {
        db.delete("Trans", "_id = '" + id + "' ", null);
    }

    public void deleteAcc(long id) {
        db.delete("accounts", "_id = '" + id + "' ", null);
    }

    public void deleteCat(long id) {
        db.delete("category", "_id = '" + id + "' ", null);
    }

*/

}
