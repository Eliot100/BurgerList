package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Variable declaration


public class Login extends AppCompatActivity {

    // Variable declaration
    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView Sign_up;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assigning object ids to variables
        Name = (EditText)findViewById(R.id.Username_text);
        Password = (EditText)findViewById(R.id.Password_text);
        Login = (Button)findViewById(R.id.Login_account);
        Sign_up = (TextView)findViewById(R.id.Signup_text);
        mAuth = FirebaseAuth.getInstance();

        //Assigning listeners
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate((String) Name.getText().toString(),Password.getText().toString());
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up_screen();
            }
        });
    }

    //This function connects registered users to the database.
    private void validate(String email, String password){
        
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Logged in", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sign_up_screen(){
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);

    }
}