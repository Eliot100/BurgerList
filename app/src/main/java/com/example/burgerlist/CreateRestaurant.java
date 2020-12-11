package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRestaurant extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    private static final int requestRestLoc = 100;
    private Button locationButton, createRestButton;
    private EditText phoneNum, RestName, RestLat, RestLng, Restcity, RestAddras;
    private Switch LocSwitch;
    private LatLng RestLocation;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        phoneNum = (EditText)findViewById(R.id.phoneNum);
        RestName = (EditText)findViewById(R.id.RestName);
        Restcity = (EditText)findViewById(R.id.City_text);
        RestAddras =(EditText)findViewById(R.id.street_text);
        RestLat = (EditText)findViewById(R.id.RestLat);
        RestLng = (EditText)findViewById(R.id.RestLng);
        LocSwitch = (Switch) findViewById(R.id.LocSwitch);
        locationButton = (Button)findViewById(R.id.locationButton);
        createRestButton = (Button)findViewById(R.id.createRestButton);


        // show or hide lat lon input text depending on user choise.
        LocSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!LocSwitch.isChecked()){
                    RestLng.setVisibility(View.VISIBLE);
                    RestLat.setVisibility(View.VISIBLE);
                    locationButton.setVisibility(View.INVISIBLE);
                } else {
                    RestLng.setVisibility(View.INVISIBLE);
                    RestLat.setVisibility(View.INVISIBLE);
                    locationButton.setVisibility(View.VISIBLE);
                }
            }
        });


        // to do
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRestLocation();
            }
        });


        createRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendRestToDB()){
                    returnToUserPage();
                }
                else{
                    Toast.makeText(getApplicationContext(), "we are having trouble processing your request please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private boolean isLegal(String restName) {
        return true; // for now all names are good
    }
        //Restaurant
    private boolean sendRestToDB() {
        if(RestName.getText().toString().length()<4 || RestName.getText().toString().length()>50){
            Toast.makeText(getApplicationContext(), "Restaurant name must be between 2 to 50 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Restcity.getText().toString().length()<2 || Restcity.getText().toString().length()>50){
            Toast.makeText(getApplicationContext(), "city name must be between 2 to 50 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Patterns.PHONE.matcher(phoneNum.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "Incorrect phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        String owner_id = MainActivity.get_user_id();
        LatLng latlng = new LatLng(150,150);// default currently does nothing.
        Restaurant ress = new Restaurant(owner_id,RestName.getText().toString(),phoneNum.getText().toString(),latlng);
        FirebaseDatabase.getInstance().getReference("Restaurants").child(owner_id).setValue(ress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "successfully added restaurant to app", Toast.LENGTH_LONG).show();


            }
        }); // added Restaurant to restaurants branch
        FirebaseDatabase.getInstance().getReference("City").child(Restcity.getText().toString()).child(owner_id).setValue(ress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }

        });//added Restaurant to cites branch
        FirebaseDatabase.getInstance().getReference("Users").child(MainActivity.user_id).child("restaurant_name").setValue(RestName.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        MainActivity.set_user_restaurant_name(RestName.getText().toString());
        return true;
    }

    private void returnToUserPage() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }













    // to do
    private void CreateRestLocation() {
//        Intent intentRestLoc = new Intent(this, GetMyRestaurantLocation.class);
//        try {
//            intentRestLoc.putExtra("currentLocation","150.0 150.0" );//currentLocation.toString()
//        } catch (Exception e){
//            Toast.makeText(getApplicationContext(),"can not set location", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Toast.makeText(getApplicationContext(),"moving to get restaurant location", Toast.LENGTH_SHORT).show();
//        startActivityForResult(intentRestLoc, requestRestLoc);
    }



}