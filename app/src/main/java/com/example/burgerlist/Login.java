package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

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
                Toast.makeText(getApplicationContext(),"ke2", Toast.LENGTH_SHORT).show();
                validate(Name.toString(),Password.toString());
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"kek", Toast.LENGTH_SHORT).show();
                sign_up_screen();
            }
        });
    }

    //This function checks if the username and password are correct
    private void validate(String username, String password){

    }

    private void sign_up_screen(){

    }
}