package com.example.burgerlist;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRating {
    private ArrayList<Ret> ratings;

    public RestaurantRating() {
        ratings =  new ArrayList<Ret>();
    }

    public int getSize() {
        return ratings.size();
    }

    public void addRet(String userId, double retValue){
        if (isRated(userId)) {
            for(Ret ret : this.ratings){
                if (ret.userId.equals(userId)) {
                    ret.retValue = retValue;
                    return;
                }
            }
        }
        this.ratings.add(new Ret(userId, retValue));
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
        private String userId;
        private double retValue;

        public Ret(){}

        public Ret(String userId, double retValue){
            this.userId = userId;
            this.retValue = retValue;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getRetValue() {
            return retValue;
        }

        public void setRetValue(double retValue) {
            this.retValue = retValue;
        }
    }
}
