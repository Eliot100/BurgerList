package com.example.burgerlist;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
/*
*
*
**/
public class Restaurant implements Serializable {
    private String OwnerId, RestName, PhoneNum;
    private LatLng RestLocation;
    private RestaurantRating restRat;

    public Restaurant(String ownerId, String restName, String phoneNum, LatLng restLocation) {
        OwnerId = ownerId;
        RestName = restName;
        PhoneNum = phoneNum;
        RestLocation = restLocation;
        restRat = new RestaurantRating();
        restRat.addRet(ownerId,5);
    }

    public Restaurant(String ownerId, String restName, String phoneNum){
        OwnerId = ownerId;
        RestName = restName;
        PhoneNum = phoneNum;
        RestLocation = new LatLng(-1, -1);
        restRat = new RestaurantRating();
        restRat.addRet(ownerId,5);
    }

    public double getCurrentRating() {
        return restRat.getCurrentRating();
    }

    public Restaurant(Restaurant rest) {
        OwnerId = rest.OwnerId;
        RestName = rest.RestName;
        PhoneNum = rest.PhoneNum;
        RestLocation = rest.RestLocation;
        restRat = rest.restRat;
    }

    public RestaurantRating getRestRat() {
        return restRat;
    }

    public void setRestRat(RestaurantRating restRat) {
        this.restRat = restRat;
    }

    public void AddRat(String userId, double retValue) {
        this.restRat.addRet(userId, retValue);
    }

    public String toString() {
        return (RestName + " "
                + PhoneNum + " "
                + RestLocation.latitude + " "
                + RestLocation.longitude);
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public String getName() {
        return RestName;
    }

    public String getPhone() {
        return PhoneNum;
    }

    public LatLng getLocation() {
        return RestLocation;
    }


}