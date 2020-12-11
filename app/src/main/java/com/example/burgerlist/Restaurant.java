package com.example.burgerlist;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {

    private String OwnerId, RestName;
    private String PhoneNum;
    private LatLng RestLocation;
    private double avg_rate;

    public Restaurant(String ownerId, String restName, String phoneNum, LatLng restLocation) {
        OwnerId = ownerId;
        RestName = restName;
        PhoneNum = phoneNum;
        RestLocation = restLocation;
        avg_rate = 0.0;
    }

    public Restaurant(String ownerId, String restName, String phoneNum) {
        OwnerId = ownerId;
        RestName = restName;
        PhoneNum = phoneNum;
        RestLocation = null;
        avg_rate = 0.0;
    }

    public Restaurant(Restaurant rest) {
        OwnerId = rest.OwnerId;
        RestName = rest.RestName;
        PhoneNum = rest.PhoneNum;
        RestLocation = rest.RestLocation;
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

    public double getAvg_rate() {
        return avg_rate;
    }

    public String getPhone() {
        return PhoneNum;
    }

    public LatLng getLocation() {
        return RestLocation;
    }

}
