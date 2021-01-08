package com.example.burgerlist;

import android.graphics.BitmapFactory;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;

public class RestView{
    private String restId, restName, restDist, restCity, currentRating;
    private RatingBar ratingBar;

    public RestView(){}

    public RestView(String restId, String restName, String restDist, String restCity, String currentRating){
        this.restId = restId;
        this.restName = restName;
        this.restDist = restDist;
        this.restCity = restCity;
        this.currentRating = currentRating;
    }

    public String getRestId() {
        return restId;
    }

    public void setCurrentRating(String currentRating) {
        this.currentRating = currentRating;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getCurrentRating() {
        return currentRating;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestDist() {
        return restDist;
    }

    public void setRestDist(String restDist) {
        this.restDist = restDist;
    }

    public String getRestCity() {
        return restCity;
    }

    public void setRestCity(String restCity) {
        this.restCity = restCity;
    }
}
