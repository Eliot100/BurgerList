package com.example.burgerlist;

public class Restaurant {

    private String RestName, PhoneNum, RestLocation;

    public Restaurant(String restName, String phoneNum , String restLocation) {
        RestName = restName;
        PhoneNum = phoneNum;
        RestLocation = restLocation;
    }

    public Restaurant(Restaurant rest) {
        RestName = rest.RestName;
        PhoneNum = rest.PhoneNum;
        RestLocation = rest.RestLocation;
    }

    public String getName() {return RestName;}
    public String getPhoneNum() {return PhoneNum;}
    public String getRestLocation() {return RestLocation;}


}
