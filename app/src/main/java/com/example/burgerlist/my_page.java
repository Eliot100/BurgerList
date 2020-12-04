package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class my_page extends AppCompatActivity {
    Button userListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);


        try{
            String id = getIntent().getStringExtra("USER_ID");
            Toast.makeText(getApplicationContext(),id, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }





        userListButton = (Button)findViewById(R.id.userListButton);

        userListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_ListPage();
            }
        });
    }



    private void start_ListPage() {
        Intent intent = new Intent(this, ListPage.class);
        Toast.makeText(getApplicationContext(),"ke2", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}