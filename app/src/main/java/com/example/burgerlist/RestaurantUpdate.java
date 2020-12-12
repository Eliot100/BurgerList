package com.example.burgerlist;

import com.google.android.gms.maps.model.LatLng;
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
