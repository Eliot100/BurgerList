package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    Button login_button;
    Button userPageButton;
    TextView welome_user;
    String user_id = "";
    String user_name="";
    boolean isloggedin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button)findViewById(R.id.login_button);
        userPageButton = (Button)findViewById(R.id.userPageButton);
        welome_user = (TextView)findViewById(R.id.user_welcom_text);


        welome_user.setVisibility(View.GONE);

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

        check_loggedin();

        //if user logged in
        if(isloggedin == true){
            login_button.setVisibility(View.GONE);
            welome_user.setText("Welcome "+user_name);
            welome_user.setVisibility(View.VISIBLE);
        }

    }

    private void check_loggedin(){
        Intent intent = getIntent();
        user_id = intent.getStringExtra("USER_ID");
        isloggedin = intent.getBooleanExtra("ISLOGGEDIN",false);



        if(isloggedin == true){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(getApplicationContext(),snapshot.child("username").getValue().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }









    }

    private void start_Login() {
        Intent intent = new Intent(this, Login.class);
        Toast.makeText(getApplicationContext(),"login", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void start_MyPage() {
        Intent intent = new Intent(this, UserPage.class);
        Toast.makeText(getApplicationContext(),"moving to User page", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }
}