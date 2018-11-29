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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import java.util.UUID;

public class vBleConnect extends AppCompatActivity {
    static final int REQUEST_ENABLE_BT = 33768;
    static final String NAME = "Arduino_calculator";
    static final UUID uid = UUID.fromString(NAME);
    public BroadcastReceiver discoverReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                //TODO list에 출력.
            }
        }
    };
    mBleVO mBleVO;
    //   boolean status;
//    BroadcastReceiver reciever;
    Switch bleSwitch;
    public BroadcastReceiver bleReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                case BluetoothAdapter.STATE_TURNING_OFF:
                case BluetoothAdapter.STATE_TURNING_ON:
                    switchStatus(true);
                    break;
                case BluetoothAdapter.STATE_ON:
                    switchStatus(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_ble_connect);
//TODO mBluetoothAdapter mBleVO로 옮기기.
        //TODO 아두이노와 연결되면 소켓통신 시작하기


        mBleVO = new mBleVO();

        switchStatus(mBleVO.isEnabled());

        //TODO 블루투스 장치 리스트 표시
        bleSwitch = findViewById(R.id.switch1);

        IntentFilter intent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bleReciever, intent);


    }

    private void scanBLE() {
        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoverReciever, intent);
    }

    public void switchStatus(boolean status) {
        //int status = mBluetoothAdapter.getState();
        bleSwitch.setChecked(status);
        if (status) {
            scanBLE();
        }
    }


    public void onSwitchBtn(View v) {
        if (!mBleVO.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            mBleVO.disable();
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(bleReciever);
        unregisterReceiver(discoverReciever);
        super.onStop();
    }


}
