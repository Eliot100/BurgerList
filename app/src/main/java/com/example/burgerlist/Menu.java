package com.example.burgerlist;

import java.util.ArrayList;

public class Menu {


    private String restId;
    private ArrayList<Dish> dishes;


    public Menu() {
        dishes = new ArrayList<Dish>();
    }

    public Menu(String restId) {
        dishes = new ArrayList<Dish>();
        this.restId = restId;
    }


    public void add_dish(Dish dish) {
        Dish d = new Dish(dish);
        dishes.add(d);
    }

    public void remove_dish(Dish dish) {
        Dish ret = search(dish);
        dishes.remove(ret);
    }

    public Dish search(Dish dish) {
        for (int i = 0; i < dishes.size(); i++) {
            if (dishes.get(i) == dish) {
                return dishes.get(i);
            }
        }
        return null;
    }

    public Dish get(int i) {
        if (i < dishes.size())
            return dishes.get(i);
        return null;
    }


    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public ArrayList<Dish> getDishes() {
        return this.dishes;
    }

    public void setResList(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

}
