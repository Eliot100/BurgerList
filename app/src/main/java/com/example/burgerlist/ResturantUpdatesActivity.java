package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

// https://www.youtube.com/watch?v=lPfQN-Sfnjw&ab_channel=CodinginFlow
public class ResturantUpdatesActivity extends AppCompatActivity {
    private ImageButton return_btn;
    private static final int PICK_REST_IMG = 1;
    private Button rest_imgBtn, addDish_btn, removeDish_btn;
    private ImageView restImg;
    private ProgressBar RestImg_progressBar;
    private EditText category_text, dishName_text, dishDescription_text, price_text;
    private ArrayList<Dish> rest_menu;
    private MenuListAdapter menuAdapter;
    private ListView menuUpdate_list;

    private Uri RestImg_uri;
    private FirebaseDatabase database;
    private StorageReference storageRef;
    private StorageTask storageTask;
    private DatabaseReference databaseRef;
    public DatabaseReference refMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_updates);
        storageRef = FirebaseStorage.getInstance().getReference("uploads").child(MainActivity.user_id);
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getInstance().getReference("uploads").child(MainActivity.get_user_id());
        refMenu = database.getReference("Restaurants").child(MainActivity.get_user_id()).child("Menu Category");

        addRestImg();
        SetMenuVariables();
        addDish();
        removeDish();
        get_menu();
        Return();
    }


    private void SetMenuVariables() {
        menuUpdate_list = findViewById(R.id.menuUpdate_list);
        category_text = findViewById(R.id.category_text);
        dishName_text = findViewById(R.id.dishName_text);
        price_text = findViewById(R.id.price_text);
        dishDescription_text = findViewById(R.id.dishDescription_text);
        rest_menu = new ArrayList<Dish>();
        menuAdapter = new MenuListAdapter(this, R.layout.adapter_res_layout, rest_menu);
        menuUpdate_list.setAdapter(menuAdapter);
    }

    private void addDish() {
        addDish_btn  = findViewById(R.id.addDish_btn);
        addDish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category_text.getText().toString().equals("") ){
                    Toast.makeText(ResturantUpdatesActivity.this, "Enter category", Toast.LENGTH_LONG).show();
                }
                else if (dishName_text.getText().toString().equals("")){
                    Toast.makeText(ResturantUpdatesActivity.this, "Enter dish name", Toast.LENGTH_LONG).show();
                }
                else if (price_text.getText().toString().equals("")){
                    Toast.makeText(ResturantUpdatesActivity.this, "Enter dish price", Toast.LENGTH_LONG).show();
                }
                else if ( dishDescription_text.getText().toString().equals("")){
                    Toast.makeText(ResturantUpdatesActivity.this, "Enter dish description", Toast.LENGTH_LONG).show();
                }
                else {
                    addDishToDB();
                    claerEditTexts();
                }
            }
        });
    }

    private void removeDish() {
        removeDish_btn = findViewById(R.id.removeDish_btn);
        removeDish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category_text.getText().toString().equals("") ){
                    Toast.makeText(ResturantUpdatesActivity.this, "Enter category", Toast.LENGTH_LONG).show();
                }
                else if (dishName_text.getText().toString().equals("")){
                    Toast.makeText(ResturantUpdatesActivity.this, "Enter dish name", Toast.LENGTH_LONG).show();
                }
                else {
                    refMenu.child(category_text.getText().toString()).child(category_text.getText().toString()).setValue(null);
                    claerEditTexts();
                }
            }
        });

    }

    private void addDishToDB() {
        Dish dish = new Dish(dishName_text.getText().toString(), dishDescription_text.getText().toString(), price_text.getText().toString());
//        String ItemKey = refMenu.child(category_text.getText().toString()).push().getKey();
        refMenu.child(category_text.getText().toString()).child(dish.name).setValue(dish).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Dish added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void claerEditTexts() {
        category_text.setText("");
        dishName_text.setText("");
        dishDescription_text.setText("");
        price_text.setText("");
    }

    public void get_menu(){
        DatabaseReference comref = FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(MainActivity.user_id).child("Menu Category");
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
                displayMenu();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void displayMenu() {
        menuAdapter = new MenuListAdapter(this, R.layout.adapter_menu_product, rest_menu);
        menuUpdate_list.setAdapter(menuAdapter);
    }

    private void addRestImg() {
        restImg = findViewById(R.id.restImg);
        rest_imgBtn = findViewById(R.id.rest_imgBtn);
        RestImg_progressBar = findViewById(R.id.RestImg_progressBar);

        rest_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask != null && storageTask.isInProgress()){
                    Toast.makeText(ResturantUpdatesActivity.this, "upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    openImage();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICK_REST_IMG:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    RestImg_uri = data.getData();
                    restImg.setImageURI(RestImg_uri);
                    uploadImage();
                }
                break;
        }
    }

    private void uploadImage() {
        if(RestImg_uri != null){
            StorageReference fileRef = storageRef.child("RestImage"+"."+getImageFileExtension(RestImg_uri));
            storageTask = fileRef.putFile(RestImg_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RestImg_progressBar.setProgress(0);
                        }
                    }, 5000);
                    Toast.makeText(ResturantUpdatesActivity.this, "upload successful", Toast.LENGTH_LONG).show();
                    ImageUpload imgUpload = new ImageUpload("RestImage", ""
                            + taskSnapshot.getUploadSessionUri().toString());
                    databaseRef.child("RestImage").setValue(imgUpload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ResturantUpdatesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            RestImg_progressBar.setProgress((int) progress);
                        }
                    });

        } else {
            Toast.makeText(ResturantUpdatesActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImageFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void Return() {
        return_btn = (ImageButton) findViewById(R.id.return_btn);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}