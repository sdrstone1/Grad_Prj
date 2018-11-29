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

import android.app.IntentService;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class mBleService extends IntentService {
    /*
onStartCommand()
The system invokes this method by calling startService() when another component (such as an activity)
requeststhat the service be started. When this method executes, the service is started and can run
in the background indefinitely.
If you implement this, it is your responsibility to stop the service when its work is complete
by calling stopSelf()or stopService().
If you only want to provide binding, you don't need to implement this method.

onBind()
The system invokes this method by calling bindService() when another component wants to bind
with the service (such as to perform RPC).
In your implementation of this method, you must provide an interface that clients use
to communicate with the service by returning an IBinder.
You must always implement this method; however, if you don't want to allow binding, you should return null.

onCreate()
The system invokes this method to perform one-time setup procedures when the service is initially created
(before it calls either onStartCommand() or onBind()). If the service is already running, this method is not called.

onDestroy()
The system invokes this method when the service is no longer used and is being destroyed.
Your service should implement this to clean up any resources such as threads, registered listeners, or receivers.
This is the last call that the service receives.
     */

    BluetoothSocket mmSocket;
    Handler mHandler;
    public mBleService(String name) {
        super("BLservice");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mBTSocketVO mBTSocket = (mBTSocketVO) getApplicationContext();
        this.mmSocket = mBTSocket.mmSocket;
        mHandler = mBTSocket.mHandler;
        BTService btService = new BTService(mmSocket, mHandler);


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }


    public static class BTService {
        private static final String TAG = "Arduino_App";
        private Handler mHandler; // handler that gets info from Bluetooth service
        private ConnectedThread connectedThread;

        // Defines several constants used when transmitting messages between the
        // service and the UI.
        private interface MessageConstants {
            public static final int MESSAGE_READ = 0;
            public static final int MESSAGE_WRITE = 1;
            public static final int MESSAGE_TOAST = 2;

            // ... (Add other message types here as needed.)
        }

        public BTService(BluetoothSocket mmSocket, Handler mHandler) {
            connectedThread = new ConnectedThread(mmSocket);
            this.mHandler = mHandler;
        }

        public void BTwrite(byte[] bytes) {
            connectedThread.write(bytes);
        }

        private class ConnectedThread extends Thread {
            private final BluetoothSocket mmSocket;
            private final InputStream mmInStream;
            private final OutputStream mmOutStream;
            private byte[] mmBuffer; // mmBuffer store for the stream

            public ConnectedThread(BluetoothSocket socket) {
                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                // Get the input and output streams; using temp objects because
                // member streams are final.
                try {
                    tmpIn = socket.getInputStream();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when creating input stream", e);
                }
                try {
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when creating output stream", e);
                }

                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            public void run() {
                mmBuffer = new byte[1024];
                int numBytes; // bytes returned from read()

                // Keep listening to the InputStream until an exception occurs.
                while (true) {
                    try {
                        // Read from the InputStream.
                        numBytes = mmInStream.read(mmBuffer);
                        // Send the obtained bytes to the UI activity.
                        //TODO Handler지우기. DB로 바로 작성할 거야.
                        Message readMsg = mHandler.obtainMessage(
                                MessageConstants.MESSAGE_READ, numBytes, -1,
                                mmBuffer);      //mHandler에서 얻은 Message가
                        readMsg.sendToTarget(); //타겟으로 보냄 == mHandler에 보내짐

                    } catch (IOException e) {
                        Log.d(TAG, "Input stream was disconnected", e);
                        break;
                    }
                }
            }

            // Call this from the main activity to send data to the remote device.
            public void write(byte[] bytes) {
                try {
                    mmOutStream.write(bytes);

                    // Share the sent message with the UI activity.
                    Message writtenMsg = mHandler.obtainMessage(
                            MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                    writtenMsg.sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when sending data", e);

                    // Send a failure message back to the activity.
                    Message writeErrorMsg =
                            mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                    Bundle bundle = new Bundle();
                    bundle.putString("toast",
                            "Couldn't send data to the other device");
                    writeErrorMsg.setData(bundle);
                    mHandler.sendMessage(writeErrorMsg);
                }
            }

            // Call this method from the main activity to shut down the connection.
            public void cancel() {
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
            }
        }
    }

}
