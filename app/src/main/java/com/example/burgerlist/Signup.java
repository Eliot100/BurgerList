package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    // Variable declaration
    private EditText Email;
    private EditText Password1;
    private EditText Password2;
    private Button Sign_up;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Assigning object ids to variables
        Email = (EditText)findViewById(R.id.Email_text);
        Password1 = (EditText)findViewById(R.id.Password_text1);
        Password2 = (EditText)findViewById(R.id.Password_text2);
        Sign_up = (Button)findViewById(R.id.Sign_up_btn);

        mAuth = FirebaseAuth.getInstance();


        //Assigning listeners
        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}