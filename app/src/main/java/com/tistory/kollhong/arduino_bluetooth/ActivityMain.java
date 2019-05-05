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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
/*
블루투스 구조
메인 : 블루투스 켜기 및 주소 전달, 서비스와 통신

설정 : 블루투스 목록 가져오기 및 주소 전달

서비스 : 주소로 연결 및 디비 저장.
 */

public class ActivityMain extends AppCompatActivity {


    private String session;
    /**
     * Messenger for communicating with the service.
     */
    private Messenger BTMessenger = null;

    /**
     * Flag indicating whether we have called bind on the service.
     */
    private boolean BTBound;

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
            Snackbar.make(findViewById(R.id.mainView), "Check ID & PW again", Snackbar.LENGTH_SHORT).show();
            return;
        }
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, BTservices.NEW_DEVICE_SELECTED, bTaddr);

        try {
            BTMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        session = intent.getStringExtra("session");


        super.onCreate(savedInstanceState);

        ImageButton calendarbutton = findViewById(R.id.calendarbutton);
        calendarbutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivityCalendar.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                });

        ImageButton mypagebutton = findViewById(R.id.mypagebutton);
        mypagebutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivityMypage.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                });
        ImageButton LEDbutton = findViewById(R.id.LEDbutton);
        LEDbutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivityLEDsettings.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                    }
                });
        ImageButton Controlbutton = findViewById(R.id.Controlbutton);
        Controlbutton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        //Intent intent = new Intent(v.getContext(), ActivitySetting.class);
                        Intent intent = new Intent(v.getContext(), ActivityBtConnect.class);    //임시
                        //intent.putExtra("session", session);
                        startActivityForResult(intent, BTservices.REQUEST_CONNECT_DEVICE);
                    }
                });

        mPreferences mPref = new mPreferences(getApplicationContext());
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mPref.getBoolValue(mPreferences.BT_Automatic_Connect))
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, BTservices.REQUEST_ENABLE_BT);
            } else {
                requestBtConnect();
            }


        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult

        /*
                if (D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BtMsgClass.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
         */
    }

    private void requestBtConnect() {
        mPreferences mPrefMan = new mPreferences(getApplicationContext());
        Message msg = new Message();
        msg.what = BTservices.NEW_DEVICE_SELECTED;
        msg.obj = new String[]{mPrefMan.getStringValue(mPreferences.BT_ADDR), session};

        try {
            BTMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, BTservices.class), BTServiceConnection,
                Context.BIND_AUTO_CREATE);
        mPreferences mPref = new mPreferences(getApplicationContext());
        String bTaddr = mPref.getStringValue(mPreferences.BT_ADDR);

        if (bTaddr != null)
            setBTServiceConnection(bTaddr);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (BTBound) {
            unbindService(BTServiceConnection);
            BTBound = false;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BTservices.REQUEST_ENABLE_BT)
            // When the request to enable Bluetooth returns
            if (resultCode != Activity.RESULT_OK) {
                // User did not enable Bluetooth or an error occured
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
            } else if (requestCode == BTservices.REQUEST_CONNECT_DEVICE) {
                if (resultCode == Activity.RESULT_OK) {
                    requestBtConnect();
                    //TODO 이건 BTservices에서 할 것. 여기서는 메시지로 주소 또는 재 연결 신호만 보냄
                /*
                String address = data.getExtras()

                        .getString(ActivityBtConnect.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mChatService.connect(device);

                 */
                }
            }
    }
}