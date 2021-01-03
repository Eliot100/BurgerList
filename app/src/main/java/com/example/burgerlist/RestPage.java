package com.example.burgerlist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class RestPage extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 2;
    private static final int RESULT_UPLODE = 3;
    private String rest_owner_id, rest_name, rest_phone, rest_rating = "5", rest_owner_id2;
    private String timeOfMessage, message, user_name, user_id = "";
    private boolean Current_User_Is_owner, Current_User_Loggedin;
    private boolean disableClick = true;
    private Button addComment_btn, addToList_btn;
    private EditText comment_text;
    private ImageButton phone_btn, map_btn, Updates_btn;
    private ImageView rest_img;
    private TextView ratingScore_text, workingHours_text, restName_title;
    private ScrollView menu_scrollview, comment_scrollview;
    private ListView mListView, menu_list;
    private RatingBar ratingBar;
    private ArrayList<Comment> rest_comments;
    private ArrayList<Dish> rest_menu;
    private CommentListAdapter commentsAdapter;
    private MenuListAdapter menuAdapter;
    private PopupWindow popUp;
    private static final int REQUEST_CALL = 1;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private StorageReference storageReference;
    private double currentRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_page);
        //initializing variable

        phone_btn = (ImageButton)findViewById(R.id.phone_button);
        map_btn = (ImageButton)findViewById(R.id.google_map);
        Updates_btn = (ImageButton)findViewById(R.id.Updates_btn);
        addComment_btn = (Button)findViewById(R.id.addComent_button);
        comment_text = (EditText)findViewById(R.id.addcomment_text);
        ratingScore_text = (TextView)findViewById(R.id.currentReating_text);
//        workingHours_text = (TextView)findViewById(R.id.workinghours_text);
        menu_scrollview = (ScrollView)findViewById(R.id.menu_scrollview);
        comment_scrollview = (ScrollView)findViewById(R.id.comment_scrollview);
        restName_title = (TextView)findViewById(R.id.restName_text);
        addToList_btn = (Button)findViewById(R.id.addtolist_btn);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        mListView = (ListView) findViewById(R.id.comment_listview);
        menu_list = (ListView) findViewById(R.id.menu_list);
        rest_comments = new ArrayList<Comment>();
        rest_menu = new ArrayList<Dish>();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Restaurants").child(this.getIntent().getStringExtra("Owner_id"));
        get_rest_data();
        rest_owner_id2 = this.getIntent().getStringExtra("Owner_id");
        setRestImage();
        storageReference = FirebaseStorage.getInstance().getReference().child("uploads").child(rest_owner_id2).child("RestImage.jpeg");
        OwnerUpdates();
        get_comments();
        get_menu();
        rating();
        ratingBarChange();
        takeACall();
    }


    private void setRestImage() {
        rest_img = (ImageView) findViewById(R.id.rest_img);
        try{
            storageReference = FirebaseStorage.getInstance().getReference().child("uploads")
                    .child(rest_owner_id2).child("RestImage.jpg");
            File restImageFile = File.createTempFile("RestImg", "jpg");
            storageReference.getFile(restImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Success to get Image", Toast.LENGTH_SHORT).show();
                    Bitmap pic = BitmapFactory.decodeFile(restImageFile.getAbsolutePath());
                    rest_img.setImageBitmap(pic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to get Image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch ( Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void OwnerUpdates() {
        Updates_btn.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), MainActivity.user_id+" "+rest_owner_id2, Toast.LENGTH_SHORT).show();
        if(MainActivity.user_id.equals(rest_owner_id2)){
            Updates_btn.setVisibility(View.VISIBLE);
            Updates_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ContextCompat.checkSelfPermission(RestPage.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        GoAndUplode();
                    } else {
                     requestStoragePermission();
                    }
                }
            });
        }
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(RestPage.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(RestPage.this).setTitle("Permission needed").setMessage("This Permission is needed to upload files.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RestPage.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(RestPage.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    private void GoAndUplode() {
        Intent intent = new Intent(RestPage.this, ResturantUpdatesActivity.class);
        startActivityForResult(intent, RESULT_UPLODE);
    }

    private void ratingBarChange() {
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
                ratingBar.setRating(Float.parseFloat(rest_rating)/2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        ratingBar.setNumStars(Integer.parseInt( rest_rating));
    }

    private void rating() {
        ratingBar.setRating(Float.parseFloat(rest_rating));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(disableClick) {
                    disableClick = false;
                    Toast.makeText(getApplicationContext(), "drag again to update rating", Toast.LENGTH_SHORT).show();
                } else if (!MainActivity.get_user_id().equals("")){
                    Toast.makeText(getApplicationContext(), "update rating", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference("Restaurants").child(rest_owner_id)
                            .child("Rating").child(MainActivity.get_user_id()).setValue(ratingBar.getRating()*2);
                    disableClick = true;
                    ratingBar.setRating(Float.parseFloat(rest_rating)/2);
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
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                GoAndUplode();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
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
                displayComments();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void displayComments(){
        commentsAdapter = new CommentListAdapter(this, R.layout.adapter_res_layout, rest_comments);
        mListView.setAdapter(commentsAdapter);
    }
    public void displayMenu(){
        menuAdapter = new MenuListAdapter(this, R.layout.adapter_res_layout, rest_menu);
        menu_list.setAdapter(menuAdapter);
    }

    private void get_menu() {
        DatabaseReference comref = FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(rest_owner_id2).child("Menu").child("Category");
        comref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rest_menu.clear();
                String name, description, price;
                for(DataSnapshot keynode : snapshot.getChildren()){
                    for(DataSnapshot keynode2 : keynode.getChildren()){
                        name = keynode2.child("name").getValue().toString();
                        description =  keynode2.child("dec").getValue().toString();
                        price = keynode2.child("price").getValue().toString();

                        Dish dish = new Dish(name, description, price);
                        rest_menu.add(dish);
                    }
                }
//                displayMenu();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case RESULT_UPLODE:
                if (resultCode == RESULT_OK ) {
                    setRestImage();
                }
                break;
        }
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