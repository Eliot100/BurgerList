package com.example.burgerlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserPage extends AppCompatActivity {
    Button userListButton;
    Button userRestButton;
    Button CreateRestButton;
    Restaurant UserRest;
    private static final int LAUNCHE_CREATE_REST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        userListButton = (Button)findViewById(R.id.userListButton);
        userRestButton = (Button)findViewById(R.id.userRestButton);
        CreateRestButton = (Button)findViewById(R.id.CreateRestButton);

        userListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_ListPage();
            }
        });

        userRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_RestPage();
            }
        });

        CreateRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_RestPage();
            }
        });
    }

    private void start_ListPage() {
        Intent intent = new Intent(this, ListPage.class);
        Toast.makeText(getApplicationContext(),"ke2", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void start_RestPage() {
        Intent intent = new Intent(this, RestPage.class);
        Toast.makeText(getApplicationContext(),"ke2", Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, LAUNCHE_CREATE_REST);
    }

    private void Create_RestPage() {
        Intent intent = new Intent(this, CreateRestaurant.class);
        Toast.makeText(getApplicationContext(),"moving to restaurant page", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LAUNCHE_CREATE_REST){
            if(requestCode == RESULT_OK){
//                UserRest = data.
                return;
            }
        }
        //error
    }
}