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
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

import static com.tistory.kollhong.arduino_bluetooth.ActivityRegister.isMyPage;
import static com.tistory.kollhong.arduino_bluetooth.ActivitySetting.REQUEST_SETTINGS;
import static com.tistory.kollhong.arduino_bluetooth.mDbMan.recordTable;
import static com.tistory.kollhong.arduino_bluetooth.mDbMan.recordTableVar;
import static com.tistory.kollhong.arduino_bluetooth.mPreferences.BT_Automatic_Connect;
/*
블루투스 구조
메인 : 블루투스 켜기 및 주소 전달, 서비스와 통신

설정 : 블루투스 목록 가져오기 및 주소 전달

서비스 : 주소로 연결 및 디비 저장.
 */

public class ActivityMain extends AppCompatActivity {

    private final String TAG = "MAIN";
    private String session;

    private BTserviceHandler bTserviceHandler = null;
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
            // Create and send a message to the service, using a supported 'what' value

            bTserviceHandler = new BTserviceHandler();

            Message msg = Message.obtain(null, BTservices.BT_Callback_Object, bTserviceHandler);
            try {
                BTMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


            mPreferences mPref = new mPreferences(getApplicationContext());
            if (mPref.getBoolValue(BT_Automatic_Connect)) requestBtConnect();

        }

        public void onServiceDisconnected(ComponentName className) {
            if (BuildConfig.DEBUG) Log.i(TAG, "Service Disconnected");
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            BTMessenger = null;
            BTBound = false;
        }
    };

    private void requestBtConnect() {
        if (BuildConfig.DEBUG) Log.i(TAG, "Request BT Connect");
        // Create and send a message to the service, using a supported 'what' value

        Message msg = Message.obtain(null, BTservices.BT_Options_Changed);

        try {
            BTMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        session = getIntent().getStringExtra("session");


        super.onCreate(savedInstanceState);


        bindService(new Intent(this, BTservices.class), BTServiceConnection,
                Context.BIND_AUTO_CREATE);


        ImageButton calendarbutton = findViewById(R.id.calendarbutton);
        calendarbutton.setOnClickListener(
                v -> {
                    Intent intent1 = new Intent(v.getContext(), ActivityCalendar.class);
                    intent1.putExtra("session", session);
                    startActivity(intent1);
                });

        ImageButton mypagebutton = findViewById(R.id.mypagebutton);
        mypagebutton.setOnClickListener(
                v -> {
                    Intent intent12 = new Intent(v.getContext(), ActivityRegister.class);
                    Bundle bd = new Bundle();
                    bd.putBoolean(isMyPage, true);
                    bd.putString("session", session);
                    intent12.putExtra("bundle", bd);
                    startActivity(intent12);
                });
        ImageButton LEDbutton = findViewById(R.id.LEDbutton);
        LEDbutton.setOnClickListener(v -> {
            if (bTserviceHandler != null) {
                Message msg = Message.obtain(null, BTservices.BT_LED_OFF, bTserviceHandler);
                try {
                    BTMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        ImageButton Controlbutton = findViewById(R.id.Controlbutton);
        Controlbutton.setOnClickListener(
                v -> {
                    Intent intent14 = new Intent(v.getContext(), ActivitySetting.class);
                    //Intent intent14 = new Intent(v.getContext(), ActivityBtConnect.class);    //임시
                    //intent.putExtra("session", session);
                    startActivityForResult(intent14, REQUEST_SETTINGS);
                });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BTservices.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_SETTINGS:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        requestBtConnect();
                        if (BuildConfig.DEBUG) Log.i(TAG, "REQUESTING BT CONNECT");
                        break;
                    default:
                        break;
                }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (BTBound) {
            unbindService(BTServiceConnection);
            BTBound = false;

        }
    }

    class BTserviceHandler implements BTservices.BTservice_CallBack{
        @Override
        public void BtNotOn() {
            if (BuildConfig.DEBUG) Log.i(TAG, "BT not ON");
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BTservices.REQUEST_ENABLE_BT);
        }

        @Override
        public void ConnLost(int sum) {
            Toast.makeText(getApplicationContext(), "Not Connected ", Toast.LENGTH_SHORT).show();

            Calendar date = Calendar.getInstance();
            if (date.get(Calendar.AM_PM) == Calendar.AM) {
                date.add(Calendar.DATE, -1);
            }
            date.set(Calendar.HOUR_OF_DAY, 22);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            long timeinmillis = date.getTimeInMillis();


            SQLiteDatabase mDb = mDbMan.DBinit(getApplicationContext(),session, true);
            ContentValues value = new ContentValues();
            value.put(recordTableVar[0], timeinmillis);
            value.put(recordTableVar[1], sum);
            mDbMan.putRecord(mDb,recordTable,value);
        }
    }
}