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


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> citys;
    private SearchView searchView;
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
        citys.add("--Not in city--");
         read_csv();

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

    private void read_csv() {
        String line = "";
        String splitBy = ",";
        String city_name;
        int counter=0;

        try
        {
            //parsing a CSV file into BufferedReader class constructor
            InputStream input = getResources().openRawResource(R.raw.citys);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while ((line = reader.readLine()) != null)   //returns a Boolean value
           {

                String[] employee = line.split(splitBy);    // use comma as separator
               if(counter>0&&!employee[3].isEmpty()){
                   city_name = employee[3];
                   city_name = city_name.toLowerCase();
                   city_name = city_name.trim();
                   if(city_name.isEmpty() == false){
                       citys.add(city_name);
                   }

               }
               counter++;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "we are having trouble processing your request please try again.", Toast.LENGTH_SHORT).show();
        }


    }


    public void go_to_search_result(String city){
        Intent intent = new Intent(this, SearchResults.class);
        intent.putExtra("Search_Res", city);
        startActivityForResult(intent,6);
    }
}

