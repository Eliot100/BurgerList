package com.example.burgerlist;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Date;

public class Comment {
    private String userId; // the user that sent the message
    private String name; // the restaurant the got the comment
    private String message;
    private String date;

   // @RequiresApi(api = Build.VERSION_CODES.O)
    public Comment(String userId, String name, String message, String date) {
        this.userId = userId;
        this.name = name;
        this.message = message;
        this.date = date;
    }
    public Comment(String userId, String name, String message) {
        this.userId = userId;
        this.name = name;
        this.message = message;
        this.date = java.util.Calendar.getInstance().getTime().toString();
    }
    public Comment(Comment comment) {
        this.userId = new String(comment.userId);
        this.name = new String(comment.name);
        this.message = new String(comment.message);
        this.date = new String(comment.date);

    }
    public String getUser() {
        return this.userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void settName(String ress) {
        this.name = name;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String getDate) {
        this.date = date;
    }


}
