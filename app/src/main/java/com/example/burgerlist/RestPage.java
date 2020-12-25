package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RestPage extends AppCompatActivity {
    private String rest_owner_id, rest_name, rest_phone, rest_rating = "5", rest_owner_id2;
    private String timeOfMessage, message, user_name, user_id = "";
    private boolean Current_User_Is_owner, Current_User_Loggedin;
    private boolean disableClick = true;
    private Button addComment_btn, addToList_btn;
    private EditText comment_text;
    private ImageButton phone_btn, map_btn;
    private TextView ratingScore_text, workingHours_text, restName_title;
    private ScrollView menu_scrollview, comment_scrollview;
    private ListView mListView;
    private RatingBar ratingBar;
    private ArrayList<Comment> rest_comments;
    private CommentListAdapter adapter, adapter2;
    private PopupWindow popUp;
    private static final int REQUEST_CALL = 1;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private double currentRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_page);
        //initializing variable

        phone_btn = (ImageButton)findViewById(R.id.phone_button);
        map_btn = (ImageButton)findViewById(R.id.google_map);
        addComment_btn = (Button)findViewById(R.id.addComent_button);
        comment_text = (EditText)findViewById(R.id.addcomment_text);
        ratingScore_text = (TextView)findViewById(R.id.currentReating_text);
        workingHours_text = (TextView)findViewById(R.id.workinghours_text);
        menu_scrollview = (ScrollView)findViewById(R.id.menu_scrollview);
        comment_scrollview = (ScrollView)findViewById(R.id.comment_scrollview);
        restName_title = (TextView)findViewById(R.id.restName_text);
        addToList_btn = (Button)findViewById(R.id.addtolist_btn);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        mListView = (ListView) findViewById(R.id.comment_listview);
        rest_comments = new ArrayList<Comment>();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Restaurants").child(this.getIntent().getStringExtra("Owner_id"));

        ratingBar.setNumStars(5);
        get_rest_data();
        rest_owner_id2 = this.getIntent().getStringExtra("Owner_id");
        get_comments();
        rating();
        ref.child("Rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                currentRating = 0; int size = 0;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    currentRating += Integer.parseInt(postSnapshot.getValue(Long.class).toString());
                    size++;
                }
                rest_rating = String.valueOf(currentRating/size);
                try{
                    ref.child("currentRating").setValue(Double.valueOf(rest_rating));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
//                ratingBar.setRating(Float.parseFloat(rest_rating)/2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        takeACall();
    }

    private void takeACall() {
        phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(ContextCompat.checkSelfPermission(RestPage.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RestPage.this,
                                new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    } else {
                        String dial = "tel:" + rest_phone;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                }catch (Exception e){
                    Toast.makeText(RestPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takeACall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void rating() {
        ratingBar.setRating(Float.parseFloat(rest_rating)/2);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(disableClick)
                    disableClick = false;
                else
                    if (!MainActivity.get_user_id().equals("")){
                    Toast.makeText(getApplicationContext(), "update rating", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference("Restaurants").child(rest_owner_id)
                            .child("Rating").child(MainActivity.get_user_id()).setValue(ratingBar.getRating()*2);
                    disableClick = true;
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(10);
                            } catch (Exception e){
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            ratingBar.setRating(Float.parseFloat(rest_rating)/2);
                        }
                    }.start();

                }

            }
        });
    }

    public void  display(){
        adapter = new CommentListAdapter(this, R.layout.adapter_res_layout, rest_comments);
        mListView.setAdapter(adapter);
    }

    private void get_rest_data(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rest_name = snapshot.child("name").getValue().toString();
                rest_owner_id =  snapshot.child("ownerId").getValue().toString();
                rest_phone = snapshot.child("phone").getValue().toString();
                rest_rating = snapshot.child("currentRating").getValue().toString();
                check_user_is_owner();
                check_user_loggeding();
                update_page();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        addComment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Current_User_Loggedin){
                    String time = java.util.Calendar.getInstance().getTime().toString();
                    Comment com =  new Comment(MainActivity.get_user_id(),MainActivity.get_user_name(),comment_text.getText().toString());
                    ref.child("Comments").child(MainActivity.user_id).child(time).setValue(com).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(), "Post successful", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "You need to be logged in to leave a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addToList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Current_User_Loggedin){
                    //String ownerId, String restName, String phoneNum
                    Restaurant rest =  new Restaurant(rest_owner_id,rest_name,rest_phone);
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

    private void update_page(){
        restName_title.setText(rest_name);
        ratingScore_text.setText("current rating: "+rest_rating);

    }

    public void get_comments(){
        DatabaseReference comref = FirebaseDatabase.getInstance().getReference("Restaurants").child(rest_owner_id2).child("Comments");
        comref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rest_comments.clear();
                for(DataSnapshot keynode : snapshot.getChildren()){
                    for(DataSnapshot keynode2 : keynode.getChildren()){
                        timeOfMessage = keynode2.child("date").getValue().toString();
                        message =  keynode2.child("message").getValue().toString();
                        user_name = keynode2.child("name").getValue().toString();
                        user_id = keynode2.child("user").getValue().toString();

                        Comment com = new Comment(user_id,user_name, message, timeOfMessage);
                        rest_comments.add(com);
                        comment_text.setText("");
                    }
                }
                display();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void check_user_is_owner(){
        if(rest_owner_id.equals(MainActivity.get_user_id()) == true){
            Current_User_Is_owner = true;
        }
    }
    private void check_user_loggeding(){
        if(MainActivity.get_user_id().length()>6){
            Current_User_Loggedin = true;
        }
    }
}