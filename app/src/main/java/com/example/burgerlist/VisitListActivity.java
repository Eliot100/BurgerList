package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class VisitListActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        Log.d(TAG, "onCreate: Started.");
        ListView mListView = (ListView) findViewById(R.id.MyListVisit);

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        BurgerListAdapter adapter = new BurgerListAdapter(this, R.layout.adapter_view_layout, restaurants);
        mListView.setAdapter(adapter);



    }
}