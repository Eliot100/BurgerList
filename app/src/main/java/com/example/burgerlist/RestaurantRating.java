package com.example.burgerlist;

import java.util.List;

public class RestaurantRating {
    private List<Ret> ratings;

    public RestaurantRating() {
    }

    public int getSize(){
        return ratings.size();
    }

    public double getCurrentRating() {
        double currentRating = 0;
        for(Ret ret : this.ratings){
            currentRating += ret.retValue;
        }
        return (currentRating/this.ratings.size());
    }

    public double getRat(String userId) {
        for(Ret ret : this.ratings){
            if (ret.userId.equals(userId))
                return ret.retValue;
        }
        return -1;
    }

    public boolean isRated(String userId) {
        for(Ret ret : this.ratings){
            if (ret.userId.equals(userId))
                return true;
        }
        return false;
    }

    private class Ret {
        String userId;
        double retValue;
    }
}
