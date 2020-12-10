package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class CreateRestaurant extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    private static final int requestRestLoc = 100;
    Button locationButton, createRestButton;
    EditText phoneNum, RestName;
    private Location currentLocation, RestLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        locationButton = (Button)findViewById(R.id.locationButton);
        createRestButton = (Button)findViewById(R.id.createRestButton);
        phoneNum = (EditText)findViewById(R.id.phoneNum);
        RestName = (EditText)findViewById(R.id.RestName);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent locationSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(locationSettings);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, REQUEST_CODE);
                Toast.makeText(getApplicationContext(), "Location Update failed 1", Toast.LENGTH_SHORT).show();
                return;
            }
//            onRequestPermissionsResult();
            Toast.makeText(getApplicationContext(), "Location Update failed 0", Toast.LENGTH_SHORT).show();
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            Toast.makeText(getApplicationContext(), "request Location Update", Toast.LENGTH_SHORT).show();
        }


        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRestLocation();
            }
        });

        createRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RestaurantIsReady()) {
                    SetRestaurant();
                    Toast.makeText(getApplicationContext(), "Set Restaurant in data base", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean RestaurantIsReady() {
        try {
            Integer.parseInt(phoneNum.getText().toString());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "cant parse Int", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(RestLocation == null ) {
            Toast.makeText(getApplicationContext(), "restaurant Location = null", Toast.LENGTH_SHORT).show();
            return false;
        } else if(isLegal(RestName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "restaurant variables isn't right", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isLegal(String restName) {
        return true; // for now all names are good
    }

    private void SetRestaurant() {
//        mAuth.createRestaurantWithUserId(RestaurantId RestaurantId)
//             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        Restaurant rest =  new Restaurant(RestName.getText().toString(),
//                            phoneNum.getText().toString(), RestLocation.toString());
//                        FirebaseDatabase.getInstance().getReference("Restaurants")
//                            .child(FirebaseAuth.getInstance().getCurrentRestaurant().getUid())
//                            .setValue(rest).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(CreateRestaurant.this,
//                                                "The restaurant Created.",
//                                                Toast.LENGTH_SHORT).show();
//
//                                        returnToUserPage();
//                                    }
//                                    else {
//                                        Toast.makeText(CreateRestaurant.this,
//                                                "failed to Created restaurant.",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                    }
//                    else {
//                        Toast.makeText(CreateRestaurant.this, "failed to create the restaurant", Toast.LENGTH_SHORT).show();
//                    }
//                }
//             });
        returnToUserPage();
    }

    private void returnToUserPage() {
        Intent resultIntent = new Intent(this, UserPage.class);
//        resultIntent.putExtra("RestID", "RestId");
        setResult(RESULT_CANCELED, resultIntent);
        Toast.makeText(getApplicationContext(),"finish create restaurant", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void CreateRestLocation() {
        Intent intentRestLoc = new Intent(this, GetMyRestaurantLocation.class);
        try {
            intentRestLoc.putExtra("currentLocation","150.0 150.0" );//currentLocation.toString()
        } catch (Exception e){
            Toast.makeText(getApplicationContext(),"can not set location", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(),"moving to get restaurant location", Toast.LENGTH_SHORT).show();
        startActivityForResult(intentRestLoc, requestRestLoc);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "onActivityResult : Getting rest loc", Toast.LENGTH_SHORT).show();
        switch (requestCode) {
//        if(requestCode == 1){
//            if(requestCode == RESULT_OK){
//                RestLocation = new Location(data.getStringExtra("RestLocation"));
//                Toast.makeText(getApplicationContext(),"The location is set", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
            case requestRestLoc:
                if (requestCode == this.RESULT_OK) {
                    RestLocation = new Location(data.getStringExtra("RestLocation"));
                    Toast.makeText(getApplicationContext(), "RestLocation: " + RestLocation.getLatitude()
                            + "" + RestLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        //error
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "fetch Last Location", Toast.LENGTH_SHORT).show();
                    fetchLastLocation();
                }
                break;
        }
    }


    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()
                            +""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}