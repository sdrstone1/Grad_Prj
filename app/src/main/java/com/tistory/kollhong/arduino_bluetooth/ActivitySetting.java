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
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;

import static com.tistory.kollhong.arduino_bluetooth.mPreferences.BT_Automatic_Connect;

public class ActivitySetting extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener {
    static final int REQUEST_SETTINGS = 200;
    static final int REQUEST_COLOR_PICKER = 201;


    private static final String TAG = "Settings";


    //private ActivityMain.BTserviceHandler bTserviceHandler = null;
    private Messenger BTMessenger = null;
    private boolean BTBound;

    /**
     * Create connection with service.
     */
    private ServiceConnection BTServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            BTMessenger = new Messenger(service);
            BTBound = true;

            if (BuildConfig.DEBUG) Log.i(TAG, "Service Connected");

        }

        public void onServiceDisconnected(ComponentName className) {
            if (BuildConfig.DEBUG) Log.i(TAG, "Service Disconnected");
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            BTMessenger = null;
            BTBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Switch autoConnectswitch = findViewById(R.id.ConnectSwitch);
        autoConnectswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPreferences mPref = new mPreferences(getApplicationContext());
            mPref.setValue(BT_Automatic_Connect, isChecked);
            Log.i("Check Changed", isChecked + "");
        });
        mPreferences mPref = new mPreferences(getApplicationContext());
        autoConnectswitch.setChecked(mPref.getBoolValue(BT_Automatic_Connect));
    }

    public void onSensorPairingBtn(View v) {
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
                    Message msg = Message.obtain(null, BTservices.BT_Device_Changed);

                    try {
                        BTMessenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
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

                Message msg = Message.obtain(null, BTservices.BT_LED_COLOR_CHANGED);

                try {
                    BTMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

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
