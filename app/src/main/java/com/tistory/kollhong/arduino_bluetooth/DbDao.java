/*
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.tistory.kollhong.arduino_bluetooth.ApplicationVO.ROOT_DIR;

public class DbDao {
    SQLiteDatabase db;
    Cursor cursor;
    Context context;

    //여기서 읽기 쓰기 작업 모두 진행
    //여기서 디비 객체 선언
    DbDao(Context context, boolean RW) {
        if (RW) {
            db = SQLiteDatabase.openDatabase(ROOT_DIR + "/database/data.db", null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } else {
            db = SQLiteDatabase.openDatabase(ROOT_DIR + "/database/data.db", null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        }
        this.context = context;
        //todo db writing
    }

    public void add() {

    }


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
