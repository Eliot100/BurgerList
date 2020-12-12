package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class RestPage extends AppCompatActivity {
    private String rest_owner_id;
    private String rest_name;
    private String rest_phone;
    private String rest_rating;
    private boolean Current_User_Is_owner;
    private boolean Current_User_Loggedin;

    private ImageButton phone_btn;
    private ImageButton map_btn;
    private Button addcomment_btn;
    private EditText comment_text;
    private Button addtolist_btn;
    private TextView ratingscore_text;
    private TextView workinghours_text;
    private TextView restname_title;
    private ScrollView menu_scrollview;
    private ScrollView comment_scrollview;
    private ListView mListView;
    private Spinner ratedropdownlist;

    private ArrayList<Comment> rest_comments;


    private String timeofmessege;
    private String messege;
    private String user_name;
    private String user_id;


    private FirebaseDatabase database;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_page);
        //initializing variable


        phone_btn = (ImageButton) findViewById(R.id.phone_button);
        map_btn = (ImageButton) findViewById(R.id.google_map);
        addcomment_btn = (Button) findViewById(R.id.addComent_button);
        comment_text = (EditText) findViewById(R.id.addcomment_text);
        ratingscore_text = (TextView) findViewById(R.id.currentReating_text);
        workinghours_text = (TextView) findViewById(R.id.workinghours_text);
        menu_scrollview = (ScrollView) findViewById(R.id.menu_scrollview);
        comment_scrollview = (ScrollView) findViewById(R.id.comment_scrollview);
        restname_title = (TextView) findViewById(R.id.restName_text);
        addtolist_btn = (Button) findViewById(R.id.addtolist_btn);

        mListView = (ListView) findViewById(R.id.comment_listview);
        rest_comments = new ArrayList<Comment>();
        ratedropdownlist = (Spinner) findViewById(R.id.rate_dropdownlist);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Restaurants").child(this.getIntent().getStringExtra("Owner_id"));


        get_rest_data();


    }


    public void display() {
        ListView menulistview = (ListView) findViewById(R.id.menu_listview);


        ArrayList<RestMenuProduct> products = new ArrayList<>();

        MenuListAdapter adapter1 = new MenuListAdapter(this, R.layout.adapter_menu_product, products);
        menulistview.setAdapter(adapter1);

        ListView commentsistview = (ListView) findViewById(R.id.comment_listview);

        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("dodo", "popo", "fuck me"));
        comments.add(new Comment("dodoladodo", "popolapopo", "fuck me la fuck"));

        CommentListAdapter adapter2 = new CommentListAdapter(this, R.layout.adapter_res_layout, comments);
        commentsistview.setAdapter(adapter2);


    }

    private void get_rest_data() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rest_name = snapshot.child("name").getValue().toString();
                rest_owner_id = snapshot.child("ownerId").getValue().toString();
                rest_phone = snapshot.child("phone").getValue().toString();
                rest_rating = snapshot.child("currentRating").getValue().toString();

                check_user_is_owner();
                check_user_loggeding();
                get_comments();
                update_page();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void update_page() {
        restname_title.setText(rest_name);
        ratingscore_text.setText(rest_rating);

        addcomment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Current_User_Loggedin) {
                    Date d = java.util.Calendar.getInstance().getTime();

                    ref.child("Comments").child(MainActivity.user_id).child(d.toString()).setValue(comment_text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(), "Post successful", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "You need to be logged in to leave a comment", Toast.LENGTH_SHORT).show();
                }
            }


        });
        addtolist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Current_User_Loggedin) {
                    //String ownerId, String restName, String phoneNum
                    Restaurant rest = new Restaurant(rest_owner_id, rest_name, rest_phone);
                    FirebaseDatabase.getInstance().getReference("Users").child(MainActivity.get_user_id()).child("My list").child(rest_owner_id).setValue(rest)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Added to your list", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

    }

    public void get_comments() {
        DatabaseReference comref = FirebaseDatabase.getInstance().getReference("Restaurants").child(rest_owner_id).child("Comments");
        comref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keynode : snapshot.getChildren()) {
                    timeofmessege = keynode.child("date").getValue().toString();
                    messege = keynode.child("message").getValue().toString();
                    user_name = keynode.child("name").getValue().toString();
                    user_id = keynode.child("user").getValue().toString();
                    Comment com = new Comment(user_id, user_name, messege, timeofmessege);
                    rest_comments.add(com);
                }
                display();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void check_user_is_owner() {
        if (rest_owner_id.equals(MainActivity.get_user_id()) == true) {
            Current_User_Is_owner = true;
        }
    }

    private void check_user_loggeding() {
        if (MainActivity.get_user_id().length() > 6) {
            Current_User_Loggedin = true;
        }
    }

    /*private void rateRestaurant() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RestPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.rating_options));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratedropdownlist.setAdapter(myAdapter);

        ratedropdownlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }

