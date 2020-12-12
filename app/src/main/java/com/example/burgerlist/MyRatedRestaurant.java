package com.example.burgerlist;

import java.util.ArrayList;

public class MyRatedRestaurant {

    private ArrayList<Ret> my_rated_ress;


    public MyRatedRestaurant(){
        my_rated_ress =  new ArrayList<Ret>();
    }


    public void add_rating(Restaurant ress , double rating){
        Ret r = new Ret(ress,rating);
        my_rated_ress.add(r);
    }

    public  void remove_rating(Restaurant ress , double rating){
        Ret ret = search(ress);
        my_rated_ress.remove(ret);
    }

    public Ret search (Restaurant ress) {
        for (int i = 0; i < my_rated_ress.size(); i++) {
            if (my_rated_ress.get(i).ress == ress) {
                return my_rated_ress.get(i);
            }
        }
        return null;
    }

    private class Ret {
        Restaurant ress;
        double retValue;

        public Ret (Restaurant ress , double rating){
            this.ress =  ress;
            this.retValue = rating;
        }
    }

}
