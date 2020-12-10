package com.example.burgerlist;

import java.util.ArrayList;

public class RestaurantRating {

    private String restaurant_name;
    private String owner_uid;
    private ArrayList<Double> ratings ;
    private double current_rating;

    public RestaurantRating() {
    }
    public RestaurantRating(String restaurant_name,String owner_uid) {
        ratings = new ArrayList<Double>();
        restaurant_name = restaurant_name;
        owner_uid = owner_uid;
    }

    public double getCurrent_rating() {
        return current_rating;
    }


    public void calculate_ratings(){
        // later
    }
    public void rate(double rating){
        ratings.add(rating);
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getOwner_uid() {
        return owner_uid;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }

}
