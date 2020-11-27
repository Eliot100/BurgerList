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
                if(Password1.getText().toString().equals(Password2.getText().toString())){
                        Sign_up_user(Email.getText().toString(),Password1.getText().toString());
                }
                else{
                    Toast.makeText(Signup.this, "Passwords dont match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void Sign_up_user(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Signup.this, "Your account has been registered",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }



}