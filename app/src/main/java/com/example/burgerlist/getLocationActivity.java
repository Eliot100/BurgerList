package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class getLocationActivity extends AppCompatActivity {
    GoogleMap googleMap;
    Button setRestLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, mapFragment)
                .commit();

        setRestLoc = (Button)findViewById(R.id.buttonRestLoc);
        setRestLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLocation(mapFragment);
            }
        });
    }

    private void returnLocation(MapFragment mapFragment) {
        LatLng finalLocation = mapFragment.getLoc();
        if( finalLocation != null){
            Intent intent = new Intent();
            intent.putExtra("Lat", finalLocation.latitude+"");
            intent.putExtra("Lng",finalLocation.longitude+"");
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            return;
        }
    }
}