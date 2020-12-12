package com.example.burgerlist;

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
        setContentView(R.layout.activity_user_page);

        userListButton = (Button)findViewById(R.id.userListButton);
        userRestButton = (Button)findViewById(R.id.MyRessButton);
        CreateRestButton = (Button)findViewById(R.id.CreateRestButton);

        CreateRestButton.setVisibility(View.GONE);
        userRestButton.setVisibility(View.GONE);

        update_ui();

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
        startActivityForResult(intent, 2);
    }

    private void Create_RestPage() {
        Intent intent = new Intent(this, CreateRestaurant.class);
        Toast.makeText(getApplicationContext(),"moving to create restaurant page", Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4) {
            if(resultCode == RESULT_OK){
                CreateRestButton.setVisibility(View.GONE);
                userRestButton.setVisibility(View.VISIBLE);
            }
            if (resultCode == RESULT_CANCELED) {
                //might be usefull later
            }
        }
    }

    private void update_ui(){
        if(MainActivity.get_isowner() == true){
            if(MainActivity.get_user_restaurant_name().equals("0") == true) {
                CreateRestButton.setVisibility(View.VISIBLE);
            }
            else {// owner with a restaurant
                userRestButton.setVisibility(View.VISIBLE);
            }
        }
        else{
            CreateRestButton.setVisibility(View.GONE);
        }

    }
}