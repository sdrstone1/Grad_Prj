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

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class vMainActivity extends AppCompatActivity implements cBleDataTransferObject.BTSocketCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cPrefDao cPrefDao = new cPrefDao(getApplicationContext());
        if (!cPrefDao.init) {

            Intent firstlaunch_intent = new Intent(getApplicationContext(), vFirstLaunch.class);
            firstlaunch_intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(firstlaunch_intent);
        }


        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_v_main);

        /*
        String uid = cPrefDao.getBTUUID();
        if(uid != null){
            BluetoothDevice btDevice = null;
            cBleDataTransferObject cBleDTO = new cBleDataTransferObject();
            Set<BluetoothDevice> pairedBTs = cBleDTO.getPairedDevices();
            for(BluetoothDevice device : pairedBTs){
                if(device.getAddress() == uid ){

                    btDevice = device;
                    break;
                }
            }
            if(btDevice != null){
                // start socket transfer
                cBleDTO.setCallBack(this);
                cBleDTO.createSocket(btDevice);

            }
            else{
                Snackbar.make(findViewById(R.id.mainLayout),R.string.plsConnect_Bluetooth,Snackbar.LENGTH_LONG).show();
            }

        }
        */


    }

    public void onSettingsBtn(View v) {
        startActivity(new Intent(getApplicationContext(), vMessageActivity.class));
    }

    public void onMsgBtn(View v) {
        startActivity(new Intent(getApplicationContext(), vMessageActivity.class));
    }

    @Override
    protected void onDestroy() {
        mApplicationVO mApplicationVO = (com.tistory.kollhong.arduino_bluetooth.mApplicationVO) getApplicationContext();
        mApplicationVO.stopService(new Intent(this, mBleService.class));
        super.onDestroy();
    }

    @Override
    public void callBackMethod(BluetoothSocket mmSocket) {
        // callback start service

        mApplicationVO mApplicationVO = (mApplicationVO) getApplicationContext();
        mApplicationVO.mmSocket = mmSocket;
        //mApplicationVO.mHandler =
        mApplicationVO.startService();
    }
}
