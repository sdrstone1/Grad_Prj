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
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BtDto extends Thread {
    static final String TAG = "Arduino_calculator";
    private final UUID uid = UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");
    BTSocketCallBack mBTSocketCallBack;
    BluetoothAdapter mBluetoothAdapter;

    public BtDto() {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }

    }

    public void disable() {
        mBluetoothAdapter.disable();
    }

    public boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    public Set<BluetoothDevice> getPairedDevices() {

        return mBluetoothAdapter.getBondedDevices();
    }

    public void createSocket(BluetoothDevice bluetoothDevice) {
        new ConnectThread(bluetoothDevice);
    }

    private void returnBTSocket(BluetoothSocket mmSocket) {

        mBTSocketCallBack.callBackMethod(mmSocket);     //CallBack method called.
        //this method calls callBackMethod.


    }

    public void setCallBack(BTSocketCallBack CallbackClass) {
        mBTSocketCallBack = CallbackClass;      //classes implementing BTSocketCallBack interface
        //will call this method to set CallBack to its classes
    }

    interface BTSocketCallBack {
        void callBackMethod(BluetoothSocket mmSocket);  //this method is defined
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(uid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            returnBTSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

}
