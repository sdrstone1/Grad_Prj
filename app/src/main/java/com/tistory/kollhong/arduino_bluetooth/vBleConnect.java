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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class vBleConnect extends AppCompatActivity {
    static final int REQUEST_ENABLE_BT = 33768;
    //static final String NAME = "Arduino_calculator";
    //static final UUID uid = UUID.fromString(NAME);

    mBleVO mBleVO;
    Switch bleSwitch;
    ListView BT_list;
    ArrayAdapter<mBleVO.listVO> arrayAdapter;
    public BroadcastReceiver discoverReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                mBleVO.listVO devVO = new mBleVO.listVO();
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devVO.setDeviceName(device.getName());
                devVO.setDeviceUUID(device.getAddress()); // MAC address

                arrayAdapter.add(devVO);
                arrayAdapter.notifyDataSetChanged();

            }
        }
    };
    List<mBleVO.listVO> pairedBTs;
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

        bleSwitch = findViewById(R.id.switch1);
        BT_list = findViewById(R.id.BT_devices);

        arrayAdapter = new BTArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2);
        //TODO 아두이노와 연결되면 소켓통신 시작하기

        mBleVO = new mBleVO();
        pairedBTs = mBleVO.getPairedDevices();
        switchStatus(mBleVO.isEnabled());

        IntentFilter intent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bleReciever, intent);


        BT_list.setAdapter(arrayAdapter);
    }

    public class BTArrayAdapter extends ArrayAdapter<mBleVO.listVO> {

        public BTArrayAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Nullable
        @Override
        public mBleVO.listVO getItem(int position) {
            return super.getItem(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 소켓 통신 시작
                    v.findViewById(android.R.id.text1);
                }
            });
            TextView text1 = convertView.findViewById(android.R.id.text1);
            TextView text2 = convertView.findViewById(android.R.id.text2);
            String text = getItem(position).deviceName;

            text2.setText(getItem(position).deviceUUID);

            for (mBleVO.listVO i : pairedBTs) {
                if (getItem(position).deviceUUID == i.deviceUUID) {
                    text += "(Paired)";
                    text1.setTypeface(text1.getTypeface(), Typeface.BOLD);
                    break;
                }
            }
            text1.setText(text);

            return convertView;
        }

        @Override
        public void add(@Nullable mBleVO.listVO object) {
            super.add(object);
        }
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
