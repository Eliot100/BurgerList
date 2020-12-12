package com.example.burgerlist;

public class RatingPreview {
    String resname;
    double rating;

    public RatingPreview(String resname, double rating) {
        this.resname = resname;
        this.rating = rating;
    }
    public String getResName() {
        return this.resname;
    }

    public void setResName(String ress) {
        this.resname = ress;
    }

    public double getRetValue() {
        return rating;
    }

    public void setRetValue(double retValue) {
        this.rating = retValue;
    }


}