package com.example.trigram_app;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;

import java.util.UUID;

public class postScanningActivity extends AppCompatActivity {

    BluetoothLeService bleService;
    Boolean mIsBound;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_scanning);

        editText = findViewById(R.id.editTextTextPersonName);
        editText.setText(bleSingleton.getBleDeviceName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mIsBound){
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }

    private void startService(){
        Intent serviceIntent = new Intent(this, BluetoothLeService.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceBindIntent =  new Intent(this, BluetoothLeService.class);
        bindService(serviceBindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.d("postScanningActivity", "ServiceConnection: connected to service.");
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            BluetoothLeService.LocalBinder binder = (BluetoothLeService.LocalBinder) iBinder;
            bleService = binder.getService();
            mIsBound = true;
            final BluetoothGattCharacteristic bluetoothGattCharacteristic =
                    new BluetoothGattCharacteristic(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"),
                                                    1,
                                                    1);
            // TODO: working on this
            bleService.connect();
            new Thread(new Runnable(){
                public void run() {
                    // TODO Auto-generated method stub
                    int counter = 0;
                    while(true)
                    {
                        /*
                        if(staticBLE.bleUARTTXcharacteristic != null) {
                            BluetoothGattCharacteristic c = bleService.mBluetoothGatt.getService(BluetoothLeService.UART_SERVICE_ID)
                                    .getCharacteristic(BluetoothLeService.UART_RX_CHARACTERISTIC_ID);
                            //Log.i("main loop", c.getUuid().toString());
                            c.setValue("hello");
                            bleService.mBluetoothGatt.writeCharacteristic(c);
                        }
                         */
                    }

                }
            }).start();
            //getRandomNumberFromService(); // return a random number from the service
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("postScanningActivity", "ServiceConnection: disconnected from service.");
            mIsBound = false;
        }
    };
}