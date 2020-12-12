package com.example.burgerlist;

public class Comment {
    String userId; // the user that sent the message
    String ress; // the restaurant the got the comment
    String message;

    public Comment(String userId, String ress, String message) {
        this.userId = userId;
        this.ress = ress;
        this.message = message;
    }
    public Comment(Comment comment) {
        this.userId = new String(comment.userId);
        this.ress = new String(comment.ress);
        this.message = new String(comment.message);
    }
    public String getUser() {
        return this.userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    public String getRess() {
        return this.ress;
    }

    public void setRess(String ress) {
        this.ress = ress;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
