package com.example.burgerlist;



public class User {

    private String Username;
    private String Email;
    private String Password1;
    private String Password2;
    private boolean Owner;

    public User() {
    }

    public User(String email, String password1, String password2, String username, boolean owner) {
        Email = email;
        Password1 = password1;
        Password2 = password2;
        Username = username;
        Owner = owner;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword1() {
        return Password1;
    }

    public void setPassword1(String password1) {
        Password1 = password1;
    }

    public String getPassword2() {
        return Password2;
    }

    public void setPassword2(String password2) {
        Password2 = password2;
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
