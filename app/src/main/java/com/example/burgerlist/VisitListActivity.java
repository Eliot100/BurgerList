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

        //MyRatedRestaurant m = new MyRatedRestaurant();
        //m.setUserId("Yali_KING");
        //m.add_rating(new Restaurant("dori","baladi","050-1234567"),7);
        //m.add_rating(new Restaurant("moshe","habad202020","050-5555567"),9);

        ArrayList<Rating> ratings = new ArrayList<>();
        //.add(new Rating(m.get(0)));
        //.add(new Rating(m.get(1)));
        BurgerListAdapter adapter = new BurgerListAdapter(this, R.layout.adapter_view_layout, ratings);
        mListView.setAdapter(adapter);


    }
}