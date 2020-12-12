package com.example.burgerlist;



public class User {

    private String Username;
    private String Email;
    private String Password;
    private boolean Owner;
    private String restaurant_name;
    private MyRatedRestaurant my_rated_res;

    public User() {
    }

    public User(String email, String password ,String username, boolean owner) {
        Email = email;
        Password = Password;
        Username = username;
        Owner = owner;
        restaurant_name = "0";
        MyRatedRestaurant my_rated_res =  new MyRatedRestaurant();
    }

    public User(User user) {
        Email = user.getEmail();
        Password = user.getPassword();
        Username = user.getUsername();
        Owner = user.isOwner();
        restaurant_name = user.restaurant_name;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public boolean isOwner() {
        return Owner;
    }

    public void setOwner(boolean owner) {
        Owner = owner;
    }
}
