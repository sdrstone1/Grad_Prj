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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by KollHong on 14/04/2018.
 */

public class mPreferences {
    private SharedPreferences mPref;

    mPreferences(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    boolean getInit() {
        if (mPref.contains("initialized")) {
            return mPref.getBoolean("initialized", false);
        } else
            return false;
    }

    //TODO save session name ; used for auto login
    void setInit() {
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        mPrefEdit.putBoolean("initialized", true);
        mPrefEdit.commit();
    }

    public String getBTaddr() {
        return mPref.getString("BTaddr", null);

    }

    void setBTaddr(String addr) {
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        mPrefEdit.putString("BTaddr", addr);
        mPrefEdit.commit();
    }


    public void setSMSRegistered(boolean set) {
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        mPrefEdit.putBoolean("SMSReg", set);
        mPrefEdit.commit();
    }

    public void ClearSharedPref() {      //if(BuildConfig.isTest)
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        mPrefEdit.clear();
        mPrefEdit.commit();
    }
}
