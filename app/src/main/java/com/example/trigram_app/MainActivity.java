package com.example.trigram_app;

import androidx.appcompat.app.AppCompatActivity;

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

    private void setStringList(){
        stringList = new ArrayList<>();
    }

    private void setListView1(){
        listView1 = (ListView)findViewById(R.id.listView1);
    }

    private void setListView1Adapter(ArrayList<String> stringContainer){
        listView1Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringContainer);
    }

    private void configureListView1(){
        listView1.setAdapter(listView1Adapter);
        AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String toPut = (String)parent.getAdapter().getItem(position);
                Toast myToast = Toast.makeText(getApplicationContext(), toPut, Toast.LENGTH_SHORT);
                myToast.show();
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
        addButton = (Button)findViewById(R.id.add_button);
    }

    private void setRemoveButton(){
        removeButton = (Button)findViewById(R.id.remove_button);
    }

    private void configureAddButton(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String toAdd = textAddString.getText().toString();
                addString(toAdd);
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
        textAddString = (EditText)findViewById(R.id.add_plain_text);
    }

    private void setRemoveString(){
        textRemoveString = (EditText)findViewById(R.id.remove_plain_text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    }
}