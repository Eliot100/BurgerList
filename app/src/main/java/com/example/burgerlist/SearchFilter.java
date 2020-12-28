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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFilter extends AppCompatActivity {
    private EditText search_bar;
    private RecyclerView result_view;
    private Button switch_search_type_btn;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        // initializing variables
        search_bar = (EditText)findViewById(R.id.search_bar_edit_text);
        result_view = (RecyclerView)findViewById(R.id.result_recylcerview);
        switch_search_type_btn = (Button)findViewById(R.id.switch_search_btn);
        kosher_swt = (Switch)findViewById(R.id.kosher_switch);
        saturday_swt = (Switch)findViewById(R.id.saturday_switch);
        vegan_swt = (Switch)findViewById(R.id.vegan_switch);
        vegetarian_swt = (Switch)findViewById(R.id.vegitarian_switch);
        rest_name_list =  new ArrayList<String>();
        rating_list =  new ArrayList<String>();
        rest_logo_list =  new ArrayList<String>();
        rest_id_list = new ArrayList<String>();

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
                    update_list(s.toString());
                }else{
                    rest_logo_list.clear();
                    rest_name_list.clear();
                    rating_list.clear();
                    result_view.removeAllViews();
                }

            }
        });
    }

    private void update_list(String usersearch) {


        ref.child("Restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int limit = 0;
                rest_logo_list.clear();
                rest_name_list.clear();
                rating_list.clear();
                result_view.removeAllViews();

                for( DataSnapshot snap: snapshot.getChildren()){
                    String restname = snap.child("name").getValue().toString();
                    String uid = snap.child("ownerId").getValue().toString();
                    String rating = snap.child("currentRating").getValue().toString();
                    //String logo = snap.child("logo").getValue().toString(); //logo when storage ready

                    if(restname.toLowerCase().contains(usersearch.toLowerCase())){
                        rest_name_list.add(restname);
                        rest_id_list.add(uid);
                        rating_list.add(rating);
                        //rest_logo_list.add(logo);   //logo when storage ready
                        limit++;
                    }


                    if(limit == 20){
                        break;
                    }

                }
                searchAdapter = new SearchAdapter(SearchFilter.this , rest_id_list ,rest_name_list ,rating_list);
                result_view.setAdapter(searchAdapter);
                String temp = ""+searchAdapter.getItemCount();
                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}