package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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
    private EditText Username;
    private Switch Owner;
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
        Username = (EditText)findViewById(R.id.Username_text);
        Owner = (Switch)findViewById(R.id.Owner_switch);
        Sign_up = (Button)findViewById(R.id.Sign_up_btn);




        //Assigning listeners
        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Password1.getText().toString().equals(Password2.getText().toString())){
                        if(Password1.getText().toString().length()>=6){
                            if(Username.getText().toString().length()>=4){
                                if(Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()){
                                    Sign_up_user(Email.getText().toString(),Password1.getText().toString(),Owner.isChecked(),Username.getText().toString());
                                }
                                else{
                                    Toast.makeText(Signup.this, "Incorrect email format", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(Signup.this, "Username needs to be at least 4 characters", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else{
                            Toast.makeText(Signup.this, "Password needs to be at least 6 characters.", Toast.LENGTH_SHORT).show();
                        }
                }
                else{
                    Toast.makeText(Signup.this, "Passwords dont match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void Sign_up_user(String Email,String Password,boolean Owner ,String Username){



    }



}