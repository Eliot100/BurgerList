package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPage extends AppCompatActivity implements SearchAdapter.OnRestClickListner{
    Button userRestButton;
    Button createRestButton;
    TextView userPage_name , reviews_text;
    long num_of_review  ;
    private RecyclerView recycler_fav;

    private DatabaseReference ref;
    private SearchAdapter searchAdapter;

    private ArrayList<String> rest_id_list;
    private ArrayList<String> rest_name_list;
    private ArrayList<String> rating_list;
    private ArrayList<String> rest_logo_list;
    private ArrayList<String> city_list;
    private ArrayList<String> distance_list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);


        userRestButton = (Button)findViewById(R.id.userRessButton);
        createRestButton = (Button)findViewById(R.id.createRestButton);
        userPage_name = (TextView)findViewById(R.id.userPage_name);
        reviews_text = (TextView)findViewById(R.id.reviews_text);

        recycler_fav = (RecyclerView)findViewById(R.id.recycler_fav);
        rest_name_list =  new ArrayList<String>();
        rating_list =  new ArrayList<String>();
        rest_logo_list =  new ArrayList<String>();
        rest_id_list = new ArrayList<String>();
        city_list = new ArrayList<String>();
        distance_list = new ArrayList<String>();

        createRestButton.setVisibility(View.GONE);
        userRestButton.setVisibility(View.GONE);

        ref = FirebaseDatabase.getInstance().getReference();

        //setting up recycle view
        recycler_fav.setHasFixedSize(true);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));
        recycler_fav.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        update_ui();
        get_fav_rest();
        set_review();



        userRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_RestPage();
            }
        });

        createRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_RestPage();
            }
        });
    }

    private void get_fav_rest() {
        try{
            ref.child("Users").child(MainActivity.get_user_id()).child("My list").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for( DataSnapshot snap: snapshot.getChildren()) {

                        String restname = snap.child("name").getValue().toString();
                        String uid = snap.child("ownerId").getValue().toString();
                        String rating = snap.child("currentRating").getValue().toString();
                        String city = "nope";

                        rest_name_list.add(restname);
                        rest_id_list.add(uid);
                        rating_list.add(rating);
                        city_list.add(rating+" Rating");
                        distance_list.add("");

                        //rest_logo_list.add(logo);   //logo when storage ready
                    }
                    searchAdapter = new SearchAdapter(UserPage.this , rest_id_list ,rest_name_list ,rating_list,city_list , distance_list , UserPage.this::onRestClick);
                    recycler_fav.setAdapter(searchAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){

        }
    }


    private void start_RestPage() {
        Intent intent = new Intent(this, RestPage.class);
        intent.putExtra("Owner_id",MainActivity.get_user_id());
        startActivityForResult(intent, 2);
    }

    private void Create_RestPage() {
        Intent intent = new Intent(this, CreateRestaurant.class);
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4) {
            if(resultCode == RESULT_OK){
                createRestButton.setVisibility(View.GONE);
                userRestButton.setVisibility(View.VISIBLE);
            }
            if (resultCode == RESULT_CANCELED) {
                //might be usefull later
            }
        }
    }

    private void update_ui(){
        // setting up name picture and other relevant info
        userPage_name.setText("Hello "+MainActivity.get_user_name());


        if(MainActivity.get_isowner() == true){
            if(MainActivity.get_user_restaurant_name().equals("0") == true) {
                createRestButton.setVisibility(View.VISIBLE);
            }
            else {// owner with a restaurant
                userRestButton.setVisibility(View.VISIBLE);
                userRestButton.setText("Go to "+MainActivity.get_user_restaurant_name());
            }
        }
        else{
            createRestButton.setVisibility(View.GONE);
        }

    }

    private void set_review() {
        DatabaseReference rev;
        rev = FirebaseDatabase.getInstance().getReference();

        try{
            rev.child("Users").child(MainActivity.get_user_id()).child("My rated ress").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    num_of_review = snapshot.getChildrenCount();
                    reviews_text.setText("Reviews: "+num_of_review);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){
        }

    }

    @Override
    public void onRestClick(int position) {
        Intent intent = new Intent(this, RestPage.class);
        intent.putExtra("Owner_id", rest_id_list.get(position));
        startActivityForResult(intent,7);
    }
}