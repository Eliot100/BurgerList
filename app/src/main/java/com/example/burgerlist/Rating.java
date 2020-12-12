package com.example.burgerlist;

public class Rating {
    Restaurant ress;
    double retValue;

    public Rating(Restaurant ress, double rating) {
        this.ress = ress;
        this.retValue = rating;
    }
    public Rating(Rating rate) {
        this.ress = rate.ress;
        this.retValue = rate.retValue;
    }
    public Restaurant getRess() {
        return this.ress;
    }

    public void setRess(Restaurant ress) {
        this.ress = ress;
    }

    public double getRetValue() {
        return retValue;
    }

    public void setRetValue(double retValue) {
        this.retValue = retValue;
    }


}
