package com.example.burgerlist;

public class Dish {
    String name; // the user that sent the message
    String description; // the restaurant the got the comment
    String price;

    public Dish(String name, String description, String price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public Dish(Dish dish) {
        this.name = dish.name;
        this.description = dish.description;
        this.price = dish.price;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDec() {
        return this.description;
    }

    public void setDec(String description) {
        this.description = description;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}

