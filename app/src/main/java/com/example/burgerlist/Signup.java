package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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

        mAuth = FirebaseAuth.getInstance();

        //Assigning listeners


        //When sign up button is pressed , will check if passwords match and above 5 in length , will check that username is above 4 chars.
        //and will check that the emial in correct format.
        // if all true will send to Sign_up_user function.
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
                    Toast.makeText(Signup.this, "Passwords don't match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // adds new user to database.
    private void Sign_up_user(String Email,String Password,boolean Owner ,String Username){
        setProgressBarVisibility(true);//do later
        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user =  new User (Email,Password,Username,Owner);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Signup.this, "You have been registered", Toast.LENGTH_SHORT).show();
                                                Go_to_Login();
                                                }
                                            else{
                                                Toast.makeText(Signup.this, "failed to registered",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(Signup.this, "failed to registered", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void Go_to_Login(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}