
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
import android.os.*;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.util.Date;

public class BTservices extends Service {
    /**
     * Command to the service to display a message
     */
    static final int NEW_DEVICE_SELECTED = 100;
    static final int STATE_BT_NOT_ON = 101;
    static final int BT_Callback_Object = 12;
    // Intent request codes
    static final int REQUEST_CONNECT_DEVICE = 200;
    static final int REQUEST_ENABLE_BT = 201;
    // Message types sent from the BluetoothChatService Handler
    static final int MESSAGE_STATE_CHANGE = 1;
    static final int MESSAGE_READ = 2;
    static final int MESSAGE_WRITE = 3;
    static final int MESSAGE_DEVICE_NAME = 4;
    static final int MESSAGE_TOAST = 5;
    static final int MESSAGE_CONN_LOST = 6;
    // Key names received from the BluetoothChatService Handler
    static final String DEVICE_NAME = "device_list_name";
    static final String TOAST = "toast";
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    private String session;
    private int milliLiterold = 0;
    private int milliLiter = 0;
    // Array adapter for the conversation thread
    //private ArrayAdapter<String> mConversationArrayAdapter;
    private int DrunkSum = 0;
    // Layout Views

    private BTservice_CallBack bTserviceCallBack=null;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    // Name of the connected device
    private String mConnectedDeviceName = null;

    //int	onStartCommand(Intent intent, int flags, int startId)
    //Called by the system every time a client explicitly starts the service by calling Context.startService(Intent), providing the arguments it supplied and a unique integer token representing the start request.
    //void	onTaskRemoved(Intent rootIntent)
    //This is called if the service is currently running and the user has removed a task that comes from the service's application.
    //boolean	onUnbind(Intent intent)
    //Called when all clients have disconnected from a particular interface published by the service.
    // String for Socket commuication

    // The Handler that gets information back from the BluetoothChatService
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BtMsgClass.STATE_CONNECTED:

                            Toast.makeText(getApplicationContext(), "Connected to "
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                            break;
                        case BtMsgClass.STATE_CONNECTING:

                            Toast.makeText(getApplicationContext(), "Connectting ", Toast.LENGTH_SHORT).show();
                            break;
                        case BtMsgClass.STATE_LISTEN:
                        case BtMsgClass.STATE_NONE:
                            Toast.makeText(getApplicationContext(), "Not Connected ", Toast.LENGTH_SHORT).show();

                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    if (D) Log.i(TAG, "MESSAGE_SENT: " + msg.arg1);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;


                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);


                    processMessage(readMessage);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_CONN_LOST:
                    //TODO 마신 양 저장
                    bTserviceCallBack.ConnLost(milliLiter);

                    break;


            }
        }
    };
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BtMsgClass mChatService = null;
    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            (view, actionId, event) -> {
                // If the action is a key-up event on the return key, send the msg_list_holder
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                    String message = view.getText().toString();
                    sendMessage(message);
                }
                if (D) Log.i(TAG, "END onEditorAction");
                return true;
            };

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

    private void processMessage(String message) {
        String[] messages = message.split("\\n");      //\n로 메시지 타이밍 구분
        int size = messages.length;     //메시지 갯수 확인
        milliLiter = Integer.parseInt(messages[size - 1]);
        Log.e("recieved msg : ", milliLiter + "");
        //마지막 배열은 다음 메시지와 연결될 수 있기 때문에
        //어레이에 추가하지 않음.
    }

    private void setupChat() {

            // Initialize the BluetoothChatService to perform bluetooth connections
            mChatService = new BtMsgClass(this, mHandler);

            // Initialize the buffer for outgoing messages
            mOutStringBuffer = new StringBuffer();
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
        if (D) Log.e(TAG, "--- ON DESTROY ---");
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
            mOutEditText.setText(mOutStringBuffer);
        }
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

                    if (!mBluetoothAdapter.isEnabled()) {

                        bTserviceCallBack.BtNotOn();
                    } else {
                        if (mChatService == null) setupChat();
                    }
                case NEW_DEVICE_SELECTED:
                    mPreferences mPref = new mPreferences(getApplicationContext());
                    String address = mPref.getStringValue(mPreferences.BT_ADDR);


                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);


                    break;
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


