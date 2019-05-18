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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

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

    private BTserviceHandler bTserviceHandler = null;
    private long timeinmillis =0L;

    private String session;
    /**
     * Messenger for communicating with the service.
     */
    private Messenger BTMessenger = null;
    /**
     * Flag indicating whether we have called bind on the service.
     */
    private boolean BTBound;
    private boolean BTON = false;

    /**
     * Class for interacting with the main interface of the service.
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


            // Create and send a message to the service, using a supported 'what' value
            Message msg = Message.obtain(null, BTservices.BT_Callback_Object, bTserviceHandler);
            try {
                BTMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            BTMessenger = null;
            BTBound = false;
        }
    };


    private void setBTServiceConnection(String bTaddr) {
        if (!BTBound) {
            Snackbar.make(findViewById(R.id.mainView), "BT Service is not bound", Snackbar.LENGTH_SHORT).show();
            return;
        }
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, BTservices.BT_Options_Changed, bTaddr);

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

        Intent intent = getIntent();
        session = intent.getStringExtra("session");


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
                    Intent intent12 = new Intent(v.getContext(), ActivityMypage.class);
                    intent12.putExtra("session", session);
                    startActivity(intent12);
                });
        ImageButton LEDbutton = findViewById(R.id.LEDbutton);
        LEDbutton.setOnClickListener(
                v -> {
                    Intent intent13 = new Intent(v.getContext(), ActivityLEDsettings.class);
                    intent13.putExtra("session", session);
                    startActivity(intent13);
                });
        ImageButton Controlbutton = findViewById(R.id.Controlbutton);
        Controlbutton.setOnClickListener(
                v -> {
                    Intent intent14 = new Intent(v.getContext(), ActivitySetting.class);
                    //Intent intent14 = new Intent(v.getContext(), ActivityBtConnect.class);    //임시
                    //intent.putExtra("session", session);
                    startActivityForResult(intent14, REQUEST_SETTINGS);
                });

        bTserviceHandler = new BTserviceHandler();

        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 22);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        timeinmillis = date.getTimeInMillis();

        mPreferences mPref = new mPreferences(getApplicationContext());
        if (mPref.getBoolValue(BT_Automatic_Connect))
            requestBtConnect();

    }

    private void requestBtConnect() {
        // Create and send a message to the service, using a supported 'what' value
        if (BTON) {
            Message msg = Message.obtain(null, BTservices.BT_Options_Changed);

            try {
                BTMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BTservices.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                } else {
                    BTON = true;
                }
                break;
            case REQUEST_SETTINGS:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Snackbar.make(findViewById(R.id.mainView), "Start to Connect BT Device", Snackbar.LENGTH_SHORT).show();
                        requestBtConnect();
                        break;
                    default:
                        break;
                }
        }
    }

    class BTserviceHandler implements BTservices.BTservice_CallBack{
        @Override
        public void BtNotOn() {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, BTservices.REQUEST_ENABLE_BT);
        }

        @Override
        public void ConnLost(int sum) {
            Toast.makeText(getApplicationContext(), "Not Connected ", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.mainView), "BT Service is not bound", Snackbar.LENGTH_SHORT).show();

            SQLiteDatabase mDb = mDbMan.DBinit(getApplicationContext(),session, true);
            ContentValues value = new ContentValues();
            value.put(recordTableVar[0], timeinmillis);
            value.put(recordTableVar[1], sum);
            mDbMan.putRecord(mDb,recordTable,value);
        }
    }
}