package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateRestaurant extends AppCompatActivity {
    Button loctionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);


        loctionButton = (Button)findViewById(R.id.loctionButton);

        loctionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_RestLocation();
            }
        });


    }

    private void Create_RestLocation() {
        Intent intent = new Intent(this, RestaurantLocation.class);
        Toast.makeText(getApplicationContext(),"CreateRestaurant", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}