package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RestPage extends AppCompatActivity {
    private String rest_owner_id;
    private String rest_name;
    private String rest_phone;
    private ArrayList<String> rest_comments;
    private String rest_rating;




    private FirebaseDatabase database;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_page);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Restaurants").child(this.getIntent().getStringExtra("Owner_id"));

        get_rest_data();





    }

    private void get_rest_data(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rest_name = snapshot.child("name").getValue().toString();
                rest_owner_id =  snapshot.child("ownerId").getValue().toString();
                rest_phone = snapshot.child("phone").getValue().toString();
                rest_rating = snapshot.child("currentRating").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}