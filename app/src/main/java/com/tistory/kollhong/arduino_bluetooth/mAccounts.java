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

import android.content.Context;
import android.util.Log;

public class mAccounts {

    Context context;
    cDb mDb;
    String session;

    mAccounts(Context context) {
        this.context = context;

    }


    public String Login(String id, String pw) {
        mDb = new cDb(context, "accounts", false);
        pw = encrypt(pw);
        session = mDb.Login(id, pw);
        if (session == "false") {
            return "false";
        }
        //TODO session ID를 저장하여 자동 로그인이 가능하도록
        return session;
    }

    public int Join(String id, String pw, String name, int age, float weight, float height, String email) {
        mDb = new cDb(context, "accounts", true);
        //check id redundancy
        if (mDb.IDredun(id)) {
            Log.e("Join Error", "ID is redundant");
            return 1;
            //
        }
        //check pw condition
        if (pw.length() > 15 || pw.length() < 8) {
            Log.e("Join Error", "pw condition not satisfied");
            return 2;
        }

        pw = encrypt(pw);

        if (mDb.Join(id, pw, name, age, weight, height, email)) {
            return 0;
        } else {
            Log.e("Join Error", "unexpected error");
            return 1;  //3 -> fail, 1 -> id redundant, 2-> pw condition not satisfied, 0->success

        }
    }

    public String encrypt(String pw) {
        //st
        return pw;
    }



}
