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

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;

import static com.tistory.kollhong.arduino_bluetooth.mPreferences.BT_Automatic_Connect;

public class ActivitySetting extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener {
    static final int REQUEST_SETTINGS = 200;
    static final int REQUEST_COLOR_PICKER = 201;


    private final String TAG = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Switch autoConnectswitch = findViewById(R.id.ConnectSwitch);
        autoConnectswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPreferences mPref = new mPreferences(getApplicationContext());
                mPref.setValue(BT_Automatic_Connect, isChecked);
                Log.i("Check Changed", isChecked + "");
            }
        });
        mPreferences mPref = new mPreferences(getApplicationContext());
        autoConnectswitch.setChecked(mPref.getBoolValue(BT_Automatic_Connect));
    }

    public void onSensorConnectBtn(View v) {
        Intent intent = new Intent(v.getContext(), ActivityBtConnect.class);    //임시
        startActivityForResult(intent, BTservices.REQUEST_CONNECT_DEVICE);

    }

    public void onLEDSettingsBtn(View v) {

        mPreferences mPref = new mPreferences(getApplicationContext());
        ColorPickerDialogFragment f = ColorPickerDialogFragment
                .newInstance(REQUEST_COLOR_PICKER, null, null, mPref.getIntValue(mPreferences.Color), true);
        f.setStyle(DialogFragment.STYLE_NORMAL, 0);
        f.show(getFragmentManager(), "d");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BTservices.REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK);
                }
                break;
        }
    }


    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {

            case REQUEST_COLOR_PICKER:
                // We got result from the other dialog, the one that is
                // shown when clicking on the icon in the action bar.
                String colorS = colorToHexString(color);
                if (BuildConfig.DEBUG) Log.i(TAG, "Color Picker : " + colorS);

                mPreferences mPref = new mPreferences(getApplicationContext());
                mPref.setValue(mPreferences.Color, color);

                setResult(Activity.RESULT_OK);
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    private String colorToHexString(int color) {
        return String.format("#%06X", 0xFFFFFFFF & color);
    }
}
