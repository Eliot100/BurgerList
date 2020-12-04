package com.example.burgerlist;



public class User {

    private String Username;
    private String Email;
    private String Password;
    private boolean Owner;

    public User() {
    }

    public User(String email, String password ,String username, boolean owner) {
        Email = email;
        Password = Password;
        Username = username;
        Owner = owner;
    }

    public User(User user) {
        Email = user.getEmail();
        Password = user.getPassword();
        Username = user.getUsername();
        Owner = user.isOwner();
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
