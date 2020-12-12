package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String name;
    private String owner_id;
    private String phone;
    private String currentRating;
    private ArrayList<Rating> ratings;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ratings = new ArrayList<Rating>();
        mListView = (ListView) findViewById(R.id.Search_ress);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("City").child(this.getIntent().getStringExtra("Search_Res"));


        read_ress();








    }

    public void display(){
        BurgerListAdapter adapter = new BurgerListAdapter(this, R.layout.adapter_view_layout, ratings);
        mListView.setAdapter(adapter);
    }

    public void read_ress(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keynode : snapshot.getChildren()){
                    name = keynode.child("name").getValue().toString();
                    owner_id =  keynode.child("ownerId").getValue().toString();
                    phone = keynode.child("phone").getValue().toString();
                    currentRating = keynode.child("currentRating").getValue().toString();
                    Restaurant ress =  new Restaurant(owner_id,name,phone);
                    Rating ret =  new Rating(ress,Double.parseDouble(currentRating));
                    ratings.add(ret);
                }
                display();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}