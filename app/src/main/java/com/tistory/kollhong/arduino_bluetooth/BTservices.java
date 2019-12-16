
/*
 * Copyright (c) 2018. KollHong. All Rights Reserved.
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
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;

import static com.tistory.kollhong.arduino_bluetooth.mPreferences.Color;

public class BTservices extends Service {
    private static final String TAG = "BT_Services";
    String addr;
    /**
     * Command to the service to display a message
     */
    static final int BT_Options_Changed = 100;
    static final int STATE_BT_NOT_ON = 101;
    static final int BT_Callback_Object = 102;
    static final int BT_LED_OFF = 103;
    // Message types sent from the BluetoothChatService Handler
    static final int BT_STATE_CHANGE = 1;

    // Intent request codes
    static final int REQUEST_CONNECT_DEVICE = 200;
    static final int REQUEST_ENABLE_BT = 201;
    static final int BT_MESSAGE_READ = 2;
    static final int BT_MESSAGE_WRITE = 3;
    static final int BT_REMOTE_DEVICE_NAME = 4;
    static final int BT_SERVICE_TOAST = 5;
    static final int BT_CONN_LOST = 6;
    private String tmpString = "";

    // Key names received from the BluetoothChatService Handler
    static final String DEVICE_NAME = "device_list_name";
    static final String TOAST = "toast";

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    private String session;
    private int milliLiter = 0;
    // Array adapter for the conversation thread
    //private ArrayAdapter<String> mConversationArrayAdapter;
    private int DrunkSum = 0;
    // Layout Views

    private BTservice_CallBack bTserviceCallBack=null;
    private ListView mConversationView;
    private boolean BT_LED_STATUS;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    //    private EditText mOutEditText;
    private Button mSendButton;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // The action listener for the EditText widget, to listen for the return key
    // Member object for the chat services
    private BtMsgClass mChatService = null;
    // The Handler that gets information back from the BluetoothChatService
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BT_STATE_CHANGE:
                    if (BuildConfig.DEBUG) Log.i(TAG, "BT_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BtMsgClass.STATE_CONNECTED:

                            Toast.makeText(getApplicationContext(), getString(R.string.bt_connected_to)
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                            RGBON();
                            break;
                        case BtMsgClass.STATE_CONNECTING:

                            Toast.makeText(getApplicationContext(), getString(R.string.bt_connecting_to), Toast.LENGTH_SHORT).show();
                            break;
                        case BtMsgClass.STATE_LISTEN:
                        case BtMsgClass.STATE_NONE:
                            //Toast.makeText(getApplicationContext(), "Not Connected ", Toast.LENGTH_SHORT).show();

                            break;
                    }
                    break;
                case BT_MESSAGE_WRITE:
                    if (BuildConfig.DEBUG) Log.i(TAG, "MESSAGE_SENT: " + msg.arg1);
                    break;
                case BT_MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;


                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);


                    processMessage(readMessage);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case BT_REMOTE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case BT_SERVICE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
                case BT_CONN_LOST:
                    //TODO 마신 양 저장
                    bTserviceCallBack.ConnLost(milliLiter);

                    break;


            }
        }
    };

    private void setupChat() {

        // Initialize the BluetoothChatService to perform bluetooth connections
        if (mChatService == null) mChatService = new BtMsgClass(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer();
    }


    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    /**
     * Sends a msg_list_holder.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BtMsgClass.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the msg_list_holder bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
        }
    }

    private void processMessage(String message) {
        if (message.contains("/")) {

            String[] messages = message.split("/");      //\n로 메시지 타이밍 구분
            int size = messages.length;     //메시지 갯수 확인
            if (BuildConfig.DEBUG) {
                for (int i = 0; i < size; i++)
                    Log.i(TAG, "Message " + i + " : " + messages[i]);
            }
            tmpString += messages[size - 1];
            milliLiter = Integer.parseInt(tmpString);
            tmpString = "";
            Log.i(TAG, "recieved msg : " + milliLiter);
        } else {
            tmpString = tmpString + message;
            Log.i(TAG, "Message 00 : " + tmpString);
        }

        //마지막 배열은 다음 메시지와 연결될 수 있기 때문에
        //어레이에 추가하지 않음.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) {
            mChatService.stop();
            mRecords mRecords = new mRecords(getApplicationContext(), session, true);
            Date date = new Date();

            mRecords.putRecord(date, (double) DrunkSum);
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "--- ON DESTROY ---");
    }

    private void RGBON() {

        mPreferences mPref = new mPreferences(getApplicationContext());
        int color = mPref.getIntValue(Color);
        if (BuildConfig.DEBUG) Log.i(TAG, "Color Picker : " + color + "");
        String RGB = String.format("#%06X", 0xFFFFFFFF & color);
        Log.i(TAG, "Color from mPref : " + RGB);
        int red = Integer.parseInt(RGB.substring(3, 5), 16);
        int green = Integer.parseInt(RGB.substring(5, 7), 16);
        int blue = Integer.parseInt(RGB.substring(7, 9), 16);


        Log.i(TAG, "RGB substring : " + red + "rx");
        Log.i(TAG, "RGB substring : " + green + "gx");
        Log.i(TAG, "RGB substring : " + blue + "bx");
        //값(0~255)색(rgb) x
        BTservices.this.sendMessage(red + "rx");
        BTservices.this.sendMessage(green + "gx");
        BTservices.this.sendMessage(blue + "bx");

        BT_LED_STATUS = true;
    }

    /**
     * Handler of incoming messages from clients.
     */
    @SuppressLint("HandlerLeak")
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BT_Callback_Object:
                    bTserviceCallBack = (BTservice_CallBack)msg.obj;
                    break;

                case BT_Options_Changed:
                    mPreferences mPref = new mPreferences(getApplicationContext());
                    String address = mPref.getStringValue(mPreferences.BT_ADDR);

                    if (!mBluetoothAdapter.isEnabled()) {

                        bTserviceCallBack.BtNotOn();
                    } else {
                        setupChat();


                    }
                    if ( !addr.equals(address)) {
                        addr = address;
                        // Get the BLuetoothDevice object
                        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                        // Attempt to connect to the device
                        mChatService.connect(device);
                    }
                    break;
                case BT_LED_OFF:
                    if (mChatService != null) {
                        if (BT_LED_STATUS) {
                            BTservices.this.sendMessage("ax");
                            BT_LED_STATUS = false;
                            Log.i(TAG, "RGB substring : ax");
                        } else RGBON();
                    }

                default:
                    super.handleMessage(msg);
            }

        }
    }

    interface BTservice_CallBack{
        void ConnLost(int sum);
        void BtNotOn();
    }

}


