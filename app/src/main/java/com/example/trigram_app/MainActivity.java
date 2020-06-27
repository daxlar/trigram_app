package com.example.trigram_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> stringList;
    ArrayAdapter<String> listView1Adapter;
    ListView listView1;
    Button scanButton;
    BleDeviceScanActivity bleDeviceScanActivity;

    private void setStringList(){
        stringList = new ArrayList<>();
    }

    private void setListView1(){
        listView1 = findViewById(R.id.listView1);
    }

    private void setListView1Adapter(ArrayList<String> stringContainer){
        listView1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringContainer);
    }

    private void configureListView1(){
        listView1.setAdapter(listView1Adapter);
        AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (!listView1Adapter.isEmpty()) {
                    String toPut = listView1Adapter.getItem(position);
                    //listView1Adapter.remove(toPut);
                    bleDeviceSingleton.setBleDevice(bleDeviceScanActivity.getBleDevice(toPut), toPut);
                    //staticBLE.bleDevice = bleDeviceScanActivity.getBleDevice(toPut);
                    //staticBLE.hexChecker = "set_device";
                    Intent intent = new Intent(MainActivity.this, postScanningActivity.class);
                    startActivity(intent);
                }
            }
        };
        listView1.setOnItemClickListener(messageClickedHandler);
    }

    private void setAddButton(){
        scanButton = findViewById(R.id.add_button);
    }

    private void configureAddButton(){
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView1Adapter.clear();
                bleDeviceScanActivity.scanLeDevice(true, listView1Adapter);
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // workaround for location enabling
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else{
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }

        setStringList();
        setListView1();
        setListView1Adapter(stringList);
        configureListView1();

        setAddButton();
        configureAddButton();

        bleDeviceScanActivity = new BleDeviceScanActivity(this);
    }
}