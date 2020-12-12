package com.example.burgerlist;

import java.util.ArrayList;

public class MyRatedRestaurant {

    private String userId;
    private ArrayList<Rating> my_rated_ress;


    public MyRatedRestaurant() {
        my_rated_ress = new ArrayList<Rating>();
    }


    public void add_rating(Restaurant ress, double rating) {
        Rating r = new Rating(ress, rating);
        my_rated_ress.add(r);
    }

    public void remove_rating(Restaurant ress, double rating) {
        Rating ret = search(ress);
        my_rated_ress.remove(ret);
    }

    public Rating search(Restaurant ress) {
        for (int i = 0; i < my_rated_ress.size(); i++) {
            if (my_rated_ress.get(i).ress == ress) {
                return my_rated_ress.get(i);
            }
        }
        return null;
    }

    public Rating get(int i) {
        if (i < my_rated_ress.size())
            return my_rated_ress.get(i);
        return null;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Rating> getResList() {
        return this.my_rated_ress;
    }

    public void setResList(ArrayList<Rating> my_rated_ress) {
        this.my_rated_ress = my_rated_ress;
    }

}
