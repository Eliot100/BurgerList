package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VisitListActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private  ArrayList<Rating> ratings;
    private  ListView mListView;
    private String rest_id;
    private String name;
    private String owner_id;
    private String phone;
    private String currentRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users").child(MainActivity.get_user_id()).child("My list");

        mListView = (ListView) findViewById(R.id.menu_list);
        ratings = new ArrayList<>();

        read_ress();

    }


    public void display(){

        BurgerListAdapter adapter = new BurgerListAdapter(this, R.layout.adapter_view_layout, ratings);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rest_id = ratings.get(position).getRess().getOwnerId().toString();
                go_to_rest_page(rest_id);
            }
        });
    }

    public void go_to_rest_page(String rest_id){
        Intent intent = new Intent(this, RestPage.class);
        intent.putExtra("Owner_id", rest_id);
        startActivityForResult(intent,7);
    }

    public void read_ress(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keynode : snapshot.getChildren()){
//                    Toast.makeText(getApplicationContext(), "You need to be logged in to leave a comment", Toast.LENGTH_SHORT).show();
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