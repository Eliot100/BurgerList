package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> citys;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView =(ListView)findViewById(R.id.list_view);
        searchView = (SearchView)findViewById(R.id.search_view);
        citys = new ArrayList<String>();

        //reading csv file of all israel citys
         // will be done later

         //place holder for citys
        citys.add("ירושלים");
        citys.add("תל אביב");
        citys.add("הוד השרון");
        citys.add("הרצליה");
        citys.add("רעננה");
        citys.add("כפר סבא");
        citys.add("נתניה");
        citys.add("פתח תקווה");
        citys.add("חדרה");
        citys.add("חיפה");
        citys.add("טבריה");
        citys.add("אילת");
        citys.add("שדרות");
        citys.add("כפר הנשיא");

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,citys);
        listView.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });



    }
}