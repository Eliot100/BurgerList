package com.example.burgerlist;

public class Dish {
    String name; // the user that sent the message
    String description; // the restaurant the got the comment
    int price;

    public Dish(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public Dish(Dish dish) {
        this.name = new String(dish.name);
        this.description = new String(dish.description);
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

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int message) {
        this.price = message;
    }


}

