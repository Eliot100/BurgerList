package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    Button login_button;
    Button userPageButton;
    Button search_button;
    TextView welome_user;
    static String user_id = "";
    static String user_name="";
    static String user_restaurant_name ;
    static boolean isowner ;
    static boolean isloggedin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button)findViewById(R.id.login_button);
        userPageButton = (Button)findViewById(R.id.userPageButton);
        search_button =(Button)findViewById(R.id.search_button);
        welome_user = (TextView)findViewById(R.id.user_welcom_text);


        welome_user.setVisibility(View.GONE);
        login_button.setVisibility(View.GONE);
        userPageButton.setVisibility(View.GONE);
        check_loggedin();

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

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { start_Search();}
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                    user_id = data.getStringExtra("USER_ID");
                    isloggedin = data.getBooleanExtra("ISLOGGEDIN",false);
                    check_loggedin();
            }
            if (resultCode == RESULT_CANCELED) {
                //might be usefull later
            }
        }
        else if (requestCode == 5){
            if(resultCode == RESULT_OK){
                // search resault here
            }

        }
    }


    private void check_loggedin(){
        // if logged in get username from db
        if(isloggedin == true){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   user_name = snapshot.child("username").getValue().toString();
                   isowner = (boolean)snapshot.child("owner").getValue();
                   user_restaurant_name = snapshot.child("restaurant_name").getValue().toString();
                   login_button.setVisibility(View.GONE);
                   welome_user.setText("Welcome "+user_name);
                   welome_user.setVisibility(View.VISIBLE);
                   userPageButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            login_button.setVisibility(View.VISIBLE);
        }
    }



    private void start_Login() {
        Intent intent = new Intent(this, Login.class);
        startActivityForResult(intent,1);

    }

    private void start_MyPage() {
        Intent intent = new Intent(this, UserPage.class);
        startActivity(intent);

    }

    private void start_Search(){
        Intent intent = new Intent(this, Search.class);
        startActivityForResult(intent,5);

    }

    public static String get_user_id(){
        return user_id;
    }
    public static String get_user_name(){
        return user_name;
    }
    public static boolean get_isowner(){
        return isowner;
    }

    public static boolean get_isloggedin() {
        return isloggedin;
    }
    public static String get_user_restaurant_name(){
        return user_restaurant_name;
    }
    public static void set_user_restaurant_name(String ress_name){
        user_restaurant_name = ress_name;
    }

}