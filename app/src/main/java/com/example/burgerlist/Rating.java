package com.example.burgerlist;

import java.util.ArrayList;

public class Rating {
    private String owner_uid;
    private double rate ;
    private String burger_uid;

    public Rating() {
    }
    public Rating(String owner_uid,double rate, String burger_uid) {
        this.owner_uid = owner_uid;
        this.rate = rate;
        this.burger_uid = burger_uid;
    }

    public String getOwner() {
        return this.owner_uid;
    }

    public double getRate() {
        return this.rate;
    }

    public String getBurger() {
        return this.burger_uid;
    }

    public void setOwner(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setBurger(String burger_uid) {
        this.burger_uid = burger_uid;
    }


}

