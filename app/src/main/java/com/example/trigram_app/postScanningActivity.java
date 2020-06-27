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
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class postScanningActivity extends AppCompatActivity {

    BluetoothLeService bleService;
    Boolean mIsBound;
    EditText editText;
    LineGraphSeries<DataPoint> series;
    GraphView graphView;
    int secondCounter;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("devices");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_scanning);

        secondCounter = 0;

        editText = findViewById(R.id.editTextTextPersonName);
        editText.setText(bleDeviceSingleton.getBleDeviceName());
        graphView = findViewById(R.id.graph);
        GridLabelRenderer glr = graphView.getGridLabelRenderer();
        glr.setPadding(50);
        series = new LineGraphSeries<>();
        series.setDrawDataPoints(true);
        graphView.addSeries(series);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(7);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScrollable(true);
        //graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);
        //final java.text.DateFormat dateTimeFormatter = DateFormat.getTimeFormat(this);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX){
                    //return dateTimeFormatter.format(new Date((long) value*1000));
                    return super.formatLabel(value, isValueX) + "s";
                }else{
                    return super.formatLabel(value, isValueX) + " devices";
                }
            }
        });
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
                    int devicesNearby = 0;
                    boolean bufferChoice = bleDataSingleton.bufferDecider.get();
                    bleDataSingleton.bufferDecider.set(!bufferChoice);
                    while(!bleDataSingleton.finishedDataOperation.get()){

                    }
                    if(!bufferChoice){
                        for (Map.Entry addresses : bleDataSingleton.macDataBuffer1.entrySet()) {
                            String macAddress = (String) addresses.getKey();
                            Integer macCount = (Integer) addresses.getValue();
                            if (macCount >= 1) {
                                devicesNearby++;
                            }
                        }
                        bleDataSingleton.macDataBuffer1.clear();
                    }else{

                        for (Map.Entry addresses : bleDataSingleton.macDataBuffer2.entrySet()) {
                            String macAddress = (String) addresses.getKey();
                            Integer macCount = (Integer) addresses.getValue();
                            if (macCount >= 1) {
                                devicesNearby++;
                            }
                        }
                        bleDataSingleton.macDataBuffer2.clear();
                    }
                    if(devicesNearby >= 1){
                        devicesNearby--;
                    }
                    Log.i("onServiceConnected", "device count " + devicesNearby);
                    Date currentTime = Calendar.getInstance().getTime();
                    String epoch = currentTime.toString();
                    DataPoint dataPoint = new DataPoint(secondCounter++, devicesNearby);
                    series.appendData(dataPoint, true,1000);
                    mConditionRef.child(epoch).setValue(devicesNearby);

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