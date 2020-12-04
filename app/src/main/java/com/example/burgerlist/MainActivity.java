package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button login_button;
    Button userListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button)findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_Login();
            }
        });

        userListButton = (Button)findViewById(R.id.userListButton);

        userListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_ListPage();
            }
        });

    }

    private void start_Login() {
        Intent intent = new Intent(this, Login.class);
        Toast.makeText(getApplicationContext(),"ke1", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void start_ListPage() {
        Intent intent = new Intent(this, ListPage.class);
        Toast.makeText(getApplicationContext(),"ke2", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


}