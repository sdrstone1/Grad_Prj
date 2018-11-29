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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.prefs.Preferences;

/**
 * Created by KollHong on 14/04/2018.
 */

public class mPrefMan {
    Preferences mPrefs;
    SharedPreferences mPref;
    SharedPreferences.Editor mPrefEdit;
    Boolean init = false;
    Context context;

    mPrefMan(Context appcontext) {
        context = appcontext;
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        //mPref = context.getSharedPreferences(Pref_name,Context.MODE_PRIVATE);
        mPrefEdit = mPref.edit();
        if (mPref.contains("initialized") == true) {
            init = mPref.getBoolean("initialized", false);
        } else
            init = false;
    }

    public void setBTUUID(String address) {
        mPrefEdit.putString("UUID", address);
    }
    public void setInit() {
        mPrefEdit.putBoolean("initialized", true);
        mPrefEdit.commit();
    }

    public boolean getSMSEnabled() {
        if (mPref.contains("SMS") == true) {
            return mPref.getBoolean("SMS", true);

        } else {
            //TODO SMS 사용 여부 없다고 알림 보내고 설정 페이지로 intent
            return false;
        }
    }

    public void setSMSRegistered(boolean set) {

        mPrefEdit.putBoolean("SMSReg", set);
        mPrefEdit.commit();
    }

    public void ClearSharedPref() {      //if(BuildConfig.isTest)
        mPrefEdit.clear();
        mPrefEdit.commit();
    }
}
