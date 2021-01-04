package com.example.burgerlist;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private final int LOCATION_REQUEST_CODE = 10;
    private final int LOCATION_PERNISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    Button login_button, logout_button;
    Button userPageButton;
    Button search_button;
    TextView welome_user;
    static String user_id = "";
    static String user_name = "guest";
    static String user_restaurant_name;
    static boolean isowner;
    static boolean isloggedin = false;
    private RecyclerView trending_view;
    private SearchAdapter searchAdapter;
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
    // defaul location for user if gps iskill
    // tel aviv כיכר המדינה
    double defult_lat = 32.086619;
    double defult_lon = 34.789621;
    protected static final int MapDiffZoom = 10;
    private boolean locationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setGoogleMapPermission();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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

    private void InitMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERNISSION_REQUEST_CODE);
            }
        }
    }

    private void update_trending() {


        try{
            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference();
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
        catch (Exception e ){

        }

    }

    private void onRestClick(int position) {
        Intent intent = new Intent(this, RestPage.class);
        intent.putExtra("Owner_id", rest_id_list.get(position));
        startActivityForResult(intent,7);
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
        if (isloggedin == true) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready",Toast.LENGTH_LONG).show();
        getLocationPermission();
        this.mMap = googleMap;
        LatLng RamatGan = new LatLng(32.080799, 34.794508);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(RamatGan).zoom(MapDiffZoom).build();
        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLoc();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        } catch (Exception e) {

        }
            try{
                database = FirebaseDatabase.getInstance();
                ref = database.getReference("Restaurants");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        googleMap.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name = (String) snapshot.child("name").getValue().toString();
                            String latitude = (String) snapshot.child("location").child("latitude").getValue().toString();
                            String longitude = (String) snapshot.child("location").child("longitude").getValue().toString();
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                            markerOptions.title(name);
                            markerOptions.snippet("ok");
                            googleMap.addMarker(markerOptions);
                            //                    markerOptions.draggable(true);
                            //                    Marker locationMarker = googleMap.addMarker(markerOptions);
                            //                    locationMarker.setDraggable(true);
                            //                    locationMarker.showInfoWindow();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }catch ( Exception e){

            }

    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapDiffZoom));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERNISSION_REQUEST_CODE:{
                if(grantResults.length > 0 ){//&& grantResults[0] ==
                    for(int j = 0; j < grantResults.length; j++){
                        if(grantResults[j] != PackageManager.PERMISSION_GRANTED){
                            locationPermissionGranted = false;
                            return;
                        }
                        locationPermissionGranted = true;
                        InitMap();
                    }
                }
            }
        }
    }

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