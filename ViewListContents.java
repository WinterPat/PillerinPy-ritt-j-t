package com.example.projekti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.projekti.DatabaseHelper.TABLE_NAME;

public class ViewListContents extends AppCompatActivity {

    DatabaseHelper myDB;
    private ListView lv1;

    private void updateUI(){

        lv1 = (ListView) findViewById(R.id.listView);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontents_layout);

        updateUI();


        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                lv1.setAdapter(listAdapter);
            }
        }


        resetButton();
        backButton();

    }
    private void backButton() {
        Button backButton = (Button) findViewById(R.id.button5);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(ViewListContents.this, MainActivity.class));
            }
        });
    }
    private void resetButton() {
        Button backButton = (Button) findViewById(R.id.button6);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDB.deleteRow();

                Intent intent = new Intent(ViewListContents.this, ViewListContents.class);
                startActivity(intent);
            }
        });
    }

}
