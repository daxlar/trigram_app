package com.example.trigram_app;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class postScanningActivity extends AppCompatActivity {

    BluetoothLeService bleService;
    Boolean mIsBound;
    EditText editText;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("testing");

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
            final Handler handler = new Handler();
            final int delay = 1000;
            handler.postDelayed(new Runnable(){
                public void run(){
                    
                    handler.postDelayed(this, delay);
                }
            }, delay);


        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("postScanningActivity", "ServiceConnection: disconnected from service.");
            mIsBound = false;
        }
    };
}