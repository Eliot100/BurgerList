package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

// https://www.youtube.com/watch?v=lPfQN-Sfnjw&ab_channel=CodinginFlow
public class ResturantUpdatesActivity extends AppCompatActivity {
    private ImageButton return_btn;
    private static final int PICK_REST_IMG = 1;
    private Button rest_imgBtn;
    private ImageView restImg;
    private ProgressBar RestImg_progressBar;

    private Uri RestImg_uri;
    private FirebaseDatabase database;
    private StorageReference storageRef;
    private StorageTask storageTask;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_updates);
        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("uploads").child(MainActivity.user_id);
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        Return();
        UplodeRestImg();
    }

    private void UplodeRestImg() {
        rest_imgBtn = findViewById(R.id.rest_imgBtn);
        RestImg_progressBar = findViewById(R.id.RestImg_progressBar);
        restImg = findViewById(R.id.restImg);

        rest_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask != null && storageTask.isInProgress()){
                    Toast.makeText(ResturantUpdatesActivity.this, "upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    openImage();
                    uploadImage();
                }
            }
        });
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
                    ImageUpload imgUpload = new ImageUpload("RestImage", taskSnapshot.getUploadSessionUri().toString());
                    String uploadId = databaseRef.push().getKey();
                    databaseRef.child(uploadId).setValue(imgUpload);
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



    private void openImage() {
        Intent i = new Intent();
        i.setType("image/*");
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
                }
                break;
        }

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