package com.example.burgerlist;

public class Rat {
    private String userId;
    private double retValue;

    public Rat(){}

    public Rat(String userId, double retValue){
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
