package com.example.burgerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchFilter extends AppCompatActivity {
    private EditText search_bar;
    private RecyclerView result_view;
    private Switch kosher_swt;
    private Switch saturday_swt;
    private Switch vegan_swt;
    private Switch vegetarian_swt;
    private DatabaseReference ref;
    private SearchAdapter searchAdapter;

    private ArrayList<String> rest_id_list;
    private ArrayList<String> rest_name_list;
    private ArrayList<String> rating_list;
    private ArrayList<String> rest_logo_list;
    private ArrayList<String> city_list;
    private ArrayList<String> distance_list;

    String current_search_text="";
    double defult_lat = 32.086619;
    double defult_lon = 34.789621;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        // initializing variables
        search_bar = (EditText)findViewById(R.id.search_bar_edit_text);
        result_view = (RecyclerView)findViewById(R.id.result_recylcerview);
        kosher_swt = (Switch)findViewById(R.id.kosher_switch);
        saturday_swt = (Switch)findViewById(R.id.saturday_switch);
        vegan_swt = (Switch)findViewById(R.id.vegan_switch);
        vegetarian_swt = (Switch)findViewById(R.id.vegitarian_switch);
        rest_name_list =  new ArrayList<String>();
        rating_list =  new ArrayList<String>();
        rest_logo_list =  new ArrayList<String>();
        rest_id_list = new ArrayList<String>();
        city_list = new ArrayList<String>();
        distance_list = new ArrayList<String>();



        ref = FirebaseDatabase.getInstance().getReference();

        //setting up recycle view
        result_view.setHasFixedSize(true);
        result_view.setLayoutManager(new LinearLayoutManager(this));
        result_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // listeners
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty() == false){
                    current_search_text = s.toString();
                    update_list(s.toString());
                }else{
                    rest_logo_list.clear();
                    rest_name_list.clear();
                    rating_list.clear();
                    result_view.removeAllViews();
                    city_list.clear();
                    distance_list.clear();
                }

            }
        });

        kosher_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                update_list(current_search_text);
            }
        });

        saturday_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                update_list(current_search_text);
            }
        });

        vegan_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                update_list(current_search_text);
            }
        });

        vegetarian_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                update_list(current_search_text);
            }
        });




    }

    private void update_list(String usersearch) {


        ref.child("Restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int current = 0;
                boolean deleted = false;
                rest_logo_list.clear();
                rest_name_list.clear();
                rating_list.clear();
                city_list.clear();
                distance_list.clear();
                result_view.removeAllViews();


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
                    double d =distance(defult_lat,Double.parseDouble(rest_lat),defult_lon,Double.parseDouble(rest_lon),0,0)/1000;
                    String distance = String.valueOf((df.format(d)));


                    //filters
                    boolean kosher =(boolean) snap.child("filter").child("kosher").getValue();
                    boolean saturday = (boolean)snap.child("filter").child("saturday").getValue();
                    boolean vegan = (boolean)snap.child("filter").child("vegan").getValue();
                    boolean vegetarian = (boolean) snap.child("filter").child("vegetarian").getValue();

                    //String logo = snap.child("logo").getValue().toString(); //logo when storage ready

                    if(restname.toLowerCase().contains(usersearch.toLowerCase())){
                        rest_name_list.add(restname);
                        rest_id_list.add(uid);
                        rating_list.add(rating);
                        city_list.add(city);
                        distance_list.add(distance);
                        //rest_logo_list.add(logo);   //logo when storage ready
                        current++;

                    }else if(city.toLowerCase().contains((usersearch.toLowerCase()))){
                        rest_name_list.add(restname);
                        rest_id_list.add(uid);
                        rating_list.add(rating);
                        city_list.add(city);
                        distance_list.add(distance);
                        //rest_logo_list.add(logo);   //logo when storage ready
                        current++;
                    }

                    // filters
                    if(kosher_swt.isChecked() == true){
                        if(kosher == false){
                            rest_name_list.remove(restname);
                            rest_id_list.remove(uid);
                            rating_list.remove(rating);
                            city_list.remove(city);
                            distance_list.remove(distance);
                            deleted = true;
                        }
                    }

                    if(saturday_swt.isChecked() == true){
                        if(saturday == false){
                            if(deleted == false){
                                rest_name_list.remove(restname);
                                rest_id_list.remove(uid);
                                rating_list.remove(rating);
                                city_list.remove(city);
                                distance_list.remove(distance);
                                deleted = true;
                            }
                        }
                    }

                    if(vegan_swt.isChecked() == true){
                        if(vegan == false){
                            if(deleted == false){
                                rest_name_list.remove(restname);
                                rest_id_list.remove(uid);
                                rating_list.remove(rating);
                                city_list.remove(city);
                                distance_list.remove(distance);
                                deleted = true;
                            }
                        }
                    }

                    if(vegetarian_swt.isChecked() == true){
                        if(vegetarian == false){
                            if(deleted == false){
                                rest_name_list.remove(restname);
                                rest_id_list.remove(uid);
                                rating_list.remove(rating);
                                city_list.remove(city);
                                distance_list.remove(distance);
                                deleted = true;
                            }
                        }
                    }


                    if(current == 20){
                        break;
                    }

                }

                searchAdapter = new SearchAdapter(SearchFilter.this , rest_id_list ,rest_name_list ,rating_list,city_list , distance_list);
                result_view.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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