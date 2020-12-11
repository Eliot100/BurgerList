package com.example.burgerlist;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantUpdate {
    public static boolean UpdateName(String OwnerId, String newName){
        return FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(OwnerId).child("name").setValue(newName).isSuccessful();
    }
    public static boolean UpdatePhoneNum(String OwnerId, String newPhoneNum){
        return FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(OwnerId).child("phone").setValue(newPhoneNum).isSuccessful();

    }
    public static boolean UpdateLocation(String OwnerId, LatLng newLatLng){
        return FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(OwnerId).child("location").setValue(newLatLng).isSuccessful();
    }
}
