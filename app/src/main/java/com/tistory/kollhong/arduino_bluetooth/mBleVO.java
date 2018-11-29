package com.tistory.kollhong.arduino_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class mBleVO extends Thread {
    static final String NAME = "Arduino_calculator";
    static final UUID uid = UUID.fromString(NAME);
    private final BluetoothServerSocket mmServerSocket;
    BluetoothAdapter mBluetoothAdapter;

    public mBleVO() {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, uid);
        } catch (IOException e) {
            Log.e(NAME, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void disable() {
        mBluetoothAdapter.disable();
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();       //BLE connection accepted : returns socket
                socket.
            } catch (IOException e) {
                Log.e(NAME, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket);
                mmServerSocket.close();
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(NAME, "Could not close the connect socket", e);
        }
    }

    public boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    }
}
