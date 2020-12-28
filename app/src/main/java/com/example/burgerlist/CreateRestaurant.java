package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
    private EditText phoneNum, RestName, RestLat, RestLng, RestAddras;
    private TextView Restcity;
    private Switch LocSwitch;
    private LatLng RestLocation = null;
    private FirebaseAuth mAuth;
    private String city;
    private Switch kosher_swt, vegan_swt, saturday_swt, vegetarian_swt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        phoneNum = (EditText)findViewById(R.id.phoneNum);
        RestName = (EditText)findViewById(R.id.RestName);
        Restcity = (TextView)findViewById(R.id.city_text);
        RestAddras =(EditText)findViewById(R.id.street_text);
        RestLat = (EditText)findViewById(R.id.RestLat);
        RestLng = (EditText)findViewById(R.id.RestLng);
        LocSwitch = (Switch) findViewById(R.id.LocSwitch);
        locationButton = (Button)findViewById(R.id.locationButton);
        createRestButton = (Button)findViewById(R.id.createRestButton);
        kosher_swt = (Switch)findViewById(R.id.kosher_switch_create);
        saturday_swt = (Switch)findViewById(R.id.saturday_switch_create);
        vegan_swt = (Switch)findViewById(R.id.vegan_switch_create);
        vegetarian_swt = (Switch)findViewById(R.id.vegetarian_switch_create);


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




        Restcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_search();
            }
        });

        createRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendRestToDB()){
                    returnToUserPage();
                }
                else{
                    Toast.makeText(getApplicationContext(), "we are having trouble processing your request please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void start_search(){
        Intent intent = new Intent(this, Search.class);
        intent.putExtra("FLAG",true);
        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Restcity.setText(data.getStringExtra("Search_Res"));
                city = data.getStringExtra("Search_Res");
            }
            if (resultCode == RESULT_CANCELED) {
                //might be usefull later
            }
        } else if(requestCode == requestRestLoc){
            if(resultCode == RESULT_OK){
                RestLocation = new LatLng(Double.parseDouble(data.getStringExtra("Lat")),
                        Double.parseDouble(data.getStringExtra("Lng")));
            }
            if (resultCode == RESULT_CANCELED) {
                //might be usefull later
            }
        }


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
        else if(city.length()<2 || city.length()>50){
            Toast.makeText(getApplicationContext(), "city name must be between 2 to 50 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Patterns.PHONE.matcher(phoneNum.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "Incorrect phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(RestLocation != null ){
            Toast.makeText(getApplicationContext(), "correct Location", Toast.LENGTH_SHORT).show();
        }
        else{
           try{
               RestLocation = new LatLng(Double.parseDouble(RestLat.getText().toString()),
                       Double.parseDouble(RestLng.getText().toString()));

           } catch (Exception e) {
               Toast.makeText(getApplicationContext(), "can't create location", Toast.LENGTH_SHORT).show();
               return false;
           }
        }

        String owner_id = MainActivity.get_user_id();
        Restaurant ress = new Restaurant(owner_id,RestName.getText().toString(),phoneNum.getText().toString(),RestLocation);



            FirebaseDatabase.getInstance().getReference("Restaurants").child(owner_id).setValue(ress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseDatabase.getInstance().getReference("Restaurants").child(owner_id).child("city").setValue(city);
                    FirebaseDatabase.getInstance().getReference("Restaurants").child(owner_id).child("filter").child("kosher").setValue(kosher_swt.isChecked());
                    FirebaseDatabase.getInstance().getReference("Restaurants").child(owner_id).child("filter").child("saturday").setValue(saturday_swt.isChecked());
                    FirebaseDatabase.getInstance().getReference("Restaurants").child(owner_id).child("filter").child("vegan").setValue(vegan_swt.isChecked());
                    FirebaseDatabase.getInstance().getReference("Restaurants").child(owner_id).child("filter").child("vegetarian").setValue(vegetarian_swt.isChecked());
                    Toast.makeText(getApplicationContext(), "successfully added restaurant to app", Toast.LENGTH_LONG).show();

                }
            }); // added Restaurant to restaurants branch
            FirebaseDatabase.getInstance().getReference("City").child(city).child(owner_id).setValue(ress).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void CreateRestLocation() {
        Toast.makeText(getApplicationContext(),"moving to get restaurant location", Toast.LENGTH_SHORT).show();
        Intent intentRestLoc = new Intent(this, getLocationActivity.class);
        startActivityForResult(intentRestLoc, requestRestLoc);
    }



}