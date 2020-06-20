package com.example.trigram_app;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class BleDeviceScanActivity extends ListActivity {

    private static final long SCAN_PERIOD = 10000;
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private boolean mScanning;
    private Handler handler;
    private HashMap<String, BluetoothDevice> deviceHashMap;
    private ArrayList<String> deviceNames;



    public BleDeviceScanActivity(Context btContext){
        deviceHashMap = new HashMap<>();
        deviceNames = new ArrayList<>();
        handler = new Handler();
        // Initializes Bluetooth adapter
        // TODO: app fails if bluetooth is not initially turned on
        BluetoothManager bluetoothManager = (BluetoothManager)btContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    // Stops scanning after 10 seconds.
    public  void scanLeDevice(final boolean enable, final ArrayAdapter<String> listAdapter) {
        Log.i("scan le device", "starting" );
        deviceHashMap.clear();
        deviceNames.clear();
        Log.i("scan le device", " cleared objects");
        // Device scan callback.
        final BluetoothAdapter.LeScanCallback leScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("scan le device", " found device");
                                String bleDeviceName = device.getName();
                                if(!deviceHashMap.containsKey(bleDeviceName) && bleDeviceName != null) {
                                    listAdapter.add(device.getName());
                                    deviceHashMap.put(device.getName(), device);
                                    deviceNames.add(device.getName());
                                }
                                Log.i("scan le device", "added object");
                            }
                        });
                    }
                };
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }


    public ArrayList<String> getDeviceNames(){
        return deviceNames;
    }

    public BluetoothDevice getBleDevice(String s){
        return deviceHashMap.get(s);
    }
}
