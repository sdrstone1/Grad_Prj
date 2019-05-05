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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by KollHong on 14/04/2018.
 */

public class mPreferences {
    private SharedPreferences mPref;
    static final String APP_INIT = "initialized";
    static final String BT_Automatic_Connect = "BTON";
    static final String BT_ADDR = "BTaddr";

    mPreferences(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    String getStringValue(String name) {
        return mPref.getString(name, null);
    }

    @SuppressLint("ApplySharedPref")
    void setStringValue(String name, String value) {
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        mPrefEdit.putString(name, value);
        mPrefEdit.commit();
    }

    boolean getBoolValue(String name) {
        return mPref.getBoolean(name, false);
    }

    @SuppressLint("ApplySharedPref")
    void setBoolValue(String name, boolean set) {
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        mPrefEdit.putBoolean(name, set);
        mPrefEdit.commit();
    }

    @SuppressLint("ApplySharedPref")
    public void ClearSharedPref() {      //if(BuildConfig.isTest)
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        mPrefEdit.clear();
        mPrefEdit.commit();
    }

}
