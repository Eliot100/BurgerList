package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


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
        ArrayList<String> filterd_list = new ArrayList<String>();
        boolean from_creat_ress = this.getIntent().getBooleanExtra("FLAG",false);

        //reading csv file of all israel citys
         // will be done later

         //place holder for citys
        citys.add("jerusalem");
        citys.add("tel aviv");
        citys.add("hod hasharon");
        citys.add("herzliya");
        citys.add("Raanana");
        citys.add("kfar saba");
        citys.add("natania");
        citys.add("petah tikva");
        citys.add("hadera");
        citys.add("haifa");
        citys.add("Tiberias");
        citys.add("eilat");
        citys.add("Sderot");
        citys.add("kfar hanassi");

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterd_list.clear();
                for (int i = 0 ; i < adapter.getCount() ; i++){
                    filterd_list.add(adapter.getItem(i).toString());

                }
                if (from_creat_ress) {
                    Toast.makeText(getApplicationContext(),filterd_list.get(position), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("Search_Res", filterd_list.get(position));
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                else{
                    go_to_search_result(filterd_list.get(position));
                };

            }
        });

    }


    public void go_to_search_result(String city){
        Intent intent = new Intent(this, SearchResults.class);
        intent.putExtra("Search_Res", city);
        startActivityForResult(intent,6);
    }
}

