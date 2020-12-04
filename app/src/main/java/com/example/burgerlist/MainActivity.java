package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button login_button;
    Button userPageButton;
    String user_id;
    boolean isloggedin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button)findViewById(R.id.login_button);
        userPageButton = (Button)findViewById(R.id.userListButton);


        try{
            user_id = getIntent().getStringExtra("USER_ID");
            Toast.makeText(getApplicationContext(),user_id, Toast.LENGTH_LONG).show();
            isloggedin = true;

        } catch (Exception e) {
                e.printStackTrace();
        }



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_Login();
            }
        });

        userPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_MyPage();
            }
        });

    }

    private void start_Login() {
        Intent intent = new Intent(this, Login.class);
        Toast.makeText(getApplicationContext(),"login", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void start_MyPage() {
        Intent intent = new Intent(this, my_page.class);
        intent.putExtra("ISLOGGEDIN",isloggedin);
        intent.putExtra("USER_ID",user_id);
        Toast.makeText(getApplicationContext(), "my page", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }
}