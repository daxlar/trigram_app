package com.example.trigram_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> stringList;
    ArrayAdapter<String> listView1Adapter;
    ListView listView1;
    Button addButton;
    Button removeButton;
    EditText textAddString;
    EditText textRemoveString;
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
                String toPut = listView1Adapter.getItem(position);
                listView1Adapter.remove(toPut);
            }
        };
        listView1.setOnItemClickListener(messageClickedHandler);
    }

    private void addString(String s){
        listView1Adapter.add(s);
    }

    private void removeString(String s){
        listView1Adapter.remove(s);
    }

    private void setAddButton(){
        addButton = findViewById(R.id.add_button);
    }

    private void setRemoveButton(){
        removeButton = findViewById(R.id.remove_button);
    }

    private void configureAddButton(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                listView1Adapter.clear();
                bleDeviceScanActivity.scanLeDevice(true, listView1Adapter);
            }
        });
    }

    private void configureRemoveButton(){
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String toRemove = textRemoveString.getText().toString();
                removeString(toRemove);
            }
        });
    }

    private void setAddString(){
        textAddString = findViewById(R.id.add_plain_text);
    }

    private void setRemoveString(){
        textRemoveString = findViewById(R.id.remove_plain_text);
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


        setAddString();
        setRemoveString();

        setAddButton();
        setRemoveButton();
        configureAddButton();
        configureRemoveButton();

        bleDeviceScanActivity = new BleDeviceScanActivity(this);
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        bleDeviceScanActivity = new BleDeviceScanActivity();
    }
     */


}