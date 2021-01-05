package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private final int LOCATION_REQUEST_CODE = 10;
    private final int LOCATION_PERNISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    Button login_button, logout_button;
    Button userPageButton, search_button;
    TextView welome_user;
    static String user_id = "";
    static String user_name = "guest";
    static String user_restaurant_name;
    static boolean isowner;
    static boolean isloggedin = false;
    private RecyclerView trending_view;
    private SearchAdapter searchAdapter, searchAdapter2;
    private ArrayList<String> rest_id_list;
    private ArrayList<String> rest_name_list;
    private ArrayList<String> rating_list;
    private ArrayList<String> rest_logo_list;
    private ArrayList<String> city_list;
    private ArrayList<String> distance_list;
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private MapFragment mapFragment;
    // defaul location for user if gps iskill
    // tel aviv כיכר המדינה
    double defult_lat = 32.086619;
    double defult_lon = 34.789621;
    protected static final int MapDiffZoom = 10, MapContryZoom = 6;
    private boolean locationPermissionGranted = false;
    private RecyclerView Loc_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            setMap();
        } catch (Exception e){

        }
        try {
        } catch (Exception e){

        }


        login_button = (Button) findViewById(R.id.login_button);
        logout_button = (Button) findViewById(R.id.logout_button);
        userPageButton = (Button) findViewById(R.id.userPageButton);
        search_button = (Button) findViewById(R.id.search_button);
        welome_user = (TextView) findViewById(R.id.user_welcom_text);
        trending_view = (RecyclerView)findViewById(R.id.trending_recycle_view);

        rest_name_list =  new ArrayList<String>();
        rating_list =  new ArrayList<String>();
        rest_logo_list =  new ArrayList<String>();
        rest_id_list = new ArrayList<String>();
        city_list = new ArrayList<String>();
        distance_list = new ArrayList<String>();


        welome_user.setText("Welcome " + user_name);
        welome_user.setVisibility(View.VISIBLE);
        login_button.setVisibility(View.GONE);
        userPageButton.setVisibility(View.GONE);
        check_loggedin();


        //setting up recycle view
        trending_view.setHasFixedSize(true);
        trending_view.setLayoutManager(new LinearLayoutManager(this));
        trending_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        update_trending();

        // listeners
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_Login();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = "";
                user_name = "guest";
                welome_user.setText("Welcome " + user_name);
                user_restaurant_name = "";
                login_button.setVisibility(View.VISIBLE);
                logout_button.setVisibility(View.GONE);
                userPageButton.setVisibility(View.GONE);
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
            public void onClick(View v) {
                start_Search();
            }
        });
    }

    private void setMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission();

        Loc_view = (RecyclerView)findViewById(R.id.Loc_view);
        Loc_view.setHasFixedSize(true);
        Loc_view.setLayoutManager(new LinearLayoutManager(this));
        Loc_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng jerusalem  = new LatLng(31.7683, 35.2137);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(jerusalem).zoom(MapContryZoom).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                if(locationPermissionGranted) {
                    try {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            enableUserLocation();
                            zoomToUserLoc();
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                addMarkers();
                addRestByLocation();
            }
        });
    }

    private void addRestByLocation() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng pos = marker.getPosition();
                FirebaseDatabase.getInstance().getReference("Restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for( DataSnapshot snap: snapshot.getChildren()){
                            String restname = snap.child("name").getValue().toString();
                            String uid = snap.child("ownerId").getValue().toString();
                            String rating = snap.child("currentRating").getValue().toString();
                            String city = snap.child("city").getValue().toString();
                            String rest_lat = snap.child("location").child("latitude").getValue().toString();
                            String rest_lon = snap.child("location").child("longitude").getValue().toString();
                            LatLng rest_Loc = new LatLng(Double.parseDouble(rest_lat),Double.parseDouble(rest_lon));

                            DecimalFormat df = new DecimalFormat("#.##");
                            double d = distance(defult_lat, rest_Loc.latitude, defult_lon, rest_Loc.longitude,0,0)/1000;
                            String distance = String.valueOf((df.format(d)));

                            if(pos.equals(rest_Loc)){
                                searchAdapter2 = new SearchAdapter(MainActivity.this, ArrayListWithStr(uid), ArrayListWithStr(restname),
                                        ArrayListWithStr(rating), ArrayListWithStr(city), ArrayListWithStr(distance), MainActivity.this::onRestClick);
                                Loc_view.setAdapter(searchAdapter2);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return false;
            }
        });
    }

    private void addMarkers() {
        try{
            ref = FirebaseDatabase.getInstance().getReference("Restaurants");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mMap.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("name").getValue().toString();
                        String rating = snapshot.child("currentRating").getValue().toString();
                        String latitude = (String) snapshot.child("location").child("latitude").getValue().toString();
                        String longitude = (String) snapshot.child("location").child("longitude").getValue().toString();
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                        markerOptions.title(name);
                        mMap.addMarker(markerOptions);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }catch ( Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true;
            Toast.makeText(MainActivity.this, "PermissionGranted",Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERNISSION_REQUEST_CODE);
        }
    }

    private void update_trending() {
        try{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Restaurants").orderByChild("currentRating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int current = 0;
                    boolean deleted = false;
                    rest_logo_list.clear();
                    rest_name_list.clear();
                    rating_list.clear();
                    city_list.clear();
                    distance_list.clear();
                    trending_view.removeAllViews();

                    for( DataSnapshot snap: snapshot.getChildren()){
                        deleted = false;
                        String restname = snap.child("name").getValue().toString();
                        String uid = snap.child("ownerId").getValue().toString();
                        String rating = snap.child("currentRating").getValue().toString();
                        String city = snap.child("city").getValue().toString();
                        String rest_lat = snap.child("location").child("latitude").getValue().toString();
                        String rest_lon = snap.child("location").child("longitude").getValue().toString();

                        //calculating distance from user to restaurant
                        DecimalFormat df = new DecimalFormat("#.##");
                        double d = distance(defult_lat, Double.parseDouble(rest_lat), defult_lon, Double.parseDouble(rest_lon),0,0)/1000;
                        String distance = String.valueOf((df.format(d)));

                        //String logo = snap.child("logo").getValue().toString(); //logo when storage ready

                        rest_name_list.add(restname);
                        rest_id_list.add(uid);
                        rating_list.add(rating);
                        city_list.add(city);
                        distance_list.add(distance);
                        //rest_logo_list.add(logo);   //logo when storage ready

                        current++;
                        if(current == 10){
                            break;
                        }
                    }
                    Collections.swap(rest_name_list,0,rest_name_list.size()-1);
                    Collections.swap(rest_id_list,0,rest_id_list.size()-1);
                    Collections.swap(rating_list,0,rating_list.size()-1);
                    Collections.swap(city_list,0,city_list.size()-1);
                    Collections.swap(distance_list,0,distance_list.size()-1);
                    searchAdapter = new SearchAdapter(MainActivity.this , rest_id_list ,rest_name_list ,city_list,rating_list, distance_list , MainActivity.this::onRestClick);
                    trending_view.setAdapter(searchAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e ){  }
    }

    private void onRestClick(int position) {
        Intent intent = new Intent(this, RestPage.class);
        intent.putExtra("Owner_id", rest_id_list.get(position));
        startActivityForResult(intent,7);
    }

    private ArrayList<String> ArrayListWithStr(String str){
        ArrayList a = new ArrayList<String>();
        a.add(str);
        return a;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                user_id = data.getStringExtra("USER_ID");
                isloggedin = data.getBooleanExtra("ISLOGGEDIN", false);
                check_loggedin();
            }
            if (resultCode == RESULT_CANCELED) {
                //might be usefull later
            }
        } else if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                // search resault here
            }
        }
    }

    private void check_loggedin() {
        // if logged in get username from db
        if (get_isloggedin()) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user_name = snapshot.child("username").getValue().toString();
                    isowner = (boolean) snapshot.child("owner").getValue();
                    user_restaurant_name = snapshot.child("restaurant_name").getValue().toString();
                    login_button.setVisibility(View.GONE);
                    logout_button.setVisibility(View.VISIBLE);
                    welome_user.setText("Welcome " + user_name);
                    welome_user.setVisibility(View.VISIBLE);
                    userPageButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            login_button.setVisibility(View.VISIBLE);
        }
    }


    private void start_Login() {
        Intent intent = new Intent(this, Login.class);
        startActivityForResult(intent, 1);

    }

    private void start_MyPage() {
        Intent intent = new Intent(this, UserPage.class);
        startActivity(intent);

    }

    private void start_Search() {
        Intent intent = new Intent(this, SearchFilter.class);
        startActivityForResult(intent, 5);

    }

    public static String get_user_id() {
        return user_id;
    }

    public static String get_user_name() {
        return user_name;
    }

    public static boolean get_isowner() {
        return isowner;
    }

    public static boolean get_isloggedin() {
        return isloggedin;
    }

    public static String get_user_restaurant_name() {
        return user_restaurant_name;
    }

    public static void set_user_restaurant_name(String ress_name) {
        user_restaurant_name = ress_name;
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapContryZoom));
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case LOCATION_PERNISSION_REQUEST_CODE:{
//                if(grantResults.length > 0 ){
//                    for(int j = 0; j < grantResults.length; j++){
//                        if(grantResults[j] != PackageManager.PERMISSION_GRANTED){
//                            locationPermissionGranted = false;
//                            return;
//                        }
//                        locationPermissionGranted = true;
//                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//                    }
//                }
//            }
//        }
//    }


    // code take from https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
    // code used only to calculate distance given 2 sets of lat and lon points
    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {
        el1 = 0;
        el2 = 0;
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}