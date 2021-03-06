package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyStore;

// Variable declaration


public class Login extends AppCompatActivity {

    // Variable declaration
    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView Sign_up;
    private DatabaseReference mDatabase;
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
                if(Name.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"Enter username", Toast.LENGTH_SHORT).show();
                }
                else if(Password.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"Enter password", Toast.LENGTH_SHORT).show();
                }
                else{
                    validate((String) Name.getText().toString(),Password.getText().toString());
                }
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
                            Go_to_main(user);
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sign_up_screen(){
        Intent intent = new Intent(this, Signup.class);
        startActivityForResult(intent,3);

    }

    private void Go_to_main(FirebaseUser user){
        Intent intent = new Intent();
        intent.putExtra("ISLOGGEDIN",true);
        intent.putExtra("USER_ID",user.getUid());
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3) {
            if(resultCode == RESULT_OK){
                //might be usefull later
            }
            if (resultCode == RESULT_CANCELED) {
                //might be usefull later
            }
        }
    }



}