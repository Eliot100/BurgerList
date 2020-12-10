package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import kotlin.contracts.Returns;

public class GetMyRestaurantLocation extends FragmentActivity implements OnMapReadyCallback {
    Button btnSetLocation;
    Location currentLocation = new Location("0.0 0.0");
    private static final int REQUEST_CODE = 101;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_my_location);

        btnSetLocation = (Button) findViewById(R.id.btnSetLocation);

        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Return_Location();
            }
        });
    }



    private void Return_Location() {
        Intent resultIntent = new Intent(this, CreateRestaurant.class);
        resultIntent.putExtra("RestLocation", currentLocation.toString());
        Toast.makeText(getApplicationContext(), "restaurant location: "+currentLocation.getLatitude()+" "+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        setResult( RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Location.");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == 1){
            if(requestCode == RESULT_OK){
                currentLocation = new Location(data.getExtras().getString("currentLocation"));
                Toast.makeText(getApplicationContext(), "Restaurant location: "+currentLocation.getLatitude()+" "+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                return;
            }
//        }
        //error
    }

}