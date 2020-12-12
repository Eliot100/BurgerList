package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private String rest_rating;

    private ImageButton phone_btn;
    private ImageButton map_btn;
    private Button addcomment_btn;
    private EditText comment_text;
    private TextView ratingscore_text;
    private TextView workinghours_text;
    private TextView restname_title;
    private ScrollView menu_scrollview;
    private ScrollView comment_scrollview;

    private ArrayList<String> rest_comments;


    private FirebaseDatabase database;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_page);
        //initializing variable
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Restaurants").child(this.getIntent().getStringExtra("Owner_id"));
        phone_btn = (ImageButton)findViewById(R.id.phone_button);
        map_btn = (ImageButton)findViewById(R.id.google_map);
        addcomment_btn = (Button)findViewById(R.id.addComent_button);
        comment_text = (EditText)findViewById(R.id.addcomment_text);
        ratingscore_text = (TextView)findViewById(R.id.currentReating_text);
       // workinghours_text = (TextView)findViewById(R.id.workinghours_text);
        menu_scrollview = (ScrollView)findViewById(R.id.menu_scrollview);
        comment_scrollview = (ScrollView)findViewById(R.id.comment_scrollview);
        restname_title = (TextView)findViewById(R.id.restName_text);


        // getting data from data base.
        get_rest_data();

        //checking if current user is resturant owner






    }

    private void get_rest_data(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rest_name = snapshot.child("name").getValue().toString();
                rest_owner_id =  snapshot.child("ownerId").getValue().toString();
                rest_phone = snapshot.child("phone").getValue().toString();
                rest_rating = snapshot.child("currentRating").getValue().toString();
                update_page();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void update_page(){
        restname_title.setText(rest_name);
        ratingscore_text.setText(rest_rating);

    }
}