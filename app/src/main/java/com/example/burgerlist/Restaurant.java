package com.example.burgerlist;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {

    private String OwnerId, RestName;
    private String PhoneNum;
    private LatLng RestLocation;

    public Restaurant(String ownerId, String restName, String phoneNum, LatLng restLocation) {
        OwnerId = ownerId;
        RestName = restName;
        PhoneNum = phoneNum;
        RestLocation = restLocation;
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

    public String getPhone() {
        return PhoneNum;
    }

    public LatLng getLocation() {
        return RestLocation;
    }
}
