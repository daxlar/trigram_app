package com.example.trigram_app;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

public class bleSingleton {

    public static BluetoothDevice bleDevice;
    public static String bleDeviceName;
    public static BluetoothGattCharacteristic bleUARTTXcharacteristic;

    public static void setBleDevice(BluetoothDevice bluetoothDevice, String bleName){
        bleDevice = bluetoothDevice;
        bleDeviceName = bleName;
        bleUARTTXcharacteristic = null;
    }

    public static String getBleDeviceName(){
        return bleDeviceName;
    }

    public static BluetoothDevice getBleDevice(){
        return bleDevice;
    }


}
