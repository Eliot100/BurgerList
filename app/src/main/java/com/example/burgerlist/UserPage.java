package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class UserPage extends AppCompatActivity implements SearchAdapter.OnRestClickListner{
    Button userRestButton, createRestButton;
    ImageButton getUserImg_btn;
    private ImageView userImg;
    TextView userPage_name , reviews_text;
    long num_of_review  ;
    private RecyclerView recycler_fav;
    private SearchAdapter searchAdapter;
    private DatabaseReference ref;
    private ArrayList<String> rest_id_list, rest_logo_list, rest_name_list, rating_list, city_list, distance_list;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private static final int PICK_REST_IMG = 1;
    private Uri UserImg_uri;
    private StorageTask storageTask;
    private DatabaseReference databaseRef;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

//        storageRef = FirebaseStorage.getInstance().getReference("uploads").child(MainActivity.user_id);


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
        setUserImage();
        addUserPic();

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

    private void setUserImage() {
        userImg = (ImageView) findViewById(R.id.userImage);
        try{
            storageReference = FirebaseStorage.getInstance().getReference().child("uploads")
                    .child(MainActivity.get_user_id()).child("UserImage.jpg");
            File restImageFile = File.createTempFile("UserImg", "jpg");
            storageReference.getFile(restImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Success to get Image", Toast.LENGTH_SHORT).show();
                    Bitmap pic = BitmapFactory.decodeFile(restImageFile.getAbsolutePath());
                    userImg.setImageBitmap(pic);
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

    private void addUserPic() {
        getUserImg_btn = findViewById(R.id.getUserImg_btn);
//        getUserImg_btn.setVisibility(View.GONE);
        getUserImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask != null && storageTask.isInProgress()){
                    Toast.makeText(UserPage.this, "upload in progress", Toast.LENGTH_LONG).show();
                } else {
                    if(ContextCompat.checkSelfPermission(UserPage.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        openImage();
                    } else {
                        requestStoragePermission();
                    }
                }
            }
        });
    }


    private void openImage() {
        Intent i = new Intent();
        i.setType("image/jpeg");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_REST_IMG);
    }

    private void uploadUserImage() {
        databaseRef = FirebaseDatabase.getInstance().getInstance().getReference("uploads").child(MainActivity.get_user_id());
        if(UserImg_uri != null) {
            Toast.makeText(UserPage.this, "upload in progress", Toast.LENGTH_LONG).show();
            StorageReference fileRef = FirebaseStorage.getInstance().getReference("uploads").child(MainActivity.user_id).child("UserImage" + "."
                    + MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(UserImg_uri)));
            storageTask = fileRef.putFile(UserImg_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserPage.this, "upload successful", Toast.LENGTH_LONG).show();
                    ImageUpload imgUpload = new ImageUpload("UserImage", taskSnapshot.getUploadSessionUri().toString());
                    databaseRef.child("RestImage").setValue(imgUpload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserPage.this, "upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(UserPage.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(UserPage.this).setTitle("Permission needed").setMessage("This Permission is needed to upload files.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UserPage.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(UserPage.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
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

        switch(requestCode) {
            case PICK_REST_IMG:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    UserImg_uri = data.getData();
                    userImg.setImageURI(UserImg_uri);
                    uploadUserImage();
                }
                break;
            case 4:
                if(resultCode == RESULT_OK){
                    createRestButton.setVisibility(View.GONE);
                    userRestButton.setVisibility(View.VISIBLE);
                }
                break;
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